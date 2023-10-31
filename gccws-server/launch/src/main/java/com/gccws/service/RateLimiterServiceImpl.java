package com.gccws.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author    Md. Mizanur Rahman
 * @Since     August 1, 2022
 * @version   1.0.0
 */

@Service
public class RateLimiterServiceImpl implements RateLimiterService{

    //Cache for API key and token bucket ( we can use any other caching method as well )
    Map<String, Bucket> bucketCache = new ConcurrentHashMap<>();

    @Override
    public Bucket resolveBucket(String apiKey) {
        return bucketCache.computeIfAbsent(apiKey, this::newBucket);
    }
    private Bucket newBucket(String s) {
        return Bucket4j.builder()
                .addLimit(Bandwidth.classic(150, Refill.intervally(150, Duration.ofMinutes(1)))).build();
    }
}
