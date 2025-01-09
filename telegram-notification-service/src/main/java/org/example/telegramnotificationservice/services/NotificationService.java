package org.example.telegramnotificationservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.telegramnotificationservice.dto.TelegramMessageDto;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@RequiredArgsConstructor
@EnableFeignClients
@Service
public class NotificationService {

    private final TelegramServiceClient telegramServiceClient;

    @KafkaListener(topics = "text_notification", groupId = "notification")
    public void sendMessage(TelegramMessageDto telegramMessageDto) {
        log.info("text_notification topic: got message {}", telegramMessageDto);
        telegramServiceClient.sendMessage(telegramMessageDto);
    }

    @FeignClient("telegram-service")
    interface TelegramServiceClient {
        @RequestMapping(method = RequestMethod.POST, value = "api/send_text_message")
        TelegramMessageDto sendMessage(@RequestBody TelegramMessageDto message);
    }
}
