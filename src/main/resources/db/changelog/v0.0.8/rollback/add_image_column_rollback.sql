alter table if exists users
    drop column if exists image;

alter table if exists organizations
    drop column if exists image;