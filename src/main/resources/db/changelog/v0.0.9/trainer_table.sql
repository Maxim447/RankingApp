create sequence if not exists trainer_sequence;

create table if not exists trainers
(
    trainer_id      bigint PRIMARY KEY not null default nextval('trainer_sequence'::regclass),
    first_name      VARCHAR(255)       NOT NULL,
    last_name       VARCHAR(255)       NOT NULL,
    middle_name     varchar(255),
    description     text,
    image           varchar(64),
    create_dttm     TIMESTAMP          NOT NULL default now(),
    modify_dttm     TIMESTAMP,
    action_index    varchar(1)         NOT NULL default 'I',
    organization_id bigint             not null
        constraint fk_organization_reference references organizations,
    coordinate_id   bigint             not null
        constraint fk_coordinate_reference references map_coordinates
)
