alter table if exists users
    add column if not exists image varchar(64);

alter table if exists organizations
    add column if not exists image varchar(64);