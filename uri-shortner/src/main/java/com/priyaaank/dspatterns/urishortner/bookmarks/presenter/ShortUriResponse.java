package com.priyaaank.dspatterns.urishortner.bookmarks.presenter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.priyaaank.dspatterns.urishortner.bookmarks.domain.Url;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShortUriResponse {

    @JsonProperty("longUri")
    private String longUri;

    @JsonProperty("shortUri")
    private String shortUri;

    public static ShortUriResponse fromDomain(Url shortUri) {
        return new ShortUriResponse(shortUri.getLongUrl(), "/"+shortUri.getShortUri());
    }
}
