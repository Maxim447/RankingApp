alter table if exists users
    add column if not exists rating numeric not null default '0';