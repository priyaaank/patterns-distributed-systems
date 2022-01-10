package com.priyaaank.dspatterns.urishortner.bookmarks.config;

import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class RabbitMqSender {

    public static final String QUEUE_MESSAGE_COUNT = "QUEUE_MESSAGE_COUNT";
    private final RabbitTemplate rabbitTemplate;
    private final RabbitAdmin rabbitAdmin;

    @Autowired
    public RabbitMqSender(RabbitTemplate rabbitTemplate,
                          RabbitAdmin rabbitAdmin) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitAdmin = rabbitAdmin;
    }

    public void sendMessage(String message) {
        Properties queueProperties = rabbitAdmin.getQueueProperties(RabbitMqConfig.queueName);
        this.rabbitTemplate.convertAndSend(RabbitMqConfig.exchangeName, RabbitMqConfig.routingKey, message);
    }
}
