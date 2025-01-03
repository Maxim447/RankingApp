package ru.hse.rankingapp.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Индекс действия (I - insert, U - update, D - delete).
 */
@Getter
@AllArgsConstructor
public enum ActionIndex {

    I ("I"),
    U ("U"),
    D ("D");

    private final String value;
}
