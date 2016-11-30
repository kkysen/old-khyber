package sen.khyber.web.mail;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class Email {
    
    private final String host;
    private final Properties properties;
    private final Session session;
    
    private Address sender;
    private String subject;
    private String message;
    private Address[] recipients;
    private RecipientType emailType;
    
    private Email(final String host) {
        this.host = host;
        properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        session = Session.getDefaultInstance(properties);
    }
    
    private Email() {
        this("localhost");
    }
    
    public Email(final String sender, final String subject, final String message, final String... recipients) throws AddressException {
        this();
        this.sender = new InternetAddress(sender);
        this.subject = subject;
        this.message = message;
        this.recipients = new Address[recipients.length];
        for (int i = 0; i < recipients.length; i++) {
            this.recipients[i] = new InternetAddress(recipients[i]);
        }
        emailType = RecipientType.TO;
    }
    
    public void send() throws MessagingException {
        final MimeMessage email = new MimeMessage(session);
        email.setFrom(sender);
        email.addRecipients(emailType, recipients);
        email.setSubject(subject);
        email.setText(message);
        Transport.send(email);
    }
    
    public static void main(final String[] args) throws Exception {
        final Email email = new Email("kkysen@gmail.com", "Java Test", "testing", "ksen1@stuy.edu");
        email.send();
    }
    
}
