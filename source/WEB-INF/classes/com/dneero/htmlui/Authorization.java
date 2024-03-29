package com.dneero.htmlui;

import org.apache.log4j.Logger;


import java.util.Iterator;

import com.dneero.dao.Userrole;
import com.dneero.dao.User;

/**
 * User: Joe Reger Jr
 * Date: Jun 23, 2006
 * Time: 1:06:30 PM
 */
public class Authorization {


    public static boolean check(String acl) {
        Logger logger = Logger.getLogger(Authorization.class);
        logger.debug("check() called");
        if (isAuthorized(acl)){
            return true;
        }
        return false;
    }

    private static boolean isAuthorized(String acl) {
        Logger logger = Logger.getLogger(Authorization.class);
        
        if (acl!=null && acl.equals("public")){
            return true;
        }

        if (Pagez.getUserSession().getUser()!=null){
            if (acl!=null && acl.equals("blogger")){
                for (Iterator<Userrole> iterator = Pagez.getUserSession().getUser().getUserroles().iterator(); iterator.hasNext();) {
                    Userrole userrole = iterator.next();
                    if (userrole.getRoleid()== Userrole.BLOGGER){
                        logger.debug("Blogger authorized.");
                        return true;
                    }
                }
                return false;
            }

            if (acl!=null && acl.equals("researcher")){
                for (Iterator<Userrole> iterator = Pagez.getUserSession().getUser().getUserroles().iterator(); iterator.hasNext();) {
                    Userrole userrole = iterator.next();
                    if (userrole.getRoleid()== Userrole.RESEARCHER){
                        logger.debug("Researcher authorized.");
                        return true;
                    }
                }
                return false;
            }

            if (acl!=null && acl.equals("customercare")){
                return isUserCustomercare(Pagez.getUserSession().getUser());
            }

            if (acl!=null && acl.equals("sysadmin")){
                return isUserSysadmin(Pagez.getUserSession().getUser());
            }

            if (acl!=null && acl.equals("account")){
                return true;
            }
        }

        return false;

    }

    public static boolean isUserSysadmin(User user){
        if (user!=null){
            for (Iterator<Userrole> iterator = user.getUserroles().iterator(); iterator.hasNext();) {
                Userrole userrole = iterator.next();
                if (userrole.getRoleid()== Userrole.SYSTEMADMIN){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isUserCustomercare(User user){
        if (user!=null){
            for (Iterator<Userrole> iterator = user.getUserroles().iterator(); iterator.hasNext();) {
                Userrole userrole = iterator.next();
                if (userrole.getRoleid()== Userrole.CUSTOMERCARE){
                    return true;
                }
            }
        }
        return false;
    }







}
