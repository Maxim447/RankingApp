alter table if exists users
    rename personal_phone to phone;

alter table if exists users
    drop column if exists emergency_phone;