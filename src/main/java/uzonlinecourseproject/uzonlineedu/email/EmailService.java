package uzonlinecourseproject.uzonlineedu.email;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final String BASE_URL = "ttp://localhost:8080/swagger-ui/index.html#";

    public void sendEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

       helper.setTo(to);
       helper.setSubject(subject);
       helper.setText(text, true); // HTML text
       helper.setFrom("ilhomjonovo200@mail.ru");

        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String to, String token) throws MessagingException {
        String subject = "Password reset";
        String link = BASE_URL + "/api/user/reset-password?token=" + token;
        String text = "<h1>Password Reset </h1>" +
                "<p>Password verification  for link butuum:</p>" +
                "<a href=\"" + link + "\">Password Reset</a>" +
                "<p>this link  duration one a hour 🙂 .</p>";

        sendEmail(to, subject, text);
    }

    public void sendVerificationEmail(String to, String token) throws MessagingException {
        String subject = "Email Verification";
        String link = BASE_URL + "/api/user/verify-email?token=" + token;
        String text = "<h1>Email Verification </h1>" +
                "<p>Email verification  for link butuum :</p>" +
                "<a href=\"" + link + "\">Email Verification</a>" +
                "<p>this link  duration one a hour 🙂 .</p>";

        sendEmail(to, subject, text);
    }
}