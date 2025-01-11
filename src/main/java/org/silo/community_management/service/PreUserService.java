package org.silo.community_management.service;

import org.silo.community_management.MailServices;
import org.silo.community_management.data.model.PreUser;
import org.silo.community_management.data.repo.PreUserRepo;
import org.silo.community_management.dtos.request.EmailRequest;
import org.springframework.stereotype.Service;

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

    public void preSignup(String email) {
        PreUser preUser = new PreUser();
        preUser.setEmail(email);
        String otp = generateOtp();
        preUser.setOtp(otp);
        preUser.setOtpExpiration(LocalDateTime.now().plusMinutes(5));
        preUserRepo.save(preUser);
        sendOtpEmail(preUser.getEmail(), otp);
    }

    public boolean verifyOtp(String email, String otp) {
        PreUser user = preUserRepo.findPreUserByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getOtp().equals(otp) && user.getOtpExpiration().isAfter(LocalDateTime.now())) {
            user.setVerified(true);
            preUserRepo.save(user);
            return true;
        }
        return false;
    }

    private String generateOtp() {
        return String.valueOf(new Random().nextInt(999999)).substring(0, 6);
    }

    private void sendOtpEmail(String email, String otp) {
        EmailRequest message  = new EmailRequest();
        message.setToEmail(email);
        message.setSubject("Your OTP Code");
        message.setBody("Your OTP is: " + otp);
        mailServices.sendEmail(message);
    }
}
