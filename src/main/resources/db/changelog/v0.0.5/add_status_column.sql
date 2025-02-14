alter table if exists competitions
    add column if not exists status varchar(25) not null default 'IN_PROGRESS';

alter table if exists competition_events
    add column if not exists status varchar(25) not null default 'IN_PROGRESS';

alter table if exists competition_events
    drop column if exists end_time;