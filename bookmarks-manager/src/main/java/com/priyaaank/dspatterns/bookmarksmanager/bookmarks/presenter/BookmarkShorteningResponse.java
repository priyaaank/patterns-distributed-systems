package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.presenter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BookmarkShorteningResponse {

    @JsonProperty("longUri")
    private String longUri;

    @JsonProperty("shortUri")
    private String shortUri;

}
