alter table if exists competition_events
    add column if not exists max_points integer not null default '0';