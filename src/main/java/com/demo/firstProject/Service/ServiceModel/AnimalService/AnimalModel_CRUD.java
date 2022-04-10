package com.demo.firstProject.Service.ServiceModel.AnimalService;

import com.demo.firstProject.DTO.Animals.AnimalDTO;
import com.demo.firstProject.JPA.Entity.Animal.AnimalEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

public interface AnimalModel_CRUD {

    Page<AnimalDTO> AnimalService_GetPage(int pageNum);

    AnimalDTO AnimalService_GetOneById(long id);

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
