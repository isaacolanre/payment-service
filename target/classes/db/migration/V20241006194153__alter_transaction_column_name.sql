DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'transactions'
        AND column_name = 'linked_transaction_id'
        AND data_type = 'uuid'
    ) THEN
ALTER TABLE transactions DROP COLUMN linked_transaction_id;
END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'transactions'
        AND column_name = 'linked_transaction_id'
    ) THEN
ALTER TABLE transactions ADD COLUMN linked_transaction_id BIGSERIAL;
END IF;
END $$;
