package com.dneero.mail;

import com.dneero.dao.Mail;
import com.dneero.dao.Mailchild;

/**
 * User: Joe Reger Jr
 * Date: Nov 29, 2008
 * Time: 5:10:33 PM
 */
public class MailtypeReviewableWarning implements Mailtype {

    public static int TYPEID = 3;

    public int getMailtypeid() {
        return TYPEID;
    }

    public String renderToHtml(Mailchild mailchild) {
        StringBuffer out = new StringBuffer();
        out.append("<font style=\"font-size: 14px; color: #cccccc;\">You've had a content warning.</font>\n" +
                "<br/><br/>\n" +
                "<font style=\"font-size: 10px; color: #000000; font-weight: bold;\">The content warned:</font>\n" +
                "<br/>\n" +
                "<font style=\"font-size: 12px; color: #000000;\">"+mailchild.getVar1()+"</font>\n" +
                "<br/><br/>\n" +
                "<font style=\"font-size: 9px; color: #000000; font-weight: bold;\">The Conversation Igniter said:</font>\n" +
                "<br/>\n" +
                "<font style=\"font-size: 8px; color: #000000;\">"+mailchild.getVar2()+"</font>\n" +
                "<br/><br/>\n" +
                "<font style=\"font-size: 9px; color: #000000; font-weight: bold;\">The System Administrator said:</font>\n" +
                "<br/>\n" +
                "<font style=\"font-size: 8px; color: #000000;\">"+mailchild.getVar3()+"</font>\n" +
                "<br/><br/>\n" +
                "<font style=\"font-size: 14px; color: #cccccc; font-weight: bold;\">What now?</font>\n" +
                "<br/>\n" +
                "<font style=\"font-size: 9px; color: #000000;\">There's nothing for you to do.  This is just a warning.  You'll continue to earn money on the warned content.  But take the warning seriously and improve the quality of your content.</font>\n" +
                "<br/><br/>");
        return out.toString();
    }

    public String renderToText(Mailchild mailchild) {
        StringBuffer out = new StringBuffer();
        out.append("You've had a content warning.\n" +
                "==========\n" +
                "The content warned:\n" +
                ""+mailchild.getVar1()+"\n" +
                "\n" +
                "The Conversation Igniter said:\n" +
                ""+mailchild.getVar2()+"\n" +
                "\n" +
                "The System Administrator said:\n" +
                ""+mailchild.getVar3()+"\n" +
                "\n" +
                "What now?\n" +
                "===========\n" +
                "There's nothing for you to do.  This is just a warning.  You'll continue to earn money on the warned content.  But take the warning seriously and improve the quality of your content.");
        return out.toString();
    }
}