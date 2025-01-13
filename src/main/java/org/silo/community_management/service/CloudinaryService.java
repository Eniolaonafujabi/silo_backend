package org.silo.community_management.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.silo.community_management.dtos.exceptions.CloudinaryException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

@Service
public class CloudinaryService {

    private final Cloudinary cloudinary;

    private final OkHttpClient httpClient;

    public CloudinaryService(Cloudinary cloudinary, OkHttpClient httpClient) {
        this.cloudinary = cloudinary;
        this.httpClient = httpClient;
    }

    public Map<String, Object> uploadImage(MultipartFile file) throws IOException {
        return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
    }

    public String getImageUrl(String publicId) {
        return cloudinary.url().generate(publicId);
    }

    public byte[] fetchImage(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = httpClient.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                return response.body().bytes();
            } else {
                throw new CloudinaryException("Failed to fetch image: " + response.code());
            }
    }

    public Map<String, Object> editFile(String publicId, MultipartFile newFile) throws IOException {

        Map<String, Object> uploadResponse = uploadImage(newFile);

        deleteFile(publicId);

        return uploadResponse;
    }

    public void deleteFile(String publicId) throws IOException {
        Map<String, Object> deleteResponse = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        if (!"ok".equals(deleteResponse.get("result"))) {
            throw new CloudinaryException("Failed to delete file with public ID: " + publicId);
        }
    }
}
