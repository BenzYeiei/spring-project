package com.demo.firstProject.Service.Resource.Image;

import com.demo.firstProject.Service.ServiceModel.ImageService.ImageModel;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Data
@Service
public class ImageService implements ImageModel {

    @Value("${image.fetch.domain}")
    private String domainUrl;

    @Value("${image.dir_name.animal}")
    private String dir_name_animal;

    @Value("${image.dir_name.illustration}")
    private String dir_name_animal_illustrations;


    @Override
    public String getImageName(String name, String originalName) {

        // get file extension
        String fileExtension = originalName.substring(originalName.lastIndexOf(".") + 1);

        LocalDateTime localDateTime = LocalDateTime.now();
        String newName = localDateTime.getYear()
                + "-" + localDateTime.getMonthValue()
                + "-" + localDateTime.getDayOfMonth()
                + "T" + localDateTime.getHour()
                + "-" + localDateTime.getMinute()
                + "-" + localDateTime.getSecond()
                + "-" + localDateTime.getNano()
                + "N" + name + "." + fileExtension;
        return newName;
    }
}
