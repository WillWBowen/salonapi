-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema library
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `library` ;

-- -----------------------------------------------------
-- Schema library
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `library` DEFAULT CHARACTER SET utf8 ;
USE `library` ;

-- -----------------------------------------------------
-- Table `library`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NULL,
  `enabled` BIT NOT NULL DEFAULT 0,
  `lastPasswordResetDate` DATE NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library`.`employees`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`employees` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `users_id` BIGINT NOT NULL,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `position` VARCHAR(45) NOT NULL,
  `status` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`, `users_id`),
  INDEX `fk_employees_users1_idx` (`users_id` ASC),
  CONSTRAINT `fk_employees_users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `library`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library`.`customers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`customers` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `users_id` BIGINT NOT NULL,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `phone` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`id`, `users_id`),
  INDEX `fk_customers_users1_idx` (`users_id` ASC),
  CONSTRAINT `fk_customers_users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `library`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library`.`administrators`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`administrators` (
  `id` BIGINT NOT NULL,
  `users_id` BIGINT NOT NULL,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`, `users_id`),
  INDEX `fk_administrators_users1_idx` (`users_id` ASC),
  CONSTRAINT `fk_administrators_users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `library`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`roles` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library`.`capabilities`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`capabilities` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library`.`skills`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`skills` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `price` INT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library`.`employee_shifts`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`employee_shifts` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `employees_id` BIGINT NOT NULL,
  `day` TINYINT NOT NULL COMMENT 'day should be a number between 1 (Sunday) and 7 (Saturday)',
  `start_time` DATETIME NOT NULL,
  `end_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`, `employees_id`),
  INDEX `fk_employee_shifts_employees1_idx` (`employees_id` ASC),
  CONSTRAINT `fk_employee_shifts_employees1`
    FOREIGN KEY (`employees_id`)
    REFERENCES `library`.`employees` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library`.`employee_schedule`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`employee_schedule` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `employees_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`, `employees_id`),
  INDEX `fk_employee_schedule_employees1_idx` (`employees_id` ASC),
  CONSTRAINT `fk_employee_schedule_employees1`
    FOREIGN KEY (`employees_id`)
    REFERENCES `library`.`employees` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library`.`bookings`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`bookings` (
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
    REFERENCES `library`.`customers` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_bookings_employees1`
    FOREIGN KEY (`employees_id`)
    REFERENCES `library`.`employees` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library`.`employees_x_skills`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`employees_x_skills` (
  `employees_id` BIGINT NOT NULL,
  `skills_id` BIGINT NOT NULL,
  PRIMARY KEY (`employees_id`, `skills_id`),
  INDEX `fk_employees_has_services_services1_idx` (`skills_id` ASC),
  INDEX `fk_employees_has_services_employees_idx` (`employees_id` ASC),
  CONSTRAINT `fk_employees_has_services_employees`
    FOREIGN KEY (`employees_id`)
    REFERENCES `library`.`employees` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_employees_has_services_services1`
    FOREIGN KEY (`skills_id`)
    REFERENCES `library`.`skills` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library`.`users_x_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`users_x_roles` (
  `users_id` BIGINT NOT NULL,
  `roles_id` BIGINT NOT NULL,
  PRIMARY KEY (`users_id`, `roles_id`),
  INDEX `fk_users_has_roles_roles1_idx` (`roles_id` ASC),
  INDEX `fk_users_has_roles_users1_idx` (`users_id` ASC),
  CONSTRAINT `fk_users_has_roles_users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `library`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_has_roles_roles1`
    FOREIGN KEY (`roles_id`)
    REFERENCES `library`.`roles` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library`.`roles_x_capabilities`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`roles_x_capabilities` (
  `roles_id` BIGINT NOT NULL,
  `capabilities_id` BIGINT NOT NULL,
  PRIMARY KEY (`roles_id`, `capabilities_id`),
  INDEX `fk_roles_has_capabilities_capabilities1_idx` (`capabilities_id` ASC),
  INDEX `fk_roles_has_capabilities_roles1_idx` (`roles_id` ASC),
  CONSTRAINT `fk_roles_has_capabilities_roles1`
    FOREIGN KEY (`roles_id`)
    REFERENCES `library`.`roles` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_roles_has_capabilities_capabilities1`
    FOREIGN KEY (`capabilities_id`)
    REFERENCES `library`.`capabilities` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `library`.`bookings_x_services`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`bookings_x_services` (
  `bookings_id` BIGINT NOT NULL,
  `services_id` BIGINT NOT NULL,
  PRIMARY KEY (`bookings_id`, `services_id`),
  INDEX `fk_bookings_has_services_services1_idx` (`services_id` ASC),
  INDEX `fk_bookings_has_services_bookings1_idx` (`bookings_id` ASC),
  CONSTRAINT `fk_bookings_has_services_bookings1`
    FOREIGN KEY (`bookings_id`)
    REFERENCES `library`.`bookings` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_bookings_has_services_services1`
    FOREIGN KEY (`services_id`)
    REFERENCES `library`.`skills` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
