package ru.hse.rankingapp.service;

import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hse.rankingapp.dto.UserAuthentication;
import ru.hse.rankingapp.dto.event.CreateEventDto;
import ru.hse.rankingapp.dto.event.EventFullInfoDto;
import ru.hse.rankingapp.dto.event.EventResultDto;
import ru.hse.rankingapp.entity.CompetitionEntity;
import ru.hse.rankingapp.entity.CompetitionUserLinkEntity;
import ru.hse.rankingapp.entity.EventEntity;
import ru.hse.rankingapp.entity.EventUserLinkEntity;
import ru.hse.rankingapp.entity.UserEntity;
import ru.hse.rankingapp.entity.enums.Role;
import ru.hse.rankingapp.enums.BusinessExceptionsEnum;
import ru.hse.rankingapp.exception.BusinessException;
import ru.hse.rankingapp.mapper.EventMapper;
import ru.hse.rankingapp.repository.CompetitionRepository;
import ru.hse.rankingapp.repository.EventRepository;
import ru.hse.rankingapp.utils.JwtUtils;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Сервис для работы с мероприятиями.
 */
@Service
@RequiredArgsConstructor
public class EventService {

    private static final String XLSX_TEMPLATE_NAME = "Шаблон_для_заполнения_результатов_заплыва_%s.xlsx";

    private final EventRepository eventRepository;
    private final CompetitionRepository competitionRepository;
    private final EventMapper eventMapper;
    private final JwtUtils jwtUtils;
    private final XlsxService xlsxService;

    /**
     * Создать мероприятие к определенному соревнованию.
     *
     * @param eventDto дто для создания мероприятия
     */
    public void createEvent(CreateEventDto eventDto) {
        UserAuthentication userInfoFromRequest = jwtUtils.getUserInfoFromRequest();

        if (userInfoFromRequest == null) {
            throw new BusinessException("Не удалось получить информацию о пользователе", HttpStatus.NOT_FOUND);
        }

        if (!userInfoFromRequest.getRoles().contains(Role.ORGANIZATION)) {
            throw new BusinessException(BusinessExceptionsEnum.NOT_ENOUGH_RULES);
        }

        CompetitionEntity competition = competitionRepository
                .findByOrganizationEmailAndCompetitionUuid(userInfoFromRequest.getEmail(), eventDto.getCompetitionUUID())
                .orElseThrow(() -> new BusinessException("Не удалось найти соревнование", HttpStatus.NOT_FOUND));

        EventEntity event = eventMapper.toEventEntity(eventDto);
        event.addCompetition(competition);

        eventRepository.save(event);
    }

    /**
     * Получить полную информацию о заплыве.
     *
     * @param uuid uuid Заплыва
     * @return Полная информация о заплыве
     */
    public EventFullInfoDto findEventByUuid(UUID uuid) {
        Optional<EventEntity> event = eventRepository.findByUuid(uuid);

        if (event.isPresent()) {
            return eventMapper.toEventFullInfoDto(event.get());
        }

        throw new BusinessException("Не удалось найти заплыв по uuid = " + uuid, HttpStatus.NOT_FOUND);
    }

    /**
     * Добавить пользователя к заплыву.
     *
     * @param user      Сущность пользователя
     * @param eventUuid Юид заплыва
     */
    @Transactional
    public void addUserToEvent(UserEntity user, UUID eventUuid) {
        EventEntity event = eventRepository.findByUuid(eventUuid).orElseThrow(() ->
                new BusinessException("Не удалось найти заплыв по uuid = " + eventUuid, HttpStatus.NOT_FOUND));

        CompetitionEntity competition = event.getCompetition();

        CompetitionUserLinkEntity competitionUserLinkEntity = new CompetitionUserLinkEntity();
        competitionUserLinkEntity.setCompetitionEntity(competition);
        competitionUserLinkEntity.setUser(user);
        competitionUserLinkEntity.setRegistrationDate(LocalDate.now());

        competition.addCompetitionUserLink(competitionUserLinkEntity);

        EventUserLinkEntity eventUserLinkEntity = new EventUserLinkEntity();
        eventUserLinkEntity.setUser(user);
        eventUserLinkEntity.setEvent(event);
        eventUserLinkEntity.setRegistrationDate(LocalDate.now());

        event.addEventUserLink(eventUserLinkEntity);

        competitionRepository.save(competition);
        eventRepository.save(event);
    }

    /**
     * Создать шаблон для заполнения результатов заплыва.
     *
     * @param eventUuid uuid Заплыва
     */
    public Pair<String, byte[]> generateXlsxTemplate(UUID eventUuid) {
        EventEntity event = eventRepository.findByUuid(eventUuid).orElseThrow(() ->
                new BusinessException("Не удалось найти заплыв по uuid = " + eventUuid, HttpStatus.NOT_FOUND));

        byte[] xlsxData = xlsxService.generateXlsxTemplateByEvent(event);

        return Pair.of(String.format(XLSX_TEMPLATE_NAME, LocalDate.now()), xlsxData);
    }

    @Transactional
    public void uploadEventResults(MultipartFile file, UUID eventUuid) {
        EventEntity event = eventRepository.findByUuid(eventUuid).orElseThrow(() ->
                new BusinessException("Не удалось найти заплыв по uuid = " + eventUuid, HttpStatus.NOT_FOUND));

        Map<String, EventUserLinkEntity> userLinkEntityMap = event.getEventUserLinks().stream()
                .collect(Collectors.toMap(
                        userEvent -> userEvent.getUser().getEmail(),
                        Function.identity()
                ));
        List<EventResultDto> eventResultDto = xlsxService.parseXlsxTemplate(file);
        eventResultDto.sort(Comparator.comparing(EventResultDto::getTime));

        int place = 1;
        for (EventResultDto resultDto : eventResultDto) {
            EventUserLinkEntity eventUserLinkEntity = userLinkEntityMap.get(resultDto.getEmail());
            eventUserLinkEntity.setPlace(place++);
            eventUserLinkEntity.setTime(resultDto.getTime());
        }
    }

    /**
     * Удалить заплыв по uuid.
     *
     * @param eventUuid Uuid заплыва
     */
    @Transactional
    public void deleteEvent(UUID eventUuid) {
        UserAuthentication userInfoFromRequest = jwtUtils.getUserInfoFromRequest();

        if (userInfoFromRequest == null) {
            throw new BusinessException("Не удалось получить информацию о пользователе", HttpStatus.NOT_FOUND);
        }

        Tuple eventTuple = eventRepository.findByUuidWithOrganization(eventUuid).orElseThrow(() ->
                new BusinessException("Не удалось найти заплыв по uuid = " + eventUuid, HttpStatus.NOT_FOUND));

        String email = eventTuple.get(1, String.class);
        if (!userInfoFromRequest.getRoles().contains(Role.ORGANIZATION) || !email.equals(userInfoFromRequest.getEmail())) {
            throw new BusinessException(BusinessExceptionsEnum.NOT_ENOUGH_RULES);
        }

        EventEntity eventEntity = eventTuple.get(0, EventEntity.class);
        eventRepository.delete(eventEntity);
    }
}
