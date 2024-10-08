CREATE TABLE IF NOT EXISTS product
(
    id                 BIGSERIAL PRIMARY KEY,
    name               VARCHAR(255) NOT NULL,
    description        TEXT,
    created_by         VARCHAR(50) DEFAULT 'system',
    created_date       TIMESTAMPTZ DEFAULT now(),
    last_modified_by   VARCHAR(50),
    last_modified_date TIMESTAMPTZ DEFAULT now(),
    version            BIGINT      DEFAULT 0
);
