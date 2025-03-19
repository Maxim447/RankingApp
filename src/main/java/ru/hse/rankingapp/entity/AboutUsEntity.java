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
 * О нас.
 */
@Table(name = "about_us")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AboutUsEntity {

    @Id
    @SequenceGenerator(name = "about_us_seq", sequenceName = "about_us_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "about_us_seq")
    @Column(name = "about_us_id", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "description")
    private String description;
}
