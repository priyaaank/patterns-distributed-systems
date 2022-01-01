package com.priyaaank.dspatterns.urishortner.bookmarks.service;

import com.priyaaank.dspatterns.urishortner.bookmarks.domain.Url;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UriShorteningService {

    @SneakyThrows
    public Url generateShortUri(Url uri) {
        return new Url(uri.getLongUrl(), UUID.randomUUID().toString().replace("-","").substring(0,8));
    }

}
