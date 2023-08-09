package pro.sky.telegrambot.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.service.MessageService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pro.sky.telegrambot.constant.Regex.MESSAGE_REGEX;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final TelegramBot telegramBot;
    @Override
    public Optional<NotificationTask> parseMessageToNotificationTask(Long chatId, String messageText) {
        Pattern pattern = Pattern.compile(MESSAGE_REGEX);
        Matcher matcher = pattern.matcher(messageText);
        NotificationTask notificationTask = null;

        if (matcher.find()) {
            try {
                LocalDateTime dateTime = LocalDateTime.parse(matcher.group(1),
                        DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                if (checkDateTimeInMessage(dateTime)) {
                    String text = matcher.group(3);
                    notificationTask = NotificationTask.builder().dateTime(dateTime).chatId(chatId).text(text).build();
                }
            } catch (RuntimeException e) {
                log.error("Something went wrong");
            }
        }

        return Optional.ofNullable(notificationTask);
    }

    @Override
    public void sendMessage(Long chatId, String text) {
        telegramBot.execute(new SendMessage(chatId, text));
    }

    private boolean checkDateTimeInMessage(LocalDateTime dateTime) {
        return !dateTime.isBefore(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
    }

}
