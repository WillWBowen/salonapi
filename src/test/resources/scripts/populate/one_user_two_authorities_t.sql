INSERT INTO users(id, username, password) VALUES (1, 'username', 'password');
INSERT INTO roles (id, name) VALUES (1, 'admin');
INSERT INTO users_x_roles (users_id, roles_id) VALUES (1, 1);
INSERT INTO authorities (id, name) VALUES (1, 'ROLE_USER');
INSERT INTO authorities (id, name) VALUES (2, 'ROLE_ADMIN');
INSERT INTO roles_x_authorities (roles_id, authorities_id) VALUES (1, 1);
INSERT INTO roles_x_authorities (roles_id, authorities_id) VALUES (1, 2);