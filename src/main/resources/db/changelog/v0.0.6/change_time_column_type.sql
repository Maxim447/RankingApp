alter table if exists event_users_link
    drop column time;

alter table if exists event_users_link
    add column time TIME;