package com.salon.www.salonapi.controller;

import com.salon.www.salonapi.exception.SkillNotFoundException;
import com.salon.www.salonapi.model.Skill;
import com.salon.www.salonapi.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class SkillController {

    private SkillService skillService;

    @Autowired
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping("/skills/{id}")
    private Skill getSkill(@PathVariable Long id) {
        return skillService.getSkill(id);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private void skillNotFoundHandler(SkillNotFoundException ex) {}
}
