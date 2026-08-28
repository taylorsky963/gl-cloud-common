package com.gl.myparadise.common.mvc.feign;

import com.gl.myparadise.common.bean.model.ServiceRsModel;
import com.gl.myparadise.common.bean.report.operationlog.PlatformOperationLogReportDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 將後台操作日誌上報到 bgauth-service 的 Feign 介面。
 * <p>所有依賴 common-mvc 的微服務都會自動註冊此 Feign Client (因為其應用啟動類在 com.gl.myparadise 包,
 * {@code @EnableFeignClients} 預設掃描該基礎包)。</p>
 *
 * <p><b>注意:</b> 此處 path 硬編碼為 {@code /bg/auth/server/log/...} 而非使用
 * {@code ${bg.feign.mapping.url}} placeholder, 因為呼叫方可能是任何 admin 模組
 * (bgactivity-admin / bgmember-admin / ...), 那些模組的 {@code bg.feign.mapping.url}
 * 值各不相同, 一旦使用 placeholder 會解析成呼叫方自己的前綴 → 404。
 * 此模式與 {@code MallVipMemberClientService} 一致。</p>
 */
@FeignClient(name = "bgauth-service", contextId = "bgAuthOperationLogReportAPI", url = "${feign.client.url:}")
public interface BgAuthOperationLogReportAPI {

    @RequestMapping(value = "/bg/auth/server/log/report-operation.htm", method = RequestMethod.POST)
    ServiceRsModel<Boolean> reportOperation(@RequestBody PlatformOperationLogReportDto dto);
}
