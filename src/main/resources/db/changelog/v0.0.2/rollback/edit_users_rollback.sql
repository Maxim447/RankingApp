alter table if exists users
    alter column phone set not null;

alter table if exists users
    add constraint users_phone_key unique (phone);

alter table if exists users
    drop column age;

alter table if exists users
    rename column user_id to id;