package ru.yandex.tonychem.watermarkbot.util;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideoNote;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Video;

import java.io.File;
import java.util.UUID;

public class BotUtils {
    /**
     * Подготовить сообщение к отправке
     */
    public static SendMessage prepareSendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        return sendMessage;
    }

    /**
     * Подготовить кружок к отправке
     */
    public static SendVideoNote prepareVideoNote(Long chatId, File file) {
        SendVideoNote sendVideoNote = new SendVideoNote();
        sendVideoNote.setVideoNote(new InputFile(file));
        sendVideoNote.setChatId(chatId);
        return sendVideoNote;
    }


    /**
     * Сгенерировать временный файл для записи входящих или обработанных видео
     */
    public static File generateTemporaryVideoFile(String directory, Video video) {
        String videoFileExtension = video.getMimeType().split("/")[1];
        String temporaryFileName = UUID.randomUUID().toString();
        return new File(directory + temporaryFileName + "." + videoFileExtension);
    }
}
