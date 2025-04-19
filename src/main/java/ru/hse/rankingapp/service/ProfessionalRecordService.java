package ru.hse.rankingapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.rankingapp.dto.paging.PageRequestDto;
import ru.hse.rankingapp.dto.paging.PageResponseDto;
import ru.hse.rankingapp.dto.professional.records.CreateProfessionalRecordDto;
import ru.hse.rankingapp.dto.professional.records.ProfessionalRecordsInfoDto;
import ru.hse.rankingapp.dto.professional.records.RecordSearchParamsDto;
import ru.hse.rankingapp.dto.professional.records.UpdateRecordTimeDto;
import ru.hse.rankingapp.entity.ProfessionalRecordEntity;
import ru.hse.rankingapp.exception.BusinessException;
import ru.hse.rankingapp.mapper.ProfessionalRecordMapper;
import ru.hse.rankingapp.repository.ProfessionalRecordRepository;
import ru.hse.rankingapp.service.search.ProfessionalRecordSearchWithSpec;

/**
 * Сервис для работы с табличкой рекордов.
 */
@Service
@RequiredArgsConstructor
public class ProfessionalRecordService {

    private final ProfessionalRecordRepository professionalRecordRepository;
    private final ProfessionalRecordSearchWithSpec professionalRecordSearchWithSpec;
    private final ProfessionalRecordMapper professionalRecordMapper;

    /**
     * Получить табличку с рекордами.
     */
    public PageResponseDto<ProfessionalRecordsInfoDto> findProfessionalRecords(
            PageRequestDto pageRequestDto, RecordSearchParamsDto searchParamsDto) {

        Specification<ProfessionalRecordEntity> specification = professionalRecordSearchWithSpec.searchWithSpec(searchParamsDto);
        Page<ProfessionalRecordsInfoDto> page = professionalRecordRepository.findAll(specification, pageRequestDto.toPageRequest())
                .map(professionalRecordMapper::mapToDto);

        return new PageResponseDto<>(page.getTotalElements(), page.getTotalPages(), page.getContent());
    }

    /**
     * Изменить время рекорда (если его побили не в рамках платформы).
     */
    @Transactional
    public void updateRecordTime(UpdateRecordTimeDto updateRecordTimeDto) {
        Long time = updateRecordTimeDto.getNewTime().toNanoOfDay() / 1_000_000;
        professionalRecordRepository.updateTimeById(time, updateRecordTimeDto.getId());
    }

    /**
     * Создать новую запись в таблице рекордов.
     */
    public void createRecord(CreateProfessionalRecordDto createProfessionalRecordDto) {
        ProfessionalRecordEntity entity = professionalRecordMapper.mapToEntity(createProfessionalRecordDto);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnorePaths("id", "time");

        if (professionalRecordRepository.exists(Example.of(entity, matcher))) {
            throw new BusinessException(String.format("Рекорд для дистанции \"%s м\", пола \"%s\" и стиля \"%s\" " +
                    "уже существует.", entity.getDistance(), entity.getGender().getValue(), entity.getStyle()), HttpStatus.BAD_REQUEST);
        }

        professionalRecordRepository.save(entity);
    }
}
