DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM meals;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

CREATE OR REPLACE FUNCTION getUserForPopulationDB() RETURNS int LANGUAGE plpgsql AS $$
    DECLARE user_id int;
BEGIN
        user_id := (SELECT id from users where email = 'user@yandex.ru')::int;
        return user_id;
END
$$;

CREATE OR REPLACE FUNCTION dateTimeFormat() RETURNS varchar LANGUAGE plpgsql AS $$
DECLARE format varchar;
BEGIN
    format := 'DD/MM/YYYY HH24:MI:SS';
    return format;
END
$$;

INSERT INTO meals (user_id, datetime, description, calories) VALUES
    (getUserForPopulationDB(), to_timestamp('30/01/2020 10:00:00',dateTimeFormat()), 'Завтрак', 500::int),
    (getUserForPopulationDB(), to_timestamp('30/01/2020 13:00:00',dateTimeFormat()), 'Обед', 1000::int),
    (getUserForPopulationDB(), to_timestamp('30/01/2020 20:00:00',dateTimeFormat()), 'Ужин', 500::int),
    (getUserForPopulationDB(), to_timestamp('31/01/2020 00:00:00',dateTimeFormat()), 'Еда на граничное значение', 100::int),
    (getUserForPopulationDB(), to_timestamp('31/01/2020 10:00:00',dateTimeFormat()), 'Завтрак', 1000::int),
    (getUserForPopulationDB(), to_timestamp('31/01/2020 13:00:00',dateTimeFormat()), 'Обед', 500::int),
    (getUserForPopulationDB(), to_timestamp('31/01/2020 20:00:00',dateTimeFormat()), 'Ужин', 410::int);
