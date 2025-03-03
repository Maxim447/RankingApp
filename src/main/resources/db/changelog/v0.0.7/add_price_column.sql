alter table if exists competition_events
    add column if not exists price numeric not null default '0.0';