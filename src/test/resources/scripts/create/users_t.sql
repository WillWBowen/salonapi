CREATE TABLE IF NOT EXISTS `users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `token` VARCHAR(255) NULL,
  PRIMARY KEY (`id`))
    ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `roles` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`id`))
    ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `users_x_roles` (
    `users_id` BIGINT NOT NULL,
    `roles_id` BIGINT NOT NULL,
    PRIMARY KEY (`users_id`, `roles_id`),
    INDEX `fk_users_has_roles_roles1_idx` (`roles_id` ASC),
    INDEX `fk_users_has_roles_users1_idx` (`users_id` ASC),
    CONSTRAINT `fk_users_has_roles_users1`
     FOREIGN KEY (`users_id`)
         REFERENCES `users` (`id`)
         ON DELETE NO ACTION
         ON UPDATE NO ACTION,
    CONSTRAINT `fk_users_has_roles_roles1`
     FOREIGN KEY (`roles_id`)
         REFERENCES `roles` (`id`)
         ON DELETE NO ACTION
         ON UPDATE NO ACTION)
    ENGINE = InnoDB;