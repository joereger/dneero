package com.dneero.facebook;

import com.dneero.util.Jsf;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
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
        try{
            //First, determine whether this is a facebook request
            if (Jsf.getRequestParam("fb_sig_api_key")!=null && !Jsf.getRequestParam("fb_sig_api_key").equals("")){
                //Set the userSession flag to display UI as Facebook
                Jsf.getUserSession().setIsfacebookui(true);
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
                    if (fb_sig_session_key!=null && !fb_sig_session_key.trim().equals("")){
                        facebookSessionKey = fb_sig_session_key;
                        Jsf.getUserSession().setFacebookSessionKey(facebookSessionKey);
                        logger.debug("Using facebookSessionKey from fb_sig_session_key param.");
                    }
                    //If user hasn't added app yet redir to the app add page
                    if (facebookSessionKey.trim().equals("")){
                        Jsf.redirectResponse("http://www.facebook.com/add.php?api_key="+FacebookVars.API_KEY);
                        return;
                    }
                    logger.debug("User has added app... we have facebookSessionKey="+facebookSessionKey);
                    FacebookRestClient facebookRestClient = new FacebookRestClient(FacebookVars.API_KEY, FacebookVars.API_SECRET, facebookSessionKey);
                    int facebookuserid = facebookRestClient.users_getLoggedInUser();
                    logger.debug("facebookRestClient.users_getLoggedInUser()="+facebookuserid);
                    //See if we have this facebook user as a dNeero user
                    User user = getdNeeroUserFromFacebookUserid(facebookuserid);
                    if (user!=null && user.getUserid()>0){
                        //Is already a dNeero user
                    } else {
                        //Is not a dNeero user yet
                        //Redirect to a screen "This is your first time visiting dNeero" that asks them to log in or click a button to create an account?
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

    public static User createdNeeroUserUsingFacebookInfo(int facebookuserid, String facebookSessionKey){
        FacebookRestClient facebookRestClient = new FacebookRestClient(FacebookVars.API_KEY, FacebookVars.API_SECRET, facebookSessionKey);
        //Get first name
        //Get last name
        return null;
    }

}
