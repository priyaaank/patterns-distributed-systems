package com.priyaaank.dspatterns.urishortner.bookmarks.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Uri {

    private String longUri;
    private String shortUri;

    public Uri(String longUri) {
        this.longUri = longUri;
    }
}
