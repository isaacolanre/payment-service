CREATE TABLE IF NOT EXISTS notification_device
(
    id                 BIGSERIAL PRIMARY KEY,
    message_channel    VARCHAR(64)  NOT NULL,
    token              VARCHAR(255) NOT NULL,
    public_id          UUID        DEFAULT gen_random_uuid(),
    account_id         BIGINT REFERENCES user_account (id),
    created_by         VARCHAR(50) DEFAULT 'system',
    created_date       TIMESTAMPTZ DEFAULT now(),
    last_modified_by   VARCHAR(50),
    last_modified_date TIMESTAMPTZ DEFAULT now(),
    version            BIGINT      DEFAULT 0
);
