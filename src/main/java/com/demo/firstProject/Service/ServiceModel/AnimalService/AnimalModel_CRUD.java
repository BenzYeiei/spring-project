package com.demo.firstProject.Service.ServiceModel.AnimalService;

import com.demo.firstProject.DTO.Animals.AnimalDTO;
import com.demo.firstProject.JPA.Entity.AnimalEntity;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

public interface AnimalModel_CRUD {

    List<AnimalEntity> AnimalService_GetList();

    AnimalEntity AnimalService_GetOneById(long id);

    AnimalEntity AnimalService_Create(
            String name,
            String animalCategory,
            int quantity,
            MultipartFile imageProfile,
            List<MultipartFile> illustrationFiles,
            String path,
            Principal principal
    );

    AnimalEntity AnimalService_Update(
            long id,
            String name,
            String animalCategory,
            int quantity,
            boolean statusState,
            MultipartFile imageProfile,
            List<MultipartFile> illustrationFiles,
            String path,
            Principal principal
    );

    void AnimalService_Delete(long id, Principal principal, String path);
}
