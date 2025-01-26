create table if not exists tokens
(
    token_id        uuid        not null unique,
    user_id         bigint references users,
    organization_id bigint references organizations,
    token_action    varchar(50) not null,
    create_dttm  TIMESTAMP    NOT NULL default now(),
    modify_dttm  TIMESTAMP,
    action_index varchar(1)   NOT NULL default 'I'
);

comment on table tokens is 'Таблица для хранения токенов для разных действий';
comment on column tokens.user_id is 'Связь с пользователями';
comment on column tokens.organization_id is 'Связь с организациями';
comment on column tokens.token_action is 'Вид токена';
COMMENT ON COLUMN tokens.create_dttm IS 'Время создания записи';
COMMENT ON COLUMN tokens.modify_dttm IS 'Время изменения записи';
COMMENT ON COLUMN tokens.action_index IS 'Индекс действия (I - insert, U - update, D - delete)';