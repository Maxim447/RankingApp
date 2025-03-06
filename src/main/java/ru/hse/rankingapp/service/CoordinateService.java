package ru.hse.rankingapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.geojson.GeoJsonReader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hse.rankingapp.dto.coordinates.SimpleGeoJsonDto;
import ru.hse.rankingapp.entity.CoordinateEntity;
import ru.hse.rankingapp.exception.BusinessException;
import ru.hse.rankingapp.repository.CoordinateRepository;
import ru.hse.rankingapp.repository.OrganizationRepository;

/**
 * Сервис для работы с координатами.
 */
@Service
@RequiredArgsConstructor
public class CoordinateService {

    private final CoordinateRepository coordinateRepository;
    private final OrganizationRepository organizationRepository;
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
}
