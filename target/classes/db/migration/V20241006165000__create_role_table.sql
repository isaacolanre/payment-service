CREATE TABLE  IF NOT EXISTS role
(
    id         BIGSERIAL PRIMARY KEY,
    role_name  VARCHAR(64) NOT NULL,
    created_at TIMESTAMPTZ DEFAULT now(),
    updated_at TIMESTAMPTZ DEFAULT now()
);
