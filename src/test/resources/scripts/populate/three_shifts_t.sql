INSERT INTO users (id, username, password) VALUES (1, 'username', 'password');
INSERT INTO users (id, username, password) VALUES (2, 'seconduser', 'password2');
INSERT INTO employees (id, users_id, first_name, last_name) VALUES (1, 1, 'User', 'Name');
INSERT INTO employees (id, users_id, first_name, last_name) VALUES (2, 2, 'Second', 'User');
INSERT into employee_shifts (id, employees_id, day, start_time, end_time) VALUES (1, 1, 2, '09:00', '17:30');
INSERT into employee_shifts (id, employees_id, day, start_time, end_time) VALUES (2, 2, 3, '10:00', '19:30');
INSERT into employee_shifts (id, employees_id, day, start_time, end_time) VALUES (3, 1, 3, '10:00', '19:30');