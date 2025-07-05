package com.osho.journalApp.scheduler;

import com.osho.journalApp.cache.AppCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CacheScheduler {

    private final AppCache appCache;

    @Autowired
    public CacheScheduler(AppCache appCache) {
        this.appCache = appCache;
    }

    @Scheduled(cron = "0 */5 * * * ?")
    public void clearCache(){
        appCache.init();
    }
}
