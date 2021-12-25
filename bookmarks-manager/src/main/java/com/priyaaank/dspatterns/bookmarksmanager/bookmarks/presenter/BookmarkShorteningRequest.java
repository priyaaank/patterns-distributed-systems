package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.presenter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookmarkShorteningRequest {

    @JsonProperty("uri")
    private String uri;

}
