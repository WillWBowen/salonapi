CREATE TABLE skills (
    `id` INT NOT NULL AUTO_INCREMENT,
     `name` VARCHAR(45) NOT NULL,
     `price` INT NOT NULL,
     PRIMARY KEY (`id`))
    ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS employees_x_skills (
   `employees_id` INT NOT NULL,
   `skills_id` INT NOT NULL,
   PRIMARY KEY (`employees_id`, `skills_id`),
   INDEX `fk_employees_has_skills_skills1_idx` (`skills_id` ASC),
   CONSTRAINT `fk_employees_has_skills_skills1`
       FOREIGN KEY (`skills_id`)
           REFERENCES skills (`id`)
           ON DELETE NO ACTION
           ON UPDATE NO ACTION)
    ENGINE = InnoDB;