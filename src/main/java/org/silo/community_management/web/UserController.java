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
@CrossOrigin("*")
public class UserController {

    private final UserServices userServices;

    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @PostMapping("sendOtp")
    public ResponseEntity<?> sendOtp(@RequestBody SendEmailRequest request){
        try {
            log.info(request.getEmail());
            String message = userServices.sendOtp(request.getEmail());
            StringResponse response = new StringResponse();
            response.setOtp(message);
            response.setStatus(true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            log.error("Error occurred while sending OTP: ", e);
            return new ResponseEntity<>(new ApiResponse(e.getMessage(),false), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("verifyOtp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtp request){
        try {
            log.info(request.getEmail());
            log.info(request.getOtp());
            boolean result = userServices.verifyOtp(request.getEmail(), request.getOtp());
            StringResponse response = new StringResponse();
            response.setStatus(result);
            return new ResponseEntity<>(response , HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while verifying OTP: ", e);
            return new ResponseEntity<>(new ApiResponse(e.getMessage(),false), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "createUser")
    public ResponseEntity<?> createAccount(@RequestBody CreateAccountRequest request) {
        CreateAccountResponse response = new CreateAccountResponse();
        log.info(request.getEmail());
        log.info(request.getPassword());
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
        log.info(request.getEmail());
        log.info(request.getPassword());
        try {
            response = userServices.logInAccount(request);
            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.OK);
        }
        catch (Exception exception) {
            log.error("Error: ", exception);
            return new ResponseEntity<>(new ApiResponse(exception.getMessage(), false), HttpStatus.BAD_REQUEST);

        }
    }

    @PostMapping(value = "createCommunity")
    public ResponseEntity<?> createCommunity(@RequestBody CreateCommunityRequest request) {
        log.info(request.getToken());
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

    @PostMapping(value = "addPost")
    public ResponseEntity<?> addPost(@ModelAttribute AddPostRequest request) {
        AddPostResponse response = new AddPostResponse();
        log.info(request.getToken());
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
        log.info(request.getToken());
        log.info(request.getEmail());
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
        log.info(request.getToken());
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
        log.info(request.getToken());
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

    @PostMapping("createSubGroup")
    public ResponseEntity<?> createSubGroup(@RequestBody CreateSubGroupRequest request) {
        log.info(request.getToken());
        CreateSubGroupResponse response = new CreateSubGroupResponse();
        try {
            response = userServices.createSubGroup(request);
            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.CREATED);
        }
        catch (Exception exception) {
            log.error("Error occurred while sending OTP: ", exception);
            return new ResponseEntity<>(new ApiResponse(exception.getMessage(), false), HttpStatus.BAD_REQUEST);

        }
    }

    @PostMapping("addMemberToSubGroup")
    public ResponseEntity<?> addMemberToSubGroup(@RequestBody AddSubGroupMemberRequest request) {
        log.info(request.getToken());
        AddMemberResponse response = new AddMemberResponse();
        try {
            response = userServices.addMemberToSubGroup(request);
            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.CREATED);
        }
        catch (Exception exception) {
            log.error("Error occurred while sending OTP: ", exception);
            return new ResponseEntity<>(new ApiResponse(exception.getMessage(), false), HttpStatus.BAD_REQUEST);

        }
    }

    @PostMapping("addAdminToSubGroup")
    public ResponseEntity<?> addAdminToSubGroup(@RequestBody AddSubGroupMemberRequest request) {
        log.info(request.getToken());
        AddMemberResponse response = new AddMemberResponse();
        try {
            response = userServices.addAdminToSubGroup(request);
            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.CREATED);
        }
        catch (Exception exception) {
            log.error("Error occurred while sending OTP: ", exception);
            return new ResponseEntity<>(new ApiResponse(exception.getMessage(), false), HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping("getAllSubGroupInACommunity")
    public ResponseEntity<?> getAllSubGroupInACommunity(@RequestBody GetAllSubGroupRequest request) {
        log.info(request.getToken());
        GetAllSubGroupResponse response = new GetAllSubGroupResponse();
        try {
            response = userServices.getAllSubGroupInACommunity(request.getCommunityId(),request.getToken());
            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.CREATED);
        }
        catch (Exception exception) {
            log.error("Error occurred while sending OTP: ", exception);
            return new ResponseEntity<>(new ApiResponse(exception.getMessage(), false), HttpStatus.BAD_REQUEST);

        }
    }

    @GetMapping("getAllUserInfo")
    public ResponseEntity<?> getAllUserInfo(@RequestBody AllUserDataRequest request) {
        log.info(request.getToken());
        AllUserDataResponse response = new AllUserDataResponse();
        try {
            response = userServices.getAllUserInfo(request);
            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.CREATED);
        }
        catch (Exception exception) {
            log.error("Error occurred while sending OTP: ", exception);
            return new ResponseEntity<>(new ApiResponse(exception.getMessage(), false), HttpStatus.BAD_REQUEST);

        }
    }
}
