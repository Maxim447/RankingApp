alter table if exists competitions
    add column if not exists max_participants integer not null default '10';

alter table if exists competition_events
    drop column if exists max_participants;