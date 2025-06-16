package service;

import model.Employee;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailNotification implements NotificationService {
    private final String fromEmail = "ab94604191@gmail.com"; // Votre adresse Gmail
    private final String password = "dysn medp fequ msqr"; // Mot de passe d'application

    @Override
    public void notifier(Employee destinataire, String message) {
        // Configurer les propriétés du serveur SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com"); // Ajouté pour éviter les problèmes SSL

        // Créer une session avec authentification
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // Créer un message
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmail));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(destinataire.getEmail()));
            msg.setSubject("Notification du système");
            msg.setText(message);

            // Envoyer le message
            Transport.send(msg);
            System.out.println("Email envoyé avec succès à " + destinataire.getEmail());
        } catch (MessagingException e) {
            System.err.println("Erreur lors de l'envoi de l'email à " + destinataire.getEmail());
            System.err.println("Détails de l'erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}