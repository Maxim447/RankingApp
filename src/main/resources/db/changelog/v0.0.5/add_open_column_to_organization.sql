alter table if exists organizations
    add column if not exists is_open boolean not null default true;