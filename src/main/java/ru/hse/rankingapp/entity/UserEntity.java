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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.hse.rankingapp.entity.enums.ActionIndex;
import ru.hse.rankingapp.entity.enums.Gender;
import ru.hse.rankingapp.enums.ParticipantsTypeEnum;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Сущность пользователя.
 */
@Table(name = "users")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Id
    @SequenceGenerator(name = "user_seq", sequenceName = "user_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @Column(name = "user_id", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "personal_phone")
    private String phone;

    @Column(name = "emergency_phone")
    private String emergencyPhone;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "create_dttm", nullable = false)
    private OffsetDateTime createDttm = OffsetDateTime.now();

    @Column(name = "modify_dttm")
    private OffsetDateTime modifyDttm;

    @Column(name = "action_index", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionIndex actionIndex = ActionIndex.I;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "best_average_time_100")
    private Long bestAverageTime100;

    @Column(name = "rating", nullable = false)
    private Double rating;

    @Column(name = "professional_max_points_1")
    private Double professionalMaxPoints1;

    @Column(name = "professional_max_points_2")
    private Double professionalMaxPoints2;

    @Column(name = "professional_max_points_3")
    private Double professionalMaxPoints3;

    @Column(name = "first_place_count", nullable = false)
    private Long firstPlaceCount = 0L;

    @Column(name = "second_place_count", nullable = false)
    private Long secondPlaceCount = 0L;

    @Column(name = "third_place_count", nullable = false)
    private Long thirdPlaceCount = 0L;

    @Column(name = "starts_count", nullable = false)
    private Long startsCount = 0L;

    @Column(name = "image")
    private String image;

    @Column(name = "participants_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ParticipantsTypeEnum participantsType = ParticipantsTypeEnum.AMATEURS;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "users_organizations_link",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "organization_id")
    )
    @ToString.Exclude
    private Set<OrganizationEntity> organizations;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<CompetitionUserLinkEntity> competitionUserLinks;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<EventUserLinkEntity> eventUserLinks;

    /**
     * Добавить пользователя к организации.
     */
    public void addOrganization(OrganizationEntity organization) {
        if (organization == null) {
            return;
        }

        if (this.organizations == null) {
            this.organizations = new HashSet<>();
        }

        this.organizations.add(organization);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
