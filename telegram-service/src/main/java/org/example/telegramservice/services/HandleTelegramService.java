package org.example.telegramservice.services;

import lombok.RequiredArgsConstructor;
import org.example.telegramservice.dto.TelegramMessageDto;
import org.example.telegramservice.dto.TelegramMessageResponseDto;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequiredArgsConstructor
@Service
@EnableFeignClients
public class HandleTelegramService {
    private final TelegramHandleService telegramHandleService;

    public String handleTextMessage(long chat_id, String message) {
        TelegramMessageDto body = TelegramMessageDto.builder().chat_id(chat_id).message(message).build();
        return telegramHandleService.handleTextMessage(body).getMessage();
    }

    @FeignClient("telegram-handle-service")
    interface TelegramHandleService {
        @RequestMapping(method = RequestMethod.POST, value = "/api/handle_text_message")
        TelegramMessageResponseDto handleTextMessage(@RequestBody TelegramMessageDto telegramMessage);
    }
}
