package com.dneero.helpers;

import com.dneero.dao.User;
import com.dneero.dao.Userrole;

import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Apr 26, 2007
 * Time: 4:12:04 PM
 */
public class UserAnnotator {

    private boolean isresearcher;
    private boolean isblogger;


    public UserAnnotator(User user){
        isresearcher = calculateIsresearcher(user);
        isblogger = calculateIsblogger(user);

    }

    private boolean calculateIsresearcher(User user){
        if (user!=null){
            for (Iterator<Userrole> iterator = user.getUserroles().iterator(); iterator.hasNext();) {
                Userrole userrole = iterator.next();
                if (userrole.getRoleid()== Userrole.RESEARCHER){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean calculateIsblogger(User user){
        if (user!=null){
            for (Iterator<Userrole> iterator = user.getUserroles().iterator(); iterator.hasNext();) {
                Userrole userrole = iterator.next();
                if (userrole.getRoleid()== Userrole.BLOGGER){
                    return true;
                }
            }
        }
        return false;
    }


    public boolean getIsresearcher() {
        return isresearcher;
    }


    public boolean getIsblogger() {
        return isblogger;
    }

}
