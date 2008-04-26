package com.dneero.facebook;


import com.dneero.systemprops.SystemProperty;
import com.dneero.systemprops.BaseUrl;
import com.dneero.dao.Survey;
import com.dneero.dao.Response;
import com.dneero.dao.Blogger;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.ui.SurveyEnhancer;
import com.dneero.util.Str;
import com.dneero.util.Num;
import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.Pagez;
import com.dneero.survey.servlet.SurveyFlashFacebookServlet;
import com.facebook.api.FacebookRestClient;
import com.facebook.api.FacebookSignatureUtil;
import com.facebook.api.TemplatizedAction;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import org.w3c.dom.Document;
import org.jdom.input.DOMBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.Element;

import java.util.*;
import java.net.URLEncoder;
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
                    if (userSession.getFacebookUser()!=null && userSession.getFacebookUser().getUid()!=null && userSession.getFacebookUser().getUid().length()>0){
                        issessionok = true;
                    } else {
                        logger.debug("don't have a facebookuserid to work with");
                    }
                }
            } catch (Exception ex){
                logger.error("",ex);
            }
        }
    }

//    public void postSurveyToFacebookMiniFeedOld(Survey survey, Response response){
//        Logger logger = Logger.getLogger(this.getClass().getName());
//        if (issessionok){
//            try{
//                SurveyEnhancer surveyEnhancer = new SurveyEnhancer(survey);
//                int BUILTINCHARS = 28 + surveyEnhancer.getWillingtopayforresponse().length(); //Just update this with the most recent count of "took the survey " etc
//                int lengthofsurveytitle = survey.getTitle().length();
//                int lengthofforcharity = 0;
//
//                String forcharity =  "";
//                if (response.getIsforcharity()){
//                    forcharity = " for charity";
//                    lengthofforcharity = 12;
//                }
//
//                int length=BUILTINCHARS + lengthofsurveytitle + lengthofforcharity;
//                int truncatetitleto = 100;
//                if (length>60){
//                    truncatetitleto = 60 - (BUILTINCHARS + lengthofforcharity);
//                }
//
//                //Limit the length to 60 chars... not counting tags... just the displayed chars
//                FacebookRestClient facebookRestClient = new FacebookRestClient(SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_KEY), SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_SECRET), facebookSessionKey);
//                facebookRestClient.feed_publishActionOfUser("took the survey <a href=\"http://apps.facebook.com/"+SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_APP_NAME)+"/?action=showsurvey"+"-"+survey.getSurveyid()+"-"+ Blogger.get(response.getBloggerid()).getUserid()+"\">"+ Str.truncateString(survey.getTitle(), truncatetitleto)+"</a> and earned "+surveyEnhancer.getWillingtopayforresponse()+forcharity, "");
//            } catch (Exception ex){logger.error("",ex);}
//        } else {logger.debug("Can't execute because issessionok = false");}
//    }


    public void postToFeed(Survey survey, Response response){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (issessionok){
            try{
                SurveyEnhancer surveyEnhancer = new SurveyEnhancer(survey);
                String forcharity =  "";
                String charitybody = "";
                if (response.getIsforcharity()){
                    forcharity = " for charity";
                    charitybody = " Earnings from this conversation were donated to <i>"+response.getCharityname()+"</i>.";
                }
                String earnings = surveyEnhancer.getWillingtopayforresponse();
                int userid = Blogger.get(response.getBloggerid()).getUserid();
                String answerslink = "<a href=\"http://apps.facebook.com/"+SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_APP_NAME)+"/?action=showsurvey-"+survey.getSurveyid()+"-"+userid+"\"> answers</a>";

                StringBuffer titleTemplate = new StringBuffer();
                titleTemplate.append("{actor} earned {earnings}{forcharity} by joining a conversation.");

                StringBuffer bodyTemplate = new StringBuffer();
                bodyTemplate.append("See {actor}'s {answerslink} to <i>{surveytitle}</i> and join the conversation!{charitybody}");

                TemplatizedAction action = new TemplatizedAction(titleTemplate.toString(), bodyTemplate.toString());
                action.addTitleParam("earnings", earnings);
                action.addTitleParam("forcharity", fbApiClean(forcharity));
                action.addBodyParam("answerslink", answerslink);
                action.addBodyParam("surveytitle", fbApiClean(survey.getTitle()));
                action.addBodyParam("surveyid", String.valueOf(survey.getSurveyid()));
                action.addBodyParam("userid", String.valueOf(userid));
                action.addBodyParam("charitybody", fbApiClean(charitybody));

                FacebookRestClient facebookRestClient = new FacebookRestClient(SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_KEY), SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_SECRET), facebookSessionKey);
                facebookRestClient.feed_PublishTemplatizedAction(action);
            } catch (Exception ex){logger.error("",ex);}
        } else {logger.debug("Can't execute because issessionok = false");}
    }

    private String fbApiClean(String in){
        //For some reason FB doesn't allow "message" in a feed story
        return in.replaceAll("message", "m3ssage");
    }

    public void updateProfile(User user){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("Starting to create FBML for profile");
        if (issessionok){
            try{
                if (user.getBloggerid()>0){
                    StringBuffer fbml = new StringBuffer();
                    double totalearnings = 0;
                    int count = 0;
                    fbml.append("<center>");
                    fbml.append("<font style=\"font-size: 14px; color: #cccccc; font-weight: bold;\">");
                    fbml.append("Most Recent Conversations I've Joined");
                    fbml.append("</font>");
                    fbml.append("</center>");
                    fbml.append("<br/>");

                    List<Response> responses = HibernateUtil.getSession().createCriteria(Response.class)
                                               .add(Restrictions.eq("bloggerid", user.getBloggerid()))
                                               .add(Restrictions.eq("ispaid", false))
                                               .add(Restrictions.ne("poststatus", Response.POSTATUS_NOTPOSTEDTIMELIMITPASSED))
                                               .addOrder(Order.desc("responsedate"))
                                               .setCacheable(false)
                                               .setMaxResults(60)
                                               .list();

                    for (Iterator<Response> iterator = responses.iterator(); iterator.hasNext();) {
                        Response response = iterator.next();
                        Survey survey = Survey.get(response.getSurveyid());
                        String forcharity =  "";
                        if (response.getIsforcharity()){
                            forcharity = " for charity";
                        }
                        String dotdotdot = "";
                        if (survey.getTitle().length()>40){
                            dotdotdot = "...";
                        }
                        SurveyEnhancer surveyEnhancer = new SurveyEnhancer(survey);
                        fbml.append("<div style=\"border: 2px solid #e6e6e6;\">");
//                            fbml.append("<table width=\"100%\">");
//                            fbml.append("<tr>");
//                            fbml.append("<td valign=\"top\">");
                                fbml.append("<a href=\"http://apps.facebook.com/"+SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_APP_NAME)+"?action=showsurvey"+"-"+survey.getSurveyid()+"-"+user.getUserid()+"\">");
                                fbml.append("<img src=\""+ BaseUrl.getNoHttp() +"/images/dneero-favicon.png\" alt=\"\" width=\"16\" height=\"16\" border=\"0\">");
                                fbml.append("<font style=\"font-size: 12px; color: #3B5998; font-weight: bold;\">");
                                fbml.append(survey.getTitle());
                                fbml.append("</font>");
                                fbml.append("</a>");
//                            fbml.append("</td>");
//                            fbml.append("<td valign=\"top\" align=\"right\" style=\"text-align: right;\">");
//                                fbml.append("<br/>");
//                                fbml.append("<img src=\""+ BaseUrl.getNoHttp() +"/images/clear.gif\" alt=\"\" width=\"16\" height=\"1\" border=\"0\">");
//                                fbml.append("<font style=\"font-size: 9px; color: #666666;\">I'm Earning "+surveyEnhancer.getWillingtopayforresponse()+forcharity+" from this conversation.</font>");
//                                fbml.append("<br/>");
//                            fbml.append("</td>");
//                            fbml.append("</tr>");
//                            fbml.append("</table>");


                            fbml.append(SurveyFlashFacebookServlet.getFBMLSyntax(BaseUrl.get(false), survey.getSurveyid(), user.getUserid(), response.getResponseid(), false, true, false));
                            fbml.append("<table width=\"100%\">");
                            fbml.append("<tr>");
                            fbml.append("<td valign=\"top\">");
                                fbml.append("<font style=\"font-size: 9px; color: #666666;\">I'm earning "+surveyEnhancer.getWillingtopayforresponse()+forcharity+" from this conversation.</font>");
                            fbml.append("</td>");
                            fbml.append("<td valign=\"top\" align=\"right\" style=\"text-align: right;\">");
                                fbml.append("<a href=\"http://apps.facebook.com/"+SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_APP_NAME)+"/?dpage=%2Fsurvey.jsp&surveyid="+survey.getSurveyid()+"&userid="+user.getUserid()+"&responseid="+response.getResponseid()+"\">");
                                fbml.append("<font style=\"font-size: 9px; color: #3B5998; font-weight: bold;\">");
                                fbml.append("How would you answer?");
                                fbml.append("</font>");
                                fbml.append("</a>");
                            fbml.append("</td>");
                            fbml.append("</tr>");
                            fbml.append("</table>");


                        fbml.append("</div>");
                        fbml.append("<img src=\""+ BaseUrl.getNoHttp() +"/images/clear.gif\" alt=\"\" width=\"1\" height=\"2\" border=\"0\">");
                    }
                    fbml.append("<br/>");


                    CharSequence cs = fbml.subSequence(0, fbml.length());
                    FacebookRestClient facebookRestClient = new FacebookRestClient(SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_KEY), SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_SECRET), facebookSessionKey);
                    boolean success = facebookRestClient.profile_setFBML(cs, Long.parseLong(String.valueOf(user.getFacebookuserid())));
                    if (success){
                        logger.debug("Apparently the setFBML was successful.");
                    } else {
                        logger.debug("Apparently the setFBML was not successful.");
                    }
                } else {
                    logger.debug("user.getBloggerid()==0... userid="+user.getUserid());
                }
            } catch (Exception ex){logger.error("",ex);}
        } else {logger.debug("Can't execute because issessionok = false");}
    }

    public ArrayList<Integer> getFriendUids(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        ArrayList<Integer> friends = new ArrayList<Integer>();
        if (issessionok){
            try{

                if (Pagez.getRequest().getParameter("fb_sig_friends")!=null){
                    ArrayList<Integer> out = new ArrayList<Integer>();
                    String[] splitfriends =  Pagez.getRequest().getParameter("fb_sig_friends").split(",");
                    for (int i=0; i<splitfriends.length; i++) {
                        String splitfriend=splitfriends[i];
                        if (Num.isinteger(splitfriend)){
                            out.add(Integer.parseInt(splitfriend));
                        }
                    }
                    logger.debug("returning friends from fb_sig_friends");
                    return out;
                }

                logger.debug("making an api call to get friends");
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
                //Extract the uids
                List allChildren = root.getChildren();
                for (Iterator iterator = allChildren.iterator(); iterator.hasNext();) {
                    Element element = (Element) iterator.next();
                    if (element.getName().equals("uid")){
                        if(Num.isinteger(element.getTextTrim())){
                            friends.add(Integer.parseInt(element.getTextTrim()));
                        }
                    }
                }
            } catch (Exception ex){logger.error("",ex);}
        } else {logger.debug("Can't execute because issessionok = false");}
        return friends;
    }


    public ArrayList<FacebookUser> getFriends(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("begin getFriends() facebookSessionKey="+facebookSessionKey);
        ArrayList<FacebookUser> friends = new ArrayList<FacebookUser>();
        if (issessionok){
            try{
                //Set up the facebook rest client
                FacebookRestClient facebookRestClient = new FacebookRestClient(SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_KEY), SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_SECRET), facebookSessionKey);
                //Get the list of uids
                ArrayList<Integer> uids = getFriendUids();

                if (uids!=null && uids.size()>0){
                    logger.debug("getFriends() uids not null facebookSessionKey="+facebookSessionKey);
                    //Create fql based on the list of uids
                    StringBuffer fqlWhere = new StringBuffer();
                    fqlWhere.append("(uid IN (");
                    for (Iterator iterator = uids.iterator(); iterator.hasNext();) {
                        Integer uid = (Integer) iterator.next();
                        fqlWhere.append(uid);
                        if (iterator.hasNext()){
                            fqlWhere.append(", ");
                        }
                    }
                    fqlWhere.append("))");
                    //Go back and get all the important info
                    String fql = "SELECT "+FacebookUser.sqlListOfCols+" FROM user WHERE "+fqlWhere;
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
                }
            } catch (Exception ex){logger.error("",ex); ex.printStackTrace();}
        } else {logger.debug("Can't execute because issessionok = false");}
        logger.debug("end getFriends() facebookSessionKey="+facebookSessionKey);
        return friends;
    }

    public TreeMap<Integer, FacebookSurveyThatsBeenTaken> getSurveysFriendsHaveTaken(int onlylookatlastxresponses){
        return getSurveysFriendsHaveTaken(getFriends(), onlylookatlastxresponses);
    }

    public TreeMap<Integer, FacebookSurveyThatsBeenTaken> getSurveysFriendsHaveTaken(ArrayList<FacebookUser> friends, int onlylookatlastxresponses){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (onlylookatlastxresponses<=0){
            onlylookatlastxresponses = 500;   
        }
        TreeMap<Integer, FacebookSurveyThatsBeenTaken> out = new TreeMap<Integer, FacebookSurveyThatsBeenTaken>();
        if (friends !=null && friends.size()>0){
            //Create sql based on friends
            StringBuffer sqlWhere = new StringBuffer();
            sqlWhere.append(" ( ");
            for (Iterator iterator = friends.iterator(); iterator.hasNext();) {
                FacebookUser facebookUser = (FacebookUser) iterator.next();
                sqlWhere.append(" facebookuserid='"+facebookUser.getUid()+"' ");
                if (iterator.hasNext()){
                    sqlWhere.append(" OR ");
                }
            }
            sqlWhere.append(" ) ");
            //Pull up all User objects for friends with the eventual goal of getting a list of surveys that friends have taken
            List users = HibernateUtil.getSession().createQuery("from User WHERE "+sqlWhere).setCacheable(true).list();
            for (Iterator iterator = users.iterator(); iterator.hasNext();) {
                User user = (User) iterator.next();
                //Find surveys
                if (user.getBloggerid()>0){
                    Blogger blogger = Blogger.get(user.getBloggerid());
                    List<Response> responses = HibernateUtil.getSession().createCriteria(Response.class)
                                                       .add(Restrictions.eq("bloggerid", blogger.getBloggerid()))
                                                       .addOrder(Order.desc("responseid"))
                                                       .setMaxResults(5)
                                                       .setCacheable(true)
                                                       .list();
                    for (Iterator<Response> iterator1 = responses.iterator(); iterator1.hasNext();) {
                        Response response = iterator1.next();
                        //Set up the taker
                        FacebookSurveyTaker facebookSurveyTaker = new FacebookSurveyTaker();
                        facebookSurveyTaker.setFacebookUser(getFacebookUserByUid(friends, String.valueOf(user.getFacebookuserid())));
                        facebookSurveyTaker.setUserid(user.getUserid());
                        facebookSurveyTaker.setResponseid(response.getResponseid());
                        //Get a facebookSurveyThatsBeenTaken object from the TreeMap (keyed by surveyid) or create one
                        FacebookSurveyThatsBeenTaken facebookSurveyThatsBeenTaken = new FacebookSurveyThatsBeenTaken();
                        if (out.containsKey(response.getSurveyid())){
                            facebookSurveyThatsBeenTaken = out.get(response.getSurveyid());
                        } else {
                            facebookSurveyThatsBeenTaken.setSurvey(Survey.get(response.getSurveyid()));
                        }
                        //Add the taker to the facebookSurveyThatsBeenTaken
                        facebookSurveyThatsBeenTaken.addFacebookSurveyTaker(facebookSurveyTaker);
                        //And add that to the out
                        out.put(response.getSurveyid(), facebookSurveyThatsBeenTaken);
                    }
                }
            }
        }
        return out;
    }

    private FacebookUser getFacebookUserByUid(ArrayList<FacebookUser> facebookUsers, String uid){
        for (Iterator<FacebookUser> iterator = facebookUsers.iterator(); iterator.hasNext();) {
            FacebookUser facebookUser = iterator.next();
            if (facebookUser.getUid().equals(uid)){
                return facebookUser;
            }
        }
        return null;
    }




    

    public String inviteFriendsTodNeero(ArrayList<Long> uids){
        Logger logger = Logger.getLogger(this.getClass().getName());
        FacebookRestClient facebookRestClient = new FacebookRestClient(SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_KEY), SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_SECRET), facebookSessionKey);
        String type = "dNeero";
        CharSequence typeChars = type.subSequence(0, type.length());
        StringBuffer content = new StringBuffer();
        content.append("You've been invited to the conversation app called dNeero that allows you to earn real money joining conversations and sharing your opinions with your friends.");
        content.append("<fb:req-choice url=\"http://apps.facebook.com/"+SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_APP_NAME)+"\" label=\"Check it Out\" />");
        CharSequence contentChars = content.subSequence(0, content.length());
        URL imgUrl = null;
        try{
            imgUrl = new URL("http", SystemProperty.getProp(SystemProperty.PROP_BASEURL), "/images/dneero-logo-100x100.png");
        } catch (Exception ex){
            logger.error("",ex);
        }
        try{
            URL url = facebookRestClient.notifications_sendRequest(uids, typeChars, contentChars, imgUrl, true);
            if (url!=null){
                logger.debug("FacebookAPI returned: " + url.toString());
                return url.toString();
            }

        } catch (Exception ex){
            logger.error("",ex);
        }
        return "";
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
            //logger.debug(indent + " " + element.getName());
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
