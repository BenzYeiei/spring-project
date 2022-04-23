package com.service.share;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class ImageInitialization {

    private final String domainUrl = "http://127.0.0.1:8080";
    private final String imageDomain = "http://127.0.0.1:8082";

    private final String dir_name_animal = "C:/WorkByJava/springBoot/animalshopping/image/uploads/animals/";
    private final String directory_animal = "C:/WorkByJava/springBoot/animalshopping/image/uploads/animals/";

    private final String dir_name_animal_illustrations = "C:/WorkByJava/springBoot/animalshopping/image/uploads/animals/illustrations/";
    private final String directory_animal_illustrations = "C:/WorkByJava/springBoot/animalshopping/image/uploads/animals/illustrations/";

    private final String renderAnimalImageProfile = imageDomain + "/api/v1/renders/animals?imageProfile=";
    private final String renderAnimalIllustration = imageDomain + "/api/v1/renders/animals?illustration=";

    private final String keyOfAnimalImageProfile = "animal_imageProfile";
    private final String keyOfAnimalIllustration = "animal_illustration";


    public String getImageName(String name, String originalName) {

        if (originalName == null) {
            return null;
        }

        // get file extension
        String fileExtension = originalName.substring(originalName.lastIndexOf(".") + 1);

        LocalDateTime localDateTime = LocalDateTime.now();

        return localDateTime.getYear()
                + "-" + localDateTime.getMonthValue()
                + "-" + localDateTime.getDayOfMonth()
                + "T" + localDateTime.getHour()
                + "-" + localDateTime.getMinute()
                + "-" + localDateTime.getSecond()
                + "-" + localDateTime.getNano()
                + "N" + name + "." + fileExtension;
    }

}
