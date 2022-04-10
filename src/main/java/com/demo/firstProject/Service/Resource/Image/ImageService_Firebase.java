package com.demo.firstProject.Service.Resource.Image;

import com.demo.firstProject.Component.FirebaseConfiguration;
import com.demo.firstProject.Exception.BaseException;
import com.demo.firstProject.Service.ServiceModel.ImageService.ImageUseFirebaseModel;
import com.google.cloud.storage.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ImageService_Firebase implements ImageUseFirebaseModel {

    private final ImageService imageService;
    private final FirebaseConfiguration firebaseConfiguration;

    public ImageService_Firebase(ImageService imageService, FirebaseConfiguration firebaseConfiguration) {
        this.imageService = imageService;
        this.firebaseConfiguration = firebaseConfiguration;
    }


    @Override
    public Resource ImageService_FireBase_Create(byte[] fileData, String fileName, String servletPath) {

        // get Service with Storage class
        Storage storage = firebaseConfiguration.getStorageOptions_FireBase().getService();

        // create BlobId
        BlobId blobId = BlobId.of(
                firebaseConfiguration.getBucketName_FireBase(),
                imageService.getImageName("fire-base", fileName)
        );

        // create BlobInfo from BlobId,
        // if not set contentTyp, type in firebase is application/octet-stream
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();

        try {
            // upload file to firebase
            Blob blob = storage.create(blobInfo, fileData);

            // get file from blob
            // getContent method return byte array
            byte[] fileData_Result = blob.getContent();

            // create log
            log.info("upload image to firebase, image name '{}', Size {} byte", fileName, fileData.length);

            // create resource from fileData
            Resource resource = new ByteArrayResource(fileData_Result);

            return resource;
        } catch (Exception e) {
            log.error("error at ImageService_FireBase_Create() method, message->{}", e.getMessage());
            throw new BaseException("server error message -> " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, servletPath);
        }
    }

    @Override
    public Resource ImageService_FireBase_Read(String fileName, String servletPath) {

        // get Service with Storage class
        Storage storage = firebaseConfiguration.getStorageOptions_FireBase().getService();

        // create BlobId
        BlobId blobId = BlobId.of(firebaseConfiguration.getBucketName_FireBase(), fileName);

        try {
            // upload file to firebase
            Blob blob = storage.get(blobId);

            // get file from blob
            // getContent method return byte array
            byte[] fileData_Result = blob.getContent();

            log.info("read image from firebase, image name '{}', Size {} byte", fileName, fileData_Result.length);

            // create resource from fileData
            Resource resource = new ByteArrayResource(fileData_Result);

            return resource;
        } catch (Exception e) {
            log.error("error at ImageService_FireBase_Read() method, message->{}", e.getMessage());
            throw new BaseException("server error message -> " + e.getMessage(), HttpStatus.NOT_FOUND, servletPath);
        }
    }

    @Override
    public boolean ImageService_FireBase_Delete(String fileName) {

        // get Service with Storage class
        Storage storage = firebaseConfiguration.getStorageOptions_FireBase().getService();

        // create BlobId
        BlobId blobId = BlobId.of(firebaseConfiguration.getBucketName_FireBase(), fileName);

        // upload file to firebase
        boolean isDelete = storage.delete(blobId);

        log.info("delete image in firebase. image name '{}'", fileName);

        return isDelete;
    }
}
