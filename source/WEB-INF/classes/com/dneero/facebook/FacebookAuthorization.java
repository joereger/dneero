package com.dneero.facebook;

import com.dneero.util.Jsf;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.session.SurveysTakenToday;
import com.facebook.api.FacebookRestClient;
import com.facebook.api.FacebookException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jul 11, 2007
 * Time: 10:26:53 PM
 */
public class FacebookAuthorization {

    public static void doAuth(){
        Logger logger = Logger.getLogger(FacebookAuthorization.class);
        logger.debug("starting FacebookAuthorization and isfacebookui="+Jsf.getUserSession().getIsfacebookui());
        try{
            //First, determine whether this is a facebook request
            if (Jsf.getRequestParam("fb_sig_api_key")!=null && !Jsf.getRequestParam("fb_sig_api_key").equals("")){
                //Set the userSession flag to display UI as Facebook
                Jsf.getUserSession().setIsfacebookui(true);
                logger.debug("setting isfacebookui=true");
                try{
                    //Need to establish a facebook session
                    String facebookSessionKey = "";
                    //Check local dNeero session
                    if (Jsf.getUserSession().getFacebookSessionKey()!=null && !Jsf.getUserSession().getFacebookSessionKey().equals("")){
                        facebookSessionKey = Jsf.getUserSession().getFacebookSessionKey();
                        logger.debug("Using facebookSessionKey from dNeero userSession object.");
                    }
                    //Check params sent by facebook
                    String fb_sig_session_key = Jsf.getRequestParam("fb_sig_session_key");
                    //I only want to run the auth stuff when I see a new Facebook session key... i.e. one that's not null, not empty and is different than the one that's in userSession
                    if (fb_sig_session_key!=null && !fb_sig_session_key.trim().equals("") && !fb_sig_session_key.trim().equals(Jsf.getUserSession().getFacebookSessionKey().trim())){
                        //Just verify again... this check was in before some restructuring
                        if (fb_sig_session_key!=null && !fb_sig_session_key.trim().equals("")){
                            facebookSessionKey = fb_sig_session_key;
                            Jsf.getUserSession().setFacebookSessionKey(facebookSessionKey);
                            logger.debug("Using facebookSessionKey from fb_sig_session_key param.");
                        }
                        //If user hasn't added app yet redir to the app add page
                        if (facebookSessionKey.trim().equals("")){
                            logger.debug("redirecting user to facebook add app page");
                            Jsf.redirectResponse("http://www.facebook.com/add.php?api_key="+FacebookVars.API_KEY);
                            return;
                        }
                        logger.debug("User has added app... we have facebookSessionKey="+facebookSessionKey);
                        FacebookRestClient facebookRestClient = new FacebookRestClient(FacebookVars.API_KEY, FacebookVars.API_SECRET, facebookSessionKey);
                        int facebookuserid = facebookRestClient.users_getLoggedInUser();
                        Jsf.getUserSession().setTempFacebookUserid(facebookuserid);
                        logger.debug("facebookRestClient.users_getLoggedInUser()="+facebookuserid);
                        //See if we have this facebook user as a dNeero user
                        User user = getdNeeroUserFromFacebookUserid(facebookuserid);
                        if (user!=null && user.getUserid()>0){
                            //Is already a dNeero user
                            Jsf.getUserSession().setUser(user);
                            Jsf.getUserSession().setIsloggedin(true);
                            Jsf.getUserSession().setSurveystakentoday(SurveysTakenToday.getNumberOfSurveysTakenToday(user));
                            logger.debug("dNeero Facebook Login: "+ user.getFirstname() + " " + user.getLastname() + " ("+user.getEmail()+") (Facebook.userid="+user.getFacebookuserid()+")");
                            //Notify via XMPP
                            SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SALES, "dNeero Facebook Login: "+ user.getFirstname() + " " + user.getLastname() + " ("+user.getEmail()+") (Facebook.userid="+user.getFacebookuserid()+")");
                            xmpp.send();
                            //Could redirect to Facebook welcome screen here.
                        } else {
                            //Is not a dNeero user yet... make sure there's no user in the session
                            Jsf.getUserSession().setUser(null);
                            Jsf.getUserSession().setIsloggedin(false);
                            logger.debug("Facebook user added app, considering taking surveys.  facebookSessionKey="+facebookSessionKey);
                            //Notify via XMPP
                            SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SALES, "Facebook user starts session.  Not yet a user.  facebookSessionKey="+facebookSessionKey);
                            xmpp.send();
                            //Could redirect to Facebook new user welcome screen here.
                        }
                    }
                } catch (FacebookException fex){
                    logger.error("Facebook Error fex", fex);
                } catch (Exception ex){
                    logger.error("Facebook Error ex", ex);
                }
            }
        } catch (Exception ex){
            logger.error("Facebook Error ex", ex);
        }
        logger.debug("leaving FacebookAuthorization and isfacebookui="+Jsf.getUserSession().getIsfacebookui());
    }



    public static User getdNeeroUserFromFacebookUserid(int facebookuserid){
        Logger logger = Logger.getLogger(FacebookAuthorization.class);
        List<User> users = HibernateUtil.getSession().createCriteria(User.class)
                                           .add(Restrictions.eq("facebookuserid", facebookuserid))
                                           .setCacheable(true)
                                           .list();
        if (users!=null && users.size()>1){
            logger.error("More than one dNeero user for facebookuserid="+facebookuserid);
        }
        for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
            User user = iterator.next();
            return user;
        }
        return null;
    }


}
