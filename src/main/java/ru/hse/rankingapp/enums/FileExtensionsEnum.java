package ru.hse.rankingapp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FileExtensionsEnum {
    JPEG("jpeg"),
    PNG("png"),
    PDF("pdf");

    private final String value;
}
