INSERT INTO users(id, username, password) VALUES (1, 'username', 'password');
INSERT INTO roles (id, name) VALUES (1, 'customer');
INSERT INTO roles (id, name) VALUES (2, 'employee');
INSERT INTO users_x_roles (users_id, roles_id) VALUES (1, 1);
INSERT INTO users_x_roles (users_id, roles_id) VALUES (1, 2);