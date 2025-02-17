package ru.hse.rankingapp.dto.event;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalTime;

/**
 * Дто с результатами заплыва.
 */
@Data
@Accessors(chain = true)
public class EventResultDto {

    private String fullName;

    private String email;

    private Integer age;

    private Integer distance;

    private String groupCategory;

    private String style;

    private LocalTime time;
}
