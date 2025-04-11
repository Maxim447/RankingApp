alter table if exists competitions
    add column if not exists video_link text;

alter table if exists competition_events
    drop column if exists video_link;