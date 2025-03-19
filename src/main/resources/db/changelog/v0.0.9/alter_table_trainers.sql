alter table if exists trainers
    add column if not exists education text;

alter table if exists trainers
    add column if not exists specialization text;

alter table if exists trainers
    add column if not exists achievements text;

alter table if exists trainers
    drop column if exists description;