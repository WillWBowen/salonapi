INSERT INTO users (id, username, password) VALUES (1, 'username', 'password');
INSERT INTO employees (id, users_id, first_name, last_name) VALUES (1, 1, 'User', 'Name');
INSERT into employee_shifts (id, employees_id, day, start_time, end_time) VALUES (1, 1, 'MONDAY', '09:00', '17:30');
INSERT into employee_shifts (id, employees_id, day, start_time, end_time) VALUES (2, 1, 'TUESDAY', '10:00', '19:30');