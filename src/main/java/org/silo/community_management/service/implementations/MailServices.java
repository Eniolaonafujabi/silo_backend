package org.silo.community_management.service.implementations;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.silo.community_management.dtos.request.EmailRequest;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

@Service
public class MailServices {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    public String sendEmail(EmailRequest emailRequest) throws IOException {
        Email from = new Email("eniolaonafujabi@gmail.com");
        Email to = new Email(emailRequest.getToEmail());
        Content content = new Content("text/plain", emailRequest.getBody());
        Mail mail = new Mail(from, emailRequest.getSubject(), to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
        return "Email sent! Status Code: " + response.getStatusCode();

    }

}
