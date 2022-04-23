package com.service.backend.Model.CustomRes;

import lombok.Data;


@Data
public class AnimalCreateRES {

    private String name;

    private String category;


    public AnimalCreateRES(String name, String category) {
        this.name = name;
        this.category = category;
    }
}
