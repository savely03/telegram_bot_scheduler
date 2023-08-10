package pro.sky.telegrambot.command;

import com.pengrad.telegrambot.model.Message;

public interface BotCommand {
    void execute(Message message);
}
