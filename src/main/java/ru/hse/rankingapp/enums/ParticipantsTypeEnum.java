package ru.hse.rankingapp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum с типом соревнования.
 */
@Getter
@AllArgsConstructor
public enum ParticipantsTypeEnum {

    PROFESSIONALS("Профессионалы", "Профессионал"),
    AMATEURS("Любители", "Любитель");

    private final String value;
    private final String singleValue;
}
