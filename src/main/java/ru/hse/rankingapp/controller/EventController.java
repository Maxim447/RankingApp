package ru.hse.rankingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.hse.rankingapp.dto.event.CreateEventDto;
import ru.hse.rankingapp.dto.event.EventFullInfoDto;
import ru.hse.rankingapp.service.EventService;
import ru.hse.rankingapp.utils.ControllerUtils;

import java.util.UUID;

/**
 * API для мероприятий.
 */
@Tag(name = "Events", description = "API для мероприятий")
@RestController
@RequestMapping("/api/v1/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    /**
     * Создать мероприятие к определенному соревнованию.
     *
     * @param eventDto дто для создания мероприятия
     */
    @PostMapping("/create")
    @Operation(summary = "Создать мероприятие к определенному соревнованию")
    public void createEvent(@RequestBody CreateEventDto eventDto) {
        eventService.createEvent(eventDto);
    }

    /**
     * Получить полную информацию о заплыве.
     *
     * @param uuid uuid Заплыва
     * @return Полная информация о заплыве
     */
    @GetMapping("/find/{uuid}")
    @Operation(summary = "Получить информацию о заплыве")
    public EventFullInfoDto findEventByUuid(@PathVariable(value = "uuid") UUID uuid) {
        return eventService.findEventByUuid(uuid);
    }

    /**
     * Создать шаблон для заполнения результатов заплыва.
     *
     * @param uuid uuid Заплыва
     * @return xlsx файл
     */
    @GetMapping("/generate-xlsx-template/{uuid}")
    @Operation(summary = "Создать шаблон для заполнения результатов заплыва")
    public ResponseEntity<byte[]> generateXlsxTemplate(@PathVariable(value = "uuid") UUID uuid) {
        Pair<String, byte[]> template = eventService.generateXlsxTemplate(uuid);
        return ControllerUtils.createFileResponse(template.getFirst(), template.getSecond());
    }

    @PostMapping(value = "/upload-result/{uuid}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Загрузить результаты в xlsx формате")
    public void uploadEventResults(@RequestPart MultipartFile file, @PathVariable("uuid") UUID uuid) {
        eventService.uploadEventResults(file, uuid);
    }

}
