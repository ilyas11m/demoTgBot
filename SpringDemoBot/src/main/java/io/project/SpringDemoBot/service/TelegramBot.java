package io.project.SpringDemoBot.service;

import io.project.SpringDemoBot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    BotConfig config;

    static final String HELP_TEXT = "This is demo bot made by Ilyas Muftiev";

    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> listofCommands = new ArrayList<>();
        listofCommands.add(new BotCommand("/start", "get a welcome message"));
        listofCommands.add(new BotCommand("/mydata", "get a data stored about user"));
        listofCommands.add(new BotCommand("/deletedata", "delete your data"));
        listofCommands.add(new BotCommand("/help", "get a help about commands"));
        listofCommands.add(new BotCommand("/settings", "set your preferences"));
        try {
            this.execute(new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null));
        }
        catch (TelegramApiException e) {

        }
    }

    @Override
    public String getBotUsername() {
        return config.botName;
    }

    @Override
    public String getBotToken() {
        return config.token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText) {
                case "нет":
                    sendMessage(chatId, "Не ври, сегодня ты не ел кексики");
                    break;
                case "ok":
                    sendMessage(chatId, "Fine i'll do it myself");
                    break;
                case "/help":
                    sendMessage(chatId, HELP_TEXT);
                    break;
                case "/love":
                    sendMessage(chatId, "I love you, dear user <3!");
                    break;
                default:
                    sendMessage(chatId, "Sorry, command was not recognised");
                    break;
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
            }
        }
    }

    private void startCommandReceived(long chatId, String name) {
        String answer = "Привет, пользователь " + name + ", завтракал сегодня ? Напиши да/нет";
        sendMessage(chatId, answer);

    }
    private void sendMessage (long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try {
            execute(message);
        }
        catch (TelegramApiException e) {
        }
    }
}
