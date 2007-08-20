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
import com.dneero.util.Num;
import com.dneero.util.Jsf;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.jdom.input.DOMBuilder;
import org.w3c.dom.Document;

import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.net.URL;

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

    public ArrayList<FacebookUser> getFriends(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        ArrayList<FacebookUser> friends = new ArrayList<FacebookUser>();
        if (issessionok){
            try{
                //Set up the facebook rest client
                FacebookRestClient facebookRestClient = new FacebookRestClient(SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_KEY), SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_SECRET), facebookSessionKey);
                //Get a list of uids
                Document w3cDoc = facebookRestClient.friends_get();
                DOMBuilder builder = new DOMBuilder();
                org.jdom.Document jdomDoc = builder.build(w3cDoc);
                logger.debug("Start Facebook API Friends Response:");
                XMLOutputter outp = new XMLOutputter();
                outp.output(jdomDoc, System.out);
                logger.debug(":End Facebook API Friends Response");
                Element root = jdomDoc.getRootElement();
                outputChildrenToLogger(root, 0);
                //Create fql based on the list of uids
                StringBuffer fqlWhere = new StringBuffer();
                List allChildren = root.getChildren();
                for (Iterator iterator = allChildren.iterator(); iterator.hasNext();) {
                    Element element = (Element) iterator.next();
                    if (element.getName().equals("uid")){
                        fqlWhere.append(" uid="+element.getTextTrim()+" ");
                        if (iterator.hasNext()){
                            fqlWhere.append(" OR ");
                        }
                    }
                }
                //Go back and get all the important info
                String fql = "SELECT first_name, last_name, birthday, sex, uid FROM user WHERE "+fqlWhere;
                Document w3cDoc2 = facebookRestClient.fql_query(fql.subSequence(0,fql.length()));
                DOMBuilder builder2 = new DOMBuilder();
                org.jdom.Document jdomDoc2 = builder2.build(w3cDoc2);
                logger.debug("Start Facebook FQL Response: "+fql);
                XMLOutputter outp2 = new XMLOutputter();
                outp2.output(jdomDoc2, System.out);
                logger.debug(":End Facebook FQL Response");
                Element root2 = jdomDoc2.getRootElement();
                //Iterate each child
                List fbusers = root2.getChildren();
                for (Iterator iterator = fbusers.iterator(); iterator.hasNext();) {
                    Element fbuser = (Element) iterator.next();
                    if (fbuser.getName().equals("user")){
                        FacebookUser facebookUser = new FacebookUser(fbuser);
                        if (facebookUser.getUid().length()>0){
                            friends.add(facebookUser);
                        }
                    }
                }
            } catch (Exception ex){logger.error(ex);}
        } else {logger.debug("Can't execute because issessionok = false");}
        return friends;
    }

    public void inviteFriendsToSurvey(ArrayList<Integer> uids, Survey survey){
        Logger logger = Logger.getLogger(this.getClass().getName());
        FacebookRestClient facebookRestClient = new FacebookRestClient(SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_KEY), SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_SECRET), facebookSessionKey);
        SurveyEnhancer surveyEnhancer = new SurveyEnhancer(survey);
        String forcharity =  "";
        if (survey.getIscharityonly()){
            forcharity = " for charity";
        }
        String type = "social survey";
        CharSequence typeChars = type.subSequence(0, type.length());
        StringBuffer content = new StringBuffer();
        content.append("You've been invited to the social survey: "+survey.getTitle());
        content.append(" ");
        content.append("Earn up to "+surveyEnhancer.getWillingtopayforresponse()+forcharity);
        content.append("<fb:req-choice url=\"http://apps.facebook.com/"+SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_APP_NAME)+"?action=showsurvey"+"-"+survey.getSurveyid()+"-"+userSession.getUser().getUserid()+"\" label=\"Check it Out\" />");
        CharSequence contentChars = content.subSequence(0, content.length());
        URL imgUrl = null;
        try{
            imgUrl = new URL("http", SystemProperty.getProp(SystemProperty.PROP_BASEURL), "/images/dneero-logo-100x100.png");
        } catch (Exception ex){
            logger.error(ex);    
        }
        try{
            URL url = facebookRestClient.notifications_sendRequest(uids, typeChars, contentChars, imgUrl, true);
            if (url!=null){
                logger.debug("FacebookAPI returned: " + url.toString());
                Jsf.redirectResponse(url.toString());
                return;
            }
            
        } catch (Exception ex){
            logger.error(ex);
        }
    }


    public static Element getChild(Element el, String name){
        List allChildren = el.getChildren();
        for (Iterator iterator = allChildren.iterator(); iterator.hasNext();) {
            Element element = (Element) iterator.next();
            if (element.getName().equals(name)){
                return element;
            }
        }
        return null;
    }

    public static void outputChildrenToLogger(Element el, int level){
        Logger logger = Logger.getLogger(FacebookApiWrapper.class);
        level = level + 1;
        String indent = "";
        for(int i=0; i<level; i++){
            indent = indent + "-";
        }
        List allChildren = el.getChildren();
        for (Iterator iterator = allChildren.iterator(); iterator.hasNext();) {
            Element element = (Element) iterator.next();
            logger.debug(indent + " " + element.getName());
            outputChildrenToLogger(element, level);
        }
    }



    public boolean getIssessionok() {
        return issessionok;
    }

    public String getFacebookSessionKey() {
        return facebookSessionKey;
    }


}
