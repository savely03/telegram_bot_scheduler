package pro.sky.telegrambot;

import com.github.javafaker.Faker;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.entity.NotificationTask;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class NotificationTaskGenerator {
    private final Faker faker = new Faker();
    public List<NotificationTask> generateNotificationTasks(int size) {
        return Stream.generate(() -> NotificationTask.builder()
                        .chatId(faker.random().nextLong())
                        .text(faker.name().name())
                        .dateTime(LocalDateTime.now().minusMinutes(1)).build())
                .limit(size)
                .collect(Collectors.toList());
    }
}
