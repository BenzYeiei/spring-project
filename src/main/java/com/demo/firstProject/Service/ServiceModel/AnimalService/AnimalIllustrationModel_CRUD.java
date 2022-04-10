package com.demo.firstProject.Service.ServiceModel.AnimalService;

import com.demo.firstProject.JPA.Entity.Account.AccountEntity;
import com.demo.firstProject.JPA.Entity.Animal.AnimalEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AnimalIllustrationModel_CRUD {

    boolean AnimalIllustrationService_Create(
            AnimalEntity animalEntity,
            AccountEntity accountEntity,
            List<MultipartFile> illustrationFiles,
            String path
    );

    void AnimalIllustrationService_Update(
            long animalId,
            long illustrationId,
            MultipartFile illustrationFile
    );

    void AnimalIllustrationService_Delete(
            long animalId,
            long illustrationId,
            String path
    );

}
