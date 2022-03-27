package model.handlers;

import lombok.NoArgsConstructor;
import model.TextModifier;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.helpCommand.HelpCommand;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;

import java.util.Optional;

@NoArgsConstructor
public class MessageHandler {

    private static final TextModifier textModifier = new TextModifier();
    private static final HelpCommand helpCommand = new HelpCommand("help", "Помощь",
            "Для того чтобы преобразовать текст, используйте команду /vatanize \n" +
                    "Бот также поддерживает inline-режим - его можно использовать в любом чате через юзернейм @vatanizerbot");

    public Optional<String> parseCommand(Message message) {
        if (message.hasEntities() && message.hasText()) {
            Optional<MessageEntity> commandEntity =
                    message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (commandEntity.isPresent()) {
                String command = message
                        .getText()
                        .substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
                if (command.contains("/vatanize")) {
                    if (message.isReply()) {
                        return Optional.of(textModifier.changeText(message.getReplyToMessage().getText()));
                    } else {
                        return Optional.of(textModifier.changeText(message.getText().substring(message.getText().indexOf(' '))));
                    }
                } else if (command.contains("/help")) {
                    return Optional.of(helpCommand.getExtendedDescription());
                }
            }
        }
        return Optional.empty();
    }
}
