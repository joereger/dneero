package com.dneero.mail;

/**
 * User: Joe Reger Jr
 * Date: Nov 29, 2008
 * Time: 5:09:38 PM
 */
public class MailtypeFactory {

    public static Mailtype get(int mailtypeid){
        if (mailtypeid==MailtypeSimple.TYPEID){
            return new MailtypeSimple();
        } else if (mailtypeid==MailtypeReviewableRejection.TYPEID){
            return new MailtypeReviewableRejection();
        } else if (mailtypeid==MailtypeReviewableWarning.TYPEID){
            return new MailtypeReviewableWarning();
        } else {
            return new MailtypeSimple();
        }
    }

}
