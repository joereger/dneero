package com.dneero.htmluibeans;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.Pagez;
import org.apache.log4j.Logger;

import java.util.*;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class AccountInbox implements Serializable {

    private List<Mail> inboxmessages;


    public AccountInbox() {

    }



    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("load called");
        User user = Pagez.getUserSession().getUser();
        if (user!=null){
            inboxmessages= HibernateUtil.getSession().createQuery("from Mail where userid='"+user.getUserid()+"' order by mailid desc").list();
        }
    }

    public List getInboxmessages() {
        return inboxmessages;
    }

    public void setInboxmessages(List inboxmessages) {
        this.inboxmessages= (ArrayList) inboxmessages;
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return false;
    }



}
