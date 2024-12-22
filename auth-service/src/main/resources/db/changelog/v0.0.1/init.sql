create sequence if not exists user_sequence;

CREATE TABLE IF NOT EXISTS users
(
    id           bigint PRIMARY KEY default nextval('user_sequence'::regclass),
    first_name   VARCHAR(255) NOT NULL,
    last_name    VARCHAR(255) NOT NULL,
    middle_name  varchar(255),
    email        TEXT UNIQUE  NOT NULL,
    phone        TEXT UNIQUE  NOT NULL,
    password     TEXT         NOT NULL,
    role         SMALLINT     NOT NULL,
    create_dttm  TIMESTAMP    NOT NULL default now(),
    modify_dttm  TIMESTAMP,
    action_index varchar(1)   NOT NULL default 'I'
);

COMMENT ON TABLE users IS 'Пользователи';
COMMENT ON COLUMN users.id IS 'Идентификатор';
COMMENT ON COLUMN users.first_name IS 'Имя пользователя';
COMMENT ON COLUMN users.last_name IS 'Фамилия пользователя';
COMMENT ON COLUMN users.middle_name IS 'Отчество пользователя';
COMMENT ON COLUMN users.email IS 'Электронная почта';
COMMENT ON COLUMN users.phone IS 'Номер телефона';
COMMENT ON COLUMN users.password IS 'Пароль';
COMMENT ON COLUMN users.role IS 'Роль (0 - администратор, 1 - пользователь)';
COMMENT ON COLUMN users.create_dttm IS 'Время создания записи';
COMMENT ON COLUMN users.modify_dttm IS 'Время изменения записи';
COMMENT ON COLUMN users.action_index IS 'Индекс действия (I - insert, U - update, D - delete)';
