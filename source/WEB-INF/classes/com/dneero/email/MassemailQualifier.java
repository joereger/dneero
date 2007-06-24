package com.dneero.email;

import com.dneero.dao.Massemail;
import com.dneero.dao.User;
import com.dneero.helpers.UserAnnotator;

/**
 * User: Joe Reger Jr
 * Date: Apr 26, 2007
 * Time: 4:13:53 PM
 */
public class MassemailQualifier {

    public static boolean isQualifiedForMassemail(Massemail massemail, User user){
        UserAnnotator ua = new UserAnnotator(user);
        if (!massemail.getIssenttouserswhooptoutofnoncriticalemails() && !user.getAllownoncriticalemails()){
            return false;
        }
        if (massemail.getIssenttoresearchers() && ua.getIsresearcher()){
            return true;
        }
        if (massemail.getIssenttobloggers() && ua.getIsblogger()){
            return true;
        }
        return false;
    }

}
