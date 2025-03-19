create sequence if not exists about_us_sequence start with 2;
create table if not exists about_us
(
    about_us_id bigint PRIMARY KEY not null default nextval('about_us_sequence'::regclass),
    description text               not null
);

insert into about_us (about_us_id, description)
VALUES (1, '«Заплыв НН» — это масштабная платформа для спортсменов и организаторов. Мы объединяем лучших пловцов со всей Нижегородской области в едином рейтинге, где можно увидеть достижения как любителей, так и профессионалов, а также спортсменов разных возрастных категорий.

Мы строго следим за качеством проведения соревнований и уверены в каждом организаторе. Вместе мы создаем историю плавания в столице закатов г.Нижнем Новгороде.');

create sequence if not exists partners_sequence;
create table if not exists partners
(
    partner_id          bigint PRIMARY KEY not null default nextval('partners_sequence'::regclass),
    partner_logo        varchar(64)        not null,
    partner_description text               not null
);

create sequence if not exists sponsors_sequence;
create table if not exists sponsors
(
    sponsor_id          bigint PRIMARY KEY not null default nextval('sponsors_sequence'::regclass),
    sponsor_logo        varchar(64)        not null,
    sponsor_description text               not null
)