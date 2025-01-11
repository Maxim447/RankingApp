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
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.hse.rankingapp.entity.enums.ActionIndex;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;

/**
 * Сущность события.
 */
@Table(name = "competition_events")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventEntity {

    @Id
    @SequenceGenerator(name = "competition_event_seq", sequenceName = "competition_event_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "competition_event_seq")
    @Column(name = "competition_event_id", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "distance", nullable = false)
    private Integer distance;

    @Column(name = "style", nullable = false)
    private String style;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "age_category", nullable = false)
    private String ageCategory;

    @Column(name = "max_points", nullable = false)
    private Integer maxPoints;

    @Column(name = "start_time", nullable = false)
    private LocalDate startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDate endTime;

    @Column(name = "create_dttm", nullable = false)
    private OffsetDateTime createDttm = OffsetDateTime.now();

    @Column(name = "modify_dttm")
    private OffsetDateTime modifyDttm;

    @Column(name = "action_index", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionIndex actionIndex = ActionIndex.I;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "competition_id")
    private CompetitionEntity competition;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<EventUserLinkEntity> eventUserLinks;
}
