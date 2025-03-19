package ru.hse.rankingapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Сущность спонсора.
 */
@Table(name = "sponsors")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SponsorEntity {

    @Id
    @SequenceGenerator(name = "sponsors_seq", sequenceName = "sponsors_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sponsors_seq")
    @Column(name = "sponsor_id", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "sponsor_logo")
    private String sponsorLogo;

    @Column(name = "sponsor_description")
    private String sponsorDescription;
}
