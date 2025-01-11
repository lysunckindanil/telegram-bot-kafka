package org.example.telegramhandleservice.services;

import org.example.telegramhandleservice.dto.TelegramMessageDto;
import org.example.telegramhandleservice.entities.Chat;
import org.example.telegramhandleservice.entities.ChatSession;
import org.example.telegramhandleservice.repos.ChatRepository;
import org.example.telegramhandleservice.repos.ChatSessionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ExtendWith(MockitoExtension.class)
class HandleTelegramTextMessageServiceTest {
    private final HandleTelegramTextMessageService service;
    private final NotificationService notificationService;
    private final ChatRepository chatRepository;
    private final ChatSessionRepository chatSessionRepository;

    @Autowired
    public HandleTelegramTextMessageServiceTest(ChatRepository chatRepository, ChatSessionRepository chatSessionRepository) {
        this.chatRepository = chatRepository;
        this.chatSessionRepository = chatSessionRepository;
        notificationService = Mockito.mock(NotificationService.class);
        Mockito.doNothing().when(notificationService).sendMessageAny(any());
        Mockito.doNothing().when(notificationService).sendMessageAll(any());
        this.service = new HandleTelegramTextMessageService(chatRepository, chatSessionRepository, notificationService);
    }


    @Test
    void handleTelegramTextMessage_GivenStartMessage_ReturnsWelcomeMessage() {
        TelegramMessageDto dto = TelegramMessageDto.builder().chat_id(1L).message("/start").build();
        Assertions.assertEquals("Welcome to bot!", service.handleTelegramTextMessage(dto));
        Mockito.verify(notificationService, Mockito.times(0)).sendMessageAny(any());
    }

    @Test
    void handleTelegramTextMessage_GivenWrongMessage_ReturnsUnrecognizedMessage() {
        TelegramMessageDto dto = TelegramMessageDto.builder().chat_id(1L).message("1").build();
        Assertions.assertEquals("I don't know that command!", service.handleTelegramTextMessage(dto));
    }

    @Test
    void handleTelegramTextMessage_GivenSendAnyMessage_AddsToSessions_ReturnsAnswer() {
        TelegramMessageDto dto = TelegramMessageDto.builder().chat_id(1L).message("/send_any").build();
        Assertions.assertEquals("Write your message, it will be sent to a random person!", service.handleTelegramTextMessage(dto));
        Assertions.assertTrue(chatSessionRepository.findById(1L).isPresent());
        Assertions.assertEquals("send_any", chatSessionRepository.getReferenceById(1L).getSession());
    }

    @Test
    void handleTelegramTextMessage_GivenSendAllMessage_AddsToSessions_ReturnsAnswer() {
        TelegramMessageDto dto = TelegramMessageDto.builder().chat_id(1L).message("/send_all").build();
        Assertions.assertEquals("Write your message, it will be sent to everybody!", service.handleTelegramTextMessage(dto));
        Assertions.assertTrue(chatSessionRepository.findById(1L).isPresent());
        Assertions.assertEquals("send_all", chatSessionRepository.getReferenceById(1L).getSession());
    }

    @Test
    void handleTelegramTextMessage_WithSessionSendAny_ReturnsAnswer_NotificationServiceExecuted_SessionDeleted() {
        chatSessionRepository.save(ChatSession.builder().chatId(1L).session("send_any").build());
        TelegramMessageDto dto = TelegramMessageDto.builder().chat_id(1L).message(any()).build();

        Assertions.assertEquals("Your message has been sent to the random person!", service.handleTelegramTextMessage(dto));
        Mockito.verify(notificationService, Mockito.times(1)).sendMessageAny(any());
        Assertions.assertTrue(chatSessionRepository.findById(1L).isEmpty());
    }

    @Test
    void handleTelegramTextMessage_WithSessionSendAll_ReturnsAnswer_NotificationServiceExecuted_SessionDeleted() {
        chatSessionRepository.save(ChatSession.builder().chatId(1L).session("send_all").build());
        TelegramMessageDto dto = TelegramMessageDto.builder().chat_id(1L).message(any()).build();
        Assertions.assertEquals("Your message has been sent to everybody!", service.handleTelegramTextMessage(dto));
        Mockito.verify(notificationService, Mockito.times(1)).sendMessageAll(any());
        Assertions.assertTrue(chatSessionRepository.findById(1L).isEmpty());
    }

    @Test
    void handleTelegramTextMessage_EnableStatus_StatusEnabled() {
        chatRepository.save(Chat.builder().chatId(1L).status("disabled").build());
        TelegramMessageDto dto = TelegramMessageDto.builder().chat_id(1L).message("/enable").build();
        Assertions.assertEquals("Notifications will be on!", service.handleTelegramTextMessage(dto));
        Assertions.assertEquals("enabled", chatRepository.getReferenceById(1L).getStatus());
    }

    @Test
    void handleTelegramTextMessage_DisableStatus_StatusDisabled() {
        chatRepository.save(Chat.builder().chatId(1L).status("enabled").build());
        TelegramMessageDto dto = TelegramMessageDto.builder().chat_id(1L).message("/disable").build();
        Assertions.assertEquals("Notifications will be off!", service.handleTelegramTextMessage(dto));
        Assertions.assertEquals("disabled", chatRepository.getReferenceById(1L).getStatus());
    }

    @Test
    void handleTelegramTextMessage_GetStatus_StatusEnabled() {
        chatRepository.save(Chat.builder().chatId(1L).status("enabled").build());
        TelegramMessageDto dto = TelegramMessageDto.builder().chat_id(1L).message("/status").build();
        Assertions.assertEquals("Notifications are on!", service.handleTelegramTextMessage(dto));
    }

    @Test
    void handleTelegramTextMessage_GetStatus_StatusDisabled() {
        chatRepository.save(Chat.builder().chatId(1L).status("disabled").build());
        TelegramMessageDto dto = TelegramMessageDto.builder().chat_id(1L).message("/status").build();
        Assertions.assertEquals("Notifications are off!", service.handleTelegramTextMessage(dto));
    }
}