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
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.hse.rankingapp.entity.enums.ActionIndex;
import ru.hse.rankingapp.entity.enums.Role;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Сущность организации.
 */
@Table(name = "organizations")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Organization implements UserDetails {

    @Id
    @SequenceGenerator(name = "organization_seq", sequenceName = "organization_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "organization_seq")
    @Column(name = "organization_id", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "organization_name", unique = true, nullable = false)
    private String name;

    @Column(name = "organization_email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private Role role = Role.ORGANIZATION;

    @Column(name = "create_dttm", nullable = false)
    private OffsetDateTime createDttm = OffsetDateTime.now();

    @Column(name = "modify_dttm")
    private OffsetDateTime modifyDttm;

    @Column(name = "action_index", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionIndex actionIndex = ActionIndex.I;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "users_organizations_link",
            joinColumns = @JoinColumn(name = "organization_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @ToString.Exclude
    private Set<User> users;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }
}
