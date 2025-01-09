package org.example.telegramhandleservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.telegramhandleservice.dto.TelegramMessageDto;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {
    private final KafkaTemplate<String, TelegramMessageDto> kafkaTemplate;

    public void sendMessage(String message, long chat_id) {
        TelegramMessageDto telegramMessageDto = TelegramMessageDto.builder().chat_id(chat_id).message(message).build();
        kafkaTemplate.send("text_notification", telegramMessageDto);
        log.info("Message sent to text_notification topic");
    }
}
