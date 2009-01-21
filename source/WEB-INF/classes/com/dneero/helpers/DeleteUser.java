package com.dneero.helpers;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.money.CurrentBalanceCalculator;
import com.dneero.htmlui.Pagez;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Dec 19, 2007
 * Time: 10:28:40 AM
 */
public class DeleteUser {

    public static void delete(User user){
        Logger logger = Logger.getLogger(DeleteUser.class);
        if (user!=null && user.getUserid()>0){
            CurrentBalanceCalculator cbc = new CurrentBalanceCalculator(user);
            if (cbc.getCurrentbalance()==0){
                if (user.getBloggerid()>0){
                    deleteObj(Questionresponse.class, "bloggerid", user.getBloggerid());
                    deleteObj(Response.class, "bloggerid", user.getBloggerid());
                    deleteObj(Revshare.class, "sourcebloggerid", user.getBloggerid());
                    deleteObj(Revshare.class, "targetbloggerid", user.getBloggerid());
                }
                if (user.getBloggerid()>0){
                    deleteObj(Emailinvitebatch.class, "researcherid", user.getResearcherid());
                    deleteObj(Panel.class, "researcherid", user.getResearcherid());
                    deleteObj(Survey.class, "researcherid", user.getResearcherid());
                }
                deleteObj(Balance.class, "userid", user.getUserid());
                deleteObj(Balancetransaction.class, "userid", user.getUserid());
                deleteObj(Blogger.class, "userid", user.getUserid());
                deleteObj(Charitydonation.class, "userid", user.getUserid());
                deleteObj(Creditcard.class, "userid", user.getUserid());
                deleteObj(Impression.class, "userid", user.getUserid());
                deleteObj(Researcher.class, "userid", user.getUserid());
                deleteObj(Responsepending.class, "userid", user.getUserid());
                deleteObj(Userpersistentlogin.class, "userid", user.getUserid());
                deleteObj(Facebookpendingreferredby.class, "referredbyuserid", user.getUserid());

                try{user.delete();}catch(Exception ex){logger.error("", ex);}
            } else {
                Pagez.getUserSession().setMessage("User's balance not zero.");
            }
        }
    }

    private static void deleteObj(Class clazz, String fieldname, int id){
        Logger logger = Logger.getLogger(DeleteUser.class);
        List objects = HibernateUtil.getSession().createCriteria(clazz)
                                           .add(Restrictions.eq(fieldname, id))
                                           .setCacheable(false)
                                           .list();
        for (Iterator iterator=objects.iterator(); iterator.hasNext();) {
            Object o= iterator.next();
            try{
                HibernateUtil.getSession().delete(o);
            } catch (Exception ex){
                logger.error("", ex);
            }
        }

    }





}
