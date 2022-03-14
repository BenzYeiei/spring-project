package com.demo.firstProject.Service.Resource.Animals;

import com.demo.firstProject.Configuration.Domain;
import com.demo.firstProject.Controller.Image.SetName;
import com.demo.firstProject.Exception.BaseException;
import com.demo.firstProject.JPA.Entity.AnimalEntity;
import com.demo.firstProject.JPA.Entity.AnimalIllustrationEntity;
import com.demo.firstProject.JPA.Repository.AnimalIllustrationRepositoty;
import com.demo.firstProject.JPA.Repository.AnimalRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class AnimalIllustrationService {

    private final AnimalRepository animalRepository;
    private final AnimalIllustrationRepositoty animalIllustrationRepositoty;

    public AnimalIllustrationService(AnimalRepository animalRepository, AnimalIllustrationRepositoty animalIllustrationRepositoty) {
        this.animalRepository = animalRepository;
        this.animalIllustrationRepositoty = animalIllustrationRepositoty;
    }


    // TODO:: Create Illustration of animals
    public void AnimalIllustrationService_Create(
            long animalId,
            MultipartFile[] illustrationFiles
    ) {
        // Files not null can save
        if (illustrationFiles != null) {
            // check null of animal id
            if (animalId == 0) {
                throw new BaseException("api.animals.illustrations.create.field.animal-id.null", HttpStatus.BAD_REQUEST);
            }
            // check exists of animal
            boolean isAnimalEntity = animalRepository.existsById(animalId);
            if (!isAnimalEntity) {
                throw new BaseException("api.animals.illustrations.create.field.animal-id.not-found", HttpStatus.NOT_FOUND);
            }
            // get object of animal entity
            AnimalEntity animalResult = animalRepository.getById(animalId);
            // save and upload image
            for (MultipartFile MultipartFileObj : illustrationFiles) {
                // create name and path of illustration
                String animalIllustrationName = SetName.getImageName(MultipartFileObj.getOriginalFilename());
                Path animalIllustrationPath = Path.of(Domain.dir_name_animal_illustrations + "/" + animalIllustrationName);
                // create object for save
                AnimalIllustrationEntity animalIllustrationEntityObj = new AnimalIllustrationEntity();
                animalIllustrationEntityObj.setName(animalIllustrationName);
                animalIllustrationEntityObj.setAnimalFK(animalResult);
                // save and upload
                try {
                    animalIllustrationRepositoty.save(animalIllustrationEntityObj);
                    Files.copy(MultipartFileObj.getInputStream(), animalIllustrationPath, StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception e) {
                    throw new BaseException("api.animals.illustrations.create.save-upload.error->with:" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
    }


    // TODO:: Update Illustration of animals
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
    public void AnimalIllustrationService_Delete(
            long animalId,
            long illustrationId
    ) {
        // check null of animal id
        if (animalId == 0) {
            throw new BaseException("api.animals.id.null", HttpStatus.BAD_REQUEST);
        }
        // check null of illustrationId
        if (illustrationId == 0) {
            throw new BaseException("api.animals." + animalId +".illustrationId.null", HttpStatus.BAD_REQUEST);
        }
        // check exists of illustration
        boolean isIllustration = animalIllustrationRepositoty.existsById(illustrationId);
        if (!isIllustration) {
            throw new BaseException("api.animals." + animalId + "." + illustrationId + ".incorrect", HttpStatus.NOT_FOUND);
        }
        // check exists of animal id and animalFK
        long animalResultId = animalIllustrationRepositoty.getById(illustrationId).getAnimalFK().getId();
        if (animalId != animalResultId) {
            throw new BaseException("api.animals." + animalId + "." + illustrationId + ".with.animalId not like animalFK", HttpStatus.BAD_REQUEST);
        }
        // get entity of illustration
        AnimalIllustrationEntity animalIllustrationResult = animalIllustrationRepositoty.getById(illustrationId);
        // create path of illustration
        Path illustrationPath = Path.of(Domain.dir_name_animal_illustrations + "/" + animalIllustrationResult.getName());
        try {
            Files.delete(illustrationPath);
        } catch (Exception e) {
            throw new BaseException("server.error.with.can't delete illustration of animal->" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
