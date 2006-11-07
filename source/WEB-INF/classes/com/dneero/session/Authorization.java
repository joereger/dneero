package com.dneero.session;

import org.apache.log4j.Logger;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIComponent;
import javax.faces.el.ValueBinding;
import java.io.IOException;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

import com.dneero.dao.Userrole;
import com.dneero.util.Jsf;

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
        String redirectonfail = (String)getAttributes().get("redirectonfail");

        if (acl==null){
            acl = "";
        }
        if (redirectonfail==null){
            redirectonfail = "true";
        }

        //@todo Remove beta login check before going live... duh
        if (!Jsf.getUserSession().getIsLoggedInToBeta()){
            context.getExternalContext().redirect("/logintobeta.jsf");
            return;
        } else {
            if (acl!=null && acl.equals("publiclogin")){
                return;
            }
        }


        if (Jsf.getUserSession().getUser()!=null && !Jsf.getUserSession().getUser().getIsactivatedbyemail()){
            context.getExternalContext().redirect("/emailactivationwaiting.jsf");
            return;
        }

        if (!isAuthorized(context, acl)){
            if (redirectonfail.equals("true")){
                UserSession userSession = Jsf.getUserSession();
                if (userSession!=null && userSession.getUser()!=null && userSession.getIsloggedin()){
                    if(acl.equals("blogger") && userSession.getUser().getBlogger()==null){
                        context.getExternalContext().redirect("/blogger/bloggerdetails.jsf");
                        return;
                    }
                    if(acl.equals("researcher") && userSession.getUser().getResearcher()==null){
                        context.getExternalContext().redirect("/researcher/researcherdetails.jsf");
                        return;
                    }
                    context.getExternalContext().redirect("/notauthorized.jsf");
                    return;
                } else {
                    context.getExternalContext().redirect("/login.jsf");
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
                    if (userrole.getRoleid()==Roles.BLOGGER){
                        logger.debug("Blogger authorized.");
                        return true;
                    }
                }
                return false;
            }

            if (acl!=null && acl.equals("researcher")){
                for (Iterator<Userrole> iterator = userSession.getUser().getUserroles().iterator(); iterator.hasNext();) {
                    Userrole userrole = iterator.next();
                    if (userrole.getRoleid()==Roles.RESEARCHER){
                        logger.debug("Researcher authorized.");
                        return true;
                    }
                }
                return false;
            }

            if (acl!=null && acl.equals("systemadmin")){
                for (Iterator<Userrole> iterator = userSession.getUser().getUserroles().iterator(); iterator.hasNext();) {
                    Userrole userrole = iterator.next();
                    if (userrole.getRoleid()==Roles.SYSTEMADMIN){
                        logger.debug("Systemadmin authorized.");
                        return true;
                    }
                }
                return false;
            }

            if (acl!=null && acl.equals("account")){
                return true;
            }
        }

        return false;

    }







}
