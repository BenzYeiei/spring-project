package com.demo.firstProject.DTO.Request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnimalRequest {

    private String name;

    private long quantity;

    private boolean statusState = false;

    private String animalCategory;
}
