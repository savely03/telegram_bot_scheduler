package pro.sky.telegrambot.service;

import pro.sky.telegrambot.entity.NotificationTask;

import java.util.Optional;

public interface ParseService {
    Optional<NotificationTask> parseMessageToNotificationTask(Long chatId, String messageText);
}
