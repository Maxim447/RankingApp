alter table if exists trainers
    drop column if exists education;

alter table if exists trainers
    drop column if exists specialization;

alter table if exists trainers
    drop column if exists achievements;

alter table if exists trainers
    add column if not exists description text;