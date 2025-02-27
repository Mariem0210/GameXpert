package tn.esprit.interfaces;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailService {

    public static void sendEmail(String to, String subject, String body) {
        String from = "mariem.chouchene@esprit.tn";  // Remplacez par votre adresse email
        String password = "hjsv usse ptdd cokj";  // Remplacez par votre mot de passe ou un mot de passe d'application

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            try {
                Transport.send(message);
                System.out.println("Email envoyé avec succès !");
            } catch (MessagingException e) {
                e.printStackTrace();
                System.out.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
