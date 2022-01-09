package com.priyaaank.dspatterns.urishortner.bookmarks.domain;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;

import java.io.IOException;

@Slf4j
public class TextRetriever {

    private Url url;

    public TextRetriever(Url url) {
        this.url = url;
    }

    public String initiate() {
        String retrievedText = null;
        try {
            Document doc = Jsoup.connect(url.getLongUrl()).get();
            retrievedText = doc.text();
        } catch (IOException ioException) {
            log.error("Could not fetch text for the webpage with uri {}", url.getLongUrl());
        } finally {
            retrievedText = StringUtil.isBlank(retrievedText) ? "Text not available for " + url.getLongUrl() : retrievedText;
        }
        log.info("Got text for {} as {}", this.url.getLongUrl(), retrievedText);
        return retrievedText;
    }

}
