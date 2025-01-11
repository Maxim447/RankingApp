create sequence if not exists competition_sequence;

create table if not exists competitions
(
    competition_id   bigint PRIMARY KEY    default nextval('competition_sequence'::regclass),
    name             varchar(255) not null,
    location         text         not null,
    date             date         not null,
    organization_id  bigint       not null
        constraint fk_organizations_reference references organizations,
    max_participants integer      not null,
    competition_type varchar(20)  not null,
    create_dttm      timestamp             default CURRENT_TIMESTAMP,
    modify_dttm      timestamp             default CURRENT_TIMESTAMP,
    action_index     varchar(1)   NOT NULL default 'I'
);

COMMENT ON TABLE competitions IS 'Описание соревнования';
COMMENT ON COLUMN competitions.competition_id IS 'Идентификатор';
COMMENT ON COLUMN competitions.name IS 'Название соревнования';
COMMENT ON COLUMN competitions.location IS 'Местоположение соревнования';
COMMENT ON COLUMN competitions.date IS 'Дата проведения соревнования';
COMMENT ON COLUMN competitions.organization_id IS 'Организатор соревнования';
COMMENT ON COLUMN competitions.max_participants IS 'Максимальное кол-во участников';
COMMENT ON COLUMN competitions.competition_type IS 'Тип соревнования';
COMMENT ON COLUMN competitions.create_dttm IS 'Время создания записи';
COMMENT ON COLUMN competitions.modify_dttm IS 'Время изменения записи';
COMMENT ON COLUMN competitions.action_index IS 'Индекс действия (I - insert, U - update, D - delete)';

create sequence if not exists competition_user_link_sequence;

create table if not exists competition_user_link
(
    competition_user_link_id bigint PRIMARY KEY  default nextval('competition_user_link_sequence'::regclass),
    competition_id           bigint     not null
        constraint fk_competitions_reference references competitions,
    user_id                  bigint     not null
        constraint fk_users_reference references users,
    registration_date        date                default CURRENT_DATE,
    create_dttm              timestamp           default CURRENT_TIMESTAMP,
    modify_dttm              timestamp           default CURRENT_TIMESTAMP,
    action_index             varchar(1) NOT NULL default 'I',
    unique (competition_id, user_id)
);

COMMENT ON TABLE competition_user_link IS 'Линковочная таблица для соревнований и участника';
COMMENT ON COLUMN competition_user_link.competition_user_link_id IS 'Идентификатор';
COMMENT ON COLUMN competition_user_link.competition_id IS 'Идентификатор соревнования';
COMMENT ON COLUMN competition_user_link.user_id IS 'Идентификатор пользователя';
COMMENT ON COLUMN competition_user_link.registration_date IS 'Дата регистрации';
COMMENT ON COLUMN competition_user_link.create_dttm IS 'Время создания записи';
COMMENT ON COLUMN competition_user_link.modify_dttm IS 'Время изменения записи';
COMMENT ON COLUMN competition_user_link.action_index IS 'Индекс действия (I - insert, U - update, D - delete)';

create sequence if not exists competition_event_sequence;

create table if not exists competition_events
(
    competition_event_id bigint PRIMARY KEY   default nextval('competition_event_sequence'::regclass),
    competition_id       bigint      not null
        constraint fk_competitions_reference references competitions,
    distance             integer     not null,
    style                varchar(50) not null,
    gender               varchar(50) not null,
    age_category         varchar(20) not null,
    max_points           integer     not null,
    start_time           timestamp   not null,
    end_time             timestamp   not null,
    create_dttm          timestamp            default CURRENT_TIMESTAMP,
    modify_dttm          timestamp            default CURRENT_TIMESTAMP,
    action_index         varchar(1)  NOT NULL default 'I'
);

COMMENT ON TABLE competition_events IS 'Описание заплыва в рамках соревнования';
COMMENT ON COLUMN competition_events.competition_id IS 'Идентификатор соревнования';
COMMENT ON COLUMN competition_events.distance IS 'Дистанция';
COMMENT ON COLUMN competition_events.style IS 'Стиль плавания';
COMMENT ON COLUMN competition_events.gender IS 'Пол участников';
COMMENT ON COLUMN competition_events.age_category IS 'Возрастная группа';
COMMENT ON COLUMN competition_events.max_points IS 'Максимальное кол-во очков';
COMMENT ON COLUMN competition_events.create_dttm IS 'Время создания записи';
COMMENT ON COLUMN competition_events.start_time IS 'Время начала события';
COMMENT ON COLUMN competition_events.end_time IS 'Время конца события';
COMMENT ON COLUMN competition_events.modify_dttm IS 'Время изменения записи';
COMMENT ON COLUMN competition_events.action_index IS 'Индекс действия (I - insert, U - update, D - delete)';

create sequence if not exists event_users_link_sequence;

create table if not exists event_users_link
(
    event_users_link_id bigint PRIMARY KEY  default nextval('event_users_link_sequence'::regclass),
    event_id            bigint     not null
        constraint fk_events_reference references competition_events,
    user_id             bigint     not null
        constraint fk_users_reference references users,
    time                numeric,
    points              integer,
    place               integer,
    registration_date   date                default CURRENT_DATE,
    create_dttm         timestamp           default CURRENT_TIMESTAMP,
    modify_dttm         timestamp           default CURRENT_TIMESTAMP,
    action_index        varchar(1) NOT NULL default 'I',
    unique (event_id, user_id)
);

COMMENT ON TABLE event_users_link IS 'Описание заплыва в рамках соревнования';
COMMENT ON COLUMN event_users_link.event_id IS 'Идентификатор события';
COMMENT ON COLUMN event_users_link.user_id IS 'Идентификатор участника';
COMMENT ON COLUMN event_users_link.time IS 'Время';
COMMENT ON COLUMN event_users_link.points IS 'Очки';
COMMENT ON COLUMN event_users_link.place IS 'Место';
COMMENT ON COLUMN event_users_link.registration_date IS 'Дата регистрации';
COMMENT ON COLUMN event_users_link.create_dttm IS 'Время создания записи';
COMMENT ON COLUMN event_users_link.modify_dttm IS 'Время изменения записи';
COMMENT ON COLUMN event_users_link.action_index IS 'Индекс действия (I - insert, U - update, D - delete)';