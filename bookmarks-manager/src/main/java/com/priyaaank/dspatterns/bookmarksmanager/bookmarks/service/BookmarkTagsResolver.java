package com.priyaaank.dspatterns.bookmarksmanager.bookmarks.service;

import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.domain.Bookmark;
import com.priyaaank.dspatterns.bookmarksmanager.bookmarks.presenter.BookmarksTagsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookmarkTagsResolver {

    private RestTemplate restTemplate;
    private String urlServiceHost;

    @Autowired
    public BookmarkTagsResolver(@Qualifier("restTemplatePoolWithoutTimeout") RestTemplate restTemplate,
                                @Value("${services.tagservice.hostport}") String urlServiceHost) {
        this.restTemplate = restTemplate;
        this.urlServiceHost = urlServiceHost;
    }

    public Bookmark fetchTags(Bookmark bookmark) {
        String url = urlServiceHost + "/tags/generate?url=" + bookmark.getLongUrl();
        ResponseEntity<BookmarksTagsResponse> tagsResponse = this.restTemplate.getForEntity(url, BookmarksTagsResponse.class);
        List<String> tags = tagsResponse == null ? new ArrayList<>() : tagsResponse.getBody().getTags();
        return bookmark.cloneBuilder().tags(tags).build();
    }

}
