package org.silo.community_management.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.cloudinary.Url;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.silo.community_management.dtos.exceptions.CloudinaryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = "spring.config.additional-location=classpath:secret.properties")
public class CloudinaryServiceTest {
    @MockitoBean
    private Cloudinary cloudinary;

    @MockitoBean
    private Uploader uploader;

    @MockitoBean
    private Url url;

    @MockitoBean
    private Call mockCall;

    @MockitoBean
    private OkHttpClient httpClient;

    @MockitoBean
    private Response mockResponse;

    @MockitoBean
    private ResponseBody responseBody;

    @Autowired
    private CloudinaryService cloudinaryService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(cloudinary.uploader()).thenReturn(uploader);
        when(cloudinary.url()).thenReturn(url);

        cloudinaryService = new CloudinaryService(cloudinary, httpClient);
    }

    @Test
    void testUploadImage() throws IOException {
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.getBytes()).thenReturn(new byte[0]);

        Map<String, Object> response = Map.of("url", "http://example.com/image.jpg");
        when(uploader.upload(any(byte[].class), any(Map.class))).thenReturn(response);

        Map<String, Object> result = cloudinaryService.uploadImage(mockFile);

        assertNotNull(result);
        assertEquals("http://example.com/image.jpg", result.get("url"));
    }

    @Test
    void testGetImageUrl() {
        String publicId = "sample_id";
        when(url.generate(publicId)).thenReturn("http://example.com/sample_id");

        String resultUrl = cloudinaryService.getImageUrl(publicId);

        assertEquals("http://example.com/sample_id", resultUrl);
    }

    @Test
    void testFetchImage() throws IOException {
        String imageUrl = "https://example.com/sample-image.jpg";
        byte[] fakeImageData = "fake image data".getBytes();

        // Mock the OkHttp Call
        when(httpClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);

        // Mock the Response
        when(mockResponse.isSuccessful()).thenReturn(true);
        when(mockResponse.body()).thenReturn(responseBody);
        when(responseBody.bytes()).thenReturn(fakeImageData);

        // Call the service method
        byte[] result = cloudinaryService.fetchImage(imageUrl);

        assertNotNull(result);
        assertArrayEquals(fakeImageData, result);

        verify(httpClient, times(1)).newCall(any(Request.class));
    }

    @Test
    void testFetchImageFailure() throws IOException {
        String imageUrl = "https://example.com/sample-image.jpg";

        // Mock the OkHttp Call
        when(httpClient.newCall(any(Request.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenReturn(mockResponse);

        // Mock the Response
        when(mockResponse.isSuccessful()).thenReturn(false);
        when(mockResponse.code()).thenReturn(404);

        try {
            cloudinaryService.fetchImage(imageUrl);
            fail("Expected CloudinaryException to be thrown");
        } catch (CloudinaryException e) {
            assertEquals("Failed to fetch image: 404", e.getMessage());
        }

        verify(httpClient, times(1)).newCall(any(Request.class));
    }

    @Test
    void testDeleteFile() throws IOException {
        // Arrange
        String publicId = "sample_id";

        // Mock the Cloudinary response for deleting a file
        when(uploader.destroy(eq(publicId), any(Map.class))).thenReturn(Map.of("result", "ok"));

        // Act and Assert
        assertDoesNotThrow(() -> cloudinaryService.deleteFile(publicId));

        // Verify the `destroy` method was called once
        verify(uploader, times(1)).destroy(eq(publicId), any(Map.class));
    }

    @Test
    void testDeleteFileFailure() throws IOException {
        // Arrange
        String publicId = "sample_id";

        // Mock a failed Cloudinary response for deleting a file
        when(uploader.destroy(eq(publicId), any(Map.class))).thenReturn(Map.of("result", "error"));

        // Act and Assert
        CloudinaryException exception = assertThrows(CloudinaryException.class, () -> cloudinaryService.deleteFile(publicId));

        // Verify exception message
        assertEquals("Failed to delete file with public ID: sample_id", exception.getMessage());

        // Verify the `destroy` method was called once
        verify(uploader, times(1)).destroy(eq(publicId), any(Map.class));
    }

}
