package pro.sky.telegrambot.command.impl;

import com.pengrad.telegrambot.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.command.BotCommand;
import pro.sky.telegrambot.service.BotService;
import pro.sky.telegrambot.service.NotificationTaskService;
import pro.sky.telegrambot.service.ParseService;

@Service
@RequiredArgsConstructor
public class BotCommandCreate implements BotCommand {

    private final ParseService parseService;
    private final BotService botService;
    private final NotificationTaskService notificationTaskService;
    private static final String SUCCESS_MESSAGE = "Ожидайте уведомление!";
    private static final String WARN_MESSAGE = "Проверьте правильность введенных данных";

    @Override
    public void execute(Message message) {
        Long chatId = message.chat().id();
        parseService.parseMessageToNotificationTask(chatId, message.text()).ifPresentOrElse(
                task -> botService.sendMessage(notificationTaskService.createTask(task).getChatId(), SUCCESS_MESSAGE),
                () -> botService.sendMessage(chatId, WARN_MESSAGE)
        );
    }
}
