package com.demo.firstProject.Service.Animals;

import com.demo.firstProject.Controller.Image.SetName;
import com.demo.firstProject.Exception.BaseException;
import com.demo.firstProject.DTO.Animals.AnimalDTO;
import com.demo.firstProject.JPA.Entity.AnimalCategoryEntity;
import com.demo.firstProject.JPA.Entity.AnimalEntity;
import com.demo.firstProject.JPA.Repository.AnimalCategoryRepository;
import com.demo.firstProject.JPA.Repository.AnimalRepository;
import com.demo.firstProject.DTO.Request.AnimalRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnimalsService {

    private final AnimalRepository animalRepository;
    private final AnimalCategoryRepository animalCategoryRepository;

    @Value("${dir_name.animal}")
    private String rootDirImage;

    public AnimalsService(AnimalRepository animalRepository, AnimalCategoryRepository animalCategoryRepository) {
        this.animalRepository = animalRepository;
        this.animalCategoryRepository = animalCategoryRepository;
    }


    // TODO:: Get List Animal
    public List<AnimalDTO> AnimalService_GetList() {
        return animalRepository.findAll().stream().map(AnimalEntity::SetAnimal_dto).collect(Collectors.toList());
    }


    // TODO:: Get One Animal by Id
    public AnimalDTO AnimalService_GetOneById(long id) {
        // Check exists by id
        boolean isAnimalEntity = animalRepository.existsById(id);
        if (!isAnimalEntity) {
            throw new BaseException("api.animals.get.field.id.not-found", HttpStatus.NOT_FOUND);
        }
        return animalRepository.getById(id).SetAnimal_dto();
    }


    // TODO:: Create Animal
    public long AnimalService_Create(AnimalRequest request, MultipartFile imageProfile) {
        // check name
        if (request.getName() == null) {
            throw new BaseException("api.animals.post.field.name.null", HttpStatus.BAD_REQUEST);
        }
        // check category
        if (request.getAnimalCategoryFK() == null) {
            throw new BaseException("api.animals.post.field.category.null", HttpStatus.BAD_REQUEST);
        }
        // check profile image
        if (imageProfile == null) {
            throw new BaseException("api.animals.post.field.imageprofile.null", HttpStatus.BAD_REQUEST);
        }
        // check category exists
        boolean isCategoryName = animalCategoryRepository.existsByCategoryName(request.getAnimalCategoryFK());
        if (!isCategoryName) {
            throw new BaseException("api.animals.post.field.category.not-straight", HttpStatus.BAD_REQUEST);
        }
        // create name image
        String imageProfileName = SetName.getImageName(imageProfile.getOriginalFilename());
        // create path image
        Path newPath = Path.of(rootDirImage + imageProfileName);
        // upload image
        try{
            Files.copy(imageProfile.getInputStream(), newPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new BaseException("can't upload image. System:" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Get object animal category for save
        AnimalCategoryEntity AnimalCategoryData = animalCategoryRepository.findByCategoryName(request.getAnimalCategoryFK());
        // Set request to AnimalEntity
        AnimalEntity animalEntityObj = new AnimalEntity();
        animalEntityObj.setName(request.getName());
        animalEntityObj.setImageProfile(imageProfileName);
        animalEntityObj.setAnimalCategoryFK(AnimalCategoryData);
        animalEntityObj.setQuantity(request.getQuantity());
        animalEntityObj.setStatusState(request.isStatusState());
        animalEntityObj.setCreateTime(LocalDateTime.now());

        // Create animal with save();
        long animalId = animalRepository.save(animalEntityObj).getId();

        // check saved data.
        if (animalId == 0) {
            try {
                Files.delete(newPath);
            } catch (Exception e) {
                throw new BaseException("Serve.File.error" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            throw new BaseException("api.animals.post.field.error.function.save()", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return animalId;
    }


    // TODO:: Update Animal
    public long AnimalService_Update(long id, AnimalRequest request, MultipartFile imageProfile) {
        // Create exists by id
        boolean isAnimalId = animalRepository.existsById(id);
        if (!isAnimalId) {
            throw new BaseException("api.animals.put.field.id.not-found", HttpStatus.NOT_FOUND);
        }

        // Check null
        if (request.getName() == null) {
            throw new BaseException("api.animals.put.field.name.null", HttpStatus.BAD_REQUEST);
        }
        if (request.getAnimalCategoryFK() == null) {
            throw new BaseException("api.animals.put.field.category.null", HttpStatus.BAD_REQUEST);
        }

        // Verify
        boolean isCategoryName = animalCategoryRepository.existsByCategoryName(request.getAnimalCategoryFK());
        if (!isCategoryName) {
            throw new BaseException("api.animals.put.field.category.not-straight", HttpStatus.BAD_REQUEST);
        }


        // Get object animal category for save
        AnimalCategoryEntity AnimalCategoryData = animalCategoryRepository.findByCategoryName(request.getAnimalCategoryFK());


        // Set request to AnimalEntity
        AnimalEntity animalEntity = animalRepository.getById(id);
        animalEntity.setName(request.getName());
        animalEntity.setQuantity(request.getQuantity());
        animalEntity.setStatusState(request.isStatusState());
        animalEntity.setAnimalCategoryFK(AnimalCategoryData);

        // set for image
        if (imageProfile != null) {
            String imageProfileName = SetName.getImageName(imageProfile.getOriginalFilename());
            Path imageProfilePath = Path.of(rootDirImage + "/" + imageProfileName);
            try {
                Files.copy(imageProfile.getInputStream(), imageProfilePath, StandardCopyOption.REPLACE_EXISTING);
                Files.delete(Path.of(rootDirImage + "/" + animalEntity.getImageProfile()));
            } catch (Exception e) {
                throw new BaseException("can't upload image. System:" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            // set request image file name to object
            animalEntity.setImageProfile(imageProfileName);
        }

        // Save
        AnimalEntity animalEntityResult = animalRepository.save(animalEntity);

        // check saved data.
        if (animalEntityResult.getName() == null) {
            try {
                String imageProfileName = SetName.getImageName(imageProfile.getOriginalFilename());
                Path imageProfilePath = Path.of(rootDirImage + "/" + imageProfileName);
                Files.delete(imageProfilePath);
            } catch (Exception e) {
                throw new BaseException("Serve.File.error" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
            throw new BaseException("api.animals.post.field.error.function.save()", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return animalEntityResult.getId();
    }


    // TODO:: Delete Animal
    public void AnimalService_Delete(long id) {
        // TODO: Check exists by id
        boolean isAnimalId = animalRepository.existsById(id);
        if (!isAnimalId) {
            throw new BaseException("api.animals.delete.field.id.not-found", HttpStatus.NOT_FOUND);
        }

        // TODO: Delete animal
        try {
            AnimalEntity animalEntityReslt = animalRepository.getById(id);
            animalRepository.deleteById(id);
            Files.delete(Path.of(rootDirImage + "/" + animalEntityReslt.getImageProfile()));
        } catch (Exception e) {
            throw new BaseException("server can't delete data.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
