package com.sunnyz.iiwebapi.schedule;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class HelloSchedule {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Scheduled(cron = "0 0 1 * * ?")
    public void run() {

    }
}
