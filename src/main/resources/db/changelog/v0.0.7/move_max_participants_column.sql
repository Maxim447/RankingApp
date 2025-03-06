alter table if exists competitions
    drop column if exists max_participants;

alter table if exists competition_events
    add column if not exists max_participants integer not null default '10';