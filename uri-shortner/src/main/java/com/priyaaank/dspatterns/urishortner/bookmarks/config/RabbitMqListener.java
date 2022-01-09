package com.priyaaank.dspatterns.urishortner.bookmarks.config;

import com.priyaaank.dspatterns.urishortner.bookmarks.domain.TextRetriever;
import com.priyaaank.dspatterns.urishortner.bookmarks.domain.Url;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RabbitMqListener {

    public void receiveMessage(String url) {
        String text = new TextRetriever(new Url(url)).initiate();
        log.info("Retrieved text for url {} as {}", url, text);
    }

}
