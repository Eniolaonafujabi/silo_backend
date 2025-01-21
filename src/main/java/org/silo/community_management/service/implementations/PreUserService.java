package org.silo.community_management.service.implementations;

import org.silo.community_management.data.model.PreUser;
import org.silo.community_management.data.repo.PreUserRepo;
import org.silo.community_management.data.repo.UserRepo;
import org.silo.community_management.dtos.request.EmailRequest;
import org.silo.community_management.dtos.exceptions.PreUserException;
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
        if (userRepo.findByEmail(email).isPresent())throw new PreUserException("User with this email already exists");
        if (preUserRepo.findPreUserByEmail(email).isPresent()){
            PreUser preUser = preUserRepo.findPreUserByEmail(email).orElseThrow(()-> new PreUserException("Pre User Not Found"));
            String otp = generateOtp();
            preUser.setOtp(otp);
            preUser.setOtpExpiration(LocalDateTime.now().plusMinutes(45));
            String result = sendOtpEmail(preUser.getEmail(), otp);
            preUserRepo.save(preUser);
            return result;
        }else {
            PreUser preUser = new PreUser();
            preUser.setEmail(email);
            String otp = generateOtp();
            preUser.setOtp(otp);
            preUser.setOtpExpiration(LocalDateTime.now().plusMinutes(45));
            String result = sendOtpEmail(preUser.getEmail(), otp);
            preUserRepo.save(preUser);
            return result;}
    }

    public boolean verifyOtp(String email, String otp) {
        PreUser user = preUserRepo.findPreUserByEmail(email).orElseThrow(() -> new PreUserException("User not found"));
        if (user.getOtp().equals(otp)) {
            if (user.getOtpExpiration().isAfter(LocalDateTime.now())){
                user.setEmailVerified(true);
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

    public boolean checkIfEmailIsVerified(String email){
        PreUser user = preUserRepo.findPreUserByEmail(email).orElseThrow(() -> new PreUserException("User not Verified"));
        return user.isEmailVerified();
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Always generates a 6-digit number
        return String.valueOf(otp);
    }

    private String sendOtpEmail(String email, String otp) throws IOException {
        EmailRequest message  = new EmailRequest();
        message.setToEmail(email);
        message.setSubject("Your OTP Code");
        message.setBody("Your OTP code expires in 45 minutes.\nYour OTP is: " + otp);
        mailServices.sendEmail(message);
        return otp;
    }
}
