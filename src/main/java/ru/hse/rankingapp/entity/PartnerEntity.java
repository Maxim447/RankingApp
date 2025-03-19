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
 * Сущность партнеров.
 */
@Table(name = "partners")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartnerEntity {

    @Id
    @SequenceGenerator(name = "partners_seq", sequenceName = "partners_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "partners_seq")
    @Column(name = "partner_id", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "partner_logo")
    private String partnerLogo;

    @Column(name = "partner_description")
    private String partnerDescription;
}
