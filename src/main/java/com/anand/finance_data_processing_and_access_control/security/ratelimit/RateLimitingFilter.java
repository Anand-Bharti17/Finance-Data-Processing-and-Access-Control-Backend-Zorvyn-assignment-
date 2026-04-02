package com.anand.finance_data_processing_and_access_control.security.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    // Store buckets in memory by IP address
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {
        // Allow 20 requests, refill 20 tokens every 1 minute
        Refill refill = Refill.greedy(20, Duration.ofMinutes(1));
        Bandwidth limit = Bandwidth.classic(20, refill);
        return Bucket.builder().addLimit(limit).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        Bucket bucket = cache.computeIfAbsent(ip, k -> createNewBucket());

        if (bucket.tryConsume(1)) {
            // Token consumed successfully, proceed with the request
            filterChain.doFilter(request, response);
        } else {
            // Rate limit exceeded
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many requests. Please try again later.");
        }
    }
}