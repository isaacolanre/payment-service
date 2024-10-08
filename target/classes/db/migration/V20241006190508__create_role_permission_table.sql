CREATE TABLE IF NOT EXISTS role_permission (
                                               role_id BIGSERIAL REFERENCES role(id) ON DELETE CASCADE,
    permission_id BIGSERIAL REFERENCES permission(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
    );
CREATE TABLE IF NOT EXISTS user_role (
                                         user_id BIGINT NOT NULL,
                                         role_id BIGINT NOT NULL,
                                         PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user
    FOREIGN KEY(user_id)
    REFERENCES users(id),
    CONSTRAINT fk_role
    FOREIGN KEY(role_id)
    REFERENCES role(id)
    );

CREATE TABLE IF NOT EXISTS users_permissions (
                                                 user_id BIGSERIAL NOT NULL,
                                                 permissions_id BIGSERIAL NOT NULL,
                                                 PRIMARY KEY (user_id, permissions_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (permissions_id) REFERENCES permission(id) ON DELETE CASCADE
    );
