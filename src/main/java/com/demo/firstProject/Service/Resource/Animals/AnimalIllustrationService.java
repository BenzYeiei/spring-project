package com.demo.firstProject.Service.Resource.Animals;

import com.demo.firstProject.Configuration.Domain;
import com.demo.firstProject.Controller.Image.SetName;
import com.demo.firstProject.Exception.BaseException;
import com.demo.firstProject.JPA.Entity.Account.AccountEntity;
import com.demo.firstProject.JPA.Entity.AnimalEntity;
import com.demo.firstProject.JPA.Entity.AnimalIllustrationEntity;
import com.demo.firstProject.JPA.Repository.Account.AccountRepository;
import com.demo.firstProject.JPA.Repository.AnimalIllustrationRepositoty;
import com.demo.firstProject.JPA.Repository.AnimalRepository;
import com.demo.firstProject.Service.Resource.Image.ImageService;
import com.demo.firstProject.Service.ServiceModel.AnimalService.AnimalIllustrationModel_CRUD;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AnimalIllustrationService implements AnimalIllustrationModel_CRUD {

    private final AnimalRepository animalRepository;
    private final AnimalIllustrationRepositoty animalIllustrationRepositoty;
    private final ImageService imageService;
    private final AccountRepository accountRepository;

    public AnimalIllustrationService(AnimalRepository animalRepository, AnimalIllustrationRepositoty animalIllustrationRepositoty, ImageService imageService, AccountRepository accountRepository) {
        this.animalRepository = animalRepository;
        this.animalIllustrationRepositoty = animalIllustrationRepositoty;
        this.imageService = imageService;
        this.accountRepository = accountRepository;
    }


    public List<AnimalIllustrationEntity> AnimalIllustrationService_GetListById(AnimalEntity animal) {

        List<AnimalIllustrationEntity> animalIllustrationList = animalIllustrationRepositoty.findAllByAnimalFK(animal);

        return animalIllustrationList;
    }

    // TODO:: Create Illustration of animals
    @Override
    public boolean AnimalIllustrationService_Create(
            AnimalEntity animalEntity,
            AccountEntity accountEntity,
            List<MultipartFile> illustrationFiles,
            String path
    ) {

        // loop
        for (MultipartFile multipartFile : illustrationFiles) {

            // check list is null
            if (multipartFile.isEmpty()) {
                return true;
            }

            // create name image and path image
            String animalIllustration_Name = imageService.getImageName("animal-illustration", multipartFile.getOriginalFilename());
            Path animalIllustration_Path = Path.of(imageService.getDir_name_animal_illustrations() + "/" + animalIllustration_Name);

            // create object
            AnimalIllustrationEntity illustrationObject = new AnimalIllustrationEntity();

            // set data
            illustrationObject.setName(animalIllustration_Name);
            illustrationObject.setAnimalFK(animalEntity);
            illustrationObject.setAccountFK(accountEntity);

            // save data
            try {
                animalIllustrationRepositoty.save(illustrationObject);
            } catch (Exception e) {
                throw new BaseException("Server message -> " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, path);
            }

            // upload image
            try {
                Files.copy(multipartFile.getInputStream(), animalIllustration_Path, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                throw new BaseException("Server message -> " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, path);
            }

        }

        return true;

    }


    // TODO:: Update Illustration of animals
    @Override
    public void AnimalIllustrationService_Update(
            long animalId,
            long illustrationId,
            MultipartFile illustrationFile
    ) {
        // check null of animalId
        if (animalId == 0) {
            throw new BaseException("api.animals.id.null", HttpStatus.BAD_REQUEST);
        }
        // check null of illustrationId
        if (illustrationId == 0) {
            throw new BaseException("api.animals." + animalId +".illustrationId.null", HttpStatus.BAD_REQUEST);
        }
        // check file of illustration
        if (illustrationFile == null) {
            throw new BaseException("api.animals." + animalId + "." + illustrationId + ".field.illustration.null", HttpStatus.BAD_REQUEST);
        }
        // check exists of illustration
        boolean isIllustration = animalIllustrationRepositoty.existsById(illustrationId);
        if (!isIllustration) {
            throw new BaseException("api.animals." + animalId + "." + illustrationId + ".incorrect", HttpStatus.NOT_FOUND);
        }
        // get illustration for update
        AnimalIllustrationEntity animalIllustrationResult = animalIllustrationRepositoty.getById(illustrationId);

        // check exists of animal id and animalFK
        if (animalId != animalIllustrationResult.getAnimalFK().getId()) {
            throw new BaseException("api.animals." + animalId + "." + illustrationId + ".with.animalId not like animalFK", HttpStatus.BAD_REQUEST);
        }

        // create name of illustration
        String illustrationName = SetName.getImageName(illustrationFile.getOriginalFilename());

        // create path of illustration for upload
        Path animalIllustrationPath = Path.of(Domain.dir_name_animal_illustrations + "/" + illustrationName);

        try {
            Files.copy(illustrationFile.getInputStream(), animalIllustrationPath, StandardCopyOption.REPLACE_EXISTING);
            // create path of illustration for delete
            Path oldPath = Path.of(Domain.dir_name_animal_illustrations + "/" + animalIllustrationResult.getName());
            Files.delete(oldPath);
        } catch (Exception e) {
            throw new BaseException("server.error.with->" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // set data to illustration
        animalIllustrationResult.setName(illustrationName);
        // update
        animalIllustrationRepositoty.save(animalIllustrationResult);
    }


    // TODO:: Delete illustration of animal
    @Override
    public void AnimalIllustrationService_Delete(
            long animalId,
            long illustrationId,
            String path
    ) {
        // find animal data
        Optional<AnimalEntity> animal_find = animalRepository.findById(animalId);

        // find illustration
        Optional<AnimalIllustrationEntity> animalIllustration_find = animalIllustrationRepositoty.findById(illustrationId);

        // check null of animal id
        if (animal_find.isEmpty()) {
            throw new BaseException("animals not found.", HttpStatus.NOT_FOUND, path);
        }

        // check null of illustrationId
        if (animalIllustration_find.isEmpty()) {
            throw new BaseException("illustrationId not found.", HttpStatus.NOT_FOUND, path);
        }

        // get data of illustration
        AnimalIllustrationEntity animalIllustrationResult = animalIllustration_find.get();

        // create path of illustration
        Path illustrationPath = Path.of(imageService.getDir_name_animal_illustrations() + "/" + animalIllustrationResult.getName());

        try {
            animalIllustrationRepositoty.deleteById(animalIllustrationResult.getId());
            Files.delete(illustrationPath);
        } catch (Exception e) {
            throw new BaseException("server error, this message -> " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
