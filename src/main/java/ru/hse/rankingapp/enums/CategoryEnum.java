package ru.hse.rankingapp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;

@Getter
@AllArgsConstructor
public enum CategoryEnum {

    // Категории для любителей
    AMATEUR_18_29(18, 29),
    AMATEUR_30_39(30, 39),
    AMATEUR_40_49(40, 49),
    AMATEUR_50_59(50, 59),
    AMATEUR_60_69(60, 69),

    // Категории по детям
    CHILDREN_8_10(8, 10),
    CHILDREN_10_12(10, 12),
    CHILDREN_12_14(12, 14),
    CHILDREN_14_16(14, 16),
    CHILDREN_16_18(16, 18),

    // Дети профессионалы
    PRO_CHILDREN_11_13(11, 13),
    PRO_CHILDREN_14_15(14, 15),
    PRO_CHILDREN_15_18(15, 18),

    // Абсолютная категория без возраста
    ABSOLUTE(18, 120);

    private final int from;
    private final int to;

    /**
     * Получить EnumSet по типу участников.
     *
     * @param participantsType Тип участников.
     */
    public static EnumSet<CategoryEnum> getEnumSetByParticipantsType(ParticipantsTypeEnum participantsType) {
        if (ParticipantsTypeEnum.AMATEURS.equals(participantsType)) {
            return EnumSet.of(
                    AMATEUR_18_29, AMATEUR_30_39, AMATEUR_40_49,
                    AMATEUR_50_59, AMATEUR_60_69, CHILDREN_8_10,
                    CHILDREN_10_12, CHILDREN_12_14, CHILDREN_14_16, CHILDREN_16_18
            );
        }

        return EnumSet.of(
                PRO_CHILDREN_11_13, PRO_CHILDREN_14_15, PRO_CHILDREN_15_18, ABSOLUTE
        );
    }

    @Override
    public String toString() {
        return from + "-" + to;
    }
}
