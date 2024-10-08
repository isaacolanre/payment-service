ALTER TABLE user_role
    ADD COLUMN new_user_id BIGSERIAL NOT NULL,
    ADD COLUMN new_role_id BIGSERIAL NOT NULL;

UPDATE user_role SET new_user_id = user_id;
UPDATE user_role SET new_role_id = role_id;

ALTER TABLE user_role
DROP COLUMN user_id,
    DROP COLUMN role_id;

ALTER TABLE user_role
    RENAME COLUMN new_user_id TO user_id;

ALTER TABLE user_role
    RENAME COLUMN new_role_id TO role_id;

ALTER TABLE user_role
    ADD CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES users(id),
    ADD CONSTRAINT fk_role
    FOREIGN KEY (role_id)
    REFERENCES role(id),
    ADD PRIMARY KEY (user_id, role_id);

ALTER TABLE users_permissions
    ADD COLUMN new_user_id BIGSERIAL NOT NULL,
    ADD COLUMN new_permissions_id BIGSERIAL NOT NULL;

UPDATE users_permissions SET new_user_id = user_id;
UPDATE users_permissions SET new_permissions_id = permissions_id;

ALTER TABLE users_permissions
DROP COLUMN user_id,
    DROP COLUMN permissions_id;

ALTER TABLE users_permissions
    RENAME COLUMN new_user_id TO user_id;

ALTER TABLE users_permissions
    RENAME COLUMN new_permissions_id TO permissions_id;

ALTER TABLE users_permissions
    ADD CONSTRAINT fk_user_permissions
        FOREIGN KEY (user_id)
            REFERENCES users(id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_permissions
    FOREIGN KEY (permissions_id)
    REFERENCES permission(id) ON DELETE CASCADE,
    ADD PRIMARY KEY (user_id, permissions_id);
