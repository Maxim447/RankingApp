package ru.hse.rankingapp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum с типом соревнования.
 */
@Getter
@AllArgsConstructor
public enum ParticipantsTypeEnum {

    PROFESSIONALS("Профессионалы"),
    AMATEURS("Любители");

    private final String value;
}
