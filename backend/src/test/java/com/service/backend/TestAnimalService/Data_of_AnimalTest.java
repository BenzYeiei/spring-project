package com.service.backend.TestAnimalService;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.List;


@NoArgsConstructor
@Getter
public class Data_of_AnimalTest {

    private String dir_name_test = "C:/WorkByJava/springBoot/firstProject/src/test/Image_Test";

    // set name of image profile
    private String testData_imageProfile = "slide-show-photo-2.jpg";

    // set name of image illustration 1
    private String testData_illustration_1 = "images.jfif";

    // set name of image illustration 2
    private String testData_illustration_2 = "9192363782_3d8544e582_b.jpg";

    // set name of image profile test update
    private String testData_Update_imageProfile = "Chonky-Animals-2-Quiz.jpg";

    private MockMultipartFile setImage(String dir_name_image, String name_of_image) {

        // variable MockMultipartFile
        MockMultipartFile mockMultipartFile;

        try {
            // get file
            File file = new File(dir_name_image + "/" + name_of_image);

            // create mock
            mockMultipartFile = new MockMultipartFile(
                    testData_imageProfile,
                    testData_imageProfile,
                    "image/png",
                    new FileInputStream(file)
            );
        } catch (IOException e) {
            e.printStackTrace();
            mockMultipartFile = null;
        }

        return mockMultipartFile;

    }

    private final String name = "AOI-for-unique-name";

    private final String animalCategory = "cat";

    private final int quantity = 11;

    private final MultipartFile imageProfile = setImage(dir_name_test, testData_imageProfile);

    private final MultipartFile imageProfile_for_update = setImage(dir_name_test, testData_Update_imageProfile);

    private final List<MultipartFile> illustrationFiles = List.of(
            setImage(dir_name_test, testData_illustration_1),
            setImage(dir_name_test, testData_illustration_2)
    );

    private final String path = "execute on unit test.";

    private final Principal principal = new Principal() {
        @Override
        public String getName() {
            return "964c0538-6163-4ea2-b491-b2b46e07e55c";
        }
    };

}
