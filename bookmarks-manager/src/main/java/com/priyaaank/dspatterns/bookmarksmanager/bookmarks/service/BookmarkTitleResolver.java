package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.service;

import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain.Bookmark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BookmarkTitleResolver {

    private RestTemplate restTemplate;
    private String urlServiceHost;

    @Autowired
    public BookmarkTitleResolver(@Qualifier("restClient") RestTemplate restTemplate,
                                 @Value("${services.urlshortner.hostport}") String urlServiceHost) {
        this.restTemplate = restTemplate;
        this.urlServiceHost = urlServiceHost;
    }

    public Bookmark fetchTitle(Bookmark bookmark) {
        String url = urlServiceHost + "/uri/title?longUrl=" + bookmark.getLongUrl();
        ResponseEntity<String> urlTitleResponse = this.restTemplate.getForEntity(url, String.class);

        return bookmark.cloneBuilder().title(urlTitleResponse.getBody()).build();
    }
}
