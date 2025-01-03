alter table if exists users
    alter column phone drop not null;

alter table if exists users
    drop constraint users_phone_key;

alter table if exists users
    add column age smallint not null default -1;

alter table if exists users
    rename column id to user_id;