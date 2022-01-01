package com.priyaaank.dspatterns.urishortner.bookmarks.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Url {

    private String longUrl;
    private String shortUri;

    public Url(String longUrl) {
        this.longUrl = longUrl;
    }

}
