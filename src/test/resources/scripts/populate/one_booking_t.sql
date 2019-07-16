INSERT INTO users (id, username, password) VALUES (1, 'customer', 'password');
INSERT INTO users (id, username, password) VALUES (2, 'employee', 'password');
INSERT INTO customers (id, users_id, first_name, last_name, email, phone) VALUES (1, 1, 'First', 'Name', 'fake@email.com', '5555555555');
INSERT INTO employees (id, users_id, first_name, last_name) VALUES (1, 1, 'Best', 'Employee');
INSERT INTO bookings (employees_id, customers_id, booking_time, end_time) VALUES (1, 1, '2019-07-30 11:00:00', '2019-07-30 12:30:00');