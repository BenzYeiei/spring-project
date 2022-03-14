package com.demo.firstProject.Controller.Test;

import com.demo.firstProject.Exception.BaseException;
import com.demo.firstProject.DTO.Request.AnimalRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@Controller
@RequestMapping("/api/tests")
public class TestFileApi {

    private final TestFileService testFileService;

    @Value("${dir_name.root}")
    String abc;

    public TestFileApi(TestFileService testFileService) {
        this.testFileService = testFileService;
    }


    @PostMapping("/uploads")
    public ResponseEntity TestFile(AnimalRequest animalRequest, @RequestParam(value = "file", required = false) MultipartFile file) {
        System.out.println(this.abc);
        System.out.println(animalRequest);
        if (file == null) {
            throw new BaseException("BABABABA", HttpStatus.NOT_FOUND);
        }

//        testFileService.FileStorageService(file);
        return ResponseEntity.status(HttpStatus.OK).body(new ArrayList<>());
    }

    @GetMapping("/uploads/123")
    public ResponseEntity<Resource> Display_img() {
        return testFileService.getOne();
    }

}
