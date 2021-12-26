package com.priyaaank.dspatterns.tagging.tags.service;

import com.priyaaank.dspatterns.tagging.tags.config.ThroughputController;
import com.priyaaank.dspatterns.tagging.tags.domain.Tags;
import com.priyaaank.dspatterns.tagging.tags.domain.Url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TaggingService {

    public static final int MAXIMUM_TAG_COUNT = 5;
    private Random randomNumGenerator = new Random();
    private RandomTagSetGenerator randomTagSetGenerator;
    private ThroughputController<Tags> throughputController;

    @Autowired
    public TaggingService(ThroughputController<Tags> throughputController) {
        this.throughputController = throughputController;
        this.randomTagSetGenerator = new RandomTagSetGenerator();
    }

    public Tags generateTagsFor(Url url) throws InterruptedException {
        return throughputController.regulate(this::selectFewTagsAtRandom);
    }

    private Tags selectFewTagsAtRandom() {
        int generateCount = randomNumGenerator.nextInt(MAXIMUM_TAG_COUNT);
        generateCount = generateCount == 0 ? 1 : generateCount;
        return new Tags(randomTagSetGenerator.generate(generateCount));
    }

    //TODO - Replace with an entity extraction service
    class RandomTagSetGenerator {
        private String[] tags = new String[]{
                "Cooking", "Technology", "Medical", "Science", "Blogging", "Podcast", "Space", "Art", "Literature",
                "Hardware", "Entertainment", "Movies", "History", "Conservation", "Architecture", "Archeology",
                "Earth", "Fossils", "Markets", "Finance", "Geology", "Money", "Food", "Nightlife", "Advertising"
        };

        public List<String> generate(Integer count) {
            List<String> generatedTags = new ArrayList<>();
            IntStream.range(0, count).forEach(e -> {
                int generateCount = tags.length < count ? count : tags.length;
                generatedTags.add(tags[randomNumGenerator.nextInt(generateCount)]);
            });

            return generatedTags.stream().distinct().collect(Collectors.toList());
        }

    }

}
