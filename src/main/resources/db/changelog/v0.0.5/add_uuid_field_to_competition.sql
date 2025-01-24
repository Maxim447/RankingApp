alter table if exists competitions
    add column if not exists competition_uuid uuid not null unique default gen_random_uuid();