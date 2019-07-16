CREATE TABLE IF NOT EXISTS `users` (
   `id` BIGINT NOT NULL AUTO_INCREMENT,
   `username` VARCHAR(45) NOT NULL,
   `password` VARCHAR(255) NOT NULL,
   `token` VARCHAR(255) NULL,
   PRIMARY KEY (`id`))
    ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `customers` (
   `id` BIGINT NOT NULL AUTO_INCREMENT,
   `users_id` BIGINT NOT NULL,
   `first_name` VARCHAR(45) NOT NULL,
   `last_name` VARCHAR(45) NOT NULL,
   `email` VARCHAR(45) NULL,
   `phone` VARCHAR(10) NULL,
   PRIMARY KEY (`id`, `users_id`),
   INDEX `fk_customers_users1_idx` (`users_id` ASC),
   CONSTRAINT `fk_customers_users1`
       FOREIGN KEY (`users_id`)
           REFERENCES `users` (`id`)
           ON DELETE NO ACTION
           ON UPDATE NO ACTION)
    ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `employees` (
   `id` BIGINT NOT NULL AUTO_INCREMENT,
   `users_id` BIGINT NOT NULL,
   `first_name` VARCHAR(45) NOT NULL,
   `last_name` VARCHAR(45) NOT NULL,
   `position` VARCHAR(45) NULL,
   `status` VARCHAR(45) NULL,
   PRIMARY KEY (`id`, `users_id`),
   INDEX `fk_employees_users1_idx` (`users_id` ASC),
   CONSTRAINT `fk_employees_users1`
       FOREIGN KEY (`users_id`)
           REFERENCES `users` (`id`)
           ON DELETE NO ACTION
           ON UPDATE NO ACTION)
    ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `bookings` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `customers_id` BIGINT NOT NULL,
    `employees_id` BIGINT NOT NULL,
    `booking_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `end_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`, `customers_id`, `employees_id`),
    INDEX `fk_bookings_customers1_idx` (`customers_id` ASC),
    INDEX `fk_bookings_employees1_idx` (`employees_id` ASC),
    CONSTRAINT `fk_bookings_customers1`
        FOREIGN KEY (`customers_id`)
            REFERENCES `customers` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_bookings_employees1`
        FOREIGN KEY (`employees_id`)
            REFERENCES `employees` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION)
    ENGINE = InnoDB;