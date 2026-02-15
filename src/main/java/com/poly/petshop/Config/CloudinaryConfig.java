package com.poly.petshop.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {
            @Bean
        public Cloudinary cloudinary() {
            return new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", "dvgbr1wxb",
                    "api_key", "948283412531119",
                    "api_secret", "z87Q7Zlo4tXxTKI1uqIfO-AR9aY"
            ));
        }
    }