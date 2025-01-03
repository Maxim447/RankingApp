alter table if exists users
    drop column birth_date;

alter table if exists users
    add column age smallint not null default -1;