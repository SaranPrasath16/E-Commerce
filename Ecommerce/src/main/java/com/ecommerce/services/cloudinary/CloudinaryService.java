package com.ecommerce.services.cloudinary;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ecommerce.exceptionhandler.EntityDeletionException;
import com.ecommerce.exceptionhandler.EntityPushException;

@Service
public class CloudinaryService {
    private final Cloudinary cloudinary;

    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }
    
    public String uploadImage(MultipartFile file) throws IOException {
        System.out.println("Cloudinary: " + cloudinary.getUserAgent());

        @SuppressWarnings("unchecked")
		Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        if(!uploadResult.isEmpty()){
            return uploadResult.get("url").toString();
        }
        throw new EntityPushException("Failed to upload images to db");
    }
    public void deleteImageFromCloudinary(String publicId) {
        try {

            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (Exception e) {
            throw new EntityDeletionException("Failed to delete image from db");
        }
    }

    public String extractPublicIdFromUrl(String url) {
        String[] urlParts = url.split("/v");
        return urlParts[1].split("/")[1].replace(".jpg", "");
    }

}
