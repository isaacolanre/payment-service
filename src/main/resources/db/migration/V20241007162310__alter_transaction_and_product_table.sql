DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'transactions'
        AND column_name = 'service_provider_id'
    ) THEN
ALTER TABLE transactions
    ADD COLUMN service_provider_id BIGINT REFERENCES service_provider(id);
END IF;

    -- Add product_id column if it doesn't exist
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'transactions'
        AND column_name = 'product_id'
    ) THEN
ALTER TABLE transactions
    ADD COLUMN product_id BIGINT REFERENCES product(id);
END IF;
END $$;


DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'product'
        AND column_name = 'value'
    ) THEN
ALTER TABLE product
    ADD COLUMN value NUMERIC(19, 2) NOT NULL DEFAULT 0.00;
END IF;
END $$;

