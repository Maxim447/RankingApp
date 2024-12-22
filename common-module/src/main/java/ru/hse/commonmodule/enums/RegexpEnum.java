package ru.hse.commonmodule.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enum с regexp.
 */
@Getter
@AllArgsConstructor
public enum RegexpEnum {

    EMAIL_PATTERN("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"),
    PHONE_PATTERN("\\+?[1-9]\\d{0,2} ?\\(?\\d{1,4}?\\)? ?\\d{1,4}[- ]?\\d{1,4}[- ]?\\d{1,9}");

    private final String pattern;
}
