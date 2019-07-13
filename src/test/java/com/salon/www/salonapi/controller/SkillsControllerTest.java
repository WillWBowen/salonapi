package com.salon.www.salonapi.controller;

import com.salon.www.salonapi.exception.SkillNotFoundException;
import com.salon.www.salonapi.model.Skill;
import com.salon.www.salonapi.service.SkillService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@WebMvcTest(SkillController.class)
public class SkillsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SkillService skillService;

    @Test
    public void getSkill_ShouldReturnSkill() throws Exception {
        given(skillService.getSkill(anyLong())).willReturn(new Skill("manicure", 20));
        mockMvc.perform(MockMvcRequestBuilders.get("/skills/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("manicure"))
                .andExpect(jsonPath("price").value(20));
    }

    @Test
    public void getSkill_notFound() throws Exception {
        given(skillService.getSkill(anyLong())).willThrow(new SkillNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.get("/skills/1"))
                .andExpect(status().isNotFound());
    }
}
