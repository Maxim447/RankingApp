alter table if exists competitions
    add column if not exists attachment varchar(64) not null default 'NoData';