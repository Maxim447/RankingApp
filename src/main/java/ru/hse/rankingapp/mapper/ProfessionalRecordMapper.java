package ru.hse.rankingapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.hse.rankingapp.dto.professional.records.CreateProfessionalRecordDto;
import ru.hse.rankingapp.dto.professional.records.ProfessionalRecordsInfoDto;
import ru.hse.rankingapp.entity.ProfessionalRecordEntity;

import java.time.LocalTime;

/**
 * Маппер для таблички рекордов.
 */
@Mapper(componentModel = "spring")
public interface ProfessionalRecordMapper {

    /**
     * Маппинг из {@link ProfessionalRecordEntity} в {@link ProfessionalRecordsInfoDto}.
     */
    @Mapping(source = "time", target = "time", qualifiedByName = "mapToLocalTime")
    ProfessionalRecordsInfoDto mapToDto(ProfessionalRecordEntity professionalRecordEntity);

    /**
     * Маппинг из {@link CreateProfessionalRecordDto} в {@link ProfessionalRecordEntity}.
     */
    ProfessionalRecordEntity mapToEntity(CreateProfessionalRecordDto createProfessionalRecordDto);

    /**
     * Маппинг времени.
     */
    @Named("mapToLocalTime")
    default LocalTime mapToLocalTime(Long time) {
        return LocalTime.ofNanoOfDay(time * 1_000_000);
    }
}
