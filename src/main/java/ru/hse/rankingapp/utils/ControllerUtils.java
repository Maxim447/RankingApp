package ru.hse.rankingapp.utils;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.charset.StandardCharsets;

/**
 * Утилитный класс для формирования ответов.
 */
public final class ControllerUtils {

    /**
     * Создать ответ с вложенным файлом.
     *
     * @param fileName Название файла
     * @param data     файл
     * @return {@link ResponseEntity}
     */
    public static <T> ResponseEntity<T> createFileResponse(String fileName, T data) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(buildContentDispositionWithAttachment(fileName));

        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }

    public static ResponseEntity<Resource> createFileResponse(Pair<MediaType, Resource> file) {
        Resource resource = file.getRight();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(file.getLeft());
        headers.setContentDisposition(buildContentDispositionWithAttachment(resource.getFilename()));

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    private static ContentDisposition buildContentDispositionWithAttachment(String fileName) {
        return ContentDisposition.attachment()
                .filename(fileName, StandardCharsets.UTF_8)
                .build();
    }
}
