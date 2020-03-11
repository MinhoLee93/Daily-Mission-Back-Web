package com.dailymission.api.springboot.web.service.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CacheEvictService {

    Logger log = LoggerFactory.getLogger(CacheEvictService.class);

    /**
    * 매주 일요일 3시 0분 1초에 schedule 캐시 삭제
    * week 가 바뀌었으므로..
    * */
    @Scheduled(cron = "1 0 3 * * SUN")
    @CacheEvict(value = "schedules", allEntries = true)
    public void removeCache(){
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>> remove cache");
    }
}
