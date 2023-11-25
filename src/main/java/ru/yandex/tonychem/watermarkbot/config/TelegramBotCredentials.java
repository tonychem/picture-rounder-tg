package ru.yandex.tonychem.watermarkbot.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Вспомогательный класс, содержащий юзернейм и токен телеграм-бота
 */
@Configuration
@Getter
public class TelegramBotCredentials {
    @Value("${telegram-bot.token}")
    private String botToken;

    @Value("${telegram-bot.username}")
    private String botUsername;
}
