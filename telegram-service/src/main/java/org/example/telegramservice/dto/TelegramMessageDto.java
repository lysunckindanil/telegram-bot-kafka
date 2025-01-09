package org.example.telegramservice.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TelegramMessageDto {
    long chat_id;
    String message;
}
