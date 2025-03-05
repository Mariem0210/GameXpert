package tn.esprit.interfaces;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailService {
    public static void sendEmail(String toEmail, String subject, String body) {
        String fromEmail = "mariem.chouchene@esprit.tn"; // Remplace par ton adresse e-mail Gmail
        String password = "hjsv usse ptdd cokj"; // Remplace par ton mot de passe ou mot de passe d'application

        // Paramètres du serveur SMTP de Gmail
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587"); // Port pour STARTTLS
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); // Assure que STARTTLS est activé

        // Authentification
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // Création du message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(body);

            // Envoi de l'email
            Transport.send(message);
            System.out.println("Email envoyé avec succès à " + toEmail);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}

