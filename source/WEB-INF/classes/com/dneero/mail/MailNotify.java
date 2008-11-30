package com.dneero.mail;

import com.dneero.dao.Mail;
import com.dneero.dao.User;
import com.dneero.dao.Mailchild;
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
                asText.append("dNeero Said:\n");
                asHtml.append("dNeero Said:<br/>");
            } else {
                asText.append("You Said:\n");
                asHtml.append("You Said:<br/>");
            }
            asText.append(mailchild.getVar1());
            asText.append("\n\n");
            asHtml.append(mt.renderToHtml(mailchild));
            asHtml.append("<br/><br/>");
        }

        String[] args = new String[3];
        args[0]=mail.getSubject();
        args[1]=asHtml.toString();
        args[2]=asText.toString();
        EmailTemplateProcessor.sendMail("dNeero Message: "+ Str.truncateString(mail.getSubject(),100), "supportissueresponse", User.get(mail.getUserid()), args);
    }

}
