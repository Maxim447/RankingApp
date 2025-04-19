create sequence if not exists professional_record_sequence;

create table if not exists professional_records
(
    professional_record_id bigint PRIMARY KEY not null default nextval('professional_record_sequence'::regclass),
    distance               integer            not null,
    style                  varchar(50)        not null,
    gender                 varchar(50)        not null,
    time                   bigint             not null,
    unique (distance, style, gender)
);

-- Вольный
INSERT INTO professional_records (distance, style, gender, time)
VALUES -- Женщины
       (50, 'Вольный', 'FEMALE', 22870),
       (100, 'Вольный', 'FEMALE', 50090),
       (200, 'Вольный', 'FEMALE', 109320),
       (400, 'Вольный', 'FEMALE', 228020),
       (800, 'Вольный', 'FEMALE', 469630),
       (1500, 'Вольный', 'FEMALE', 891690),
       -- Мужчины
       (50, 'Вольный', 'MALE', 20250),
       (100, 'Вольный', 'MALE', 45390),
       (200, 'Вольный', 'MALE', 98810),
       (400, 'Вольный', 'MALE', 213180),
       (800, 'Вольный', 'MALE', 437980),
       (1500, 'Вольный', 'MALE', 843780);

-- На спине
INSERT INTO professional_records (distance, style, gender, time)
VALUES -- Женщины
       (50, 'На спине', 'FEMALE', 26020),
       (100, 'На спине', 'FEMALE', 55530),
       (200, 'На спине', 'FEMALE', 119280),
       -- Мужчины
       (50, 'На спине', 'MALE', 22810),
       (100, 'На спине', 'MALE', 49980),
       (200, 'На спине', 'MALE', 108420);

-- Брасс
INSERT INTO professional_records (distance, style, gender, time)
VALUES -- Женщины
       (50, 'Брасс', 'FEMALE', 28240),
       (100, 'Брасс', 'FEMALE', 62120),
       (200, 'Брасс', 'FEMALE', 133240),
       -- Мужчины
       (50, 'Брасс', 'MALE', 25130),
       (100, 'Брасс', 'MALE', 55100),
       (200, 'Брасс', 'MALE', 121550);

-- Баттерфляй
INSERT INTO professional_records (distance, style, gender, time)
VALUES -- Женщины
       (50, 'Баттерфляй', 'FEMALE', 23660),
       (100, 'Баттерфляй', 'FEMALE', 53740),
       (200, 'Баттерфляй', 'FEMALE', 118000),
       -- Мужчины
       (50, 'Баттерфляй', 'MALE', 21570),
       (100, 'Баттерфляй', 'MALE', 47900),
       (200, 'Баттерфляй', 'MALE', 106890);

-- Комплексный
INSERT INTO professional_records (distance, style, gender, time)
VALUES -- Женщины
       (200, 'Комплексный', 'FEMALE', 122170),
       (400, 'Комплексный', 'FEMALE', 257550),
       -- Мужчины
       (200, 'Комплексный', 'MALE', 110430),
       (400, 'Комплексный', 'MALE', 234910);