package com.service.image.Service.Resource;

import org.springframework.core.io.Resource;

public interface Animal_Fetch_Model {

    Resource fetchImageProfile(String imageProfile);

    Resource fetchIllustration(String illustration);

}
