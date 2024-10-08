CREATE TABLE IF NOT EXISTS transactions
(
    id                      BIGSERIAL PRIMARY KEY,
    public_id               UUID        DEFAULT gen_random_uuid(),
    transaction_type        VARCHAR(64)    NOT NULL,
    amount                  NUMERIC(19, 2) NOT NULL,
    currency                VARCHAR(3)     NOT NULL,
    status                  VARCHAR(64)    NOT NULL,
    provider_id             VARCHAR(255)   NOT NULL,
    transaction_reference   VARCHAR(255),
    description             TEXT,
    phone_number            VARCHAR(16),
    payment_method          VARCHAR(255),
    payment_provider        VARCHAR(255),
    fee                     NUMERIC(19, 2),
    total_amount            NUMERIC(19, 2) NOT NULL,
    created_at              TIMESTAMPTZ DEFAULT now(),
    updated_at              TIMESTAMPTZ,
    completed_at            TIMESTAMPTZ,
    error_message           TEXT,
    error_code              VARCHAR(255),
    external_transaction_id VARCHAR(255),
    linked_transaction_id   UUID,
    geo_location_id         BIGSERIAL REFERENCES geo_location (id),
    account_id              BIGSERIAL REFERENCES user_account (id),
    user_id              BIGSERIAL REFERENCES users (id)
    );


CREATE TABLE IF NOT EXISTS notification
(
    id                BIGSERIAL PRIMARY KEY,
    public_id         UUID        DEFAULT gen_random_uuid(),
    user_id           BIGSERIAL REFERENCES users (id),
    transaction_id    BIGSERIAL REFERENCES transactions (id),
    message           TEXT        NOT NULL,
    notification_type VARCHAR(64) NOT NULL,
    delivery_method   VARCHAR(64) NOT NULL,
    status            VARCHAR(64) DEFAULT 'PENDING',
    sent_at           TIMESTAMPTZ,
    created_at        TIMESTAMPTZ DEFAULT now()
);
