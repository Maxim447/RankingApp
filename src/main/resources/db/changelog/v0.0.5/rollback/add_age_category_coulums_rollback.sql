alter table if exists competition_events
    drop column if exists age_from;

alter table if exists competition_events
    drop column if exists age_to;

alter table if exists competition_events
    add column if not exists age_category varchar(20) not null default '10-55';