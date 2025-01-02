create sequence if not exists organization_sequence;

create table if not exists organizations
(
    organization_id    bigint PRIMARY KEY not null default nextval('organization_sequence'::regclass),
    organization_name  varchar(255)       not null unique,
    organization_email TEXT UNIQUE        NOT NULL unique,
    password           TEXT               NOT NULL,
    role               SMALLINT           NOT NULL,
    create_dttm        TIMESTAMP          NOT NULL default now(),
    modify_dttm        TIMESTAMP,
    action_index       varchar(1)         NOT NULL default 'I'
);

COMMENT ON TABLE organizations IS 'Организации';
COMMENT ON COLUMN organizations.organization_name IS 'Название организации';
COMMENT ON COLUMN organizations.organization_email IS 'Почта организации';
COMMENT ON COLUMN organizations.password IS 'Пароль организации';
COMMENT ON COLUMN users.role IS 'Роль (0 - администратор, 1 - пользователь, 2 - Организация)';
COMMENT ON COLUMN organizations.create_dttm IS 'Время создания записи';
COMMENT ON COLUMN organizations.modify_dttm IS 'Время изменения записи';
COMMENT ON COLUMN organizations.action_index IS 'Индекс действия (I - insert, U - update, D - delete)';

create sequence if not exists users_organizations_link_sequence;

create table if not exists users_organizations_link
(
    users_organizations_link_id bigint PRIMARY KEY not null default nextval('users_organizations_link_sequence'::regclass),
    user_id                     bigint
        constraint fk_users_reference references users,
    organization_id             bigint
        constraint fk_organization_reference references organizations,
    create_dttm                 TIMESTAMP          NOT NULL default now(),
    modify_dttm                 TIMESTAMP,
    action_index                varchar(1)         NOT NULL default 'I'
);

COMMENT ON TABLE users_organizations_link IS 'Линковочная таблица для пользователей и организаций';
COMMENT ON COLUMN users_organizations_link.user_id IS 'Id пользователя';
COMMENT ON COLUMN users_organizations_link.organization_id IS 'Id организации';
COMMENT ON COLUMN users_organizations_link.create_dttm IS 'Время создания записи';
COMMENT ON COLUMN users_organizations_link.modify_dttm IS 'Время изменения записи';
COMMENT ON COLUMN users_organizations_link.action_index IS 'Индекс действия (I - insert, U - update, D - delete)';