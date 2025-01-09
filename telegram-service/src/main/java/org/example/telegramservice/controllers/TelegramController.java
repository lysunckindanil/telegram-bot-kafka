package org.example.telegramservice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.telegramservice.dto.TelegramMessageDto;
import org.example.telegramservice.services.TelegramBot;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class TelegramController {
    private final TelegramBot telegramBot;

    @PostMapping("send_text_message")
    public void sendMessage(@RequestBody TelegramMessageDto message) {
        telegramBot.sendMessage(message.getChat_id(), message.getMessage());
    }
}
