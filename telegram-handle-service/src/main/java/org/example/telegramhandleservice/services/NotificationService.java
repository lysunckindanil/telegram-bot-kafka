package org.example.telegramhandleservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.telegramhandleservice.dto.TelegramMessageTopicDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {
    private final KafkaTemplate<String, TelegramMessageTopicDto> kafkaTemplate;

    public void sendMessageAny(String message) {
        TelegramMessageTopicDto messageTopicDto = TelegramMessageTopicDto.builder().message(message).build();
        kafkaTemplate.send("send-any", messageTopicDto);
    }

    public void sendMessageAll(String message) {
        TelegramMessageTopicDto messageTopicDto = TelegramMessageTopicDto.builder().message(message).build();
        kafkaTemplate.send("send-all", messageTopicDto);
    }
}
