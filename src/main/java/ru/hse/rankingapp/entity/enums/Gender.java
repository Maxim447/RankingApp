package ru.hse.rankingapp.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum c полом
 */
@Getter
@RequiredArgsConstructor
public enum Gender {
    MALE("Мужчина"),
    FEMALE("Женщина"),
    MIXED("Смешанный");

    private final String value;
}
