create sequence if not exists account_sequence;

create table if not exists accounts
(
    account_id bigint PRIMARY KEY default nextval('account_sequence'::regclass),
    email      TEXT UNIQUE NOT NULL,
    password   TEXT        NOT NULL
);

comment on table accounts is 'Таблица с аккаунтами пользователей';
comment on column accounts.email is 'Почта пользователя';
comment on column accounts.password is 'Пароль пользователя';

CREATE TABLE if not exists account_roles
(
    account_id BIGINT,
    role       smallint,
    FOREIGN KEY (account_id) REFERENCES accounts (account_id)
);

-- Перенос данных из users в accounts
INSERT INTO accounts (email, password)
SELECT email, password
FROM users
ON CONFLICT (email) DO NOTHING;


-- Перенос ролей из users в account_roles
INSERT INTO account_roles (account_id, role)
SELECT a.account_id, u.role
FROM accounts a
         JOIN users u ON a.email = u.email;

-- Перенос данных из organizations в accounts
INSERT INTO accounts (email, password)
SELECT organization_email, password
FROM organizations
ON CONFLICT (email) DO NOTHING;
-- Игнорируем конфликты по email, если они уже есть в accounts

-- Перенос ролей из organizations в account_roles
INSERT INTO account_roles (account_id, role)
SELECT a.account_id, o.role
FROM accounts a
         JOIN organizations o ON a.email = o.organization_email;

alter table if exists users
    drop column if exists role;

alter table if exists users
    drop column if exists password;

alter table if exists organizations
    drop column if exists role;

alter table if exists organizations
    drop column if exists password;