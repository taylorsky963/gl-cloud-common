package com.gl.myparadise.common.mvc.feign;

import com.gl.myparadise.common.bean.model.ServiceRsModel;
import com.gl.myparadise.common.bean.report.operationlog.PlatformOperationLogIgnoreRuleDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 拉取操作日誌忽略規則的 Feign 介面。
 * <p>由 {@link com.gl.myparadise.common.mvc.Interceptor.PlatformOperationLogIgnoreRuleCache}
 * 在啟動及 TTL 過期時呼叫，組成本地快照供 aspect 使用。</p>
 *
 * <p>path 硬編碼原因見 {@link BgAuthOperationLogReportAPI} 註解。</p>
 */
@FeignClient(name = "bgauth-service", contextId = "bgAuthOperationLogIgnoreRuleAPI", url = "${feign.client.url:}")
public interface BgAuthOperationLogIgnoreRuleAPI {

    @RequestMapping(value = "/bg/auth/server/log/list-ignore-rules.htm", method = RequestMethod.POST)
    ServiceRsModel<List<PlatformOperationLogIgnoreRuleDto>> listIgnoreRules();
}
