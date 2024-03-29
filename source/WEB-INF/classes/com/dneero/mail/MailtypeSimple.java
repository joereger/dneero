package com.dneero.mail;

import com.dneero.dao.Mail;
import com.dneero.dao.Mailchild;

/**
 * User: Joe Reger Jr
 * Date: Nov 29, 2008
 * Time: 5:10:33 PM
 */
public class MailtypeSimple implements Mailtype {

    public static int TYPEID = 1;

    public int getMailtypeid() {
        return TYPEID;
    }

    public String renderToHtml(Mailchild mailchild) {
        return mailchild.getVar1();
    }

    public String renderToText(Mailchild mailchild) {
        return mailchild.getVar1();
    }
}
