alter table if exists competitions
    add column if not exists contact_link text not null default 'N/D';

alter table if exists competitions
    add column if not exists description text;