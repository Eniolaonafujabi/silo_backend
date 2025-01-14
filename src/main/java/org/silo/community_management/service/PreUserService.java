package org.silo.community_management.service;

import org.silo.community_management.data.model.PreUser;
import org.silo.community_management.data.repo.PreUserRepo;
import org.silo.community_management.data.repo.UserRepo;
import org.silo.community_management.dtos.request.EmailRequest;
import org.silo.community_management.dtos.request.PreUserException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PreUserService {

    private final PreUserRepo preUserRepo;

    private final UserRepo userRepo;

    private final MailServices mailServices;

    public PreUserService(PreUserRepo preUserRepo, UserRepo userRepo, MailServices mailServices) {
        this.preUserRepo = preUserRepo;
        this.userRepo = userRepo;
        this.mailServices = mailServices;
    }

    public String preSignup(String email) throws IOException {
        if (userRepo.findByEmail(email).isPresent())throw new PreUserException(email + " already exists");
        if (preUserRepo.findByEmail(email).isPresent()){
            PreUser preUser = preUserRepo.findPreUserByEmail(email).orElseThrow(()-> new PreUserException("Pre User Not Found"));
            String otp = generateOtp();
            preUser.setOtp(otp);
            preUser.setOtpExpiration(LocalDateTime.now().plusMinutes(45));
            preUserRepo.save(preUser);
            return sendOtpEmail(preUser.getEmail(), otp);
        }else {
            PreUser preUser = new PreUser();
            preUser.setEmail(email);
            String otp = generateOtp();
            preUser.setOtp(otp);
            preUser.setOtpExpiration(LocalDateTime.now().plusMinutes(45));
            preUserRepo.save(preUser);
            return sendOtpEmail(preUser.getEmail(), otp);}
    }

    public boolean verifyOtp(String email, String otp) {
        PreUser user = preUserRepo.findPreUserByEmail(email).orElseThrow(() -> new PreUserException("User not found"));
        if(user.isVerified())return true;
        if (user.getOtp().equals(otp)) {
            if (user.getOtpExpiration().isAfter(LocalDateTime.now())){
                user.setVerified(true);
                preUserRepo.save(user);
                return true;
            }else {
                throw new PreUserException("OTP Has Expired");
            }
        }else {
            throw new PreUserException("OTP does not match");
        }
    }

    public void deletePreUser(String email){
        preUserRepo.deletePreUserByEmail(email);
    }

    public boolean checkIfAccountIsVerified(String email){
        PreUser user = preUserRepo.findPreUserByEmail(email).orElseThrow(() -> new PreUserException("User not Verified"));
        return user.isVerified();
    }

    private String generateOtp() {
        return String.valueOf(new Random().nextInt(999999)).substring(0, 6);
    }

    private String sendOtpEmail(String email, String otp) throws IOException {
        EmailRequest message  = new EmailRequest();
        message.setToEmail(email);
        message.setSubject("Your OTP Code");
        message.setBody("Your OTP code expires in 45 minutes.\nYour OTP is: " + otp);
        return mailServices.sendEmail(message);
    }
}
