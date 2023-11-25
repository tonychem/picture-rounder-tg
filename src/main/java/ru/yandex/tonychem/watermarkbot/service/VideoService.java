package ru.yandex.tonychem.watermarkbot.service;

import lombok.RequiredArgsConstructor;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Video;
import ru.yandex.tonychem.watermarkbot.util.BotUtils;

import java.io.File;
import java.io.IOException;

/**
 * Сервис, ответственный за взаимодействие с ffmpeg
 */
@Service
@RequiredArgsConstructor
public class VideoService {
    @Value("${resources.temporaryFolderOutput}")
    private String TEMPORARY_OUTPUT_FOLDER_PATH;

    private final FFmpegCommandLineFactory ffMpegCommandLineFactory;

    /**
     * Добавляет вотермарку
     * @param video телеграмм видеофайл
     * @param videoFile скачанный видеофайл
     * @param logo файл логотипа
     */
    public File addWatermark(Video video, File videoFile, File logo) throws IOException {
        Integer videoWidth = video.getWidth();
        String videoExtension = video.getMimeType().split("/")[1];

        File outputFile = BotUtils.generateTemporaryVideoFile(TEMPORARY_OUTPUT_FOLDER_PATH, video);

        new FFmpegExecutor()
                .createJob(ffMpegBuilder(videoFile, logo, videoWidth, videoExtension, outputFile))
                .run();

        return outputFile;
    }

    /**
     * Метод, возвращающий готовый объект FFmpegBuilder для запуска программы ffmpeg
     * @param inputVideo скачанный видеофайл
     * @param logo файл логотипа
     * @param width ширина видео
     * @param fileExtension расширение видеофайла
     * @param outputFile файл, куда будет записано обработанное видео
     * @return
     */
    private FFmpegBuilder ffMpegBuilder(File inputVideo, File logo, Integer width, String fileExtension, File outputFile) {
        FFmpegBuilder bld = ffMpegCommandLineFactory.getFFMpegBuilder(width);

        bld.setInput(inputVideo.getAbsolutePath())
                .addInput(logo.getAbsolutePath())
                .addOutput(outputFile.getAbsolutePath())
                .setFormat(fileExtension)
                .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL)
                .done();

        return bld;
    }
}
