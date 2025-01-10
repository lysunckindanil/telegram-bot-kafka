package org.example.telegramnotificationservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.telegramnotificationservice.dto.TelegramMessageDto;
import org.example.telegramnotificationservice.dto.TelegramMessageTopicDto;
import org.example.telegramnotificationservice.entities.Chat;
import org.example.telegramnotificationservice.repos.ChatRepository;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@EnableFeignClients
@Service
public class NotificationService {

    private final TelegramServiceClient telegramServiceClient;
    private final ChatRepository chatRepository;

    @KafkaListener(topics = "send-all", groupId = "notification")
    public void sendMessageAll(TelegramMessageTopicDto topic) {
        log.info("text_notification topic: got message {}", topic);

        List<Chat> chats = chatRepository.findAll().stream().filter(x -> x.getStatus().equals("enabled")).toList();
        if (!chats.isEmpty()) {
            Random random = new Random();
            long chat_id = chats.get(random.nextInt(chats.size())).getChatId();
            TelegramMessageDto telegramMessageDto = TelegramMessageDto.builder().chat_id(chat_id).message(topic.getMessage()).build();
            telegramServiceClient.sendMessage(telegramMessageDto);
        }
    }

    @KafkaListener(topics = "send-any", groupId = "notification")
    public void sendMessageAny(TelegramMessageTopicDto topic) {
        log.info("text_notification topic: got message {}", topic);

        chatRepository.findAll().stream()
                .filter(x -> x.getStatus().equals("enabled"))
                .map(Chat::getChatId)
                .forEach((chat_id -> {
                    TelegramMessageDto telegramMessageDto = TelegramMessageDto.builder().chat_id(chat_id).message(topic.getMessage()).build();
                    telegramServiceClient.sendMessage(telegramMessageDto);
                }));
    }


    @FeignClient("telegram-service")
    interface TelegramServiceClient {
        @RequestMapping(method = RequestMethod.POST, value = "api/send_text_message")
        TelegramMessageDto sendMessage(@RequestBody TelegramMessageDto message);
    }
}
