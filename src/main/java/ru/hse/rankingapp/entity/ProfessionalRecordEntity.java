package ru.hse.rankingapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.hse.rankingapp.entity.enums.Gender;

import java.time.LocalTime;

/**
 * Сущность таблички с рекордами профессионалов.
 */
@Table(name = "professional_records")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfessionalRecordEntity {

    @Id
    @SequenceGenerator(name = "professional_record_seq", sequenceName = "professional_record_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "professional_record_seq")
    @Column(name = "professional_record_id", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "distance", nullable = false)
    private Integer distance;

    @Column(name = "style", nullable = false)
    private String style;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "time", nullable = false)
    private Long time;

    public LocalTime getLocalTime() {
        if (this.time == null) {
            return null;
        }

        return LocalTime.ofNanoOfDay(this.time * 1_000_000);
    }

    public void setTime(LocalTime localTime) {
        this.time = localTime.toNanoOfDay() / 1_000_000;
    }
}
