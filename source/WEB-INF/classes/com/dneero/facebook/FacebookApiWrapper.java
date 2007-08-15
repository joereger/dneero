package com.dneero.facebook;

import com.facebook.api.FacebookRestClient;
import com.dneero.session.UserSession;
import com.dneero.dao.Survey;
import com.dneero.dao.Response;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.ui.SurveyEnhancer;
import com.dneero.systemprops.SystemProperty;
import com.dneero.systemprops.BaseUrl;
import com.dneero.util.Str;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;

import java.util.List;
import java.util.Iterator;

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
                FacebookRestClient facebookRestClient = new FacebookRestClient(SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_KEY), SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_SECRET), facebookSessionKey);
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
                FacebookRestClient facebookRestClient = new FacebookRestClient(SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_KEY), SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_SECRET), facebookSessionKey);
                facebookRestClient.feed_publishActionOfUser("took the survey <a href=\"http://apps.facebook.com/"+SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_APP_NAME)+"/?action=showsurvey"+"-"+survey.getSurveyid()+"-"+userSession.getUser().getUserid()+"\">"+survey.getTitle()+"</a> and earned "+surveyEnhancer.getWillingtopayforresponse()+forcharity, "");
            } catch (Exception ex){logger.error(ex);}
        } else {logger.debug("Can't execute because issessionok = false");}
    }

    public void updateFacebookProfile(User user){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("Starting to create FBML for profile");
        if (issessionok){
            try{
                if (user.getBloggerid()>0){
                    StringBuffer fbml = new StringBuffer();
                    double totalearnings = 0;
                    int count = 0;
                    fbml.append("<b>Most Recent Surveys I've Taken</b>");
                    fbml.append("<br/>");
                    fbml.append("<hr style=\"border: 0; color:  #cccccc; background: #cccccc; height: 1px;\">");
                    fbml.append("<table>");

                    List<Response> responses = HibernateUtil.getSession().createCriteria(Response.class)
                                               .add(Restrictions.eq("bloggerid", user.getBloggerid()))
                                               .setCacheable(true)
                                               .addOrder(Order.desc("responsedate"))
                                               .list();
                    for (Iterator<Response> iterator = responses.iterator(); iterator.hasNext();) {
                        Response response = iterator.next();
                        Survey survey = Survey.get(response.getSurveyid());
                        totalearnings = totalearnings + survey.getWillingtopayperrespondent();
                        count = count + 1;
                        if (count<=10){
                            String forcharity =  "";
                            if (response.getIsforcharity()){
                                forcharity = " for charity";
                            }
                            String dotdotdot = "";
                            if (survey.getTitle().length()>40){
                                dotdotdot = "...";
                            }
                            SurveyEnhancer surveyEnhancer = new SurveyEnhancer(survey);
                            fbml.append("<tr>");
                                fbml.append("<td>");
                                    fbml.append("<a href=\"http://apps.facebook.com/"+SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_APP_NAME)+"?action=showsurvey"+"-"+survey.getSurveyid()+"-"+userSession.getUser().getUserid()+"\">");
                                    fbml.append("<img src=\""+ BaseUrl.getNoHttp() +"/images/dneero-favicon.png\" alt=\"\" width=\"16\" height=\"16\">");
                                    fbml.append(" "+Str.truncateString(survey.getTitle(), 40)+dotdotdot);
                                    fbml.append("</a>");
                                fbml.append("</td>");
                                fbml.append("<td>");
                                    fbml.append(" (I earned "+surveyEnhancer.getWillingtopayforresponse()+forcharity+"."+")");
                                fbml.append("</td>");
                            fbml.append("</tr>");
                        }
                    }
                    fbml.append("</table>");
                    fbml.append("<hr style=\"border: 0; color:  #cccccc; background: #cccccc; height: 1px;\">");
                    //@todo this facebook profile earnings number doesn't represent full earnings... impressions aren't included.
                    fbml.append("<b>My Total dNeero Earnings: "+"$"+ Str.formatForMoney(totalearnings)+"</b>");

                    CharSequence cs = fbml.subSequence(0, fbml.length());
                    FacebookRestClient facebookRestClient = new FacebookRestClient(SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_KEY), SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_SECRET), facebookSessionKey);
                    boolean success = facebookRestClient.profile_setFBML(cs, userSession.getUser().getFacebookuserid());
                    if (success){
                        logger.debug("Apparently the setFBML was successful.");
                    } else {
                        logger.debug("Apparently the setFBML was not successful.");
                    }
                } else {
                    logger.debug("user.getBloggerid()==0... userid="+user.getUserid());
                }
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
