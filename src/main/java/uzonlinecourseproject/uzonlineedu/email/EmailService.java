package uzonlinecourseproject.uzonlineedu.email;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    @Value("${app.base-url:http://localhost:8080}")
    private String BASE_URL;

    public void sendEmail(String to, String subject, String text) throws MessagingException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);
            helper.setFrom("ilhomjonovo200@mail.ru");
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new MessagingException("Emailni yuborishda xatolik yuz berdi: " + e.getMessage(), e);
        }
    }

    private String createEmailTemplate(String title, String buttonText, String link, String fallbackText) {
        return "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 8px;'>"
                + "<h2 style='color: #333; text-align: center;'>" + title + "</h2>"
                + "<p style='color: #555;'>" + fallbackText + "</p>"
                + "<div style='text-align: center; margin: 20px 0;'>"
                + "<a href='" + link + "' style='background-color: #4CAF50; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px; font-weight: bold;'>" + buttonText + "</a>"
                + "</div>"
                + "<p style='color: #555;'>Agar tugma ishlamasa, quyidagi linkni nusxalab brauzeringizga joylashtiring:</p>"
                + "<p><a href='" + link + "'>" + link + "</a></p>"
                + "</div>";
    }

    public void sendPasswordResetEmail(String to, String token) throws MessagingException {
        String subject = "Parolni tiklash";
        String link = String.format("%s/api/users/reset-password?token=%s", BASE_URL, token);
        String text = createEmailTemplate("Parolni Tiklash", "Parolni Tiklash", link, "Yangi parol o‘rnatish uchun quyidagi tugmani bosing:");
        sendEmail(to, subject, text);
    }

    public void sendVerificationEmail(String to, String token) throws MessagingException {
        String subject = "Email tasdiqlash";
        String link = String.format("%s/api/users/verify-email?token=%s", BASE_URL, token);
        String text = createEmailTemplate("Hisobingizni tasdiqlang!", "Hisobni Faollashtirish", link, "Iltimos, hisobingizni faollashtirish uchun quyidagi tugmani bosing:");
        sendEmail(to, subject, text);
    }
}