package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.service;

import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain.Bookmark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BookmarkTextResolver {

    private RestTemplate restTemplate;
    private String urlServiceHost;

    @Autowired
    public BookmarkTextResolver(@Qualifier("restTemplatePoolWithoutTimeout") RestTemplate restTemplate,
                                @Value("${services.urlshortner.hostport}") String urlServiceHost) {
        this.restTemplate = restTemplate;
        this.urlServiceHost = urlServiceHost;
    }

    public Bookmark fetchText(Bookmark bookmark) {
        String url = urlServiceHost + "/uri/text?url=" + bookmark.getLongUrl();
        ResponseEntity<String> urlTitleResponse = this.restTemplate.getForEntity(url, String.class);

        return bookmark.cloneBuilder().text(urlTitleResponse.getBody()).build();
    }

    public String enqueue(Bookmark bookmark) {
        String url = urlServiceHost + "/uri/text/async?url=" + bookmark.getLongUrl();
        ResponseEntity<String> urlTitleResponse = this.restTemplate.getForEntity(url, String.class);

        return urlTitleResponse.getBody();
    }
}
