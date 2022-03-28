package com.demo.firstProject.Service.ServiceModel.ImageService;

import org.springframework.core.io.Resource;

public interface ImageUseFirebaseModel {

    Resource ImageService_FireBase_Create(byte[] fileData, String fileName, String servletPath);

    Resource ImageService_FireBase_Read(String fileName, String servletPath);

    boolean ImageService_FireBase_Delete(String fileName);

}
