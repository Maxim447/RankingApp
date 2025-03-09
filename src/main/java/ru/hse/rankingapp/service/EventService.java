package ru.hse.rankingapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.hse.rankingapp.dto.UserAuthentication;
import ru.hse.rankingapp.dto.event.CreateEventDto;
import ru.hse.rankingapp.dto.event.EventFullInfoDto;
import ru.hse.rankingapp.dto.event.EventResultDto;
import ru.hse.rankingapp.dto.event.EventUserSearchRequestDto;
import ru.hse.rankingapp.dto.event.EventUserSearchResponseDto;
import ru.hse.rankingapp.dto.paging.PageRequestDto;
import ru.hse.rankingapp.dto.paging.PageResponseDto;
import ru.hse.rankingapp.entity.CompetitionEntity;
import ru.hse.rankingapp.entity.CompetitionUserLinkEntity;
import ru.hse.rankingapp.entity.EventEntity;
import ru.hse.rankingapp.entity.EventUserLinkEntity;
import ru.hse.rankingapp.entity.UserEntity;
import ru.hse.rankingapp.entity.enums.Gender;
import ru.hse.rankingapp.entity.enums.Role;
import ru.hse.rankingapp.entity.enums.StatusEnum;
import ru.hse.rankingapp.enums.BusinessExceptionsEnum;
import ru.hse.rankingapp.enums.CategoryEnum;
import ru.hse.rankingapp.enums.ParticipantsTypeEnum;
import ru.hse.rankingapp.exception.BusinessException;
import ru.hse.rankingapp.mapper.EventMapper;
import ru.hse.rankingapp.repository.CompetitionRepository;
import ru.hse.rankingapp.repository.EventRepository;
import ru.hse.rankingapp.repository.EventUserRepository;
import ru.hse.rankingapp.repository.UserRepository;
import ru.hse.rankingapp.service.search.EventUserSearchWithSpec;
import ru.hse.rankingapp.utils.FioUtils;
import ru.hse.rankingapp.utils.JwtUtils;
import ru.hse.rankingapp.utils.RatingCalculator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.HashMap;
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
    private final EventUserRepository eventUserRepository;
    private final EventUserSearchWithSpec eventUserSearchWithSpec;
    private final UserRepository userRepository;

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

        if (!competitionRepository.userExistsInCompetition(competition.getId(), user.getId())) {
            CompetitionUserLinkEntity competitionUserLinkEntity = new CompetitionUserLinkEntity();
            competitionUserLinkEntity.setCompetitionEntity(competition);
            competitionUserLinkEntity.setUser(user);
            competitionUserLinkEntity.setRegistrationDate(LocalDate.now());

            competition.addCompetitionUserLink(competitionUserLinkEntity);
            competitionRepository.save(competition);
        }

        if (!eventRepository.userExistsInEvent(event.getId(), user.getId())) {
            EventUserLinkEntity eventUserLinkEntity = new EventUserLinkEntity();
            eventUserLinkEntity.setUser(user);
            eventUserLinkEntity.setEvent(event);
            eventUserLinkEntity.setRegistrationDate(LocalDate.now());

            event.addEventUserLink(eventUserLinkEntity);

            eventRepository.save(event);
        }
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

        if (StatusEnum.ENDED.equals(event.getStatus())) {
            throw new BusinessException("Заплыв уже завершился. " +
                    "Если при внесении данных была совершена ошибка, обратитесь к администратору.", HttpStatus.BAD_REQUEST);
        }

        Map<String, EventUserLinkEntity> userLinkEntityMap = event.getEventUserLinks().stream()
                .collect(Collectors.toMap(
                        userEvent -> userEvent.getUser().getEmail(),
                        Function.identity()
                ));
        List<EventResultDto> resultDtos = xlsxService.parseXlsxTemplate(file);
        Map<Gender, List<EventResultDto>> collect = resultDtos.stream()
                .collect(Collectors.groupingBy(
                        EventResultDto::getGender,
                        Collectors.collectingAndThen(Collectors.toList(), list -> {
                            list.sort(Comparator.comparing(EventResultDto::getTime));
                            return list;
                        }))
                );

        resultDtos.sort(Comparator.comparing(EventResultDto::getTime));

        CompetitionEntity competition = event.getCompetition();
        ParticipantsTypeEnum participantsType = competition.getParticipantsType();

        Map<String, Double> pointsByEmail = new HashMap<>();
        for (Map.Entry<Gender, List<EventResultDto>> genderListEntry : collect.entrySet()) {
            List<EventResultDto> value = genderListEntry.getValue();
            LocalTime fastestTime = value.getFirst().getTime();

            for (EventResultDto resultDto : value) {
                if (ParticipantsTypeEnum.AMATEURS.equals(participantsType)) {
                    Double points = RatingCalculator.calculate(fastestTime, resultDto.getTime(), resultDto.getDistance());
                    pointsByEmail.put(resultDto.getEmail(), points);
                }
            }

        }

        int place = 1;

        for (EventResultDto resultDto : resultDtos) {
            EventUserLinkEntity eventUserLinkEntity = userLinkEntityMap.get(resultDto.getEmail());
            Double points = pointsByEmail.get(resultDto.getEmail());

            eventUserLinkEntity.setPoints(points);
            eventUserLinkEntity.setPlace(place);
            eventUserLinkEntity.setTime(resultDto.getTime());
            UserEntity user = eventUserLinkEntity.getUser();

            Long bestAverageTime100 = getBestAverageTime100(user.getBestAverageTime100(), resultDto);

            Double updatedRating = user.getRating() + points;

            if (place == 1) {
                userRepository.updateUserRating(user.getId(), updatedRating, bestAverageTime100,
                        1L, 0L, 0L);
            } else if (place == 2) {
                userRepository.updateUserRating(user.getId(), updatedRating, bestAverageTime100,
                        0L, 1L, 0L);
            } else if (place == 3) {
                userRepository.updateUserRating(user.getId(), updatedRating, bestAverageTime100,
                        0L, 0L, 1L);
            } else {
                userRepository.updateUserRating(user.getId(), updatedRating, bestAverageTime100,
                        0L, 0L, 0L);
            }

            place++;
        }

        event.setStatus(StatusEnum.ENDED);
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

        EventEntity eventEntity = eventRepository.findByUuidWithOrganization(eventUuid).orElseThrow(() ->
                new BusinessException("Не удалось найти заплыв по uuid = " + eventUuid, HttpStatus.NOT_FOUND));

        String email = eventEntity.getCompetition().getOrganization().getEmail();
        if (!userInfoFromRequest.getRoles()
                .contains(Role.ORGANIZATION) || !email.equals(userInfoFromRequest.getEmail())) {
            throw new BusinessException(BusinessExceptionsEnum.NOT_ENOUGH_RULES);
        }

        eventRepository.delete(eventEntity);
    }

    public PageResponseDto<EventUserSearchResponseDto> searchEventUsers(UUID eventUuid, PageRequestDto pageRequestDto, EventUserSearchRequestDto searchDto) {
        EventEntity event = eventRepository.findByUuid(eventUuid).orElseThrow(() ->
                new BusinessException("Не удалось найти заплыв по uuid = " + eventUuid, HttpStatus.NOT_FOUND));

        CompetitionEntity competition = event.getCompetition();
        ParticipantsTypeEnum participantsType = competition.getParticipantsType();

        EnumSet<CategoryEnum> categoryEnums = CategoryEnum.getEnumSetByParticipantsType(participantsType);

        Specification<EventUserLinkEntity> specification = eventUserSearchWithSpec.searchWithSpec(searchDto, eventUuid);

        Page<EventUserSearchResponseDto> page = eventUserRepository.findAll(specification, pageRequestDto.toPageRequest())
                .map(element -> {
                    UserEntity user = element.getUser();
                    LocalDate birthDate = user.getBirthDate();
                    long fullUserAge = ChronoUnit.YEARS.between(birthDate, LocalDate.now());

                    CategoryEnum category = categoryEnums.stream()
                            .filter(categoryEnum -> categoryEnum.getFrom() <= fullUserAge && categoryEnum.getTo() > fullUserAge)
                            .findFirst()
                            .orElse(null);

                    return new EventUserSearchResponseDto()
                            .setAge(fullUserAge)
                            .setGender(user.getGender().getValue())
                            .setRating(user.getRating())
                            .setFullName(FioUtils.buildFullName(user))
                            .setCategory(category == null ? null : category.getStringValue());
                });

        return new PageResponseDto<>(page.getTotalElements(), page.getTotalPages(), page.getContent());
    }

    private Long getBestAverageTime100(Long bestAverage100, EventResultDto result) {
        Integer distance = result.getDistance();
        Long time = result.getTime().toNanoOfDay() / 1_000_000 / (long) (distance / 100);

        if (bestAverage100 != null && bestAverage100 < time) {
            return bestAverage100;
        }

        return time;
    }
}
