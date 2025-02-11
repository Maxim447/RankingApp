package ru.hse.rankingapp.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.hse.rankingapp.entity.enums.ActionIndex;
import ru.hse.rankingapp.entity.enums.TokenAction;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Сущность таблицы токенов.
 */
@Entity
@Table(name = "tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenEntity {

    @Id
    @Column(name = "token_id")
    private UUID id;

    @Column(name = "token_action", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private TokenAction tokenAction;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "organization_id")
    private OrganizationEntity organization;

    @Column(name = "create_dttm", nullable = false)
    private OffsetDateTime createDttm = OffsetDateTime.now();

    @Column(name = "modify_dttm")
    private OffsetDateTime modifyDttm;

    @Column(name = "action_index", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActionIndex actionIndex = ActionIndex.I;

    /**
     * Создать сущность.
     */
    public TokenEntity(UUID id, TokenAction tokenAction) {
        this.id = id;
        this.tokenAction = tokenAction;
    }

    /**
     * Создать сущность.
     */
    public TokenEntity(UUID id, TokenAction tokenAction, UserEntity user, OrganizationEntity organization) {
        this.id = id;
        this.tokenAction = tokenAction;
        this.user = user;
        this.organization = organization;
    }
}
