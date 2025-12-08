package com.project.vistaro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.cloudinary.Cloudinary;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dpi6axcyb");
        config.put("api_key", "913854668167578");
        config.put("api_secret", "Nij-0WJWTRL8Oppk9Akw7ap8Nkg");

        return new Cloudinary(config);
    }
}