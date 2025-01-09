package org.example.telegramnotificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class TelegramNotificationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelegramNotificationServiceApplication.class, args);
    }

}
