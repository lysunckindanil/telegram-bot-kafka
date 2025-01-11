package org.example.telegramhandleservice.services;

import org.example.telegramhandleservice.dto.TelegramMessageTopicDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {
    @Mock
    KafkaTemplate<String, TelegramMessageTopicDto> kafkaTemplate;

    @InjectMocks
    NotificationService notificationService;

    @Test
    void sendMessageAny_SendsToKafka() {
        TelegramMessageTopicDto dto = TelegramMessageTopicDto.builder().message("message").build();
        notificationService.sendMessageAny("message");
        Mockito.verify(kafkaTemplate, Mockito.times(1)).send("send-any", dto);
    }

    @Test
    void sendMessageAll_SendsToKafka() {
        TelegramMessageTopicDto dto = TelegramMessageTopicDto.builder().message("message").build();
        notificationService.sendMessageAll("message");
        Mockito.verify(kafkaTemplate, Mockito.times(1)).send("send-all", dto);
    }
}