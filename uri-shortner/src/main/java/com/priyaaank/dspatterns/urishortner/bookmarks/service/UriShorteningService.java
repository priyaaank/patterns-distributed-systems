package com.priyaaank.dspatterns.urishortner.bookmarks.service;

import com.priyaaank.dspatterns.urishortner.bookmarks.domain.Uri;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UriShorteningService {

    public Uri generateShortUri(Uri uri) {
        return new Uri(uri.getLongUri(), UUID.randomUUID().toString().replace("-","").substring(0,8));
    }

}
