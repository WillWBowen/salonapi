INSERT INTO users (id, username, password) VALUES (1, 'username', 'password');
INSERT INTO employees (id, users_id, first_name, last_name) VALUES (1, 1, 'User', 'Name');

INSERT INTO users (id, username, password) VALUES (2, 'customername', 'password');
INSERT INTO customers (id, users_id, first_name, last_name, email) VALUES (1, 1, 'Customer', 'Name', 'user.name@test.com');