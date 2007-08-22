package com.dneero.email;

import org.apache.log4j.Logger;
import org.apache.commons.mail.HtmlEmail;

import javax.mail.Address;
import javax.mail.Header;
import javax.mail.BodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.activation.DataHandler;
import java.util.Enumeration;
import java.io.Serializable;

import com.dneero.systemprops.SystemProperty;
import com.dneero.threadpool.ThreadPool;

/**
 * Sends automates email subscription emails
 */
public class EmailSendThread implements Runnable, Serializable {

    public HtmlEmail htmlEmail;
    public static String DEFAULTFROM = "info@dneero.com";
    private static ThreadPool tp;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public EmailSendThread() {

    }

    public void run() {
        try{
            logger.debug("Start sending htmlEmail subject:"+htmlEmail.getSubject());
            logger.debug("SystemProperty.PROP_SMTPOUTBOUNDSERVER="+SystemProperty.getProp(SystemProperty.PROP_SMTPOUTBOUNDSERVER));
            if (htmlEmail.getMimeMessage().getAllRecipients()!=null && htmlEmail.getMimeMessage().getAllRecipients().length>0){
                htmlEmail.setHostName(SystemProperty.getProp(SystemProperty.PROP_SMTPOUTBOUNDSERVER));
                htmlEmail.send();
            }
            logger.debug("End sending htmlEmail subject:"+htmlEmail.getSubject());
        } catch (Exception e){
            logger.error(e);
        } finally {
            try{
                if (htmlEmail.getMimeMessage().getAllRecipients()!=null && htmlEmail.getMimeMessage().getAllRecipients().length>0){
                    logEmailSend(htmlEmail);
                }
            } catch (Exception ex){logger.error(ex);}
        }
    }

    public void startThread(){
        if (tp==null){
            tp = new ThreadPool(15);
        }
        tp.assign(this);
    }

    private void logEmailSend(HtmlEmail htmlEmail){
        try{
            MimeMessage message = htmlEmail.getMimeMessage();
            Address[] recipients = message.getAllRecipients();
            for (int i = 0; i < recipients.length; i++) {
                Address recipient = recipients[i];
                logger.debug("   EMAIL: To: "+recipient.toString());
            }
            Address[] from = message.getFrom();
            for (int i = 0; i < from.length; i++) {
                Address fromaddr = from[i];
                logger.debug("   EMAIL: From: "+fromaddr.toString());
            }
            Address[] replyto = message.getReplyTo();
            for (int i = 0; i < replyto.length; i++) {
                Address replytoaddr = replyto[i];
                logger.debug("   EMAIL: ReplyTo: "+replytoaddr.toString());
            }
            Enumeration enumer = message.getAllHeaders();
            while (enumer.hasMoreElements()) {
                Header header = (Header)enumer.nextElement();
                logger.debug("   EMAIL: Header: "+header.getName()+"="+header.getValue());
            }
            logger.debug("   EMAIL: Sender: "+String.valueOf(message.getSender()));
            logger.debug("   EMAIL: Subject: "+String.valueOf(message.getSubject()));
            logger.debug("   EMAIL: Encoding: "+String.valueOf(message.getEncoding()));
            if (message.getContent() instanceof MimeMultipart){
                MimeMultipart mimemessage = (MimeMultipart)message.getContent();
                for(int i=0; i<mimemessage.getCount(); i++){
                    BodyPart bodypart = mimemessage.getBodyPart(i);
                    logger.debug("   EMAIL: Content: "+String.valueOf(bodypart.getContent().toString()));
                }
            } else {
                logger.debug("   EMAIL: Content: "+String.valueOf(message.getContent().toString()));
            }
        } catch (Exception ex){
            logger.error(ex);
        }
    }




}
