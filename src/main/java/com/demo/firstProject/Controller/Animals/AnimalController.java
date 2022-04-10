package com.demo.firstProject.Controller.Animals;

import com.demo.firstProject.DTO.Animals.AnimalDTO;
import com.demo.firstProject.Exception.BaseException;
import com.demo.firstProject.JPA.Entity.Animal.AnimalEntity;
import com.demo.firstProject.Service.Resource.Animals.AnimalIllustrationService;
import com.demo.firstProject.Service.Resource.Animals.AnimalsService;
import com.demo.firstProject.Service.Resource.Image.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/animals") @Slf4j
public class AnimalController {

    private final AnimalsService animalsService;
    private final AnimalIllustrationService animalIllustrationService;
    private final ImageService imageService;

    public AnimalController(AnimalsService animalsService, AnimalIllustrationService animalIllustrationService, ImageService imageService) {
        this.animalsService = animalsService;
        this.animalIllustrationService = animalIllustrationService;
        this.imageService = imageService;
    }


    // TODO:: Get Page
    @GetMapping("/page")
    public ResponseEntity<Page> AnimalGet_Page(
            @RequestParam(name = "number", required = false) String pageNum, // variable Sting because can't throw error if int not assign data
            HttpServletRequest request
    ) {

        // check null of numPage
        if (pageNum == null) {
            log.error("number of page is null.");
            throw new BaseException("must send number of page.", HttpStatus.BAD_REQUEST, request.getServletPath());
        }

        //convert String to int
        int convertPageNum;
        try {
            convertPageNum = Integer.parseInt(pageNum);
        } catch (NumberFormatException e) {
            log.error("number params not integer.");
            throw new BaseException("must assign number params with integer", HttpStatus.BAD_REQUEST, request.getServletPath());
        }

        // get page from service
        Page<AnimalDTO> page = animalsService.AnimalService_GetPage(convertPageNum);

        return ResponseEntity.ok().body(page);
    }


    // TODO: GET -> get one by id
    @GetMapping("/{id}")
    public ResponseEntity<AnimalDTO> AnimalAPI_GetById(@PathVariable long id) {

        // get animal dto from service
        AnimalDTO animal = animalsService.AnimalService_GetOneById(id);

        return ResponseEntity.status(HttpStatus.OK).body(animal);
    }



    // TODO: POST Create Animal
    @PostMapping("")
    @ResponseBody
//    @ResponseStatus(HttpStatus.CREATED)
//    public String AnimalCrate(
    public ResponseEntity<AnimalDTO> AnimalCrate(
            @RequestPart(name = "name", required = false) String name,
            @RequestPart(name = "category", required = false) String animalCategory,
            @RequestPart(name = "quantity", required = false) String quantity,
            @RequestPart(name = "imageProfile", required = false) MultipartFile imageProfile,
            @RequestPart(name = "illustration", required = false) List<MultipartFile> illustrationFiles,
            HttpServletRequest request,
            Principal principal
    ) {

        // create animal data
        AnimalEntity animalResult = animalsService.AnimalService_Create(
                name,
                animalCategory,
                Integer.parseInt(quantity),
                imageProfile,
                illustrationFiles,
                request.getServletPath(),
                principal
        );

        AnimalDTO animal = animalsService.AnimalService_GetOneById(animalResult.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(animal);
    }


    // TODO: PUT Update Animal
    @PutMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AnimalDTO> AnimalUpdate(
            @PathVariable long id,
            @RequestPart(name = "name", required = false) String name,
            @RequestPart(name = "category", required = false) String animalCategory,
            @RequestPart(name = "quantity", required = false) String quantity,
            @RequestPart(name = "statusState", required = false) String statusState,
            @RequestPart(name = "imageProfile", required = false) MultipartFile imageProfile,
            @RequestPart(name = "illustration", required = false) List<MultipartFile> illustrationFiles,
            HttpServletRequest request,
            Principal principal
    ) {
        AnimalEntity animalResult = animalsService.AnimalService_Update(
                id,
                name,
                animalCategory,
                Integer.parseInt(quantity),
                Boolean.parseBoolean(statusState),
                imageProfile,
                illustrationFiles,
                request.getServletPath(),
                principal
        );

//        animalIllustrationService.AnimalIllustrationService_Create(animalEntity, illustrationFiles);

        AnimalDTO animal = animalsService.AnimalService_GetOneById(animalResult.getId());

        return ResponseEntity.status(HttpStatus.OK).body(animal);
    }


    // TODO: DELETE -> Delete Animal
    @DeleteMapping("/{id}")
    public ResponseEntity<ArrayList<AnimalDTO>> AnimalDelete(@PathVariable long id, Principal principal, HttpServletRequest request) {
        animalsService.AnimalService_Delete(id, principal, request.getServletPath());
        return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<AnimalDTO>());
    }

}
