INSERT INTO service_provider (name, api_url, api_key, description, product_type, status, timeout, base_currency, support_contact, last_updated)
VALUES
    ('Airtime Provider', 'https://api.airtimeprovider.com', 'APIKEY123', 'Provides airtime services', 'AIRTIME', 'ACTIVE', 30, 'NGN', 'support@airtimeprovider.com', now()),
    ('Utility Bill Provider', 'https://api.utilitybillprovider.com', 'APIKEY456', 'Handles utility bill payments', 'UTILITY_BILL', 'ACTIVE', 60, 'NGN', 'support@utilitybillprovider.com', now()),
    ('Subscription Provider', 'https://api.subscriptionprovider.com', 'APIKEY789', 'Manages subscriptions', 'SUBSCRIPTION', 'INACTIVE', 45, 'USD', 'support@subscriptionprovider.com', now());

INSERT INTO product (name, description)
VALUES
    ('AIRTIME', 'Product for purchasing mobile airtime'),
    ('UTILITY_BILL', 'Product for paying utility bills like electricity, water, etc.'),
    ('SUBSCRIPTION', 'Product for handling various subscription services');

INSERT INTO product_service_provider (product_id, service_provider_id)
VALUES
    ((SELECT id FROM product WHERE name = 'AIRTIME'), (SELECT id FROM service_provider WHERE name = 'Airtime Provider')),
    ((SELECT id FROM product WHERE name = 'UTILITY_BILL'), (SELECT id FROM service_provider WHERE name = 'Utility Bill Provider')),
    ((SELECT id FROM product WHERE name = 'SUBSCRIPTION'), (SELECT id FROM service_provider WHERE name = 'Subscription Provider'));
