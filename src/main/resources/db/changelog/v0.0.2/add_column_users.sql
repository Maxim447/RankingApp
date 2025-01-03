alter table if exists users
    rename phone to personal_phone;

alter table if exists users
    add column if not exists emergency_phone text not null default 'N/D';

comment on column users.emergency_phone is 'Номер по которому можно позвонить в случае чего';
comment on column users.age is 'Возраст';
