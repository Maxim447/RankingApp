alter table if exists users
    add column if not exists gender VARCHAR(6) not null default 'MALE' CHECK (gender IN ('MALE', 'FEMALE'));