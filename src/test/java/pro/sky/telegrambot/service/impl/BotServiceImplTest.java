package pro.sky.telegrambot.service.impl;

import com.github.javafaker.Faker;
import com.pengrad.telegrambot.TelegramBot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.NotificationTaskGenerator;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.service.NotificationTaskService;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BotServiceImplTest {

    private final NotificationTaskGenerator generator = new NotificationTaskGenerator();
    @Mock
    private TelegramBot telegramBot;
    @Mock
    private NotificationTaskService notificationTaskService;
    @InjectMocks
    private BotServiceImpl out;

    private final Faker faker = new Faker();


    @Test
    void sendMessageTest() {
        out.sendMessage(faker.random().nextLong(), faker.name().name());

        verify(telegramBot, times(1)).execute(any());
    }

    @Test
    void sendScheduledMessagesTest() {
        List<NotificationTask> tasks = generator.generateNotificationTasks(10);

        when(notificationTaskService.findAllByDateTime()).thenReturn(tasks);

        out.sendScheduledMessages();

        verify(telegramBot, times(tasks.size())).execute(any());
    }
}