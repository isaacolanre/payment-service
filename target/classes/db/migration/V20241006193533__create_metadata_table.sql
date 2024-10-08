

CREATE TABLE IF NOT EXISTS service_provider_metadata (
                                                         service_provider_id BIGINT NOT NULL,
                                                         key VARCHAR(255) NOT NULL,
    value TEXT,
    PRIMARY KEY (service_provider_id, key),
    FOREIGN KEY (service_provider_id) REFERENCES service_provider(id) ON DELETE CASCADE
    );
CREATE TABLE IF NOT EXISTS service_provider_supported_currencies (
                                                                     service_provider_id BIGINT NOT NULL,
                                                                     supported_currencies VARCHAR(255) NOT NULL,
    FOREIGN KEY (service_provider_id) REFERENCES service_provider(id) ON DELETE CASCADE
    );


CREATE TABLE IF NOT EXISTS transaction_metadata (
                                                    transaction_id BIGINT NOT NULL,
                                                    key VARCHAR(255) NOT NULL,
    value TEXT,
    PRIMARY KEY (transaction_id, key),
    FOREIGN KEY (transaction_id) REFERENCES transactions(id) ON DELETE CASCADE
    );
