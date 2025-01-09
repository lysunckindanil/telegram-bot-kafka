package org.example.telegramservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class TelegramServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TelegramServiceApplication.class, args);
    }

}
