package com.priyaaank.dspatterns.urishortner.bookmarks.presenter;

import com.priyaaank.dspatterns.urishortner.bookmarks.domain.Url;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShortenUriRequest {

    private String uri;

    public Url toDomain() {
        return new Url(this.uri);
    }
}
