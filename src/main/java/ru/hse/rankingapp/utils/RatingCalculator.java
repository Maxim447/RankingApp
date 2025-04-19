package ru.hse.rankingapp.utils;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;

/**
 * Утилитный класс для подсчета очков.
 */
@UtilityClass
public class RatingCalculator {

    /**
     * Рассчитать очки за заплыв (Любители).
     *
     * @param fastestTime Лучшее время участника определенного пола
     * @param time        время участника
     * @param distance    дистанция
     * @return кол-во очков
     */
    public Double calculate(LocalTime fastestTime, LocalTime time, Integer distance) {
        double result = 10d * convertTimeToSeconds(fastestTime) / convertTimeToSeconds(time) + getPointsByDistance(distance);
        return BigDecimal.valueOf(result)
                .setScale(3, RoundingMode.HALF_UP)
                .doubleValue();
    }

    /**
     * Рассчитать очки за заплыв (Профи).
     *
     * @param fastestTime Время рекорда
     * @param time        время участника
     * @return кол-во очков
     */
    public Double calculateProfessional(LocalTime fastestTime, LocalTime time) {
        double fastestTimeSeconds = convertTimeToSeconds(fastestTime);
        double userTimeSeconds = convertTimeToSeconds(time);

        return BigDecimal.valueOf(1000 * Math.pow(fastestTimeSeconds / userTimeSeconds, 3.0))
                .setScale(3, RoundingMode.HALF_UP)
                .doubleValue();
    }

    private double convertTimeToSeconds(LocalTime time) {
        int hours = time.getHour();
        int minutes = time.getMinute();
        int seconds = time.getSecond();
        int millis = time.getNano() / 1_000_000;

        return hours * 3600 + minutes * 60 + seconds + millis / 1000.0;
    }

    private double getPointsByDistance(Integer distance) {
        if (distance < 500) {
            return 10d;
        }

        if (distance < 1000) {
            return 15d;
        }

        if (distance < 1500) {
            return 20d;
        }

        return 25d;
    }
}
