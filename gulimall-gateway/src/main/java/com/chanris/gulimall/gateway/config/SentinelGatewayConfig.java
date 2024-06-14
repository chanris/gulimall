package com.chanris.gulimall.gateway.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.fastjson2.JSON;
import com.chanris.gulimall.common.exception.CodeEnum;
import com.chanris.gulimall.common.utils.Result;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author chenyue7@foxmail.com
 * @date 2024/6/14
 * @description
 */
@Configuration
public class SentinelGatewayConfig {

    public SentinelGatewayConfig() {
        GatewayCallbackManager.setBlockHandler(new BlockRequestHandler() {
            //网关限流后，调用此回调
            @Override
            public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {
                Result<?> result = new Result<>();
                result.error(CodeEnum.TOO_MANY_REQUEST.code, CodeEnum.TOO_MANY_REQUEST.msg);
                String jsonRes = JSON.toJSONString(result);
                return ServerResponse.ok().body(Mono.just(jsonRes), String.class);
            }
        });
    }
}
