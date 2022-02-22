package com.demo.firstProject.DTO.Request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnimalRequest {
    private long id;

    private String name;

    private long quantity;

    private boolean statusState = false;

    private LocalDateTime createTime;

    private String animalCategoryFK;
}
