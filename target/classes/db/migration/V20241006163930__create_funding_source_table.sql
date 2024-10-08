CREATE TABLE IF NOT EXISTS funding_source
(
    id                      BIGSERIAL PRIMARY KEY,
    version                 INT     DEFAULT 0,
    type                    VARCHAR(64) NOT NULL,
    account_public_id       UUID,
    public_id               UUID    DEFAULT gen_random_uuid(),
    institution_code        VARCHAR(64),
    supports_debit          BOOLEAN DEFAULT FALSE,
    account_identifier      VARCHAR(255),
    funding_source_provider VARCHAR(64),
    status                  VARCHAR(64),
    user_id                 BIGSERIAL REFERENCES users (id),
    account_id              BIGINT REFERENCES user_account (id)
);
