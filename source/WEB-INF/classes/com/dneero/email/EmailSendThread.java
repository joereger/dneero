package com.dneero.email;

import org.apache.log4j.Logger;
import org.apache.commons.mail.HtmlEmail;

import javax.mail.Session;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import java.util.Properties;

/**
 * Sends automates email subscription emails
 */
public class EmailSendThread extends Thread {

    public HtmlEmail htmlEmail;

    //@todo configurable outbound smtp host
    public static String SMTPSERVER = "localhost";
    public static String DEFAULTFROM = "info@dneero.com";

    Logger logger = Logger.getLogger(this.getClass().getName());

    public EmailSendThread() {

    }

    public void init() {
        this.setPriority(Thread.MIN_PRIORITY);
        run();
    }

	public void run() {
        try{
            logger.debug("Start sending htmlEmail subject:"+htmlEmail.getSubject());
            htmlEmail.setHostName(SMTPSERVER);
            htmlEmail.send();
            logger.debug("End sending htmlEmail subject:"+htmlEmail.getSubject());
        } catch (Exception e){
            logger.error(e);
        }
    }




}
