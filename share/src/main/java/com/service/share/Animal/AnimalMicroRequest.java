package com.service.share.Animal;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
public class AnimalMicroRequest implements Serializable {
    private MultipartFile image;
    private String imageName;
}
