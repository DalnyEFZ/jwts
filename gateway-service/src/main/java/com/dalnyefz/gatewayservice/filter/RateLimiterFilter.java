package com.dalnyefz.gatewayservice.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基于滑动窗口的限流过滤器
 * 实现简单的限流逻辑，防止服务被过多请求压垮
 */
@Component
public class RateLimiterFilter implements GlobalFilter, Ordered {

    // 每个 IP 的请求计数器
    private final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();

    // 时间窗口的开始时间
    private final Map<String, Long> windowStartTimes = new ConcurrentHashMap<>();

    // 时间窗口大小（毫秒）- 1秒
    private static final long WINDOW_SIZE = 1000;

    // 每个时间窗口内允许的最大请求数
    private static final int MAX_REQUESTS_PER_WINDOW = 10;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String clientIp = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        long currentTime = System.currentTimeMillis();

        // 获取或初始化客户端的请求计数
        AtomicInteger count = requestCounts.computeIfAbsent(clientIp, k -> new AtomicInteger(0));
        Long windowStart = windowStartTimes.computeIfAbsent(clientIp, k -> currentTime);

        // 检查是否需要重置时间窗口
        if (currentTime - windowStart > WINDOW_SIZE) {
            count.set(0);
            windowStartTimes.put(clientIp, currentTime);
        }

        // 检查是否超过限流阈值
        if (count.incrementAndGet() > MAX_REQUESTS_PER_WINDOW) {
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            exchange.getResponse().getHeaders().add("X-Rate-Limit", String.valueOf(MAX_REQUESTS_PER_WINDOW));
            exchange.getResponse().getHeaders().add("X-Rate-Limit-Remaining", "0");
            exchange.getResponse().getHeaders().add("X-Rate-Limit-Reset", String.valueOf(windowStart + WINDOW_SIZE));
            return exchange.getResponse().setComplete();
        }

        // 添加限流信息到响应头
        exchange.getResponse().getHeaders().add("X-Rate-Limit", String.valueOf(MAX_REQUESTS_PER_WINDOW));
        exchange.getResponse().getHeaders().add("X-Rate-Limit-Remaining",
                String.valueOf(MAX_REQUESTS_PER_WINDOW - count.get()));

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        // 设置过滤器优先级，数值越小优先级越高
        return -100;
    }
}
