package com.salon.www.salonapi.controller;

import com.salon.www.salonapi.exception.SkillNotFoundException;
import com.salon.www.salonapi.model.Skill;
import com.salon.www.salonapi.service.itf.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class SkillController {

    private SkillService skillService;

    @Autowired
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping("/skills/{id}")
    private ResponseEntity<Skill> getSkill(@PathVariable Long id) {

        return new ResponseEntity<>(skillService.getSkill(id), HttpStatus.OK);
    }

    @PostMapping(value = "/skills", consumes = MediaType.APPLICATION_JSON_VALUE, produces =  MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> postSkill(@RequestBody Skill skill) {
        skillService.createSkill(skill);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private void skillNotFoundHandler(SkillNotFoundException ex) {}
}
