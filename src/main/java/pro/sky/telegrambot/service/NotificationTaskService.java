package pro.sky.telegrambot.service;

import pro.sky.telegrambot.entity.NotificationTask;

import java.util.List;

public interface NotificationTaskService {
    NotificationTask createTask(NotificationTask task);

    List<NotificationTask> findAllByDateTime();

    void notifyScheduledTasks();
}
