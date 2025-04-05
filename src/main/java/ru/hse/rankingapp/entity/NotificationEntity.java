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

import java.time.OffsetDateTime;

/**
 * Сущность события.
 */
@Table(name = "notifications")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEntity {

    @Id
    @SequenceGenerator(name = "notification_seq", sequenceName = "notification_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notification_seq")
    @Column(name = "notification_id", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "notification_text")
    private String text;

    @Column(name = "create_dttm", nullable = false)
    private OffsetDateTime createDttm = OffsetDateTime.now();

    @Column(name = "modify_dttm")
    private OffsetDateTime modifyDttm;

    @Column(name = "action_index", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionIndex actionIndex = ActionIndex.I;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "account_id")
    private AccountEntity accountEntity;
}
