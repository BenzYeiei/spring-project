package com.demo.firstProject.Service.Resource.Image;

import com.demo.firstProject.Exception.BaseException;
import com.demo.firstProject.Service.ServiceModel.ImageService.ImageUseFirebaseModel;
import com.google.cloud.storage.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ImageService_Firebase implements ImageUseFirebaseModel {

    private final StorageOptions cloud_FireStorage;
    private final String getBucketName_FireBase;
    private final ImageService imageService;

    public ImageService_Firebase(StorageOptions cloud_fireStorage, String getBucketName_fireBase, ImageService imageService) {
        cloud_FireStorage = cloud_fireStorage;
        getBucketName_FireBase = getBucketName_fireBase;
        this.imageService = imageService;
    }


    @Override
    public Resource ImageService_FireBase_Create(byte[] fileData, String fileName, String servletPath) {

        // get Service with Storage class
        Storage storage = cloud_FireStorage.getService();

        // create BlobId
        BlobId blobId = BlobId.of(
                getBucketName_FireBase,
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

            // create resource from fileData
            Resource resource = new ByteArrayResource(fileData_Result);

            return resource;
        } catch (Exception e) {
            throw new BaseException("server error message -> " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, servletPath);
        }
    }

    @Override
    public Resource ImageService_FireBase_Read(String fileName, String servletPath) {

        // get Service with Storage class
        Storage storage = cloud_FireStorage.getService();

        // create BlobId
        BlobId blobId = BlobId.of(getBucketName_FireBase, fileName);

        try {
            // upload file to firebase
            Blob blob = storage.get(blobId);

            // get file from blob
            // getContent method return byte array
            byte[] fileData_Result = blob.getContent();

            // create resource from fileData
            Resource resource = new ByteArrayResource(fileData_Result);

            return resource;
        } catch (Exception e) {
            throw new BaseException("server error message -> " + e.getMessage(), HttpStatus.NOT_FOUND, servletPath);
        }
    }

    @Override
    public boolean ImageService_FireBase_Delete(String fileName) {

        // get Service with Storage class
        Storage storage = cloud_FireStorage.getService();

        // create BlobId
        BlobId blobId = BlobId.of(getBucketName_FireBase, fileName);

        // upload file to firebase
        boolean isDelete = storage.delete(blobId);

        return isDelete;
    }
}
