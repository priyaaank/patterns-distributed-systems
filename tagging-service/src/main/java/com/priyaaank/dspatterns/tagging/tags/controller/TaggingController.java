package com.priyaaank.dspatterns.tagging.tags.controller;

import com.priyaaank.dspatterns.tagging.tags.domain.Tags;
import com.priyaaank.dspatterns.tagging.tags.domain.Url;
import com.priyaaank.dspatterns.tagging.tags.presenter.TagsPresenter;
import com.priyaaank.dspatterns.tagging.tags.service.TaggingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/tags")
public class TaggingController {

    private TaggingService taggingService;

    @Autowired
    public TaggingController(TaggingService taggingService) {
        this.taggingService = taggingService;
    }

    @GetMapping("/generate")
    public TagsPresenter generate(@RequestParam String url) throws InterruptedException {
        Tags tags = taggingService.generateTagsFor(new Url(url));
        return TagsPresenter.fromDomain(url, tags);
    }
}
