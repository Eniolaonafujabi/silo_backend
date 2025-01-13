package org.silo.community_management.web;

import lombok.extern.apachecommons.CommonsLog;
import org.silo.community_management.dtos.request.*;
import org.silo.community_management.dtos.response.*;
import org.silo.community_management.service.UserServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CommonsLog
@RestController("v1/")
public class UserController {

    private final UserServices userServices;

    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @PostMapping("sendOtp")
    public ResponseEntity<?> sendOtp(@RequestParam("email") String email){
        try {
            String message = userServices.sendOtp(email);
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (IOException e) {
            log.error("Error occurred while sending OTP: ", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("verifyOtp")
    public ResponseEntity<?> verifyOtp(@RequestBody verifyOtp request){
        try {
            boolean message = userServices.verifyOtp(request.getEmail(), request.getOtp());
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while sending OTP: ", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "createUser", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createAccount(@RequestParam(value = "file",required = false) MultipartFile file,
                                        @RequestParam("name") String name,
                                        @RequestParam("bio") String bio,
                                        @RequestParam("email") String email,
                                        @RequestParam("phoneNumber") String phoneNumber,
                                        @RequestParam("password") String password) {
        CreateAccountRequest request = new CreateAccountRequest(name,password,email,phoneNumber,bio,file);
        CreateAccountResponse response = new CreateAccountResponse();
        try {
            response = userServices.createAccount(request);
            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.CREATED);

        }
        catch (Exception exception) {
            log.error("Error occurred while sending OTP: ", exception);
            return new ResponseEntity<>(new ApiResponse(exception.getMessage(), false), HttpStatus.BAD_REQUEST);

        }
    }

    @PostMapping("logIn")
    public ResponseEntity<?> logIn(@RequestBody LogInRequest request) {
        LogInResponse response = new LogInResponse();
        try {
            response = userServices.LogInAccount(request);
            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.CREATED);
        }
        catch (Exception exception) {
            log.error("Error occurred while sending OTP: ", exception);
            return new ResponseEntity<>(new ApiResponse(exception.getMessage(), false), HttpStatus.BAD_REQUEST);

        }
    }

    @PostMapping(value = "createCommunity", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createCommunity(@RequestParam("file") MultipartFile file,
                                           @RequestParam("communityName") String communityName,
                                           @RequestParam("communityDescription") String communityDescription,
                                           @RequestParam("founderId") String founderId) {
        CreateCommunityRequest request = new CreateCommunityRequest(file,communityName,communityDescription,founderId);
        CreateCommunityResponse response = new CreateCommunityResponse();
        try {
            response = userServices.createCommunity(request);
            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.CREATED);

        }
        catch (Exception exception) {
            return new ResponseEntity<>(new ApiResponse(exception.getMessage(), false), HttpStatus.BAD_REQUEST);

        }
    }

    @PostMapping(value = "addPost", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addPost(@RequestParam("file") MultipartFile file,
                                             @RequestParam("memberId") String memberId,
                                             @RequestParam("communityId") String communityId,
                                             @RequestParam("title") String title,
                                             @RequestParam("content") String content) {
        AddPostRequest request = new AddPostRequest(memberId,title,content,file,communityId);
        AddPostResponse response = new AddPostResponse();
        try {
            response = userServices.addPost(request);
            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.CREATED);

        }
        catch (Exception exception) {
            log.error("Error occurred while sending OTP: ", exception);
            return new ResponseEntity<>(new ApiResponse(exception.getMessage(), false), HttpStatus.BAD_REQUEST);

        }
    }

    @PostMapping("addMember")
    public ResponseEntity<?> addMember(@RequestBody AddMemberRequest request) {
        AddMemberResponse response = new AddMemberResponse();
        try {
            response = userServices.addMember(request);
            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.CREATED);
        }
        catch (Exception exception) {
            log.error("Error occurred while sending OTP: ", exception);
            return new ResponseEntity<>(new ApiResponse(exception.getMessage(), false), HttpStatus.BAD_REQUEST);

        }
    }

    @PostMapping("addAdmin")
    public ResponseEntity<?> addAdmin(@RequestBody AddMemberRequest request) {
        AddMemberResponse response = new AddMemberResponse();
        try {
            response = userServices.addAdmin(request);
            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.CREATED);
        }
        catch (Exception exception) {
            log.error("Error occurred while sending OTP: ", exception);
            return new ResponseEntity<>(new ApiResponse(exception.getMessage(), false), HttpStatus.BAD_REQUEST);

        }
    }

    @PostMapping("viewCommunity")
    public ResponseEntity<?> viewCommunity(@RequestBody ViewCommunityRequest request) {
        ViewCommunityResponse response = new ViewCommunityResponse();
        try {
            response = userServices.viewCommunity(request);
            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.CREATED);
        }
        catch (Exception exception) {
            log.error("Error occurred while sending OTP: ", exception);
            return new ResponseEntity<>(new ApiResponse(exception.getMessage(), false), HttpStatus.BAD_REQUEST);

        }
    }
}
