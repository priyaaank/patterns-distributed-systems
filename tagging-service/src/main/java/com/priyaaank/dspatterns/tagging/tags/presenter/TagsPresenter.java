package com.priyaaank.dspatterns.tagging.tags.presenter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.priyaaank.dspatterns.tagging.tags.domain.Tags;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class TagsPresenter {

    @JsonProperty("url")
    private String url;

    @JsonProperty("tags")
    private String[] tags;

    public static TagsPresenter fromDomain(String url, Tags tags) {
        return new TagsPresenter(url, tags.getTagValues().toArray(new String[0]));
    }
}
