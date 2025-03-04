alter table if exists event_users_link
    rename time to time_old;

alter table if exists event_users_link
    add column if not exists time bigint;

update event_users_link
set time = (EXTRACT(EPOCH FROM time_old) * 1000)::bigint
where time_old is not null;

alter table if exists event_users_link
    drop column if exists time_old;