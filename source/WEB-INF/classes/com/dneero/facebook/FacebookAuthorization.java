package com.dneero.facebook;

import com.dneero.util.Jsf;
import com.dneero.util.Num;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.session.SurveysTakenToday;
import com.dneero.session.UrlSplitter;
import com.dneero.systemprops.SystemProperty;
import com.dneero.formbeans.PublicFacebookLandingPage;
import com.dneero.survey.servlet.RecordImpression;
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

            //Determine whether this is a facebook request
            //Facebook sends this any time it puts the app inside an iFrame
            if (Jsf.getRequestParam("fb_sig_api_key")!=null && !Jsf.getRequestParam("fb_sig_api_key").equals("")){
                //Set the userSession flag to display UI as Facebook
                Jsf.getUserSession().setIsfacebookui(true);
                logger.debug("setting isfacebookui=true");
                try{
                    //See if the app is added... this url check can be overridden below
//                    if (Jsf.getRequestParam("fb_sig_added")!=null && !Jsf.getRequestParam("fb_sig_added").equals("")){
//                        if (Jsf.getRequestParam("fb_sig_added").trim().equals("1")){
//                            Jsf.getUserSession().setIsfacebookappadded(true);
//                        }
//                    }
                    //Need to establish a facebook session
                    String facebookSessionKey = "";
                    //Check local dNeero session
                    if (Jsf.getUserSession().getFacebookSessionKey()!=null && !Jsf.getUserSession().getFacebookSessionKey().equals("")){
                        facebookSessionKey = Jsf.getUserSession().getFacebookSessionKey();
                        logger.debug("Using facebookSessionKey from dNeero userSession object.");
                    }
                    //Check params sent by facebook
                    String fb_sig_session_key = Jsf.getRequestParam("fb_sig_session_key");
                    //I only want to run the auth stuff when I see a new Facebook session key...
                    //i.e. one that's not null, not empty and is different than the one that's in userSession.
                    //Facebook only sends the fb_sig_session_key after the user has added the app
                    if (fb_sig_session_key!=null && !fb_sig_session_key.trim().equals("") && !fb_sig_session_key.trim().equals(Jsf.getUserSession().getFacebookSessionKey().trim()) || (Jsf.getRequestParam("fb_sig_added").trim().equals("1") && !Jsf.getUserSession().getIsfacebookui())){
                        //Just verify again... this check was in before some restructuring
                        if (fb_sig_session_key!=null && !fb_sig_session_key.trim().equals("")){
                            facebookSessionKey = fb_sig_session_key;
                            Jsf.getUserSession().setFacebookSessionKey(facebookSessionKey);
                            logger.debug("Using facebookSessionKey from fb_sig_session_key param.");
                        }
                        //Go get some details on this facebookuser
                        FacebookRestClient facebookRestClient = new FacebookRestClient(SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_KEY), SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_SECRET), facebookSessionKey);
                        int facebookuserid = facebookRestClient.users_getLoggedInUser();
                        FacebookUser facebookUser = new FacebookUser(facebookuserid, Jsf.getUserSession().getFacebookSessionKey());
                        Jsf.getUserSession().setIsfacebookappadded(facebookRestClient.users_isAppAdded());
                        Jsf.getUserSession().setTempFacebookUserid(facebookuserid);
                        Jsf.getUserSession().setFacebookUser(facebookUser);
                        logger.debug("facebookRestClient.users_getLoggedInUser()="+facebookuserid);
                        //If user hasn't added app yet redir to the app add page
                        if (facebookSessionKey.trim().equals("")){
                            logger.debug("redirecting this user to facebook add app page");
                            //Notify via XMPP
                            SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "Redirecting "+ facebookUser.getFirst_name() + " " + facebookUser.getLast_name() + " to add app page. " + "(facebook.uid="+facebookUser.getUid()+")");
                            xmpp.send();
                            //Need to record impressions if we're gonna send them away
                            if (Jsf.getRequestParam("action")!=null && Jsf.getRequestParam("action").indexOf("showsurvey")>-1){
                                RecordImpression.record(Jsf.getHttpServletRequest());
                            }
                            Jsf.redirectResponse("http://www.facebook.com/add.php?api_key="+ SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_KEY));
                            return;
                        }
                        logger.debug("User has added app... we have facebookSessionKey="+facebookSessionKey);
                        //See if we have this facebook user as a dNeero user
                        User user = getdNeeroUserFromFacebookUserid(facebookuserid);
                        if (user!=null && user.getUserid()>0){
                            //Is already a dNeero user
                            Jsf.getUserSession().setUser(user);
                            Jsf.getUserSession().setIsloggedin(true);
                            Jsf.getUserSession().setSurveystakentoday(SurveysTakenToday.getNumberOfSurveysTakenToday(user));
                            logger.debug("dNeero Facebook Login: "+ user.getFirstname() + " " + user.getLastname() + " ("+user.getEmail()+") (Facebook.userid="+user.getFacebookuserid()+")");
                            //If their account is marked as having removed the app but facebook says they've got it added, update the User object
                            if (facebookUser.getHas_added_app() && user.getIsfacebookappremoved()){
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
                            logger.debug("Facebook user added app, considering taking surveys.  facebookSessionKey="+facebookSessionKey);
                            //Notify via XMPP
                            SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "Facebook user '"+facebookUser.getFirst_name()+" "+facebookUser.getLast_name()+"' starts session.  Not yet a dNeero user.");
                            xmpp.send();
                            //Could redirect to Facebook new user welcome screen here.
                        }

                    } else {
                        logger.debug("no Jsf.getRequestParam(\"fb_sig_session_key\") found");
                    }
                } catch (FacebookException fex){
                    logger.error("Facebook Error fex", fex);
                } catch (Exception ex){
                    logger.error("Facebook Error ex", ex);
                }
            } else {
                logger.debug("no Jsf.getRequestParam(\"fb_sig_api_key\") found");
            }
        } catch (Exception ex){
            logger.error("Facebook Error ex", ex);
        }


        //If is coming from facebook but hasn't added app, make them add it
        if (Jsf.getUserSession().getIsfacebookui() && !Jsf.getUserSession().getIsfacebookappadded()){
            //UrlSplitter urlSplitter = new UrlSplitter(Jsf.getHttpServletRequest());
            //If the showsurvey var isn't set in the incoming request, make them add it... this is currently the only exception
            if (Jsf.getRequestParam("stoplooping")==null || !Jsf.getRequestParam("stoplooping").equals("1")){

                //Need to record impressions if we're gonna send them away
                if (Jsf.getRequestParam("action")!=null && Jsf.getRequestParam("action").indexOf("showsurvey")>-1){
                    RecordImpression.record(Jsf.getHttpServletRequest());
                }
                logger.debug("redirecting to facebook add app page");
                try{Jsf.redirectResponse("/facebooklandingpage.jsf?stoplooping=1&action="+Jsf.getRequestParam("action"));return;}catch(Exception ex){logger.error(ex);}
                //PublicFacebookLandingPage pfblp = new PublicFacebookLandingPage();
                //try{Jsf.redirectResponse(pfblp.getAddurl());return;}catch(Exception ex){logger.error(ex);}
            }

        }
        
        logger.debug("leaving FacebookAuthorization and isfacebookui="+Jsf.getUserSession().getIsfacebookui() +" and isfacebookappadded="+Jsf.getUserSession().getIsfacebookappadded());
    }



    public static User getdNeeroUserFromFacebookUserid(int facebookuserid){
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
