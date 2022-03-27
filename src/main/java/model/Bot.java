package model;

import lombok.SneakyThrows;
import model.handlers.InlineUpdateHandler;
import model.handlers.MessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {
    private static final Logger logger = LoggerFactory.getLogger(Bot.class);

    private final String username;
    private final String token;
    private final MessageHandler messageHandler = new MessageHandler();
    private static final int CACHE_TIME = 10;

    public Bot(String username, String token) {
        super();
        this.username = username;
        this.token = token;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        if (update.hasInlineQuery()) {
            try {
                InlineUpdateHandler inlineUpdateHandler = new InlineUpdateHandler();
                inlineUpdateHandler.setInlineQueryResults(update.getInlineQuery());
                execute(AnswerInlineQuery.builder()
                        .inlineQueryId(update.getInlineQuery().getId())
                        .results(inlineUpdateHandler.getInlineQueryResults())
                        .cacheTime(CACHE_TIME)
                        .build());
            } catch (TelegramApiException e) {
                logger.error(e.getLocalizedMessage(), e);
                e.printStackTrace();
            }
        } else if (update.hasMessage() && update.getMessage().isUserMessage()) {
            if (messageHandler.parseCommand(update.getMessage()).isPresent()) {
                try {
                    execute(SendMessage.builder()
                            .text(messageHandler.parseCommand(update.getMessage()).get())
                            .chatId(update.getMessage().getChatId().toString())
                            .build());
                } catch (TelegramApiException e) {
                    logger.error(e.getLocalizedMessage(), e);
                    e.printStackTrace();
                }
            }
        }
    }
}
