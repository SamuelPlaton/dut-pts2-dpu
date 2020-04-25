/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

 

/**
 *
 * @author pti-c
 */

/*la classe SendMail s'utilise ainsi :
    1 -> Création de l'objet avec l'adresse cible en parametre du contructeur;
    2 -> utilisation de la methode send(objectDuMail, message)
*/

public class SendMail {
    
    private String username = "projetpts2@gmail.com";
    private String password = "clement2000";
    private String mailCible;
    
    public SendMail(String mailCible){
        this.mailCible = mailCible;
    }

    SendMail() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void sendMail(String newPasswd){
        // Etape 1 : Création de la session
        
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.host","smtp.gmail.com");
        props.put("mail.smtp.port","587");
        
        Session session;
        session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        
        // Etape 2 : Création de l'objet Message
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
            InternetAddress.parse(mailCible));
            message.setSubject("DPU - Nouveau mot de passe");
            message.setText("Bonjour, suite à votre de demande de réinitialisation de mot de passe, voici un mot de passe temporaire : " + newPasswd + "\n Pour définir votre nouveau mot de passe, veuillez vous connecter à l'application et cliquer sur le bouton \"Redéfinir le mot de passe\".");
            // Etape 3 : Envoyer le message
            Transport.send(message);
            System.out.println("Message_envoye");
        } catch (MessagingException e) {
        throw new RuntimeException(e);
        } }
       
    }