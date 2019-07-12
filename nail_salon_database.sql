-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `token` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`employees`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`employees` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `users_id` INT NOT NULL,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `position` VARCHAR(45) NOT NULL,
  `status` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`, `users_id`),
  INDEX `fk_employees_users1_idx` (`users_id` ASC),
  CONSTRAINT `fk_employees_users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `mydb`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`customers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`customers` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `users_id` INT NOT NULL,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `phone` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`id`, `users_id`),
  INDEX `fk_customers_users1_idx` (`users_id` ASC),
  CONSTRAINT `fk_customers_users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `mydb`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`administrators`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`administrators` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `users_id` INT NOT NULL,
  PRIMARY KEY (`id`, `users_id`),
  INDEX `fk_administrators_users1_idx` (`users_id` ASC),
  CONSTRAINT `fk_administrators_users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `mydb`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`roles` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`capabilities`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`capabilities` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`services`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`services` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `price` INT NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`employee_shifts`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`employee_shifts` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `employees_id` INT NOT NULL,
  `employees_users_id` INT NOT NULL,
  `day` VARCHAR(45) NOT NULL,
  `start_time` VARCHAR(45) NULL,
  `end_time` VARCHAR(45) NULL,
  PRIMARY KEY (`id`, `employees_id`, `employees_users_id`),
  INDEX `fk_employee_shifts_employees1_idx` (`employees_id` ASC, `employees_users_id` ASC),
  CONSTRAINT `fk_employee_shifts_employees1`
    FOREIGN KEY (`employees_id` , `employees_users_id`)
    REFERENCES `mydb`.`employees` (`id` , `users_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`employee_schedule`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`employee_schedule` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `employees_id` INT NOT NULL,
  `employees_users_id` INT NOT NULL,
  PRIMARY KEY (`id`, `employees_id`, `employees_users_id`),
  INDEX `fk_employee_schedule_employees1_idx` (`employees_id` ASC, `employees_users_id` ASC),
  CONSTRAINT `fk_employee_schedule_employees1`
    FOREIGN KEY (`employees_id` , `employees_users_id`)
    REFERENCES `mydb`.`employees` (`id` , `users_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`bookings`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`bookings` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `customers_id` INT NOT NULL,
  `customers_users_id` INT NOT NULL,
  `employees_id` INT NOT NULL,
  `employees_users_id` INT NOT NULL,
  PRIMARY KEY (`id`, `customers_id`, `customers_users_id`, `employees_id`, `employees_users_id`),
  INDEX `fk_bookings_customers1_idx` (`customers_id` ASC, `customers_users_id` ASC),
  INDEX `fk_bookings_employees1_idx` (`employees_id` ASC, `employees_users_id` ASC),
  CONSTRAINT `fk_bookings_customers1`
    FOREIGN KEY (`customers_id` , `customers_users_id`)
    REFERENCES `mydb`.`customers` (`id` , `users_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_bookings_employees1`
    FOREIGN KEY (`employees_id` , `employees_users_id`)
    REFERENCES `mydb`.`employees` (`id` , `users_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`employees_x_services`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`employees_x_services` (
  `employees_id` INT NOT NULL,
  `services_id` INT NOT NULL,
  PRIMARY KEY (`employees_id`, `services_id`),
  INDEX `fk_employees_has_services_services1_idx` (`services_id` ASC),
  INDEX `fk_employees_has_services_employees_idx` (`employees_id` ASC),
  CONSTRAINT `fk_employees_has_services_employees`
    FOREIGN KEY (`employees_id`)
    REFERENCES `mydb`.`employees` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_employees_has_services_services1`
    FOREIGN KEY (`services_id`)
    REFERENCES `mydb`.`services` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`users_x_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`users_x_roles` (
  `users_id` INT NOT NULL,
  `roles_id` INT NOT NULL,
  PRIMARY KEY (`users_id`, `roles_id`),
  INDEX `fk_users_has_roles_roles1_idx` (`roles_id` ASC),
  INDEX `fk_users_has_roles_users1_idx` (`users_id` ASC),
  CONSTRAINT `fk_users_has_roles_users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `mydb`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_users_has_roles_roles1`
    FOREIGN KEY (`roles_id`)
    REFERENCES `mydb`.`roles` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`roles_x_capabilities`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`roles_x_capabilities` (
  `roles_id` INT NOT NULL,
  `capabilities_id` INT NOT NULL,
  PRIMARY KEY (`roles_id`, `capabilities_id`),
  INDEX `fk_roles_has_capabilities_capabilities1_idx` (`capabilities_id` ASC),
  INDEX `fk_roles_has_capabilities_roles1_idx` (`roles_id` ASC),
  CONSTRAINT `fk_roles_has_capabilities_roles1`
    FOREIGN KEY (`roles_id`)
    REFERENCES `mydb`.`roles` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_roles_has_capabilities_capabilities1`
    FOREIGN KEY (`capabilities_id`)
    REFERENCES `mydb`.`capabilities` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`bookings_x_services`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`bookings_x_services` (
  `bookings_id` INT NOT NULL,
  `bookings_customers_id` INT NOT NULL,
  `bookings_customers_users_id` INT NOT NULL,
  `services_id` INT NOT NULL,
  PRIMARY KEY (`bookings_id`, `bookings_customers_id`, `bookings_customers_users_id`, `services_id`),
  INDEX `fk_bookings_has_services_services1_idx` (`services_id` ASC),
  INDEX `fk_bookings_has_services_bookings1_idx` (`bookings_id` ASC, `bookings_customers_id` ASC, `bookings_customers_users_id` ASC),
  CONSTRAINT `fk_bookings_has_services_bookings1`
    FOREIGN KEY (`bookings_id` , `bookings_customers_id` , `bookings_customers_users_id`)
    REFERENCES `mydb`.`bookings` (`id` , `customers_id` , `customers_users_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_bookings_has_services_services1`
    FOREIGN KEY (`services_id`)
    REFERENCES `mydb`.`services` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
