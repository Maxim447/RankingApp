package ru.hse.rankingapp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.hse.rankingapp.enums.SeparatorEnum;
import ru.hse.rankingapp.exception.BusinessException;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    @Value("${file-storage}")
    private String storage;

    public String saveFile(MultipartFile multipartFile) {
        try {
            String fileExtension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
            String fileName = UUID.randomUUID() + SeparatorEnum.DOT.getValue() + fileExtension;
            Path path = Paths.get(storage, fileName);

            Files.createDirectories(path.getParent());

            Files.write(path, multipartFile.getBytes());
            return fileName;
        } catch (Exception e) {
            throw new BusinessException("Не удалось сохранить файл", HttpStatus.BAD_REQUEST);
        }
    }

    public Optional<Pair<MediaType, Resource>> getFile(String fileName) {
        Path path = Paths.get(storage, fileName);

        if (!Files.exists(path)) {
            log.warn("Файл {} не существует", fileName);
            return Optional.empty();
        }

        URI uri = path.toUri();
        try {
            MediaType mediaType = MediaType.parseMediaType(Files.probeContentType(path));
            return Optional.of(Pair.of(mediaType, new UrlResource(uri)));
        } catch (IOException e) {
            log.warn("Не удалось получить фйал по пути {}", uri);
            return Optional.empty();
        }
    }
}
