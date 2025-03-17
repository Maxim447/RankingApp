alter table if exists users
    add column if not exists participants_type varchar(32) not null default 'AMATEURS';