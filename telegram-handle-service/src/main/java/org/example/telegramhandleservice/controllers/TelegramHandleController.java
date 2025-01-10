package org.example.telegramhandleservice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.telegramhandleservice.dto.TelegramMessageDto;
import org.example.telegramhandleservice.dto.TelegramMessageResponseDto;
import org.example.telegramhandleservice.services.HandleTelegramTextMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class TelegramHandleController {
    private final HandleTelegramTextMessageService handleTelegramTextMessageService;

    @Value("${eureka.instance.instance-id}")
    private String instance_id;

    @PostMapping("/handle_text_message")
    public TelegramMessageResponseDto handleTextMessage(@RequestBody TelegramMessageDto telegramMessageDto) {
        String message = handleTelegramTextMessageService.handleTelegramTextMessage(telegramMessageDto);
        return TelegramMessageResponseDto.builder().message(message + "\n\n" + instance_id).build();
    }
}
