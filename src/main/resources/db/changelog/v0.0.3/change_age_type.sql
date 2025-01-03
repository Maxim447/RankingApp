alter table if exists users
    add column birth_date date not null default '2000-01-01';

alter table if exists users
    drop column age;