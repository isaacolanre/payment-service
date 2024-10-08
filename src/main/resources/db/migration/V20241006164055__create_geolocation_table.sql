CREATE TABLE IF NOT EXISTS geo_location
(
    id          BIGSERIAL PRIMARY KEY,
    public_id   UUID DEFAULT gen_random_uuid(),
    city        VARCHAR(255),
    country     VARCHAR(255),
    region      VARCHAR(255),
    postal_code VARCHAR(64)
);
