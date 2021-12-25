package com.priyaaank.dspatterns.urishortner.bookmarks.presenter;

import com.priyaaank.dspatterns.urishortner.bookmarks.domain.Uri;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShortenUriRequest {

    private String uri;

    public Uri toDomain() {
        return new Uri(this.uri);
    }
}
