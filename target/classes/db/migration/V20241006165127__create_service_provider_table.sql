CREATE TABLE IF NOT EXISTS service_provider
(
    id                   BIGSERIAL PRIMARY KEY,
    name                 VARCHAR(255) NOT NULL UNIQUE,
    api_url              TEXT         NOT NULL,
    api_key              VARCHAR(255) NOT NULL,
    description          TEXT,
    product_type         VARCHAR(64),
    status               VARCHAR(64)  NOT NULL,
    timeout              INT,
    supported_currencies TEXT[],
    base_currency        VARCHAR(3),
    support_contact      VARCHAR(255),
    last_updated         TIMESTAMPTZ,
    created_by           VARCHAR(50) DEFAULT 'system',
    created_date         TIMESTAMPTZ DEFAULT now(),
    last_modified_by     VARCHAR(50),
    last_modified_date   TIMESTAMPTZ DEFAULT now(),
    version              BIGINT      DEFAULT 0
);
