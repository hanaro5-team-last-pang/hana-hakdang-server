INSERT INTO uuser (career_info_id, role, name, email, password, birth_date, profile_image_url,
                   is_active, created_at, updated_at)
VALUES (null,
        'MENTOR',
        '삼하늘',
        'tmp@gmail.com',
        '$2b$12$BsQLLSMhHiLvf9NGbjZopuGahPK62R5MeUGbc5h60caKHz0oRIX3m', -- 5678 인코딩
        '200-05-09',
        null,
        TRUE,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP);
