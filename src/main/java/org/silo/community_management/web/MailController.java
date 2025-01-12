package org.silo.community_management.web;

import org.silo.community_management.MailServices;
import org.silo.community_management.dtos.request.EmailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class MailController {
        @Autowired
        private MailServices emailService;

        @PostMapping("/send")
        public String sendEmail(@RequestBody EmailRequest emailRequest) {
            try {
                return emailService.sendEmail(emailRequest);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
}
