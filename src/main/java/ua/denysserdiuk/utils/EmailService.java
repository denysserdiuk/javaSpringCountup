package ua.denysserdiuk.utils;


import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Value("${sendgrid.api.key}")
    private String sendgridApiKey;

    public void sendVerificationEmail(String toEmail, int verificationCode) throws IOException {
        Email from = new Email("verify@countup.online");
        String subject = "Email Verification";
        Email to = new Email(toEmail);
        Content content = new Content("text/plain", "Your verification code is: " + verificationCode);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendgridApiKey);
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        sg.api(request);
    }

    public void sendContactEmail(String name, String email, String phone, String messageContent) throws IOException {
        Email from = new Email("verify@countup.online"); // Your verified sender email
        String subject = "New Contact Form Submission";
        Email to = new Email("verify@countup.online"); // Your email address to receive the message
        Content content = new Content("text/plain", buildEmailContent(name, email, phone, messageContent));
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendgridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            // Optional: Handle response
            System.out.println("SendGrid Response Status Code: " + ((Response) response).getStatusCode());
            System.out.println("SendGrid Response Body: " + response.getBody());
            System.out.println("SendGrid Response Headers: " + response.getHeaders());
        } catch (IOException ex) {
            // Log or rethrow the exception
            throw ex;
        }
    }

    private String buildEmailContent(String name, String email, String phone, String messageContent) {
        return "Name: " + name + "\n"
                + "Email: " + email + "\n"
                + "Phone: " + phone + "\n"
                + "Message:\n" + messageContent;
    }
}
