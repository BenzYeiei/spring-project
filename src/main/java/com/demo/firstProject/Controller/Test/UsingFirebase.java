package com.demo.firstProject.Controller.Test;

import com.demo.firstProject.Exception.BaseException;
import com.demo.firstProject.Service.Resource.Image.ImageService;
import com.demo.firstProject.Service.Resource.Image.ImageService_Firebase;
import com.google.cloud.storage.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;

@RequestMapping(path = "/api/tests/fire-base/")
@Controller
public class UsingFirebase {

    private final StorageOptions cloud_FireStorage;
    private final String getBucketName_FireBase;
    private final ImageService_Firebase imageService_firebase;

    public UsingFirebase(StorageOptions cloud_fireStorage, String getBucketName_fireBase, ImageService_Firebase imageService_firebase) {
        cloud_FireStorage = cloud_fireStorage;
        getBucketName_FireBase = getBucketName_fireBase;
        this.imageService_firebase = imageService_firebase;
    }


    @PostMapping(path = "/upload")
    public ResponseEntity Test_Firebase_Upload(
            @RequestPart(name = "fils", required = false) MultipartFile file,
            HttpServletRequest request
    ) {
        // check field of file
        if (file.isEmpty()) {
            throw new BaseException("field file is null.", HttpStatus.BAD_REQUEST, request.getServletPath());
        }

        // getBytes() method in class MultipartFile must throw IOException
        try {
            // get resource
            Resource resource = imageService_firebase.ImageService_FireBase_Create(
                    file.getBytes(),
                    file.getOriginalFilename(),
                    request.getServletPath()
            );

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/png").body(resource);
        } catch (IOException e) {
            throw new BaseException("Error with getBytes() this message -> " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, request.getServletPath());
        }

    }

    @GetMapping(path = "/read")
    public ResponseEntity Test_Firebase_Read(
            @RequestParam(name = "name", required = false) String fileName,
            HttpServletRequest request
    ) {

        // check RequestParam
        if (fileName == null) {
            throw new BaseException("not found image.", HttpStatus.NOT_FOUND, request.getServletPath());
        }

        // get resource from ImageService_FireBase_Read
        Resource resource = imageService_firebase.ImageService_FireBase_Read(
                fileName,
                request.getServletPath()
        );

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/png").body(resource);
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity Test_Firebase_Delete(
            @RequestParam(name = "name", required = false) String fileName,
            HttpServletRequest request
    ){

        // check RequestParam
        if (fileName == null) {
            throw new BaseException("not found image.", HttpStatus.NOT_FOUND, request.getServletPath());
        }

        // get process service
        boolean isDelete = imageService_firebase.ImageService_FireBase_Delete(
                fileName
        );

        // check process service
        if (!isDelete) {
            throw  new BaseException("server can't delete file. or file name not exists.", HttpStatus.NOT_FOUND, request.getServletPath());
        }

        return ResponseEntity.ok().body(new ArrayList<>());
    }

}
