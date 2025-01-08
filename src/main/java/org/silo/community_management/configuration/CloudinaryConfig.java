package org.silo.community_management.configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Value("${cloudinary.cloud_name}")
    private String cloudinaryName;

    @Value("${cloudinary.cloud_api_key}")
    private String cloudinaryApiKey;

    @Value("${cloudinary.cloud_api_secret}")
    private String cloudinaryApiSecretKey;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudinaryName,
                "api_key", cloudinaryApiKey,
                "api_secret", cloudinaryApiSecretKey
        ));
    }

}
