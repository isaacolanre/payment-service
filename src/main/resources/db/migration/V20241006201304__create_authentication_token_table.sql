CREATE TABLE IF NOT EXISTS authorization_token (
                                                   id BIGSERIAL PRIMARY KEY,
                                                public_id UUID,
                                                   user_id BIGSERIAL NOT NULL,
                                                   access_token VARCHAR(2048) NOT NULL UNIQUE,
    refresh_token VARCHAR(2048) NOT NULL UNIQUE,
    expiry_date TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
    );

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_user') THEN
ALTER TABLE authorization_token
    ADD CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id);
END IF;
END $$;
