CREATE TABLE IF NOT EXISTS `users` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(45) NOT NULL,
    `password` VARCHAR(255) NOT NULL,
    `email` VARCHAR(255) NULL,
    `enabled` BIT NOT NULL DEFAULT 0,
    `lastPasswordResetDate` DATE NULL,
    PRIMARY KEY (`id`))
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