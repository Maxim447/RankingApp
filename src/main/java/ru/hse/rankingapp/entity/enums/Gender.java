package ru.hse.rankingapp.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.EnumSet;

/**
 * Enum c полом
 */
@Getter
@RequiredArgsConstructor
public enum Gender {
    MALE("Мужчина", "М"),
    FEMALE("Женщина", "Ж"),
    MIXED("Смешанный", "М/Ж");

    private final String value;
    private final String shortValue;

    public static Gender getGenderByValue(String value) {
        return EnumSet.allOf(Gender.class).stream()
                .filter(gender -> gender.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }
}
