package com.dneero.session;

import org.apache.log4j.Logger;

import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIComponent;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import com.dneero.dao.Userrole;
import com.dneero.dao.User;
import com.dneero.util.Jsf;
import com.dneero.systemprops.SystemProperty;
import com.dneero.eula.EulaHelper;
import com.dneero.xmpp.SendXMPPMessage;

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

        //Persistent login start
        boolean wasAutoLoggedIn = false;
        if (!Jsf.getUserSession().getIsloggedin()){
            //See if the incoming request has a persistent login cookie
            Cookie[] cookies = Jsf.getHttpServletRequest().getCookies();
            logger.debug("looking for cookies");
            if (cookies!=null && cookies.length>0){
                logger.debug("cookies found.");
                for (int i = 0; i < cookies.length; i++) {
                    if (cookies[i].getName().equals(PersistentLogin.cookieName)){
                        logger.debug("persistent cookie found.");
                        int useridFromCookie = PersistentLogin.checkPersistentLogin(cookies[i]);
                        if (useridFromCookie>-1){
                            logger.debug("setting userid="+useridFromCookie);
                            User user = User.get(useridFromCookie);
                            if (user!=null && user.getUserid()>0){
                                UserSession newUserSession = new UserSession();
                                newUserSession.setUser(user);
                                newUserSession.setIsloggedin(true);
                                newUserSession.setIsLoggedInToBeta(Jsf.getUserSession().getIsLoggedInToBeta());
                                Jsf.bindObjectToExpressionLanguage("#{userSession}", newUserSession);
                                wasAutoLoggedIn = true;
                                //Notify customer care group
                                SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "dNeero User Login: "+ user.getFirstname() + " " + user.getLastname() + " ("+user.getEmail()+")");
                                xmpp.send();
                                //Now check the eula
                                if (!EulaHelper.isUserUsingMostRecentEula(user)){
                                    System.out.println("redirecting to force eula accept");
                                    context.getExternalContext().redirect("/loginagreeneweula.jsf?msg=autologin");
                                    return;
                                }
                                System.out.println("through eula check");
                                //Now dispatch request to the same page so that header is changed to reflect logged-in status
                                if (wasAutoLoggedIn){
                                    try{
                                        System.out.println("about to redir");
//                                        ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
//                                        HttpServletRequest request = (HttpServletRequest)ectx.getRequest();
//                                        HttpServletResponse response = (HttpServletResponse)ectx.getResponse();
//                                        response.reset();
//                                        response.resetBuffer();
//                                        System.out.println("request.getRequestURI()="+request.getRequestURI());
//                                        RequestDispatcher dispatcher = request.getRequestDispatcher(request.getRequestURI());
//                                        dispatcher.forward(request,response);
                                        context.getExternalContext().redirect("/account/accountmain.jsf?msg=autologin");
                                        return;
                                    } catch (Exception ex){
                                        logger.error(ex);
                                        ex.printStackTrace();
                                        context.getExternalContext().redirect("/index.jsf");
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        //Persistent login end



        String acl = (String)getAttributes().get("acl");
        String redirectonfail = (String)getAttributes().get("redirectonfail");

        if (acl==null){
            acl = "";
        }
        if (redirectonfail==null){
            redirectonfail = "true";
        }

        if (SystemProperty.getProp(SystemProperty.PROP_ISEVERYTHINGPASSWORDPROTECTED).equals("1") && !Jsf.getUserSession().getIsLoggedInToBeta()){
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
                for (Iterator<Userrole> iterator = userSession.getUser().getUserroles().iterator(); iterator.hasNext();) {
                    Userrole userrole = iterator.next();
                    if (userrole.getRoleid()== Userrole.SYSTEMADMIN){
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
