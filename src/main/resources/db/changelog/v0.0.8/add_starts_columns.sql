alter table if exists users
    add column if not exists first_place_count bigint not null default '0';

alter table if exists users
    add column if not exists second_place_count bigint not null default '0';

alter table if exists users
    add column if not exists third_place_count bigint not null default '0';

alter table if exists users
    add column if not exists starts_count bigint not null default '0';