package ru.hse.rankingapp.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Geometry;

/**
 * Сущность аккаунта.
 */
@Table(name = "map_coordinates")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CoordinateEntity {

    @Id
    @SequenceGenerator(name = "coordinate_seq", sequenceName = "map_coordinates_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coordinate_seq")
    @Column(name = "coordinate_id", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(columnDefinition = "Geometry")
    @JdbcTypeCode(SqlTypes.GEOMETRY)
    private Geometry geometry;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "organization_id")
    private OrganizationEntity organization;

}
