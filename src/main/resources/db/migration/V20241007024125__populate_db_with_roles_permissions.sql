INSERT INTO permission (description) VALUES
                                         ('VIEW_ACCOUNT'),
                                         ('EDIT_ACCOUNT'),
                                         ('DELETE_ACCOUNT'),
                                         ('CREATE_TRANSACTION'),
                                         ('VIEW_TRANSACTION'),
                                         ('MANAGE_USERS'),
                                         ('VIEW_REPORTS');

-- Insert Roles
INSERT INTO role (role_name) VALUES
                                 ('CUSTOMER'),
                                 ('AGENT'),
                                 ('ADMIN'),
                                 ('CUSTOMER_SUPPORT');

INSERT INTO role_permission (role_id, permission_id)
VALUES
    ((SELECT id FROM role WHERE role_name = 'ADMIN'), (SELECT id FROM permission WHERE description = 'VIEW_ACCOUNT')),
    ((SELECT id FROM role WHERE role_name = 'ADMIN'), (SELECT id FROM permission WHERE description = 'EDIT_ACCOUNT')),
    ((SELECT id FROM role WHERE role_name = 'ADMIN'), (SELECT id FROM permission WHERE description = 'MANAGE_USERS')),
    ((SELECT id FROM role WHERE role_name = 'CUSTOMER'), (SELECT id FROM permission WHERE description = 'VIEW_ACCOUNT')),
    ((SELECT id FROM role WHERE role_name = 'CUSTOMER'), (SELECT id FROM permission WHERE description = 'VIEW_TRANSACTION')),
    ((SELECT id FROM role WHERE role_name = 'CUSTOMER_SUPPORT'), (SELECT id FROM permission WHERE description = 'EDIT_ACCOUNT')),
    ((SELECT id FROM role WHERE role_name = 'CUSTOMER_SUPPORT'), (SELECT id FROM permission WHERE description = 'VIEW_REPORTS'));
