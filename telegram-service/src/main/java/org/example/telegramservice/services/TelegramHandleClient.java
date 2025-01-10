package org.example.telegramservice.services;

import org.example.telegramservice.dto.TelegramMessageDto;
import org.example.telegramservice.dto.TelegramMessageResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient("telegram-handle-service")
public interface TelegramHandleClient {
    @RequestMapping(method = RequestMethod.POST, value = "/api/handle_text_message")
    TelegramMessageResponseDto handleTextMessage(@RequestBody TelegramMessageDto telegramMessage);
}
