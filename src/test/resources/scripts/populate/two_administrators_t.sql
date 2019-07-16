INSERT INTO users (id, username, password) VALUES (1, 'username', 'password');
INSERT INTO users (id, username, password) VALUES (2, 'seconduser', 'password2');
INSERT INTO administrators (id, users_id, first_name, last_name) VALUES (1, 1, 'User', 'Name');
INSERT INTO administrators (id, users_id, first_name, last_name) VALUES (2, 2, 'Second', 'User');