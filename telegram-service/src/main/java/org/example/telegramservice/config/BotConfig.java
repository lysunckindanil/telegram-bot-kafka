package org.example.telegramservice.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
@PropertySource("classpath:token.properties")
@Configuration
public class BotConfig {
    @Value("${bot.token}")
    String token;
}
