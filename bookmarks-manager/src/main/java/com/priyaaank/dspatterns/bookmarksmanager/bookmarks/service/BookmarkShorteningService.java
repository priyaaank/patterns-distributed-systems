package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.service;

import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain.Bookmark;
import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.presenter.BookmarkShorteningRequest;
import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.presenter.BookmarkShorteningResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BookmarkShorteningService {

    private RestTemplate restTemplate;
    private String urlServiceHost;

    @Autowired
    public BookmarkShorteningService(@Qualifier("restClient") RestTemplate restTemplate,
                                     @Value("${services.urlshortner.hostport}") String urlServiceHost) {
        //A common thread pool
        this.restTemplate = restTemplate;
        this.urlServiceHost = urlServiceHost;
    }

    //Make API call to another web service over HTTP get short url
    public Bookmark shorten(Bookmark bookmark) {
        String url = urlServiceHost + "/uri/shorten";
        BookmarkShorteningRequest requestBody = new BookmarkShorteningRequest(bookmark.getLongUrl());
        ResponseEntity<BookmarkShorteningResponse> shortendUrlResponse = this.restTemplate.postForEntity(url, requestBody, BookmarkShorteningResponse.class);

        return bookmark.cloneBuilder().shortenedUrl(shortendUrlResponse.getBody().getShortUri()).build();
    }

}
