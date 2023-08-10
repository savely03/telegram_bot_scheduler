package pro.sky.telegrambot.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.service.BotService;
import pro.sky.telegrambot.service.NotificationTaskService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BotServiceImpl implements BotService {

    private final TelegramBot telegramBot;
    private final NotificationTaskService notificationTaskService;

    @Override
    public void sendMessage(Long chatId, String message) {
        telegramBot.execute(new SendMessage(chatId, message));
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void sendScheduledMessages() {
        List<NotificationTask> tasks = notificationTaskService.findAllByDateTime();
        tasks.forEach(task -> sendMessage(task.getChatId(), task.getText()));
        notificationTaskService.deleteAll(tasks);
    }
}
