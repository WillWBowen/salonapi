package com.salon.www.salonapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Skill {
    private long id;
    private String name;
    private int price;

    public Skill(String name, int price){
        this.name = name;
        this.price = price;
    }
}
