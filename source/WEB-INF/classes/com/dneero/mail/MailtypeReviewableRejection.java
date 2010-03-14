package com.dneero.mail;

import com.dneero.dao.Mailchild;

/**
 * User: Joe Reger Jr
 * Date: Nov 29, 2008
 * Time: 5:10:33 PM
 */
public class MailtypeReviewableRejection implements Mailtype {

    public static int TYPEID = 2;

    public int getMailtypeid() {
        return TYPEID;
    }

    public String renderToHtml(Mailchild mailchild) {
        StringBuffer out = new StringBuffer();
        out.append("<font style=\"font-size: 14px; color: #cccccc;\">You've had content rejected.</font>\n" +
                "<br/><br/>\n" +
                "<font style=\"font-size: 10px; color: #000000; font-weight: bold;\">The content rejected:</font>\n" +
                "<br/>\n" +
                "<font style=\"font-size: 12px; color: #000000;\">"+mailchild.getVar1()+"</font>\n" +
                "<br/><br/>\n" +
                "<font style=\"font-size: 9px; color: #000000; font-weight: bold;\">The Conversation Creator said:</font>\n" +
                "<br/>\n" +
                "<font style=\"font-size: 8px; color: #000000;\">"+mailchild.getVar2()+"</font>\n" +
                "<br/><br/>\n" +
                "<font style=\"font-size: 9px; color: #000000; font-weight: bold;\">The System Administrator said:</font>\n" +
                "<br/>\n" +
                "<font style=\"font-size: 8px; color: #000000;\">"+mailchild.getVar3()+"</font>\n" +
                "<br/><br/>\n" +
                "<font style=\"font-size: 14px; color: #cccccc; font-weight: bold;\">What now?</font>\n" +
                "<br/>\n" +
                "<font style=\"font-size: 9px; color: #000000;\">Log on to your account.  You'll see a link to Flagged Items.  Read the details of the flagging, including how to remedy the situation.  While items are flagged they can't earn money so you need to get them fixed as quickly as possible.  Once you edit the item it'll go up for review again.</font>\n" +
                "<br/><br/>");
        return out.toString();
    }

    public String renderToText(Mailchild mailchild) {
        StringBuffer out = new StringBuffer();
        out.append("You've had content rejected.\n" +
                "==========\n" +
                "The content rejected:\n" +
                ""+mailchild.getVar1()+"\n" +
                "\n" +
                "The Conversation Creator said:\n" +
                ""+mailchild.getVar2()+"\n" +
                "\n" +
                "The System Administrator said:\n" +
                ""+mailchild.getVar3()+"\n" +
                "\n" +
                "What now?\n" +
                "===========\n" +
                "Log on to your account.  You'll see a link to Flagged Items.  Read the details of the flagging, including how to remedy the situation.  While items are flagged they can't earn money so you need to get them fixed as quickly as possible.  Once you edit the item it'll go up for review again.");
        return out.toString();
    }
}