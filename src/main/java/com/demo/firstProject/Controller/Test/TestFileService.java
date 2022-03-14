package com.demo.firstProject.Controller.Test;

import com.demo.firstProject.Exception.BaseException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;


@Service
public class TestFileService {


    public void FileStorageService(MultipartFile[] requestFile) {
        if (requestFile.length == 0) {
            throw new BaseException("api.uploads.field.file.null", HttpStatus.BAD_REQUEST);
        }

        System.out.println("1 2 3 4 5 6 7");

        try {
            Arrays.stream(requestFile).forEach(file -> Savesave(file));
//            Files.copy(requestFile.getInputStream(), Paths.get("D:/WorkByJava/springBoot/firstProject/firstProject/uploads/"+ requestFile.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            System.out.println(e);
            throw new BaseException("Server can't storage file." + " :" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void Savesave(MultipartFile file) {
        try {
            Files.copy(file.getInputStream(), Paths.get("D:/WorkByJava/springBoot/firstProject/firstProject/uploads/"+ file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new BaseException("Server can't storage file. with file name:" + file.getOriginalFilename() + "\n message Server:" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    public ResponseEntity<Resource> getOne() {
        try {
            Path pp = Paths.get("uploads").toAbsolutePath().normalize();
            Path pp2 = Paths.get(pp + "/123.jpeg");
            System.out.println(pp2);
            Path file = Paths.get("D:/WorkByJava/springBoot/firstProject/firstProject/uploads/123.jpeg");

//            if (!resource.exists() || !resource.isReadable()) {
//                throw new BaseException("Could not read the file!", HttpStatus.INTERNAL_SERVER_ERROR);
//            }
            Resource resource = new UrlResource(pp2.toUri());
            System.out.println(resource.getFilename());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header(HttpHeaders.CONTENT_TYPE, "image/png")
//                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; fileName="+resource.getFilename())
                    .body(resource);
//            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(file);
        } catch (IOException e) {
            throw new BaseException("Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
