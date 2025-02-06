alter table if exists competition_events
    add column if not exists event_uuid uuid not null unique default gen_random_uuid();