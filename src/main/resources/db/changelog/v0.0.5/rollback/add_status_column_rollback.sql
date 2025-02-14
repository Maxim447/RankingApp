alter table if exists competitions
    drop column if exists status;

alter table if exists competition_events
    drop column if exists status;

alter table if exists competition_events
    add column if not exists end_time timestamp not null default now();