package ru.yandex.tonychem.watermarkbot.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideoNote;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.Video;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.yandex.tonychem.watermarkbot.config.TelegramBotCredentials;
import ru.yandex.tonychem.watermarkbot.util.BotUtils;

import java.io.File;

@Service
@RequiredArgsConstructor
public class WatermarkService extends TelegramLongPollingBot {
    @Value("${resources.temporaryFolderInput}")
    private String TEMPORARY_FOLDER_INPUT_PATH;

    private final TelegramBotCredentials botCredentials;
    private final VideoService videoService;

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasVideo()) {
            Video inputVideoFile = update.getMessage().getVideo();
            Integer duration = inputVideoFile.getDuration();

            if (duration > 60) {
                SendMessage message = BotUtils.prepareSendMessage(update.getMessage().getChatId(),
                        "Видео не должно быть длинyее 60 секунд!");
                execute(message);
            }

            File downloadedVideoFile = downloadFile(inputVideoFile,
                    BotUtils.generateTemporaryVideoFile(TEMPORARY_FOLDER_INPUT_PATH, inputVideoFile));
            File watermarkedVideoFile = videoService.addWatermark(inputVideoFile, downloadedVideoFile,
                    new File("logos/defaultLogo.png"));

            SendVideoNote videoNote = BotUtils.prepareVideoNote(update.getMessage().getChatId(), watermarkedVideoFile);
            execute(videoNote);
        }
    }

    private File downloadFile(Video video, File outputFile) throws TelegramApiException {
        String videoFileId = video.getFileId();

        GetFile getFile = new GetFile();
        getFile.setFileId(videoFileId);

        org.telegram.telegrambots.meta.api.objects.File prepareFile = execute(getFile);
        return downloadFile(prepareFile, outputFile);
    }

    @Override
    public String getBotUsername() {
        return botCredentials.getBotUsername();
    }

    @Override
    public String getBotToken() {
        return botCredentials.getBotToken();
    }
}
