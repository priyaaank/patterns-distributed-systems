package com.priyaaank.dspatterns.urishortner.bookmarks.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqSender {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public RabbitMqSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String message) {
        this.rabbitTemplate.convertAndSend(RabbitMqConfig.exchangeName, RabbitMqConfig.routingKey, message);
    }
}
