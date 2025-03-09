alter table if exists users
    drop column if exists first_place_count;

alter table if exists users
    drop column if exists second_place_count;

alter table if exists users
    drop column if exists third_place_count;

alter table if exists users
    drop column if exists starts_count;