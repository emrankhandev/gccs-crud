package com.gccws.service;

import io.github.bucket4j.Bucket;

/**
 * @Author    Md. Mizanur Rahman
 * @Since     August 1, 2022
 * @version   1.0.0
 */
public interface RateLimiterService {
    Bucket resolveBucket(String apiKey);
}
