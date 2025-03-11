create sequence if not exists news_sequence;

create table if not exists news
(
    news_id    bigint PRIMARY KEY    default nextval('news_sequence'::regclass),
    topic      varchar(255) not null,
    text       text         not null,
    start_date DATE         not null,
    end_date   DATE         not null,
    image_1    varchar(64),
    image_2    varchar(64),
    image_3    varchar(64),
    created_at timestamp    not null default now()
);