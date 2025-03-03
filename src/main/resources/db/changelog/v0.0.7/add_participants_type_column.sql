alter table if exists competitions
    add column if not exists participants_type varchar(50) not null default 'AMATEURS';