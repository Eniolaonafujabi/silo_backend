package org.silo.community_management.service;

import org.silo.community_management.MailServices;
import org.silo.community_management.data.model.PreUser;
import org.silo.community_management.data.repo.PreUserRepo;
import org.silo.community_management.dtos.request.EmailRequest;
import org.silo.community_management.dtos.request.PreUserException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class PreUserService {

    private final PreUserRepo preUserRepo;

    private final MailServices mailServices;

    public PreUserService(PreUserRepo preUserRepo, MailServices mailServices) {
        this.preUserRepo = preUserRepo;
        this.mailServices = mailServices;
    }

    public String preSignup(String email) throws IOException {
        if (preUserRepo.findByEmail(email) != null){
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
