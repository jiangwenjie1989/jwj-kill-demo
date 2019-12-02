package com.jwj.im.service;

import com.jwj.im.response.SimpleResponse;

public interface KillService {


    SimpleResponse robGoodsByAopLock(Long userId, Long killActivityId);

    SimpleResponse robGoodsByRedissonLock(Long userId, Long killActivityId);

    SimpleResponse robGoodsByRedisPush(Long userId, Long killActivityId);

    SimpleResponse robGoodsByOptimismLock(Long userId, Long killActivityId);
}
