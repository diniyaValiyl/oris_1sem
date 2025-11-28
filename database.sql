-- =============================================
-- БАЗА ДАННЫХ: Стоматологическая клиника "Вильдан"
-- Автор: Valiyllina Diniya
-- Дата создания: 2025
-- =============================================

-- Создание базы данных (если не существует)
CREATE DATABASE oris
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'Russian_Russia.1251'
    LC_CTYPE = 'Russian_Russia.1251'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

-- Подключение к базе данных
\c oris;

-- =============================================
-- ТАБЛИЦА: ПОЛЬЗОВАТЕЛИ
-- =============================================
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20) NOT NULL,
    role VARCHAR(20) DEFAULT 'PATIENT',
    gender VARCHAR(10),
    birth_year INTEGER CHECK (birth_year >= 1900 AND birth_year <= EXTRACT(YEAR FROM CURRENT_DATE)),
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP,

    -- Ограничения
    CONSTRAINT chk_username_length CHECK (LENGTH(username) >= 3 AND LENGTH(username) <= 50),
    CONSTRAINT chk_phone_format CHECK (LENGTH(REGEXP_REPLACE(phone, '\D', '', 'g')) >= 10)
);

COMMENT ON TABLE users IS 'Таблица пользователей системы';
COMMENT ON COLUMN users.username IS 'Уникальный логин пользователя';
COMMENT ON COLUMN users.password_hash IS 'Хэшированный пароль';
COMMENT ON COLUMN users.role IS 'Роль: PATIENT, ADMIN, DOCTOR';

-- =============================================
-- ТАБЛИЦА: ВРАЧИ
-- =============================================
CREATE TABLE doctors (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    photo_url VARCHAR(255) DEFAULT '/images/doctor-default.jpg',
    description TEXT,
    education TEXT,
    experience INTEGER CHECK (experience >= 0 AND experience <= 60),
    schedule_template VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Ограничения
    CONSTRAINT chk_experience_range CHECK (experience >= 0 AND experience <= 60)
);

COMMENT ON TABLE doctors IS 'Таблица врачей клиники';
COMMENT ON COLUMN doctors.specialization IS 'Специализация врача';
COMMENT ON COLUMN doctors.experience IS 'Стаж работы в годах';

-- =============================================
-- ТАБЛИЦА: ЗАПИСИ НА ПРИЕМ
-- =============================================
CREATE TABLE appointments (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    status VARCHAR(20) DEFAULT 'SCHEDULED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Внешние ключи
    CONSTRAINT fk_appointment_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_appointment_doctor
        FOREIGN KEY (doctor_id)
        REFERENCES doctors(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    -- Уникальность записи (врач + дата + время)
    CONSTRAINT unique_doctor_timeslot
        UNIQUE (doctor_id, appointment_date, appointment_time),

    -- Ограничения
    CONSTRAINT chk_appointment_date
        CHECK (appointment_date >= CURRENT_DATE),

    CONSTRAINT chk_status
        CHECK (status IN ('SCHEDULED', 'COMPLETED', 'CANCELLED', 'NO_SHOW'))
);

COMMENT ON TABLE appointments IS 'Таблица записей на прием';
COMMENT ON COLUMN appointments.status IS 'Статус: SCHEDULED, COMPLETED, CANCELLED, NO_SHOW';

-- =============================================
-- ТАБЛИЦА: ОТЗЫВЫ
-- =============================================
CREATE TABLE reviews (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    rating INTEGER CHECK (rating >= 1 AND rating <= 5),
    status VARCHAR(20) DEFAULT 'PUBLISHED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Внешние ключи
    CONSTRAINT fk_review_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    -- Ограничения
    CONSTRAINT chk_content_length
        CHECK (LENGTH(TRIM(content)) >= 10 AND LENGTH(content) <= 1000),

    CONSTRAINT chk_review_status
        CHECK (status IN ('PUBLISHED', 'PENDING', 'REJECTED'))
);

COMMENT ON TABLE reviews IS 'Таблица отзывов пациентов';
COMMENT ON COLUMN reviews.rating IS 'Рейтинг от 1 до 5 звезд';
COMMENT ON COLUMN reviews.status IS 'Статус модерации: PUBLISHED, PENDING, REJECTED';

-- =============================================
-- ТАБЛИЦА: НАСТРОЙКИ КЛИНИКИ
-- =============================================
CREATE TABLE clinic_settings (
    id BIGSERIAL PRIMARY KEY,
    setting_key VARCHAR(50) UNIQUE NOT NULL,
    setting_value TEXT NOT NULL,
    description VARCHAR(255),
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_setting_key_length CHECK (LENGTH(setting_key) >= 1)
);

COMMENT ON TABLE clinic_settings IS 'Таблица настроек клиники';

-- =============================================
-- ТАБЛИЦА: ЛОГИ БЕЗОПАСНОСТИ
-- =============================================
CREATE TABLE security_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    action_type VARCHAR(50) NOT NULL,
    ip_address VARCHAR(45),
    user_agent TEXT,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Внешние ключи
    CONSTRAINT fk_security_log_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

COMMENT ON TABLE security_logs IS 'Таблица логов безопасности';

-- =============================================
-- ИНДЕКСЫ ДЛЯ ПРОИЗВОДИТЕЛЬНОСТИ
-- =============================================

-- Индексы для пользователей
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email) WHERE email IS NOT NULL;
CREATE INDEX idx_users_phone ON users(phone);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_created_at ON users(created_at);

-- Индексы для врачей
CREATE INDEX idx_doctors_specialization ON doctors(specialization);
CREATE INDEX idx_doctors_experience ON doctors(experience);
CREATE INDEX idx_doctors_created_at ON doctors(created_at);

-- Индексы для записей
CREATE INDEX idx_appointments_user_id ON appointments(user_id);
CREATE INDEX idx_appointments_doctor_id ON appointments(doctor_id);
CREATE INDEX idx_appointments_date ON appointments(appointment_date);
CREATE INDEX idx_appointments_status ON appointments(status);
CREATE INDEX idx_appointments_doctor_date ON appointments(doctor_id, appointment_date);
CREATE INDEX idx_appointments_user_date ON appointments(user_id, appointment_date);
CREATE INDEX idx_appointments_created_at ON appointments(created_at);

-- Индексы для отзывов
CREATE INDEX idx_reviews_user_id ON reviews(user_id);
CREATE INDEX idx_reviews_rating ON reviews(rating) WHERE rating IS NOT NULL;
CREATE INDEX idx_reviews_status ON reviews(status);
CREATE INDEX idx_reviews_created_at ON reviews(created_at);

-- Индексы для логов безопасности
CREATE INDEX idx_security_logs_user_id ON security_logs(user_id);
CREATE INDEX idx_security_logs_action_type ON security_logs(action_type);
CREATE INDEX idx_security_logs_created_at ON security_logs(created_at);

-- =============================================
-- ТЕСТОВЫЕ ДАННЫЕ
-- =============================================

-- Вставка тестовых врачей
INSERT INTO doctors (full_name, specialization, photo_url, description, education, experience, schedule_template) VALUES
('Эргашев А.Р.', 'Хирург-имплантолог', '/images/ergashev.jpg',
 'Специалист по имплантации и костной пластике. Более 10 лет опыта в сложных случаях. Владеет современными методиками дентальной имплантации.',
 'Казанский государственный медицинский университет, ординатура по хирургической стоматологии',
 12, 'SAT-WED_8-14'),

('Валиуллина Л.Н.', 'Ортодонт', '/images/valiullina.jpg',
 'Специалист по исправлению прикуса и выравниванию зубов. Работает с современными брекет-системами и элайнерами. Индивидуальный подход к каждому пациенту.',
 'Казанский государственный медицинский университет, ординатура по ортодонтии',
 8, 'MON-SAT_VAR'),

('Вильданов Р.Ш.', 'Терапевт-стоматолог', '/images/vildanov.jpg',
 'Специалист по лечению кариеса, пульпита, периодонтита. Восстановление зубов современными материалами. Эстетическая реставрация передних зубов.',
 'Казанский государственный медицинский университет, ординатура по терапевтической стоматологии',
 15, 'MON-FRI_VAR'),

('Гарифуллина А.М.', 'Детский стоматолог', '/images/garifullina.jpg',
 'Специалист по лечению зубов у детей. Работает с маленькими пациентами от 3 лет. Использует методики адаптации детей к стоматологическому лечению.',
 'Казанский государственный медицинский университет, ординатура по детской стоматологии',
 6, 'MON-FRI_9-15');

-- Вставка тестового пользователя (пароль: test123)
INSERT INTO users (username, password_hash, full_name, email, phone, gender, birth_year, address) VALUES
('testuser', '$2a$12$LQv3c1yqBWVHxkd0g6f7Qu/35BMOY7MSIn7xV.CJmkKGCWNDlRhO2',
 'Иванов Иван Иванович', 'ivanov@example.com', '+7 (917) 123-45-67', 'male', 1985,
 'г. Казань, ул. Пушкина, д. 10, кв. 25');

-- Вставка тестовых отзывов
INSERT INTO reviews (user_id, content, rating, status) VALUES
(1, 'Очень доволен лечением! Доктор Эргашев профессионал своего дела. Установил имплант безболезненно, все подробно объяснил. Рекомендую!', 5, 'PUBLISHED'),
(1, 'Валиуллина Л.Н. - прекрасный специалист. Исправляла прикус, результат превзошел ожидания. Вежливая и внимательная.', 5, 'PUBLISHED'),
(1, 'Была на приеме у Вильданова Р.Ш. Качественно пролечила кариес, поставили пломбу, которую не отличить от собственного зуба. Спасибо!', 4, 'PUBLISHED'),
(1, 'Водила ребенка к Гарифуллиной А.М. Доктор нашла подход к дочке, лечение прошло без слез. Теперь дочка не боится стоматологов.', 5, 'PUBLISHED'),
(1, 'Клиника оснащена современным оборудованием, чисто и уютно. Персонал вежливый. Цены адекватные.', 4, 'PUBLISHED');

-- Вставка настроек клиники
INSERT INTO clinic_settings (setting_key, setting_value, description) VALUES
('clinic_name', 'Стоматологическая клиника "Вильдан"', 'Название клиники'),
('clinic_address', 'проспект Ямашева, 43', 'Адрес клиники'),
('clinic_phone', '+7 (917) 909-17-17', 'Основной телефон клиники'),
('working_hours_weekdays', '08:00-20:00', 'График работы в будни'),
('working_hours_saturday', '08:00-14:00', 'График работы в субботу'),
('working_hours_sunday', 'Выходной', 'График работы в воскресенье'),
('patient_count', '15000', 'Количество обслуженных пациентов'),
('min_appointment_advance_days', '1', 'Минимальное количество дней для записи вперед'),
('max_appointment_advance_days', '30', 'Максимальное количество дней для записи вперед');

-- Вставка тестовых логов безопасности
INSERT INTO security_logs (user_id, action_type, ip_address, description) VALUES
(1, 'USER_REGISTRATION', '192.168.1.100', 'Новый пользователь зарегистрирован'),
(1, 'LOGIN_SUCCESS', '192.168.1.100', 'Успешный вход в систему'),
(NULL, 'LOGIN_FAILED', '192.168.1.101', 'Неудачная попытка входа с неверным паролем');

-- =============================================
-- ФУНКЦИИ И ТРИГГЕРЫ
-- =============================================

-- Функция для автоматического обновления времени изменения настроек
CREATE OR REPLACE FUNCTION update_settings_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_settings_timestamp
    BEFORE UPDATE ON clinic_settings
    FOR EACH ROW
    EXECUTE FUNCTION update_settings_timestamp();

-- Функция для логирования действий пользователей
CREATE OR REPLACE FUNCTION log_user_action()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO security_logs (user_id, action_type, description)
    VALUES (NEW.id, 'USER_CREATED', 'Создан новый пользователь: ' || NEW.username);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_log_new_user
    AFTER INSERT ON users
    FOR EACH ROW
    EXECUTE FUNCTION log_user_action();

-- Функция для проверки доступности времени записи
CREATE OR REPLACE FUNCTION check_appointment_availability()
RETURNS TRIGGER AS $$
BEGIN
    -- Проверяем, не занято ли это время у врача
    IF EXISTS (
        SELECT 1 FROM appointments
        WHERE doctor_id = NEW.doctor_id
        AND appointment_date = NEW.appointment_date
        AND appointment_time = NEW.appointment_time
        AND status != 'CANCELLED'
    ) THEN
        RAISE EXCEPTION 'Это время уже занято у выбранного врача';
    END IF;

    -- Проверяем, что запись не в прошлом
    IF (NEW.appointment_date < CURRENT_DATE) OR
       (NEW.appointment_date = CURRENT_DATE AND NEW.appointment_time < CURRENT_TIME) THEN
        RAISE EXCEPTION 'Нельзя записываться на прошедшее время';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_check_appointment_availability
    BEFORE INSERT ON appointments
    FOR EACH ROW
    EXECUTE FUNCTION check_appointment_availability();

-- =============================================
-- ПРЕДСТАВЛЕНИЯ (VIEWS)
-- =============================================

-- Представление для отображения отзывов с информацией о пользователях
CREATE VIEW v_reviews_with_users AS
SELECT
    r.id,
    r.content,
    r.rating,
    r.status,
    r.created_at,
    u.full_name as user_name,
    u.id as user_id
FROM reviews r
JOIN users u ON r.user_id = u.id
WHERE r.status = 'PUBLISHED'
ORDER BY r.created_at DESC;

-- Представление для отображения записей с полной информацией
CREATE VIEW v_appointments_full AS
SELECT
    a.id,
    a.appointment_date,
    a.appointment_time,
    a.status,
    a.created_at,
    u.full_name as patient_name,
    u.phone as patient_phone,
    d.full_name as doctor_name,
    d.specialization as doctor_specialization
FROM appointments a
JOIN users u ON a.user_id = u.id
JOIN doctors d ON a.doctor_id = d.id
ORDER BY a.appointment_date DESC, a.appointment_time DESC;

-- Простое представление для статистики врачей
CREATE VIEW v_doctors_stats AS
SELECT
    d.id,
    d.full_name,
    d.specialization,
    d.experience,
    COUNT(a.id) as total_appointments
FROM doctors d
LEFT JOIN appointments a ON d.id = a.doctor_id AND a.status = 'COMPLETED'
GROUP BY d.id, d.full_name, d.specialization, d.experience;

-- =============================================
-- ПРАВА ДОСТУПА
-- =============================================

-- Создание пользователя для приложения (опционально)
-- CREATE USER dental_app WITH PASSWORD 'secure_password';

-- Выдача прав
GRANT CONNECT ON DATABASE oris TO dental_app;
GRANT USAGE ON SCHEMA public TO dental_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO dental_app;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO dental_app;

-- =============================================
-- СООБЩЕНИЕ О ЗАВЕРШЕНИИ
-- =============================================

DO $$
BEGIN
    RAISE NOTICE '=========================================';
    RAISE NOTICE 'БАЗА ДАННЫХ УСПЕШНО СОЗДАНА!';
    RAISE NOTICE '=========================================';
    RAISE NOTICE 'Таблицы:';
    RAISE NOTICE '  - users (% записей)', (SELECT COUNT(*) FROM users);
    RAISE NOTICE '  - doctors (% записей)', (SELECT COUNT(*) FROM doctors);
    RAISE NOTICE '  - appointments (% записей)', (SELECT COUNT(*) FROM appointments);
    RAISE NOTICE '  - reviews (% записей)', (SELECT COUNT(*) FROM reviews);
    RAISE NOTICE '=========================================';
    RAISE NOTICE 'Тестовые данные добавлены';
    RAISE NOTICE 'Логин для тестирования: testuser';
    RAISE NOTICE 'Пароль для тестирования: test123';
    RAISE NOTICE '=========================================';
END $$;