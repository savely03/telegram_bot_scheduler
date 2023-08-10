package pro.sky.telegrambot.command.impl;

import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.command.BotCommand;
import pro.sky.telegrambot.service.BotService;

@Service
@RequiredArgsConstructor
public class BotCommandStart implements BotCommand {

    private static final String WELCOME_MESSAGE = """
            Здравствуйте!
            Чтобы создать напоминание, введите текст в формате 'dd.MM.yyyy HH:mm Some text'
            """;
    private final BotService botService;

    @Override
    public void execute(Message message) {
        botService.sendMessage(message.chat().id(), WELCOME_MESSAGE);
    }
}
