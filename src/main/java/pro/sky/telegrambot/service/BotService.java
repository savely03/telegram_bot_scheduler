package pro.sky.telegrambot.service;

public interface BotService {
    void sendMessage(Long chatId, String message);
}
