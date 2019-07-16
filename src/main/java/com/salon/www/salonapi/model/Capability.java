package com.salon.www.salonapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Capability {
    private long id;
    private String name;

    public Capability(String name) {
        this.name = name;
    }
}
