package org.silo.community_management.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.silo.community_management.dtos.request.*;
import org.silo.community_management.dtos.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "spring.config.additional-location=classpath:secret.properties")
public class CommunityServiceTest {

    @Autowired
    private CommunityService communityService;

    @MockitoBean
    private MultipartFile mockFile;

    @BeforeEach
    public void setUp() throws IOException {
        mockFile = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                new FileInputStream("src/main/resources/WhatsAppProect1 Image 2024-12-27 at 23.37.27_cb03c0a3.jpg")
        );
    }

    @Test
    public void testThatICanCreateCommunity() throws IOException {

        CreateCommunityResponse response = getCreateCommunityResponse();
        assertNotNull(response.getId());
    }

    @Test
    public void testThatICanViewCommunity() throws IOException {

        CreateCommunityResponse response = getCreateCommunityResponse();
        assertEquals(response.getMessage(),"Created Successfully");

        ViewCommunityRequest request1 = new ViewCommunityRequest();
        request1.setCommunityId(response.getId());
        ViewCommunityResponse response1 = communityService.viewCommunity(request1);
        assertEquals(response1.getMessage(),"View Community");
    }

    @Test
    public void testThatICanEditCommunity() throws IOException {
        CreateCommunityResponse response = getCreateCommunityResponse();
        assertEquals(response.getMessage(),"Created Successfully");
        EditCommunityRequest request = new EditCommunityRequest();
        request.setCommunityId(response.getId());
        request.setCommunityName("New Name");
        request.setDescription("New Description");
        request.setImageVideo(mockFile);
        EditCommunityResponse response1 = communityService.editCommunity(request);
        assertEquals(response1.getMessage(),"Edited Successfully");
    }

    @Test
    public void testICanAddMemberToCommunity() throws IOException {
        CreateCommunityResponse response = getCreateCommunityResponse();
        assertEquals(response.getMessage(),"Created Successfully");
        AddMemberRequest request = new AddMemberRequest();
        request.setCommunityId(response.getId());
        request.setMemberId("1");
        AddMemberResponse response1 = communityService.addMemberToCommunity(request);
        assertEquals(response1.getMessage(),"Added Successfully");
    }

    @Test
    public void testICanAddAdminToCommunityWhenIsAMember() throws IOException {
        CreateCommunityResponse response = getCreateCommunityResponse();
        assertEquals(response.getMessage(),"Created Successfully");
        AddMemberRequest request = new AddMemberRequest();
        request.setCommunityId(response.getId());
        request.setMemberId("1");
        AddMemberResponse response1 = communityService.addMemberToCommunity(request);
        assertEquals(response1.getMessage(),"Added Successfully");
        AddMemberResponse response2 = communityService.addAdminToCommunity(request);
        assertEquals(response2.getMessage(),"Added Successfully");
    }

    @Test
    public void testICanAddAdminToCommunity() throws IOException {
        CreateCommunityResponse response = getCreateCommunityResponse();
        assertEquals(response.getMessage(),"Created Successfully");
        AddMemberRequest request = new AddMemberRequest();
        request.setCommunityId(response.getId());
        request.setMemberId("1");
        AddMemberResponse response1 = communityService.addAdminToCommunity(request);
        assertEquals(response1.getMessage(),"Added Successfully");
    }

//    @Test
//    public void testICanAddPostToCommunity() throws IOException {
//        CreateCommunityResponse response = getCreateCommunityResponse();
//        assertEquals(response.getMessage(),"Created Successfully");
//
//        AddMemberRequest request = new AddMemberRequest();
//        request.setCommunityId(response.getId());
//        request.setMemberId("1");
//        AddMemberResponse response1 = communityService.addMemberToCommunity(request);
//        assertEquals(response1.getMessage(),"Added Successfully");
//        AddPostRequest request1 = new AddPostRequest();
//        request1.setCommunityId(response.getId());
//        request1.setMemberId("1");
//        request1.setId("1");
//        AddPostResponse response2 = communityService.addPostToCommunity(request1);
//        assertEquals(response2.getMessage(),"Added Successfully");
//    }

    private CreateCommunityResponse getCreateCommunityResponse() throws IOException {
        CreateCommunityRequest request = new CreateCommunityRequest();
        request.setCommunityName("test");
        request.setDescription("description");
        request.setToken("1");
        request.setImageVideo(mockFile);
        return communityService.createCommunity(request);
    }

}