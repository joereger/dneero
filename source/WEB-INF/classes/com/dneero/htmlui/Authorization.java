package com.dneero.htmlui;

import org.apache.log4j.Logger;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;
import java.util.Iterator;

import com.dneero.session.UserSession;
import com.dneero.util.Jsf;
import com.dneero.dao.Userrole;
import com.dneero.dao.User;

/**
 * User: Joe Reger Jr
 * Date: Jun 23, 2006
 * Time: 1:06:30 PM
 */
public class Authorization extends UIComponentBase {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public String getFamily(){
        return "dNeeroAuth";
    }

    public void encodeBegin(FacesContext context) throws IOException {
        logger.debug("encodeBegin called");

        String acl = (String)getAttributes().get("acl");
        if (acl==null){
            acl = "";
        }

        String redirectonfail = (String)getAttributes().get("redirectonfail");
        if (redirectonfail==null){
            redirectonfail = "true";
        }

        //Acl authorization
        if (!isAuthorized(context, acl)){
            if (redirectonfail.equals("true")){
                com.dneero.session.UserSession userSession = Jsf.getUserSession();
                if (userSession!=null && userSession.getUser()!=null && userSession.getIsloggedin()){
                    Jsf.redirectResponse("/notauthorized.jsf");
                    return;
                } else {
                    Jsf.redirectResponse("/login.jsf");
                    return;
                }
            } else {
                setRendered(false);
                List children = getChildren();
                for (Iterator it = children.iterator(); it.hasNext(); ) {
                    UIComponent child = (UIComponent)it.next();
                    child.setRendered(false);
                }
            }
        }
    }

    private boolean isAuthorized(FacesContext context, String acl)  throws IOException {

        UserSession userSession = Jsf.getUserSession();

        if (acl!=null && acl.equals("public")){
            return true;
        }

        if (userSession.getUser()!=null){

            if (acl!=null && acl.equals("blogger")){
                for (Iterator<Userrole> iterator = userSession.getUser().getUserroles().iterator(); iterator.hasNext();) {
                    Userrole userrole = iterator.next();
                    if (userrole.getRoleid()== Userrole.BLOGGER){
                        logger.debug("Blogger authorized.");
                        return true;
                    }
                }
                return false;
            }

            if (acl!=null && acl.equals("researcher")){
                for (Iterator<Userrole> iterator = userSession.getUser().getUserroles().iterator(); iterator.hasNext();) {
                    Userrole userrole = iterator.next();
                    if (userrole.getRoleid()== Userrole.RESEARCHER){
                        logger.debug("Researcher authorized.");
                        return true;
                    }
                }
                return false;
            }

            if (acl!=null && acl.equals("systemadmin")){
                return isUserSysadmin(userSession.getUser());
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







}
