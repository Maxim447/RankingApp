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
    MALE("Мужчина"),
    FEMALE("Женщина"),
    MIXED("Смешанный");

    private final String value;

    public static Gender getGenderByValue(String value) {
        return EnumSet.allOf(Gender.class).stream()
                .filter(gender -> gender.getValue().equals(value))
                .findFirst()
                .orElse(null);
    }
}
