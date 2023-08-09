package pro.sky.telegrambot.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.entity.NotificationTask;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {

    @Mock
    private TelegramBot telegramBot;
    @InjectMocks
    private MessageServiceImpl out;
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

    @Test
    void sendMessage() {
        out.sendMessage(chatId, text);

        Mockito.verify(telegramBot, times(1)).execute(any());
    }
}