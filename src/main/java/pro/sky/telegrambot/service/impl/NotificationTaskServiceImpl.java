package pro.sky.telegrambot.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;
import pro.sky.telegrambot.service.NotificationTaskService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationTaskServiceImpl implements NotificationTaskService {

    private final NotificationTaskRepository notificationTaskRepository;
    private final TelegramBot telegramBot;

    @Override
    @Transactional
    public NotificationTask createTask(NotificationTask task) {
        return notificationTaskRepository.save(task);
    }

    @Override
    public List<NotificationTask> findAllByDateTime() {
        return notificationTaskRepository.findAllByDateTimeLessThanEqual(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
    }

    @Override
    @Transactional
    public void notifyScheduledTasks() {
        List<NotificationTask> tasks = findAllByDateTime();
        tasks.forEach(task -> telegramBot.execute(new SendMessage(task.getChatId(), task.getText())));
        notificationTaskRepository.deleteAll(tasks);
    }
}
