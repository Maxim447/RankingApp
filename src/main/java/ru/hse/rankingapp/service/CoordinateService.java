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
import ru.hse.rankingapp.entity.CoordinateEntity;
import ru.hse.rankingapp.entity.OrganizationEntity;
import ru.hse.rankingapp.exception.BusinessException;
import ru.hse.rankingapp.mapper.CoordinateMapper;
import ru.hse.rankingapp.repository.CoordinateRepository;
import ru.hse.rankingapp.repository.OrganizationRepository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с координатами.
 */
@Service
@RequiredArgsConstructor
public class CoordinateService {

    private final CoordinateRepository coordinateRepository;
    private final OrganizationRepository organizationRepository;
    private final CoordinateMapper coordinateMapper;
    private final ObjectMapper objectMapper;
    private final GeoJsonReader geoJsonReader = new GeoJsonReader();

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
}
