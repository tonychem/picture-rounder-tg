package ru.yandex.tonychem.watermarkbot.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.yandex.tonychem.watermarkbot.service.WatermarkService;

/**
 * Класс, ответственный за запуск телеграм-бота после старта spring-контекста
 */
@Configuration
@RequiredArgsConstructor
public class TelegramBotInitializer {
    private final WatermarkService watermarkService;

    @EventListener(ContextRefreshedEvent.class)
    public void startBot() throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(watermarkService);
    }
}
