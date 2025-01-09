package org.example.telegramhandleservice.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TelegramMessageResponseDto {
    private String message;
}
