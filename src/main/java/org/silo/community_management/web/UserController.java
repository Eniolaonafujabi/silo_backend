package org.silo.community_management.web;

import lombok.extern.apachecommons.CommonsLog;
import org.silo.community_management.dtos.request.*;
import org.silo.community_management.dtos.response.*;
import org.silo.community_management.service.implementations.UserServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CommonsLog
@RestController("v1/")
public class UserController {

    private final UserServices userServices;

    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @PostMapping("sendOtp")
    public ResponseEntity<?> sendOtp(@RequestBody SendEmailRequest request){
        try {
            String message = userServices.sendOtp(request.getEmail());
            StringResponse response = new StringResponse();
            response.setMessage(message);
            return new ResponseEntity<>(new ApiResponse(response,true), HttpStatus.OK);
        } catch (IOException e) {
            log.error("Error occurred while sending OTP: ", e);
            return new ResponseEntity<>(new ApiResponse(e.getMessage(),false), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("verifyOtp")
    public ResponseEntity<?> verifyOtp(@RequestBody verifyOtp request){
        try {
            userServices.verifyOtp(request.getEmail(), request.getOtp());
            StringResponse response = new StringResponse();
            response.setMessage("successfully");
            return new ResponseEntity<>(new ApiResponse(response,true), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while sending OTP: ", e);
            return new ResponseEntity<>(new ApiResponse(e.getMessage(),false), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "createUser")
    public ResponseEntity<?> createAccount(@RequestBody CreateAccountRequest request) {
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
    public ResponseEntity<?> createCommunity(@RequestBody CreateCommunityRequest request) {
        CreateCommunityResponse response = new CreateCommunityResponse();
        try {
            response = userServices.createCommunity(request);
            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.CREATED);

        }
        catch (Exception exception) {
            log.error("Error occurred while sending OTP: ", exception);
            return new ResponseEntity<>(new ApiResponse(exception.getMessage(), false), HttpStatus.BAD_REQUEST);

        }
    }

    @PostMapping(value = "addPost", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> addPost(@ModelAttribute AddPostRequest request) {
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

    @GetMapping("viewCommunity")
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
