package com.dneero.mail;

import com.dneero.dao.Mail;
import com.dneero.dao.User;
import com.dneero.dao.Mailchild;
import com.dneero.dao.Pl;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.email.EmailTemplateProcessor;
import com.dneero.util.Str;

import java.util.List;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Nov 29, 2008
 * Time: 5:12:38 PM
 */
public class MailNotify {

    public static void notify(Mail mail){
        StringBuffer asHtml = new StringBuffer();
        StringBuffer asText = new StringBuffer();

        List<Mailchild> mailchildren = HibernateUtil.getSession().createQuery("from Mailchild where mailid='"+mail.getMailid()+"' order by mailchildid asc").list();
        for (Iterator<Mailchild> mailchildIterator=mailchildren.iterator(); mailchildIterator.hasNext();) {
            Mailchild mailchild=mailchildIterator.next();
            Mailtype mt = MailtypeFactory.get(mailchild.getMailtypeid());
            if (mailchild.getIsfromcustomercare()){
                asText.append("System Administrator Said:\n");
                asHtml.append("System Administrator Said:<br/>");
            } else {
                asText.append("You Said:\n");
                asHtml.append("You Said:<br/>");
            }
            asText.append(mt.renderToText(mailchild));
            asText.append("\n\n");
            asHtml.append(mt.renderToHtml(mailchild));
            asHtml.append("<br/><br/>");
        }
        Pl pl = Pl.get(User.get(mail.getUserid()).getPlid());
        String[] args = new String[3];
        args[0]=mail.getSubject();
        args[1]=asHtml.toString();
        args[2]=asText.toString();
        EmailTemplateProcessor.sendMail(pl.getNameforui()+" Message: "+ Str.truncateString(mail.getSubject(),100), "inboxmessage", User.get(mail.getUserid()), args);
    }

}
