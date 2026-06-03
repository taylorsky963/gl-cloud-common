package com.gl.myparadise.common.mvc.Interceptor;

import com.gl.myparadise.common.bean.model.ServiceRsModel;
import com.gl.myparadise.common.bean.report.operationlog.PlatformOperationLogIgnoreRuleDto;
import com.gl.myparadise.common.mvc.feign.BgAuthOperationLogIgnoreRuleAPI;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 操作日誌忽略規則本地快照 + 非同步刷新。
 * <p>各 admin 服務啟動時嘗試從 bgauth-service 拉取一次，aspect hot path 永遠讀快照，
 * 過期時非同步觸發 reload，reload 失敗則保留舊快照，避免 hot path 阻塞或丟訊息。</p>
 */
@Slf4j
@Component
public class PlatformOperationLogIgnoreRuleCache {

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    /**
     * 預編譯後的單條規則。
     */
    private static final class Rule {
        /** 已 uppercase；null 表 ANY。 */
        final String method;
        final String urlPattern;

        Rule(String method, String urlPattern) {
            this.method = StringUtils.isBlank(method) ? null : method.toUpperCase();
            this.urlPattern = urlPattern;
        }
    }

    /**
     * 快照：載入時間 + 規則列表，整個物件 immutable，方便 CAS 替換。
     */
    private static final class Snapshot {
        final long loadAtMs;
        final List<Rule> rules;

        Snapshot(long loadAtMs, List<Rule> rules) {
            this.loadAtMs = loadAtMs;
            this.rules = rules;
        }
    }

    private final AtomicReference<Snapshot> snapshotRef = new AtomicReference<>();
    private final AtomicBoolean refreshing = new AtomicBoolean(false);

    @Autowired(required = false)
    private BgAuthOperationLogIgnoreRuleAPI bgAuthOperationLogIgnoreRuleAPI;

    @Autowired
    @Qualifier("operationLogExecutor")
    private ThreadPoolTaskExecutor operationLogExecutor;

    /**
     * TTL 毫秒；預設 5 分鐘。
     */
    @Value("${platform.operation-log.ignore-rule.ttl-ms:300000}")
    private long ttlMs;

    @PostConstruct
    public void init() {
        try {
            reloadSync();
        } catch (Exception e) {
            log.warn("PlatformOperationLogIgnoreRuleCache initial load failed, will lazy retry", e);
        }
    }

    /**
     * aspect hot path 呼叫；命中規則即回 true。
     */
    public boolean shouldIgnore(String requestMethod, String url) {
        Snapshot snapshot = snapshotRef.get();
        long now = System.currentTimeMillis();

        if (snapshot == null) {
            if (refreshing.compareAndSet(false, true)) {
                submitAsyncReload();
            }
            return false;
        }

        if (now - snapshot.loadAtMs > ttlMs) {
            if (refreshing.compareAndSet(false, true)) {
                submitAsyncReload();
            }
        }

        if (snapshot.rules.isEmpty() || StringUtils.isBlank(url)) {
            return false;
        }
        String upperMethod = requestMethod == null ? null : requestMethod.toUpperCase();
        for (Rule rule : snapshot.rules) {
            if (rule.method != null && !rule.method.equals(upperMethod)) {
                continue;
            }
            try {
                if (PATH_MATCHER.match(rule.urlPattern, url)) {
                    return true;
                }
            } catch (Exception e) {
                log.debug("bad urlPattern, skip: {}", rule.urlPattern, e);
            }
        }
        return false;
    }

    private void submitAsyncReload() {
        try {
            operationLogExecutor.execute(() -> {
                try {
                    reloadSync();
                } catch (Exception e) {
                    log.warn("PlatformOperationLogIgnoreRuleCache async reload failed", e);
                } finally {
                    refreshing.set(false);
                }
            });
        } catch (Exception e) {
            refreshing.set(false);
            log.warn("PlatformOperationLogIgnoreRuleCache submit reload failed", e);
        }
    }

    private void reloadSync() {
        if (bgAuthOperationLogIgnoreRuleAPI == null) {
            log.debug("BgAuthOperationLogIgnoreRuleAPI not present, skip reload");
            return;
        }
        ServiceRsModel<List<PlatformOperationLogIgnoreRuleDto>> rs = bgAuthOperationLogIgnoreRuleAPI.listIgnoreRules();
        if (rs == null || rs.getRsData() == null) {
            log.debug("reload ignore rules: empty response");
            snapshotRef.set(new Snapshot(System.currentTimeMillis(), Collections.<Rule>emptyList()));
            return;
        }
        List<PlatformOperationLogIgnoreRuleDto> dtos = rs.getRsData();
        List<Rule> rules = new ArrayList<>(dtos.size());
        for (PlatformOperationLogIgnoreRuleDto dto : dtos) {
            if (dto == null || StringUtils.isBlank(dto.getUrlPattern())) {
                continue;
            }
            if (dto.getIsEnable() != null && dto.getIsEnable() == 0) {
                continue;
            }
            rules.add(new Rule(dto.getRequestMethod(), dto.getUrlPattern()));
        }
        snapshotRef.set(new Snapshot(System.currentTimeMillis(), Collections.unmodifiableList(rules)));
        log.info("PlatformOperationLogIgnoreRuleCache loaded {} rule(s)", rules.size());
    }
}
