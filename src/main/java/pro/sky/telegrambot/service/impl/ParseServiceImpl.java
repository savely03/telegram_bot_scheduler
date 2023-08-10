package pro.sky.telegrambot.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.service.ParseService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pro.sky.telegrambot.constant.Regex.MESSAGE_REGEX;

@Service
@Slf4j
public class ParseServiceImpl implements ParseService {
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

    private boolean checkDateTimeInMessage(LocalDateTime dateTime) {
        return !dateTime.isBefore(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
    }

}
