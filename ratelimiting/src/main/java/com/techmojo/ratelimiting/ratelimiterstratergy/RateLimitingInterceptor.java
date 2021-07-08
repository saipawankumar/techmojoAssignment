package com.techmojo.ratelimiting.ratelimiterstratergy;

import com.techmojo.ratelimiting.dto.RateLimit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RateLimitingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RateLimitingInterceptor.class);

    @Value("${rate.limit.enabled}")
    private boolean enabled;

    @Value("${rate.limit.hourly.limit}")
    private int hourlyLimit;

    private Map<String, Optional<RateLimit>> limiters = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!enabled) {
            return true;
        }
        String clientId = request.getHeader("Client-Id");
        // let non-API requests pass
        if (clientId == null) {
            return true;
        }
        RateLimit rateLimiter = getRateLimiter(clientId);
        boolean allowRequest = rateLimiter.tryAcquire();

        if (!allowRequest) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.sendError(HttpStatus.TOO_MANY_REQUESTS.value());
        }
        response.addHeader("X-RateLimit-Limit", String.valueOf(hourlyLimit));
        return allowRequest;
    }

    private RateLimit getRateLimiter(String clientId) {
        if (limiters.containsKey(clientId)) {
            return limiters.get(clientId).get();
        } else {
            synchronized(clientId.intern()) {
                // double-checked locking to avoid multiple_reinitializations
                if (limiters.containsKey(clientId)) {
                    return limiters.get(clientId).get();
                }

                RateLimit rateLimiter = RateLimit.create(100, TimeUnit.HOURS);

                limiters.put(clientId, Optional.of(rateLimiter));
                return rateLimiter;
            }
        }
    }

    @PreDestroy
    public void destroy() {
        // loop and finalize all limiters
    }
}