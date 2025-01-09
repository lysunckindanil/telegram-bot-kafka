package org.example.telegramhandleservice.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TelegramMessageDto {
    private long chat_id;
    private String message;
}
