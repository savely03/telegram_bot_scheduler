package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.sky.telegrambot.command.BotCommand;
import pro.sky.telegrambot.command.BotCommandDefiner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TelegramBotUpdatesListenerTest {
    @Mock
    private TelegramBot telegramBot;
    @Mock
    private BotCommandDefiner definer;
    @Mock
    private BotCommand botCommand;
    @Mock
    private Update update;
    @Mock
    private Message message;
    @InjectMocks
    private TelegramBotUpdatesListener out;

    @Test
    void initTest() {
        out.init();

        verify(telegramBot, times(1)).setUpdatesListener(any());
    }

    @Test
    void processTest() {
        List<String> commands = List.of(
                "/start",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),
                "someText"
        );
        String command = commands.get(ThreadLocalRandom.current().nextInt(commands.size()));

        when(message.text()).thenReturn(command);
        when(update.message()).thenReturn(message);
        when(definer.defineCommand(command)).thenReturn(botCommand);

        out.process(List.of(update));

        verify(update, times(1)).message();
        verify(message, times(1)).text();
        verify(definer, times(1)).defineCommand(command);
        verify(botCommand, times(1)).execute(any());
    }
}