package com.priyaaank.dspatterns.urishortner.bookmarks.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitMqListener {

    public void receiveMessage(String message) {
        log.info(message);
    }

}
