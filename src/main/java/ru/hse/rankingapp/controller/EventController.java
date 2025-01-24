package ru.hse.rankingapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hse.rankingapp.dto.event.CreateEventDto;
import ru.hse.rankingapp.service.EventService;

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

}
