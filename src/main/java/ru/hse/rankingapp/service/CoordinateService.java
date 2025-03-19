package ru.hse.rankingapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.geojson.GeoJsonReader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.rankingapp.dto.coordinates.GeoJson;
import ru.hse.rankingapp.dto.coordinates.GeometryDto;
import ru.hse.rankingapp.dto.coordinates.PropertiesDto;
import ru.hse.rankingapp.dto.coordinates.SimpleGeoJsonDto;
import ru.hse.rankingapp.dto.coordinates.SimpleGeoJsonResponseDto;
import ru.hse.rankingapp.dto.trainer.TrainerCreateDto;
import ru.hse.rankingapp.dto.trainer.TrainerUpdateDto;
import ru.hse.rankingapp.entity.CoordinateEntity;
import ru.hse.rankingapp.entity.OrganizationEntity;
import ru.hse.rankingapp.entity.TrainerEntity;
import ru.hse.rankingapp.exception.BusinessException;
import ru.hse.rankingapp.mapper.CoordinateMapper;
import ru.hse.rankingapp.mapper.TrainerMapper;
import ru.hse.rankingapp.repository.CoordinateRepository;
import ru.hse.rankingapp.repository.OrganizationRepository;
import ru.hse.rankingapp.repository.TrainerRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с координатами.
 */
@Service
@RequiredArgsConstructor
public class CoordinateService {

    private final TrainerRepository trainerRepository;
    private final TrainerMapper trainerMapper;
    private final CoordinateRepository coordinateRepository;
    private final OrganizationRepository organizationRepository;
    private final CoordinateMapper coordinateMapper;
    private final ObjectMapper objectMapper;
    private final GeoJsonReader geoJsonReader = new GeoJsonReader();
    private final FileService fileService;

    /**
     * Сохранить местоположение организации.
     *
     * @param simpleGeoJsonDto информация о координатах
     */
    @Transactional
    public void addCoordinates(SimpleGeoJsonDto simpleGeoJsonDto) {
        try {
            JsonNode geometryJson = objectMapper.valueToTree(simpleGeoJsonDto.getGeometry());

            Geometry geometry = geoJsonReader.read(geometryJson.toString());

            CoordinateEntity geoData = new CoordinateEntity();
            geoData.setGeometry(geometry);
            geoData.setName(simpleGeoJsonDto.getName());
            geoData.setDescription(simpleGeoJsonDto.getDescription());
            geoData.setOrganization(organizationRepository.findByEmail(simpleGeoJsonDto.getEmail()));

            coordinateRepository.save(geoData);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Ошибка при формировании geoJson'а", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new BusinessException("Ошибка при сохранении geoJson'а", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Получить все координаты.
     *
     * @return Информация о координатах.
     */
    public List<SimpleGeoJsonResponseDto> getAllCoordinates() {
        return coordinateRepository.findAllCoordinates().stream()
                .map(coordinate -> {
                    PropertiesDto propertiesDto = new PropertiesDto();
                    propertiesDto.setDescription(coordinate.getDescription());
                    propertiesDto.setName(coordinate.getName());

                    Geometry geometry = coordinate.getGeometry();

                    GeometryDto geometryDto = new GeometryDto();
                    geometryDto.setType(geometry.getGeometryType());

                    Coordinate[] coordinates = geometry.getCoordinates();
                    geometryDto.setCoordinates(List.of(coordinates[0].getX(), coordinates[0].getY()));

                    GeoJson geoJson = new GeoJson()
                            .setProperties(propertiesDto)
                            .setGeometry(geometryDto);

                    return new SimpleGeoJsonResponseDto()
                            .setId(coordinate.getId())
                            .setGeoJson(geoJson);
                })
                .collect(Collectors.toList());
    }

    /**
     * Удалить координаты по id.
     *
     * @param id Id в таблице координат
     */
    public void deleteById(Long id) {
        coordinateRepository.deleteCoordinateById(id);
    }

    /**
     * Обновить местоположение организации.
     *
     * @param simpleGeoJsonDto информация о координатах
     */
    @Transactional
    public void updateCoordinates(Long id, SimpleGeoJsonDto simpleGeoJsonDto) {
        CoordinateEntity coordinateEntity = coordinateRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Не удалсь найти координаты по id = " + id, HttpStatus.NOT_FOUND));

        coordinateMapper.updateCoordinate(simpleGeoJsonDto, coordinateEntity);

        if (simpleGeoJsonDto.getGeometry() != null) {
            try {
                JsonNode geometryJson = objectMapper.valueToTree(simpleGeoJsonDto.getGeometry());
                Geometry geometry = geoJsonReader.read(geometryJson.toString());

                coordinateEntity.setGeometry(geometry);
            } catch (IllegalArgumentException | ParseException e) {
                throw new BusinessException("Ошибка при обновлении geoJson'а", HttpStatus.BAD_REQUEST);
            }
        }

        if (simpleGeoJsonDto.getEmail() != null) {
            OrganizationEntity organization = organizationRepository.findByEmail(simpleGeoJsonDto.getEmail());
            coordinateEntity.setOrganization(organization);
        }

        coordinateRepository.save(coordinateEntity);
    }

    /**
     * Добавить тренеров к местоположению.
     */
    @Transactional
    public void addTrainers(Long coordinateId, TrainerCreateDto trainerCreateDto) {
        CoordinateEntity coordinateEntity = coordinateRepository.findById(coordinateId)
                .orElseThrow(() -> new BusinessException("Не найдено местоположение по id", HttpStatus.NOT_FOUND));
        OrganizationEntity organization = coordinateEntity.getOrganization();

        TrainerEntity trainerEntity = trainerMapper.mapTrainer(trainerCreateDto);

        trainerEntity.setCoordinate(coordinateEntity);
        trainerEntity.setOrganization(organization);

        trainerRepository.save(trainerEntity);
    }

    /**
     * Обновить тренера.
     */
    @Transactional
    public void updateTrainer(Long trainerId, TrainerUpdateDto trainerUpdateDto) {
        TrainerEntity trainerEntity = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new BusinessException("Не найден тренер по id = " + trainerId, HttpStatus.NOT_FOUND));

        trainerMapper.updateTrainerEntity(trainerUpdateDto, trainerEntity);

        if (Boolean.TRUE.equals(trainerUpdateDto.getIsNeedUpdatePhoto()) && trainerUpdateDto.getImage() != null) {
            fileService.deleteFile(trainerEntity.getImage());
            String image = fileService.saveFile(trainerUpdateDto.getImage());
            trainerEntity.setImage(image);
        }
    }
}
