-- Insert first user and corresponding account
DO
$$
DECLARE
v_user_id INT;
BEGIN
    -- Insert user 'biller_airtime'
INSERT INTO users (
    public_id,
    first_name,
    last_name,
    username,
    gender,
    email,
    mobile,
    namespace,
    kyc_level,
    status
) VALUES (
             gen_random_uuid(),
             'Biller',
             'Airtime Provider',
             'biller_airtime',
             'OTHER',  -- Adjust if needed
             'airtime@provider.com',
             '07000000001',
             'SYSTEM',
             'TIER_THREE',
             'ENABLED'
         )
    RETURNING id INTO v_user_id;

-- Insert corresponding account for 'biller_airtime'
INSERT INTO user_account (
    balance,
    firstname,
    lastname,
    account_type,
    account_kyc_level,
    currency,
    overdraft_limit,
    locked_balance,
    lien_amount,
    account_number,
    pan,
    bvn,
    public_id,
    is_primary_account,
    parent_account,
    account_status,
    user_id
) VALUES (
             0.00,
             'Biller 1',
             'Airtime Provider',
             'CASH',
             'TIER_THREE',
             'NGN',
             0.00,
             0.00,
             0.00,
             'SP-ACCOUNT-001',
             '1234567890123456',
             '12345678901',
             gen_random_uuid(),
             TRUE,
             NULL,
             'ACTIVE',
             v_user_id  -- Use the returned user ID
         );
END
$$;

-- Insert second user and corresponding account
DO
$$
DECLARE
v_user_id INT;
BEGIN
    -- Insert user 'biller_utility'
INSERT INTO users (
    public_id,
    first_name,
    last_name,
    username,
    gender,
    email,
    mobile,
    namespace,
    kyc_level,
    status
) VALUES (
             gen_random_uuid(),
             'Biller',
             'Utility Bill Provider',
             'biller_utility',
             'OTHER',
             'utility@provider.com',
             '07000000002',
             'SYSTEM',
             'TIER_THREE',
             'ENABLED'
         )
    RETURNING id INTO v_user_id;

-- Insert corresponding account for 'biller_utility'
INSERT INTO user_account (
    balance,
    firstname,
    lastname,
    account_type,
    account_kyc_level,
    currency,
    overdraft_limit,
    locked_balance,
    lien_amount,
    account_number,
    pan,
    bvn,
    public_id,
    is_primary_account,
    parent_account,
    account_status,
    user_id
) VALUES (
             0.00,
             'Default Biller 2',
             'Utility Bill Provider',
             'CASH',
             'TIER_THREE',
             'NGN',
             0.00,
             0.00,
             0.00,
             'SP-ACCOUNT-002',
             '2345678901234567',
             '23456789012',
             gen_random_uuid(),
             TRUE,
             NULL,
             'ACTIVE',
             v_user_id  -- Use the returned user ID
         );
END
$$;

-- Insert third user and corresponding account
DO
$$
DECLARE
v_user_id INT;
BEGIN
    -- Insert user 'biller_subscription'
INSERT INTO users (
    public_id,
    first_name,
    last_name,
    username,
    gender,
    email,
    mobile,
    namespace,
    kyc_level,
    status
) VALUES (
             gen_random_uuid(),
             'Biller',
             'Subscription Provider',
             'biller_subscription',
             'OTHER',
             'subscription@provider.com',
             '07000000003',
             'SYSTEM',
             'TIER_THREE',
             'ENABLED'
         )
    RETURNING id INTO v_user_id;

-- Insert corresponding account for 'biller_subscription'
INSERT INTO user_account (
    balance,
    firstname,
    lastname,
    account_type,
    account_kyc_level,
    currency,
    overdraft_limit,
    locked_balance,
    lien_amount,
    account_number,
    pan,
    bvn,
    public_id,
    is_primary_account,
    parent_account,
    account_status,
    user_id
) VALUES (
             0.00,
             'Default Biller 3',
             'Subscription Provider',
             'CASH',
             'TIER_THREE',
             'NGN',
             0.00,
             0.00,
             0.00,
             'SP-ACCOUNT-003',
             '3456789012345678',
             '34567890123',
             gen_random_uuid(),
             TRUE,
             NULL,
             'ACTIVE',
             v_user_id  -- Use the returned user ID
         );
END
$$;
