alter table if exists users
    add column if not exists professional_max_points_1 numeric;

alter table if exists users
    add column if not exists professional_max_points_2 numeric;

alter table if exists users
    add column if not exists professional_max_points_3 numeric;