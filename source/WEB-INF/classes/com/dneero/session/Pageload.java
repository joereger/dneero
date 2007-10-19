package com.dneero.session;

import org.apache.log4j.Logger;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Iterator;

import com.dneero.util.Jsf;
import com.dneero.util.Time;
import com.dneero.systemprops.SystemProperty;
import com.dneero.systemprops.BaseUrl;
import com.dneero.facebook.FacebookAuthorization;
import com.dneero.dao.User;
import com.dneero.dao.Userrole;
import com.dneero.eula.EulaHelper;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.formbeans.LoginAgreeNewEula;

/**
 * User: Joe Reger Jr
 * Date: Jun 23, 2006
 * Time: 1:06:30 PM
 */
public class Pageload extends UIComponentBase {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public String getFamily(){
        return "dNeeroPageload";
    }

    public void encodeBegin(FacesContext context) throws IOException {
        logger.debug("encodeBegin called");

        //Production redirect to www.dneero.com for https
        //@todo make this configurable... i.e. no hard-coded urls
        UrlSplitter urlSplitter = new UrlSplitter(Jsf.getHttpServletRequest());
        if (urlSplitter.getRawIncomingServername().equals("dneero.com")){
            if (urlSplitter.getMethod().equals("GET")){
                Jsf.redirectResponse(urlSplitter.getScheme()+"://"+"www.dneero.com"+urlSplitter.getServletPath()+urlSplitter.getParametersAsQueryStringQuestionMarkIfRequired());
                return;
            } else {
                Jsf.redirectResponse(urlSplitter.getScheme()+"://"+"www.dneero.com/");
                return;
            }
        }

        //Redirect login page to https
        if (SystemProperty.getProp(SystemProperty.PROP_ISSSLON).equals("1") && urlSplitter.getScheme().equals("http") && urlSplitter.getServletPath().equals("login.jsf")){
            try{
                Jsf.redirectResponse(BaseUrl.get(true)+"login.jsf");
                return;
            } catch (Exception ex){
                logger.error(ex);
                return;
            }
        }

        //Facebook start
        FacebookAuthorization.doAuth();
        //Make sure they've filled out profile
        if (Jsf.getUserSession().getIsfacebookui()){
            if (Jsf.getUserSession().getUser()!=null){
                if (Jsf.getUserSession().getUser().getBloggerid()<=0){
                    //User hasn't filled out profile info... send to profile page
                    
                }
            }
        }
        //Facebook end

        logger.debug("before persistent login and isfacebookui="+Jsf.getUserSession().getIsfacebookui());

        //Persistent login start
        boolean wasAutoLoggedIn = false;
        if (!Jsf.getUserSession().getIsfacebookui()){
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
                                if (user!=null && user.getUserid()>0 && user.getIsenabled()){
                                    UserSession newUserSession = new UserSession();
                                    newUserSession.setUser(user);
                                    newUserSession.setIsloggedin(true);
                                    newUserSession.setIsLoggedInToBeta(true);
                                    newUserSession.setPendingSurveyReferredbyuserid(Jsf.getUserSession().getPendingSurveyReferredbyuserid());
                                    newUserSession.setPendingSurveyResponseAsString(Jsf.getUserSession().getPendingSurveyResponseAsString());
                                    newUserSession.setPendingSurveyResponseSurveyid(Jsf.getUserSession().getPendingSurveyResponseSurveyid());
                                    newUserSession.setCurrentSurveyid(Jsf.getUserSession().getCurrentSurveyid());
                                    newUserSession.setSurveystakentoday(SurveysTakenToday.getNumberOfSurveysTakenToday(user));
                                    //Check the eula
                                    if (!EulaHelper.isUserUsingMostRecentEula(user)){
                                        newUserSession.setIseulaok(false);
                                    } else {
                                        newUserSession.setIseulaok(true);
                                    }
                                    Jsf.bindObjectToExpressionLanguage("#{userSession}", newUserSession);
                                    wasAutoLoggedIn = true;
                                    //Notify via XMPP
                                    SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SALES, "dNeero User Auto-Login: "+ user.getFirstname() + " " + user.getLastname() + " ("+user.getEmail()+")");
                                    xmpp.send();
                                    //Now dispatch request to the same page so that header is changed to reflect logged-in status
                                    if (wasAutoLoggedIn){
                                        try{
                                            if (SystemProperty.getProp(SystemProperty.PROP_ISSSLON).equals("1")){
                                                try{
                                                    Jsf.redirectResponse(BaseUrl.get(true)+"account/index.jsf?msg=autologin");
                                                    return;
                                                } catch (Exception ex){
                                                    logger.error(ex);
                                                    Jsf.redirectResponse("/account/index.jsf?msg=autologin");
                                                    return;
                                                }
                                            } else {
                                                Jsf.redirectResponse("/account/index.jsf?msg=autologin");
                                                return;
                                            }
                                        } catch (Exception ex){
                                            logger.error(ex);
                                            ex.printStackTrace();
                                            Jsf.redirectResponse("/index.jsf");
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        //Persistent login end

        //logger.debug("after persistent login and isfacebookui="+Jsf.getUserSession().getIsfacebookui());


        //Account activation
        if (Jsf.getUserSession().getUser()!=null && !Jsf.getUserSession().getUser().getIsactivatedbyemail()){
            //User isn't activated but they get a grace period
            int daysInGracePeriod = 3;
            Calendar startOfGracePeriod = Time.xDaysAgoStart(Calendar.getInstance(), daysInGracePeriod);
            if (Jsf.getUserSession().getUser().getCreatedate().before(startOfGracePeriod.getTime())){
                if (urlSplitter.getRequestUrl().indexOf("emailactivationwaiting.jsf")==-1){
                    Jsf.redirectResponse("/emailactivationwaiting.jsf");
                    return;
                }
            }
        }

        //Now check the eula
        if (Jsf.getUserSession().getIsloggedin() && !Jsf.getUserSession().getIseulaok()){
            System.out.println("redirecting to force eula accept");
            LoginAgreeNewEula bean = (LoginAgreeNewEula)Jsf.getManagedBean("loginAgreeNewEula");
            bean.beginView();
            if (urlSplitter.getRequestUrl().indexOf("loginagreeneweula.jsf")==-1){
                Jsf.redirectResponse("/loginagreeneweula.jsf");
                return;
            }
        }


    }









}
