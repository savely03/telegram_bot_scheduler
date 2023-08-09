package pro.sky.telegrambot.service.impl;

import com.github.javafaker.Faker;
import com.pengrad.telegrambot.TelegramBot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;
import pro.sky.telegrambot.service.NotificationTaskService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@SpringBootTest
@ExtendWith(MockitoExtension.class)
class NotificationTaskServiceImplTest {

    @MockBean
    private TelegramBot telegramBot;

    @Autowired
    private NotificationTaskRepository repository;

    @Autowired
    private NotificationTaskService out;

    private final Faker faker = new Faker();

    @Test
    void createTaskTest() {
        long count = repository.count();

        NotificationTask task = generateNotificationTasks(1).get(0);

        assertThat(out.createTask(task)).isEqualTo(task);
        assertThat(repository.count()).isEqualTo(count + 1);
    }

    @Test
    void findAllByDateTimeTest() {
        List<NotificationTask> notificationTasks = repository.saveAll(generateNotificationTasks(10));

        assertThat(out.findAllByDateTime()).containsExactlyInAnyOrderElementsOf(notificationTasks);
    }

    @Test
    void notifyScheduledTasksTest() {
        List<NotificationTask> notificationTasks = repository.saveAll(generateNotificationTasks(10));

        out.notifyScheduledTasks();

        assertThat(repository.count()).isZero();
        assertThat(repository.findAll()).doesNotContainAnyElementsOf(notificationTasks);
        verify(telegramBot, times(10)).execute(any());
    }

    private List<NotificationTask> generateNotificationTasks(int size) {
        return Stream.generate(() -> NotificationTask.builder()
                        .chatId(faker.random().nextLong())
                        .text(faker.name().name())
                        .dateTime(LocalDateTime.now().minusMinutes(1)).build())
                .limit(size)
                .collect(Collectors.toList());
    }

    @AfterEach
    void throwDown() {
        repository.deleteAll();
    }
}