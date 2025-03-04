ALTER TABLE IF EXISTS event_users_link
    ADD COLUMN time_tmp TIME;

UPDATE event_users_link
SET time_tmp = time '00:00:00' + ((time::double precision) / 1000 * interval '1 second')
WHERE time IS NOT NULL;

ALTER TABLE IF EXISTS event_users_link
    DROP COLUMN time;

ALTER TABLE IF EXISTS event_users_link
    RENAME COLUMN time_tmp TO time;