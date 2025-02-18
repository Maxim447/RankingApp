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
import ru.hse.rankingapp.entity.enums.Gender;
import ru.hse.rankingapp.entity.enums.StatusEnum;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "age_from", nullable = false)
    private Integer ageFrom;

    @Column(name = "age_to", nullable = false)
    private Integer ageTo;

    @Column(name = "max_points", nullable = false)
    private Integer maxPoints;

    @Column(name = "start_time", nullable = false)
    private OffsetDateTime startTime;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    @Column(name = "event_uuid", nullable = false)
    private UUID eventUuid = UUID.randomUUID();

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

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private Set<EventUserLinkEntity> eventUserLinks;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventEntity that = (EventEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    /**
     * Добавить соревнование
     */
    public void addCompetition(CompetitionEntity competition) {
        if (competition == null) {
            return;
        }

        this.competition = competition;
        competition.addEvent(this);
    }

    /**
     * Связать пользователя с заплывом.
     */
    public void addEventUserLink(EventUserLinkEntity entity) {
        if (this.eventUserLinks == null) {
            this.eventUserLinks = new HashSet<>();
        }

        this.eventUserLinks.add(entity);
    }
}
