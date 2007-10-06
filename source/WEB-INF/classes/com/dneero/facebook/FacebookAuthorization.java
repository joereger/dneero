package com.dneero.facebook;

import com.dneero.util.Jsf;
import com.dneero.util.Num;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.session.SurveysTakenToday;
import com.dneero.session.UrlSplitter;
import com.dneero.session.UserSession;
import com.dneero.systemprops.SystemProperty;
import com.dneero.formbeans.PublicFacebookLandingPage;
import com.dneero.survey.servlet.RecordImpression;
import com.dneero.cache.providers.CacheFactory;
import com.facebook.api.FacebookRestClient;
import com.facebook.api.FacebookException;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.jdom.input.DOMBuilder;
import org.jdom.output.XMLOutputter;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Date;

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

            //Set referred by userid
            try{
                if (Jsf.getRequestParam("action")!=null && Jsf.getRequestParam("action").indexOf("showsurvey")>-1){
                    String[] split = Jsf.getRequestParam("action").split("-");
                    if (split.length>=3){
                        //Set the referredbyuserid value in the session
                        if (split[2]!=null && Num.isinteger(split[2])){
                            Jsf.getUserSession().setReferredbyOnlyUsedForSignup(Integer.parseInt(split[2]));
                        }
                    }
                }
            } catch (Exception ex) {logger.error(ex);}

            //Need a session key
            //auth_token should immediately be traded in for a valid fb_sig_session_key
            if ((Jsf.getRequestParam("auth_token")!=null && !Jsf.getRequestParam("auth_token").trim().equals(""))){
                logger.debug("auth_token found in request... will try to convert to session_key");
                FacebookRestClient facebookRestClient = new FacebookRestClient(SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_KEY), SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_SECRET), Jsf.getUserSession().getFacebookSessionKey());
                String facebooksessionkey = facebookRestClient.auth_getSession(Jsf.getRequestParam("auth_token").trim());
                Jsf.getUserSession().setFacebookSessionKey(facebooksessionkey);
            } else {
                //No auth_token was sent (it's only sent for new apps and new logins, etc) so look to session_key
                logger.debug("no auth_token found in request, looking for fb_sig_session_key");
                if ((Jsf.getRequestParam("fb_sig_session_key")!=null && !Jsf.getRequestParam("fb_sig_session_key").trim().equals(""))){
                    logger.debug("found a fb_sig_session_key in request");
                    Jsf.getUserSession().setFacebookSessionKey((Jsf.getRequestParam("fb_sig_session_key").trim()));
                } else {
                    logger.debug("no fb_sig_session_key found in request... aborting FacebookAuthorization");
                    //Jsf.redirectResponse("http://apps.facebook.com/"+SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_APP_NAME)+"/");
                    return;
                }
            }

            //@todo This could cause problems.  The facebook user doesn't get to this point except on their first click from the facebook ui.
            //@todo I try to pull their session from the cache (it won't be there).
            //@todo Then I put the populated session back into the cache.  But it's already in the Jsf session... the cache here won't ever be used again.
            //@todo And, the problem, if they do come back from the ui, they'll get a stale session... I don't think I need my own session cache for dNeero.

            //Pull userSession from cache
            boolean foundSessionInCache = false;
//            Object obj = CacheFactory.getCacheProvider().get(Jsf.getUserSession().getFacebookSessionKey(), "FacebookUserSession");
//            if (obj!=null && (obj instanceof UserSession)){
//                logger.debug("found a userSession in the cache");
//                Jsf.bindObjectToExpressionLanguage("#{userSession}", (UserSession)obj);
//                foundSessionInCache = true;
//            } else {
//                logger.debug("no userSession in cache");
//            }

            //In general try not to handle request vars below this line
            //I only want to run this stuff when I see a new Facebook session key...
            if (!foundSessionInCache) {
                logger.debug("running heavy Facebook user setup with api calls due to new facebooksessionkey");

                //Go get some details on this facebookuser
                FacebookRestClient facebookRestClient = null;
                try {
                    facebookRestClient = new FacebookRestClient(SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_KEY), SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_SECRET), Jsf.getUserSession().getFacebookSessionKey());
                    Jsf.getUserSession().setFacebookUser(new FacebookUser(facebookRestClient.users_getLoggedInUser(), Jsf.getUserSession().getFacebookSessionKey()));
                } catch (FacebookException fex) {
                    logger.error("Facebook Error fex", fex);
                }

                //If we have a facebook user to work with
                if (Jsf.getUserSession().getFacebookUser()!=null && !Jsf.getUserSession().getFacebookUser().getUid().trim().equals("")){
                    //Set facebookui
                    Jsf.getUserSession().setIsfacebookui(true);
                    //See if we have this facebook user as a dNeero user
                    User user = null;
                    if (Num.isinteger(Jsf.getUserSession().getFacebookUser().getUid())){
                        user = getdNeeroUserFromFacebookUserid(Integer.parseInt(Jsf.getUserSession().getFacebookUser().getUid()));
                    }
                    if (user!=null && user.getUserid()>0){
                        //Is already a dNeero user
                        Jsf.getUserSession().setUser(user);
                        Jsf.getUserSession().setIsloggedin(true);
                        Jsf.getUserSession().setSurveystakentoday(SurveysTakenToday.getNumberOfSurveysTakenToday(user));
                        logger.debug("dNeero Facebook Login: "+ user.getFirstname() + " " + user.getLastname() + " ("+user.getEmail()+") (Facebook.userid="+user.getFacebookuserid()+")");
                        //If their account is marked as having removed the app but facebook says they've got it added, update the User object
                        if (Jsf.getUserSession().getFacebookUser().getHas_added_app() && user.getIsfacebookappremoved()){
                            user.setIsfacebookappremoved(false);
                            user.setFacebookappremoveddate(user.getCreatedate());
                            try {user.save();} catch (Exception ex) {logger.error(ex);}
                        }
                        //Notify via XMPP
                        SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "Facebook Login: "+ user.getFirstname() + " " + user.getLastname() + " (email="+user.getEmail()+") (facebook.uid="+user.getFacebookuserid()+")");
                        xmpp.send();
                    } else {
                        //Is not a dNeero user yet... make sure there's no user in the session
                        Jsf.getUserSession().setUser(null);
                        Jsf.getUserSession().setIsloggedin(false);
                        logger.debug("Facebook user added app, considering taking surveys.  facebookSessionKey="+Jsf.getUserSession().getFacebookSessionKey());
                        //Notify via XMPP
                        SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "Facebook user '"+Jsf.getUserSession().getFacebookUser().getFirst_name()+" "+Jsf.getUserSession().getFacebookUser().getLast_name()+"' starts session.  Not yet a dNeero user.");
                        xmpp.send();
                        //Could redirect to Facebook new user welcome screen here.
                    }
                } else {
                    logger.debug("userSession.getFacebookUser() is empty after calling facebook api");
                }
            } else {
                logger.debug("didn't find a new facebooksessionkey so didn't make api call to load facebook user");
            }

            //Save UserSession in Cache
            //CacheFactory.getCacheProvider().put(Jsf.getUserSession().getFacebookSessionKey(), "FacebookUserSession", Jsf.getUserSession());

        } catch (Exception ex){
            ex.printStackTrace();
            logger.error(ex);
        }

        //If is coming from facebook but hasn't added app, make them add it
        if (Jsf.getUserSession().getIsfacebookui() && Jsf.getUserSession().getFacebookUser()!=null && !Jsf.getUserSession().getFacebookUser().getHas_added_app()){
            //UrlSplitter urlSplitter = new UrlSplitter(Jsf.getHttpServletRequest());
            //If the showsurvey var isn't set in the incoming request, make them add it... this is currently the only exception
            if (Jsf.getRequestParam("stoplooping")==null || !Jsf.getRequestParam("stoplooping").equals("1")){
                //Need to record impressions if we're gonna send them away
                if (Jsf.getRequestParam("action")!=null && Jsf.getRequestParam("action").indexOf("showsurvey")>-1){
                    RecordImpression.record(Jsf.getHttpServletRequest());
                }
                logger.debug("redirecting to facebook add app page");
                try{Jsf.redirectResponse("/facebooklandingpage.jsf?stoplooping=1&action="+Jsf.getRequestParam("action"));return;}catch(Exception ex){logger.error(ex);}
            }

        }
        
        logger.debug("leaving FacebookAuthorization and isfacebookui="+Jsf.getUserSession().getIsfacebookui() +"");
    }



    private static User getdNeeroUserFromFacebookUserid(int facebookuserid){
        Logger logger = Logger.getLogger(FacebookAuthorization.class);
        logger.debug("looking for user with facebookid="+facebookuserid);
        List<User> users = HibernateUtil.getSession().createCriteria(User.class)
                                           .add(Restrictions.eq("facebookuserid", facebookuserid))
                                           .setCacheable(true)
                                           .list();
        if (users!=null && users.size()>1){
            logger.error("More than one dNeero user for facebookuserid="+facebookuserid);
        }
        for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
            User user = iterator.next();
            logger.debug("returning userid="+user.getUserid()+" for facebookid="+facebookuserid);
            return user;
        }
        logger.debug("returning null user for facebookid="+facebookuserid);
        return null;
    }


}
