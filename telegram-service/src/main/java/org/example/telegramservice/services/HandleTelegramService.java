package org.example.telegramservice.services;

import lombok.RequiredArgsConstructor;
import org.example.telegramservice.dto.TelegramMessageDto;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@EnableFeignClients
public class HandleTelegramService {
    private final TelegramHandleClient telegramHandleService;

    public String handleTextMessage(long chat_id, String message) {
        TelegramMessageDto body = TelegramMessageDto.builder().chat_id(chat_id).message(message).build();
        return telegramHandleService.handleTextMessage(body).getMessage();
    }
}
