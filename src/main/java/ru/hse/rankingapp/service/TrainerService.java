package ru.hse.rankingapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.hse.rankingapp.dto.paging.PageRequestDto;
import ru.hse.rankingapp.dto.paging.PageResponseDto;
import ru.hse.rankingapp.dto.trainer.TrainerInfoDto;
import ru.hse.rankingapp.entity.TrainerEntity;
import ru.hse.rankingapp.mapper.TrainerMapper;
import ru.hse.rankingapp.repository.TrainerRepository;
import ru.hse.rankingapp.service.search.TrainerSearchWithSpec;

/**
 * Сервис для тренеров.
 */
@Service
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainerMapper trainerMapper;
    private final TrainerSearchWithSpec trainerSearchWithSpec;

    /**
     * Получить тренеров по id местоположения.
     */
    public PageResponseDto<TrainerInfoDto> getTrainersByCoordinateId(Long coordinateId, PageRequestDto pageRequestDto) {
        Specification<TrainerEntity> specification = trainerSearchWithSpec.searchWithSpec(coordinateId);

        Page<TrainerInfoDto> page = trainerRepository.findAll(specification, pageRequestDto.toPageRequest())
                .map(trainerMapper::mapTrainerInfo);

        return new PageResponseDto<>(page.getTotalElements(), page.getTotalPages(), page.getContent());
    }
}
