package pro.sky.telegrambot.service.impl;

import org.junit.jupiter.api.Test;
import pro.sky.telegrambot.entity.NotificationTask;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;

class ParseServiceImplTest {
    private final ParseServiceImpl out = new ParseServiceImpl();
    private final Long chatId = ThreadLocalRandom.current().nextLong();
    private final String text = "SomeText";


    @Test
    void parseMessageToNotificationTaskTest() {
        LocalDateTime dateTime = LocalDateTime.now().plusMinutes(1);
        String dateTimeString = dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        String messageText = dateTimeString + " " + text;
        NotificationTask expected = NotificationTask.builder().chatId(chatId).dateTime(dateTime).text(text).build();

        Optional<NotificationTask> task = out.parseMessageToNotificationTask(chatId, messageText);

        assertThat(task).contains(expected);
    }

    @Test
    void parseMessageToNotificationTaskWhenDatetimeBeforeNowTest() {
        LocalDateTime dateTime = LocalDateTime.now().minusMinutes(1);
        String dateTimeString = dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        String messageText = dateTimeString + " " + text;

        assertThat(out.parseMessageToNotificationTask(chatId, messageText)).isEmpty();
    }

    @Test
    void parseMessageToNotificationWhenNotMatchesTest() {
        assertThat(out.parseMessageToNotificationTask(chatId, "")).isEmpty();
    }
}