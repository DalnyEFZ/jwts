package com.dalnyefz.gatewayservice.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * 网关级熔断过滤器
 * 当后端服务超时或不可用时，返回友好的错误信息
 */
@Component
public class CircuitBreakerFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                .onErrorResume(throwable -> {
                    // 记录错误日志
                    System.err.println("CircuitBreakerFilter 捕获异常: " + throwable.getMessage());

                    // 判断异常类型
                    HttpStatus status;
                    String message;

                    if (throwable instanceof TimeoutException) {
                        status = HttpStatus.GATEWAY_TIMEOUT;
                        message = "服务响应超时，请稍后重试";
                    } else if (throwable instanceof java.net.ConnectException) {
                        status = HttpStatus.SERVICE_UNAVAILABLE;
                        message = "服务暂时不可用，请稍后重试";
                    } else {
                        status = HttpStatus.INTERNAL_SERVER_ERROR;
                        message = "服务内部错误，请稍后重试";
                    }

                    // 返回友好的错误响应
                    exchange.getResponse().setStatusCode(status);
                    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

                    String body = String.format(
                            "{\"code\":%d,\"message\":\"%s\",\"data\":null}",
                            status.value(), message
                    );

                    byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
                    org.springframework.core.io.buffer.DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
                    return exchange.getResponse().writeWith(Mono.just(buffer));
                });
    }

    @Override
    public int getOrder() {
        // 设置较低的优先级，让它在其他过滤器之后执行
        return 100;
    }
}
