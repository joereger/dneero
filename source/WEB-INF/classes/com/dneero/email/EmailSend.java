package com.dneero.email;

import org.apache.log4j.Logger;

import javax.mail.Session;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import java.util.Properties;


/**
 * Send email class.
 */
public class EmailSend {

    public static String SMTPSERVER = "localhost";

    public static void sendMail(String from, String to, String subject, String message, boolean isHtmlEmail){
        Logger logger = Logger.getLogger(EmailSend.class);
        if (from.equals("")){
            from = "info@dneero.com";
        }
        logger.debug("EMAIL BEGIN SEND VIA THREAD: to"+to+" from:"+from+" subject:"+subject);
        logger.debug("Email Sent<br>To:" +to+"<br>From:" + from + "<br>Subject:" + subject + "<br>Body:" + message + "<br>isHtmlEmail:" + isHtmlEmail);
        try {
            //Kick off a thread to send the email
            EmailSendThread eThr = new EmailSendThread();
            eThr.setPriority(Thread.MIN_PRIORITY);
            eThr.to = to;
            eThr.from = from;
            eThr.subject = subject;
            eThr.message = message;
            eThr.isHtmlEmail = isHtmlEmail;
            eThr.start();
        }catch (Exception e) {
            logger.error("Error starting email thread.");
        }
        logger.debug("EMAIL END SEND VIA THREAD: to"+to+" from:"+from+" subject:"+subject);
    }

    public static void sendMail(String from, String to, String subject, String message){
        sendMail(from, to, subject, message, false);
    }

    public static void sendMailNoThread(String from, String to, String subject, String message, boolean isHtmlEmail) throws EmailSendException{
        Logger logger = Logger.getLogger(EmailSend.class);
        try{

            System.out.println("EMAIL BEGIN SEND: to"+to+" from:"+from+" subject:"+subject);

            Properties props =  System.getProperties();
            props.put("mail.smtp.host",  SMTPSERVER);
            props.put("mail.transport.protocol", "smtp");
            Session session = Session.getDefaultInstance(props, null);



            Message msg = new MimeMessage(session);

            InternetAddress addr = new InternetAddress(to);
            InternetAddress[] addrArray = new InternetAddress[] {addr};
            msg.addRecipients(Message.RecipientType.TO, new InternetAddress[] {addr});
            InternetAddress from_addr = new InternetAddress(from);
            msg.setFrom(from_addr);
            msg.setSubject(subject);

            if (isHtmlEmail){
                msg.setContent(message, "text/html");
            } else {
                msg.setContent(message, "text/plain");
            }

            msg.saveChanges();

            //Transport.send(msg);
            Transport transport = session.getTransport("smtp");
            transport.connect(SMTPSERVER, null, null);
            transport.sendMessage(msg, addrArray);



        } catch (javax.mail.SendFailedException nsend){
            logger.debug(nsend);
            EmailSendException ex = new EmailSendException("Email send failed: " + nsend.getMessage());
            throw ex;
        } catch (javax.mail.internet.AddressException ex){
            logger.debug(ex);
            EmailSendException esex = new EmailSendException("Email address invalid: " + ex.getMessage());
            throw esex;
        } catch (Exception e) {
            logger.error(e);
            EmailSendException esex = new EmailSendException("Email send failed: " + e.getMessage());
            throw esex;
        }

        logger.debug("EMAIL END SEND: to"+to+" from:"+from+" subject:"+subject);
    }




}
