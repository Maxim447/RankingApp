create sequence if not exists notification_sequence;

create table if not exists notifications
(
    notification_id   bigint PRIMARY KEY  default nextval('notification_sequence'::regclass),
    account_id        bigint     not null references accounts,
    notification_text text       not null,
    create_dttm       TIMESTAMP  NOT NULL default now(),
    modify_dttm       TIMESTAMP,
    action_index      varchar(1) NOT NULL default 'I'
);