alter table if exists competition_events
    add column age_from smallint not null default 0;

alter table if exists competition_events
    add column age_to smallint not null default 99;

alter table if exists competition_events
    drop column if exists age_category;