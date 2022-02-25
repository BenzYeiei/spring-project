package com.demo.firstProject.Model.ForTest;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class ForStream implements Serializable {

    private long id;
    private String name;
    private int age;
    private LocalDate dateCreated;

}
