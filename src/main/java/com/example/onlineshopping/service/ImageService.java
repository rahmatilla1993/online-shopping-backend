package com.example.onlineshopping.service;

import com.cloudinary.utils.ObjectUtils;
import com.example.onlineshopping.config.CloudinaryConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class ImageService {

    private final CloudinaryConfig cloudinaryConfig;

    @Autowired
    public ImageService(CloudinaryConfig cloudinaryConfig) {
        this.cloudinaryConfig = cloudinaryConfig;
    }
    public String uploadImage(MultipartFile multipartFile) throws IOException {

        String fileName = "product-images/%s".formatted(UUID.randomUUID().toString());
        return cloudinaryConfig
                .cloudinary()
                .uploader()
                .upload(multipartFile.getBytes(),
                        Map.of("public_id", fileName))
                .get("url")
                .toString();
    }
    public void deleteImage(String imageUrl) throws IOException {
        String imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.lastIndexOf("."));
        cloudinaryConfig
                .cloudinary()
                .uploader()
                .destroy(String.format("product-images/%s", imageName),
                        ObjectUtils.emptyMap());
    }
}
