package com.dneero.htmluibeans;

import org.apache.log4j.Logger;



import com.dneero.dao.Mail;
import com.dneero.dao.Mailchild;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;

import com.dneero.util.GeneralException;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.mail.MailtypeSimple;

import java.util.*;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class AccountInboxDetail implements Serializable {

    private int mailid;
    private String notes;
    private ArrayList<Mailchild> inboxdetails;
    private Mail mail;

    public AccountInboxDetail(){

    }

    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("beginView called: mailid="+ mailid);
        String tmpMailid = Pagez.getRequest().getParameter("mailid");
        if (com.dneero.util.Num.isinteger(tmpMailid)){
            logger.debug("beginView called: found mailid in param="+tmpMailid);
            Mail mail = Mail.get(Integer.parseInt(tmpMailid));
            if (Pagez.getUserSession().getUser()!=null && mail.canEdit(Pagez.getUserSession().getUser())){
                this.mail= mail;
                this.mailid= mail.getMailid();
                inboxdetails= new ArrayList<Mailchild>();
                List<Mailchild> inboxchildren = HibernateUtil.getSession().createQuery("from Mailchild where mailid='"+mailid+"' order by mailchildid asc").list();
                for (Iterator<Mailchild> mailchildIterator=inboxchildren.iterator(); mailchildIterator.hasNext();) {
                    Mailchild mailchild=mailchildIterator.next();
                    inboxdetails.add(mailchild);
                }
                //Mark as new again
                this.mail.setIsread(true);
                try{this.mail.save();}catch(Exception ex){logger.error("",ex);}
            }
        } else {
            logger.debug("beginView called: NOT found mailid in param="+tmpMailid);
        }
    }

    public void newNote() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        if(mailid<=0){
            logger.debug("mailid not found: "+ mailid);
            vex.addValidationError("mailid not found.");
            throw vex;
        } else {
            mail= Mail.get(mailid);
        }

        Mailchild mailchild = new Mailchild();
        mailchild.setMailid(mailid);
        mailchild.setDate(new Date());
        mailchild.setIsfromcustomercare(false);
        mailchild.setMailtypeid(MailtypeSimple.TYPEID);
        mailchild.setVar1(notes);
        mailchild.setVar2("");
        mailchild.setVar3("");
        mailchild.setVar4("");
        mailchild.setVar5("");
        try{
            mailchild.save();
        } catch (GeneralException gex){
            vex.addValidationError("Sorry, there was an error saving the record.  Please try again.");
            logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
            throw vex;
        }

        //Mark as new again
        mail.setIsflaggedforcustomercare(true);
        try{mail.save();}catch(Exception ex){logger.error("",ex);}

        //Send xmpp message
        SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_CUSTOMERSUPPORT, "New dNeero Customer Support Comment: "+mail.getSubject()+" (mailid="+ mailid +") ("+Pagez.getUserSession().getUser().getEmail()+") "+notes);
        xmpp.send();

        //Refresh the bean
        initBean();

    }

    public int getMailid() {
        return mailid;
    }

    public void setMailid(int mailid) {
        this.mailid=mailid;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes=notes;
    }

    public ArrayList<Mailchild> getInboxdetails() {
        return inboxdetails;
    }

    public void setInboxdetails(ArrayList<Mailchild> inboxdetails) {
        this.inboxdetails=inboxdetails;
    }

    public Mail getMail() {
        return mail;
    }

    public void setMail(Mail mail) {
        this.mail=mail;
    }
}
