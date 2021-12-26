package com.priyaaank.dspatterns.tagging.tags.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public class Tags {

    private List<String> tagValues;

    public Tags(String...tagValues) {
        this.tagValues = Arrays.asList(tagValues);
    }

}
