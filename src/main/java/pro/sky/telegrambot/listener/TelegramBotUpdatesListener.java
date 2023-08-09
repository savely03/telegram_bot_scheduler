package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.service.MessageService;
import pro.sky.telegrambot.service.NotificationTaskService;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final TelegramBot telegramBot;
    private final NotificationTaskService notificationTaskService;
    private final MessageService messageService;
    private static final String WELCOME_MESSAGE = "Введите уведомление в формате 'dd.MM.yyyy HH:mm Some text'";
    private static final String SUCCESS_MESSAGE = "Ожидайте уведомление!";
    private static final String WARN_MESSAGE = "Проверьте правильность введенных данных";

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            Message message = update.message();
            if ("/start".equals(message.text())) {
                messageService.sendMessage(getChatId(message), WELCOME_MESSAGE);
            }
            messageService.parseMessageToNotificationTask(getChatId(message), message.text()).ifPresentOrElse(
                    task -> messageService.sendMessage(notificationTaskService.createTask(task).getChatId(), SUCCESS_MESSAGE),
                    () -> messageService.sendMessage(getChatId(message), WARN_MESSAGE)
            );
            log.info("Processing update: {}", update);
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void process() {
        log.info("Processing notify");
        notificationTaskService.notifyScheduledTasks();
    }

    private Long getChatId(Message message) {
        return message.chat().id();
    }

}
