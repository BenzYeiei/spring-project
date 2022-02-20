package com.demo.firstProject.DTO.Animals;

import com.demo.firstProject.JPA.Entity.AnimalEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
public class AnimalIllustrationDTO {

    private long id;

    private String name;

}
