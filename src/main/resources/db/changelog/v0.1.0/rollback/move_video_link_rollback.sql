alter table if exists competition_events
    add column if not exists video_link text;

alter table if exists competitions
    drop column if exists video_link;