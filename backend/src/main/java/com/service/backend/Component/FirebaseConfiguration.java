package com.service.backend.Component;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.StorageOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

@Slf4j
@Component
public class FirebaseConfiguration {

    // TODO:: get bucket name
    public String getBucketName_FireBase() {

        try {
            String bucketName = null;

            File getFile = new File(System.getProperty("user.dir") + "\\secret\\bucket.txt");

            Scanner myReader = new Scanner(getFile);
            while (myReader.hasNextLine()) {
                bucketName = myReader.nextLine();
            }
            myReader.close();
            return bucketName;
        } catch (FileNotFoundException e) {
            log.error("error in FirebaseConfiguration method getBucketName_FireBase(), message->{}", e.getMessage());
            return null;
        }
    }

    // TODO:: get storage option
    public StorageOptions getStorageOptions_FireBase() {

        try {
            // get admin-sdk
            FileInputStream serviceAccount = serviceAccount = new FileInputStream(
                    System.getProperty("user.dir") + "\\secret\\firebase-adminsdk.json"
            );

            Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(
                    System.getProperty("user.dir") + "\\secret\\firebase-adminsdk.json"
            ));

            return StorageOptions.newBuilder()
                    .setProjectId(getBucketName_FireBase().replace(".appspot.com", ""))
//					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setCredentials(credentials)
                    .build();
        } catch (FileNotFoundException e) {
            log.error("error in FirebaseConfiguration method getStorageOptions_FireBase(), message->{}", e.getMessage());
            return null;
        } catch (IOException e) {
            log.error("error in FirebaseConfiguration method getStorageOptions_FireBase(), message->{}", e.getMessage());
            return null;
        }
    }
}
