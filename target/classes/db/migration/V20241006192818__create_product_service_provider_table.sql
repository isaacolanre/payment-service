CREATE TABLE IF NOT EXISTS product_service_provider (
                                                        product_id BIGINT NOT NULL,
                                                        service_provider_id BIGINT NOT NULL,
                                                        PRIMARY KEY (product_id, service_provider_id),
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE,
    FOREIGN KEY (service_provider_id) REFERENCES service_provider(id) ON DELETE CASCADE
    );
