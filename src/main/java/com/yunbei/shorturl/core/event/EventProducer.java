package com.yunbei.shorturl.core.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yunbei.shorturl.core.cache.RedisCache;

@Service
public class EventProducer {

    private static final Logger LOG = LoggerFactory.getLogger(EventProducer.class);

    @Autowired
    private RedisCache redisCache;

    public boolean submit(Event event) {

        try {
            String key = redisCache.genKey(event);
            redisCache.lpushObj(key, event);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return true;
    }

}
