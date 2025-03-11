package ru.hse.rankingapp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.rankingapp.service.FileService;
import ru.hse.rankingapp.utils.ControllerUtils;

import java.util.Optional;

/**
 * API для работы с файлами.
 */
@Tag(name = "File API", description = "API для работы с файлами")
@RequestMapping("/api/v1/file")
@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    /**
     * Скачать файл.
     *
     * @param path Путь до файла
     * @return Файл
     */
    @GetMapping("/download/{path}")
    public ResponseEntity<Resource> downloadFile(@PathVariable(value = "path") String path) {
        Optional<Pair<MediaType, Resource>> file = fileService.getFile(path);
        return file.map(ControllerUtils::createFileResponse)
                .orElse(ResponseEntity.noContent().build());
    }
}
