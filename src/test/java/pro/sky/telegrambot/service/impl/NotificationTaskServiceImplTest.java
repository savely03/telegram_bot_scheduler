package pro.sky.telegrambot.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pro.sky.telegrambot.NotificationTaskGenerator;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;
import pro.sky.telegrambot.service.NotificationTaskService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class NotificationTaskServiceImplTest {

    @Autowired
    private NotificationTaskGenerator generator;

    @Autowired
    private NotificationTaskRepository repository;

    @Autowired
    private NotificationTaskService out;

    @Test
    void createTaskTest() {
        long count = repository.count();

        NotificationTask task = generator.generateNotificationTasks(1).get(0);

        assertThat(out.createTask(task)).isEqualTo(task);
        assertThat(repository.count()).isEqualTo(count + 1);
    }

    @Test
    void findAllByDateTimeTest() {
        List<NotificationTask> notificationTasks = repository.saveAll(generator.generateNotificationTasks(10));

        assertThat(out.findAllByDateTime()).containsExactlyInAnyOrderElementsOf(notificationTasks);
    }

    @Test
    void deleteAllTest() {
        List<NotificationTask> tasks = repository.saveAll(generator.generateNotificationTasks(10));

        out.deleteAll(tasks);

        assertThat(repository.count()).isZero();
        assertThat(repository.findAll()).doesNotContainAnyElementsOf(tasks);
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }
}