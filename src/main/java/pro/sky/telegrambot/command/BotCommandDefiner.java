package pro.sky.telegrambot.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.command.impl.BotCommandCreate;
import pro.sky.telegrambot.command.impl.BotCommandStart;

@Service
@RequiredArgsConstructor
public class BotCommandDefiner {
    private final BotCommandCreate botCommandCreate;
    private final BotCommandStart botCommandStart;
    public BotCommand defineCommand(String text) {
        if ("/start".equals(text)) {
            return botCommandStart;
        }
        return botCommandCreate;
    }
}
