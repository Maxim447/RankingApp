package ru.hse.rankingapp.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.hse.rankingapp.dto.trainer.TrainerCreateDto;
import ru.hse.rankingapp.dto.trainer.TrainerInfoDto;
import ru.hse.rankingapp.dto.trainer.TrainerUpdateDto;
import ru.hse.rankingapp.entity.TrainerEntity;
import ru.hse.rankingapp.service.FileService;

/**
 * Маппер для тренеров.
 */
@Mapper(componentModel = "spring", uses = FileService.class)
public interface TrainerMapper {

    /**
     * Маппинг списка тренеров.
     */
    @Mapping(source = "image", target = "image", qualifiedByName = "saveFile")
    TrainerEntity mapTrainer(TrainerCreateDto trainerCreateDtoList);

    /**
     * Маппинг информации о тренере.
     */
    TrainerInfoDto mapTrainerInfo(TrainerEntity trainerEntity);

    /**
     * Обновить сущность тренера.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "image", ignore = true)
    void updateTrainerEntity(TrainerUpdateDto trainerUpdateDto, @MappingTarget TrainerEntity trainerEntity);
}
