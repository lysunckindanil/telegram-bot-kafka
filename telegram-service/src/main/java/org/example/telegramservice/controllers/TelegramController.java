package org.example.telegramservice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.telegramservice.dto.TelegramMessageDto;
import org.example.telegramservice.services.TelegramBot;
import org.example.telegramservice.services.TelegramHandleClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@EnableFeignClients
public class TelegramController {
    private final TelegramBot telegramBot;
    private final TelegramHandleClient telegramHandleServiceTest;

    @PostMapping("send_text_message")
    public void sendMessage(@RequestBody TelegramMessageDto message) {
        telegramBot.sendMessage(message.getChat_id(), message.getMessage());
    }

    @PostMapping("test_bot")
    public ResponseEntity<?> testBot(@RequestBody TelegramMessageDto message) {
        telegramHandleServiceTest.handleTextMessage(message);
        return ResponseEntity.ok().build();
    }
}
