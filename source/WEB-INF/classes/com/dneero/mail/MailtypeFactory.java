package com.dneero.mail;

/**
 * User: Joe Reger Jr
 * Date: Nov 29, 2008
 * Time: 5:09:38 PM
 */
public class MailtypeFactory {

    public static Mailtype get(int mailtypeid){
        if (mailtypeid==1){
            return new MailtypeSimple();
        } else {
            return new MailtypeSimple();
        }
    }

}
