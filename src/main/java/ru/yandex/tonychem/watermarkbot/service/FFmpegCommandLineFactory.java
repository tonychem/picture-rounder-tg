package ru.yandex.tonychem.watermarkbot.service;

import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.stereotype.Component;

/**
 * Класс-фабрика, возвращающая флаг filter_complex программы ffmpeg в зависимости от ширины входящего видео.
 * При ширине видео большего размера, чем MAX_VIDEO_NOTE_ROUNDING_WIDTH, входящее видео сужается до этой величины, а
 * длина обрезается до квадратного формата, иначе - только обрезается
 */
@Component
public class FFmpegCommandLineFactory {
    private static final Integer MAX_VIDEO_NOTE_ROUNDING_WIDTH = 640;

    private final String largeVideoQuery = "[0]scale=640:-1[scaled];" +
            "[scaled]crop=640:640:0:(in_h-640)/2[cr];" +
            "[1]format=rgba,colorchannelmixer=aa=0.7[logo];" +
            "[logo][cr]scale2ref=oh*mdar:ih[logo][video];" +
            "[video][logo]overlay=(main_w-overlay_w)/2:(main_h-overlay_h)/2";

    private final String smallVideoQuery = "crop=%d:%d:0:(in_h-%d)/2[cr];" +
            "[1]format=rgba,colorchannelmixer=aa=0.7[logo];" +
            "[logo][cr]scale2ref=oh*mdar:ih[logo][video];" +
            "[video][logo]overlay=(main_w-overlay_w)/2:(main_h-overlay_h)/2";

    public FFmpegBuilder getFFMpegBuilder(Integer width) {
        FFmpegBuilder bld = new FFmpegBuilder();

        if (width > MAX_VIDEO_NOTE_ROUNDING_WIDTH) {
            bld.setComplexFilter(largeVideoQuery);
        } else {
            bld.setComplexFilter(String.format(smallVideoQuery, width, width, width));
        }
        return bld;
    }
}
