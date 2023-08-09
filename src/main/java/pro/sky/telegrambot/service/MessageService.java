package pro.sky.telegrambot.service;

import pro.sky.telegrambot.entity.NotificationTask;

import java.util.Optional;

public interface MessageService {
    Optional<NotificationTask> parseMessageToNotificationTask(Long chatId, String messageText);

    void sendMessage(Long chatId, String text);
}
