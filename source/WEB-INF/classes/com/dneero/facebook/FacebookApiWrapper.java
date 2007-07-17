package com.dneero.facebook;

import com.facebook.api.FacebookRestClient;
import com.dneero.session.UserSession;
import com.dneero.dao.Survey;
import com.dneero.dao.Response;
import com.dneero.ui.SurveyEnhancer;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jul 16, 2007
 * Time: 10:41:57 AM
 */
public class FacebookApiWrapper {

    private UserSession userSession = null;
    private String facebookSessionKey = "";
    private boolean issessionok = false;

    public FacebookApiWrapper(UserSession userSession){
        Logger logger = Logger.getLogger(this.getClass().getName());
        this.userSession = userSession;
        if (userSession.getFacebookSessionKey()!=null && !userSession.getFacebookSessionKey().trim().equals("")){
            facebookSessionKey = userSession.getFacebookSessionKey().trim();
            try{
                FacebookRestClient facebookRestClient = new FacebookRestClient(FacebookVars.API_KEY, FacebookVars.API_SECRET, facebookSessionKey);
                if (userSession.getUser()!=null && userSession.getUser().getUserid()>0){
                    if (userSession.getUser().getFacebookuserid()>0){
                        if (userSession.getUser().getFacebookuserid()==facebookRestClient.users_getLoggedInUser()){
                            issessionok = true;
                        } else {
                            logger.debug("userSession.getUser().getFacebookuserid()!=facebookRestClient.users_getLoggedInUser()");
                        }
                    } else {
                        logger.debug("userSession.getUser() (userid="+userSession.getUser().getUserid()+") passed to FacebookApiWrapper does not have a saved facebookuserid");
                    }
                } else {
                    logger.debug("userSession.getUser() passed to FacebookApiWrapper is either null or has a userid=0");
                }
            } catch (Exception ex){
                logger.error(ex);
            }
        }
    }

    public void postSurveyToFacebookMiniFeed(Survey survey, Response response){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (issessionok){
            try{
                SurveyEnhancer surveyEnhancer = new SurveyEnhancer(survey);
                String forcharity =  "";
                if (response.getIsforcharity()){
                    forcharity = " for charity";
                }
                FacebookRestClient facebookRestClient = new FacebookRestClient(FacebookVars.API_KEY, FacebookVars.API_SECRET, facebookSessionKey);
                facebookRestClient.feed_publishActionOfUser("took the survey <a href=\"http://apps.facebook.com/"+FacebookVars.APP_NAME+"/?action=showsurvey"+"-"+survey.getSurveyid()+"-"+userSession.getUser().getUserid()+"\">"+survey.getTitle()+"</a> and earned "+surveyEnhancer.getWillingtopayforresponse()+forcharity, "");
            } catch (Exception ex){logger.error(ex);}
        } else {logger.debug("Can't execute because issessionok = false");}
    }

    public void postSurveyToFacebookProfile(Survey survey, Response response){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (issessionok){
            try{
//                SurveyEnhancer surveyEnhancer = new SurveyEnhancer(survey);
//                String fbml = "Took the survey <a href=\"http://apps.facebook.com/"+FacebookVars.APP_NAME+"/?action=showsurvey&surveyid="+survey.getSurveyid()+"\">"+survey.getTitle()+"</a> and earned "+surveyEnhancer.getWillingtopayforresponse();
//                CharSequence cs = fbml.subSequence(0, fbml.length()-1);
//                FacebookRestClient facebookRestClient = new FacebookRestClient(FacebookVars.API_KEY, FacebookVars.API_SECRET, facebookSessionKey);
//                facebookRestClient.profile_setFBML(cs, userSession.getUser().getFacebookuserid());
            } catch (Exception ex){logger.error(ex);}
        } else {logger.debug("Can't execute because issessionok = false");}
    }

//    public void doSomething(){
//        Logger logger = Logger.getLogger(this.getClass().getName());
//        if (issessionok){
//            try{
//                FacebookRestClient facebookRestClient = new FacebookRestClient(FacebookVars.API_KEY, FacebookVars.API_SECRET, facebookSessionKey);
//                facebookRestClient.feed_publishActionOfUser("A survey was taken", "The survey was called "+survey.getTitle());
//            } catch (Exception ex){logger.error(ex);}
//        } else {logger.debug("Can't execute because issessionok = false");}
//    }







    public boolean getIssessionok() {
        return issessionok;
    }

    public String getFacebookSessionKey() {
        return facebookSessionKey;
    }


}
