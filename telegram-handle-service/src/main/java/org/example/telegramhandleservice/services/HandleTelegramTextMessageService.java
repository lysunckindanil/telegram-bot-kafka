package org.example.telegramhandleservice.services;

import lombok.RequiredArgsConstructor;
import org.example.telegramhandleservice.dto.TelegramMessageDto;
import org.example.telegramhandleservice.entities.Chat;
import org.example.telegramhandleservice.entities.ChatSession;
import org.example.telegramhandleservice.repos.ChatRepository;
import org.example.telegramhandleservice.repos.ChatSessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class HandleTelegramTextMessageService {

    private final ChatRepository chatRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final NotificationService notificationService;

    public String handleTelegramTextMessage(TelegramMessageDto telegramMessageDto) {
        String message = telegramMessageDto.getMessage();
        long chat_id = telegramMessageDto.getChat_id();
        Optional<ChatSession> sessionStatus = getSessionStatus(chat_id);
        if (sessionStatus.isEmpty())
            return switch (message) {
                case "/start" -> start(chat_id);
                case "/status" -> status(chat_id);
                case "/send_any" -> sendAny(chat_id);
                case "/send_all" -> sendAll(chat_id);
                case "/enable" -> enable(chat_id);
                case "/disable" -> disable(chat_id);
                default -> "I don't know that command!";
            };
        if (sessionStatus.get().getSession().equals("send_any")) {
            sendNotificationAny(message);
            chatSessionRepository.deleteById(chat_id);
            return "Your message has been sent to the random person!";
        }
        if (sessionStatus.get().getSession().equals("send_all")) {
            sendNotificationAll(message);
            chatSessionRepository.deleteById(chat_id);
            return "Your message has been sent to everybody!";
        }
        return "I can't recognize your status!";
    }

    private Optional<ChatSession> getSessionStatus(long chat_id) {
        return chatSessionRepository.findById(chat_id);
    }

    private void sendNotificationAny(String message) {
        Random random = new Random();
        List<Chat> chats = chatRepository.findAll().stream().filter(x -> x.getStatus().equals("enabled")).toList();
        if (!chats.isEmpty())
            notificationService.sendMessage(message, chats.get(random.nextInt(chats.size())).getChatId());
    }

    private void sendNotificationAll(String message) {
        chatRepository.findAll().stream()
                .filter(x -> x.getStatus().equals("enabled"))
                .map(Chat::getChatId)
                .forEach((chat_id -> notificationService.sendMessage(message, chat_id)));
    }

    private String start(long chat_id) {
        chatRepository.save(Chat.builder().chatId(chat_id).status("enabled").build());
        return "Welcome to bot!";
    }

    private String sendAny(long chat_id) {
        chatSessionRepository.save(ChatSession.builder().chatId(chat_id).session("send_any").build());
        return "Write your message, it will be sent to a random person!";
    }

    private String sendAll(long chat_id) {
        chatSessionRepository.save(ChatSession.builder().chatId(chat_id).session("send_all").build());
        return "Write your message, it will be sent to everybody!";
    }

    private String status(long chat_id) {
        String status = chatRepository.getReferenceById(chat_id).getStatus();
        if (status.equals("enabled"))
            return "Notifications are on!";
        return "Notifications are off!";
    }

    private String disable(long chat_id) {
        chatRepository.save(Chat.builder().chatId(chat_id).status("disabled").build());
        return "Notifications will be off!";
    }

    private String enable(long chat_id) {
        chatRepository.save(Chat.builder().chatId(chat_id).status("enabled").build());
        return "Notifications will be on!";
    }
}
