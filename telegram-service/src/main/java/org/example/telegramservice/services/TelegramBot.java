package org.example.telegramservice.services;

import org.example.telegramservice.config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;


@Component
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private final BotConfig botConfig;
    private final HandleTelegramService handleTelegramService;

    public TelegramBot(BotConfig botConfig, HandleTelegramService handleTelegramService) throws TelegramApiException {
        this.handleTelegramService = handleTelegramService;
        this.botConfig = botConfig;
        telegramClient = new OkHttpTelegramClient(botConfig.getToken());
        BotCommand command1 = BotCommand.builder().command("/send_all").description("to send every body").build();
        BotCommand command2 = BotCommand.builder().command("/send_any").description("to send any").build();
        BotCommand command3 = BotCommand.builder().command("/disable").description("to disable notifications").build();
        BotCommand command4 = BotCommand.builder().command("/enable").description("to enable notifications").build();
        BotCommand command5 = BotCommand.builder().command("/status").description("to see status").build();
        List<BotCommand> LIST_OF_COMMANDS = List.of(command1, command2, command3, command4, command5);
        telegramClient.execute(new SetMyCommands(LIST_OF_COMMANDS, new BotCommandScopeDefault(), null));
    }

    @Override
    public void consume(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chat_id = update.getMessage().getChatId();
            String response = handleTelegramService.handleTextMessage(chat_id, update.getMessage().getText());
            sendMessage(chat_id, response);
        }
    }

    public void sendMessage(long chat_id, String message_text) {
        SendMessage message = SendMessage
                .builder()
                .chatId(chat_id)
                .text(message_text)
                .build();
        try {
            telegramClient.execute(message);
        } catch (TelegramApiException ignored) {
        }
    }

    @Override
    public String getBotToken() {
        return this.botConfig.getToken();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }
}
