package com.service.backend.DTO.Request;

import lombok.Data;

@Data
public class AnimalRequest {

    private String name;

    private long quantity;

    private boolean statusState = false;

    private String animalCategory;
}
