package com.service.backend.Model;


import lombok.Data;

import java.io.Serializable;

@Data
public class TestRedisModel implements Serializable {

    private String id;

    private String name;

}
