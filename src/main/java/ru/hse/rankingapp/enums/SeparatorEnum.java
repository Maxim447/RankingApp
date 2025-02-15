package ru.hse.rankingapp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Енам с разделителями.
 */
@Getter
@AllArgsConstructor
public enum SeparatorEnum {

    SPACE(" "),
    DASH("-");

    private final String value;
}
