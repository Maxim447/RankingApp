package ru.hse.rankingapp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileExtensionsEnum {
    JPEG("jpeg"),
    PNG("png");

    private final String value;
}
