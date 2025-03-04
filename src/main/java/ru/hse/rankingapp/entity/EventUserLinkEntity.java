package ru.hse.rankingapp.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.hse.rankingapp.entity.enums.ActionIndex;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;

/**
 * Описание результата заплыва в рамках соревнования.
 */
@Table(name = "event_users_link")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventUserLinkEntity {

    @Id
    @SequenceGenerator(name = "event_users_link_seq", sequenceName = "event_users_link_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_users_link_seq")
    @Column(name = "event_users_link_id", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "time")
    private Long time;

    @Column(name = "points")
    private Double points;

    @Column(name = "place")
    private Integer place;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "create_dttm", nullable = false)
    private OffsetDateTime createDttm = OffsetDateTime.now();

    @Column(name = "modify_dttm")
    private OffsetDateTime modifyDttm;

    @Column(name = "action_index", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionIndex actionIndex = ActionIndex.I;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "event_id")
    private EventEntity event;

    public LocalTime getTime() {
        if (this.time == null) {
            return null;
        }

        return LocalTime.ofNanoOfDay(this.time * 1_000_000);
    }

    public void setTime(LocalTime localTime) {
        this.time = localTime.toNanoOfDay() / 1_000_000;
    }
}
