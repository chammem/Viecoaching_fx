package entities;

import services.ServiceReset;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class Sendmail extends ServiceReset {

	final String username = "sinda.user@outlook.fr"; // Your Outlook username
	final String password = "sindauser2024"; // Your Outlook password

	public void envoyer(String Toemail, String Subject, String Object) {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS
		props.put("mail.smtp.host", "smtp.office365.com");
		props.put("mail.smtp.port", "587"); // Correct port for STARTTLS

		Session session = Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(username));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(Toemail, false));
			msg.setSubject(Subject);
			msg.setText(Object);
			msg.setSentDate(new Date());
			Transport.send(msg);
			System.out.println("Message sent.");
		} catch (MessagingException e) {
			System.out.println("Erreur d'envoi, cause: " + e);
		}
	}
}
