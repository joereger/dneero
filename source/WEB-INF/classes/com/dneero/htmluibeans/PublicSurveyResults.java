package com.dneero.htmluibeans;


import com.dneero.cache.html.DbcacheexpirableCache;
import com.dneero.dao.Blogger;
import com.dneero.dao.Response;
import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dbgrid.Grid;
import com.dneero.dbgrid.GridCol;
import com.dneero.display.SurveyResultsDisplay;
import com.dneero.display.SurveyResultsUserQuestions;
import com.dneero.display.SurveyResultsUserQuestionsListitem;
import com.dneero.facebook.FacebookUser;
import com.dneero.helpers.UserInputSafe;
import com.dneero.htmlui.Pagez;
import com.dneero.util.Num;
import com.dneero.util.Str;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class PublicSurveyResults implements Serializable {



    private Survey survey;
    private User userwhotooksurvey = null;
    private String resultsHtml = "";
    private String resultsUserquestionsHtml = "";
    private String resultsHtmlForUserWhoTookSurvey = "";
    private String resultsYourFriends = "";
    private String resultsfriendstabtext = "People from site you were at";
    private boolean resultsshowyourfriendstab = false;
    private int resultstabselectedindex = 0;
    private List<PublicSurveyFacebookFriendListitem> facebookuserswhotooksurvey = new ArrayList<PublicSurveyFacebookFriendListitem>();
    private TreeMap<String, String> facebookuserswhodidnottakesurvey = new TreeMap<String, String>();


    public PublicSurveyResults(){

    }

    public void initBean(){
        //Set up logger
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("Instanciated.");

        //Surveyid from session or url
        int surveyid = Pagez.getUserSession().getCurrentSurveyid();
        if (Num.isinteger(Pagez.getRequest().getParameter("surveyid"))){
            surveyid = Integer.parseInt(Pagez.getRequest().getParameter("surveyid"));
        } else if (Num.isinteger(Pagez.getRequest().getParameter("s"))) {
            surveyid = Integer.parseInt(Pagez.getRequest().getParameter("s"));
        }
        Pagez.getUserSession().setCurrentSurveyid(surveyid);
        logger.debug("surveyid found: "+surveyid);

        //If we don't have a surveyid, shouldn't be on this page
        if (surveyid<=0){
            return;
        }

        //Load up the survey
        survey = Survey.get(surveyid);

        //If the survey is draft or waiting
        if (survey.getStatus()<Survey.STATUS_OPEN){
            return;
        }

        //Userid from url
        int userid = 0;
        if (Num.isinteger(Pagez.getRequest().getParameter("userid"))){
            userid = Integer.parseInt(Pagez.getRequest().getParameter("userid"));
        } else if (Num.isinteger(Pagez.getRequest().getParameter("u"))){
            userid = Integer.parseInt(Pagez.getRequest().getParameter("u"));
        }


        //Set userwhotooksurvey, first verifying that they've actually taken the survey
        userwhotooksurvey = null;
        if (userid>0){
            User userTmp = User.get(userid);
            if (userTmp.getIsenabled()){
                if (userTmp.getBloggerid()>0){
                    Blogger blogger = Blogger.get(userTmp.getBloggerid());
                    for (Iterator<Response> iterator = blogger.getResponses().iterator(); iterator.hasNext();) {
                        Response response = iterator.next();
                        if (response.getSurveyid()==survey.getSurveyid()){
                            userwhotooksurvey = userTmp;
                            break;
                        }
                    }
                }
            }
        }




        //Results main tab
        if (!survey.getIsresultshidden()){
            String resultsHtmlKey = "surveyresults.jsp-resultsHtml-surveyid"+survey.getSurveyid();
            String group = "PublicSurveyResults.java-surveyid-"+survey.getSurveyid();
            Object fromCache = DbcacheexpirableCache.get(resultsHtmlKey, group);
            if (fromCache!=null){
                try{resultsHtml = (String)fromCache;}catch(Exception ex){logger.error("", ex);}
            } else {
                resultsHtml = SurveyResultsDisplay.getHtmlForResults(survey, null, 0, new ArrayList<Integer>(), null, true, false);
                DbcacheexpirableCache.put(resultsHtmlKey, group, resultsHtml, DbcacheexpirableCache.expireSurveyInXHrs(survey, 3));
            }
        } else {
            resultsHtml = "<font class=\"smallfont\">The creator has chosen to hide overall aggregate results.  However, we don't allow researchers to hide aggregate results from individual blogs so those results are still available.  To see such results, find a place where this is posted and click the See How Others Voted link... you'll see how others from that blog answered.</font>";
        }

        //Results user questions html
        if (!survey.getIsresultshidden()){
            int userquestionspage = 1;
            if (Pagez.getRequest().getParameter("userquestionspage")!=null && Num.isinteger(Pagez.getRequest().getParameter("userquestionspage"))){
                userquestionspage = Integer.parseInt(Pagez.getRequest().getParameter("userquestionspage"));
            }
            String resultsHtmlKey = "surveyresults.jsp-resultsUserquestionsHtml-surveyid"+survey.getSurveyid()+"-userquestionspage"+userquestionspage;
            String group = "PublicSurveyResults.java-surveyid-"+survey.getSurveyid();
            Object fromCache = DbcacheexpirableCache.get(resultsHtmlKey, group);
            if (fromCache!=null){
                try{resultsUserquestionsHtml = (String)fromCache;}catch(Exception ex){logger.error("", ex);}
            } else {
                //Go get all results for user questions
                ArrayList<SurveyResultsUserQuestionsListitem> sruqli = SurveyResultsUserQuestions.getUserQuestionResults(survey, null, 0, new ArrayList<Integer>(), null);
                //Create a template for the display
                StringBuffer template = new StringBuffer();
                template.append("<font class=\"smallfont\"><b><a href=\"/profile.jsp?userid=<$user.userid$>\"><$user.nickname$></a> wanted to know:</b></font>");
                template.append("<br/>");
                template.append("<b><$question.question$></b>");
                template.append("<br/>");
                template.append("<$htmlForResult$>");
                template.append("<br/>");
                template.append("<br/>");
                //Create a Grid rendering
                ArrayList<GridCol> cols=new ArrayList<GridCol>();
                cols.add(new GridCol("", template.toString(), false, "", "", "background: #ffffff;", ""));
                resultsUserquestionsHtml = Grid.render(sruqli, cols, 25, "/surveyresults.jsp?surveyid="+survey.getSurveyid()+"&panel=panel1a", "userquestionspage");
                //Put into cache
                DbcacheexpirableCache.put(resultsHtmlKey, group, resultsUserquestionsHtml, DbcacheexpirableCache.expireSurveyInXHrs(survey, 3));
            }
//            if (HtmlCache.isStale(resultsHtmlKey, 6000)){
//                //Go get all results for user questions
//                ArrayList<SurveyResultsUserQuestionsListitem> sruqli = SurveyResultsUserQuestions.getUserQuestionResults(survey, null, 0, new ArrayList<Integer>(), null);
//                //Create a template for the display
//                StringBuffer template = new StringBuffer();
//                template.append("<font class=\"smallfont\"><b><a href=\"/profile.jsp?userid=<$user.userid$>\"><$user.nickname$></a> wanted to know:</b></font>");
//                template.append("<br/>");
//                template.append("<b><$question.question$></b>");
//                template.append("<br/>");
//                template.append("<$htmlForResult$>");
//                template.append("<br/>");
//                template.append("<br/>");
//                //Create a Grid rendering
//                ArrayList<GridCol> cols=new ArrayList<GridCol>();
//                cols.add(new GridCol("", template.toString(), false, "", "", "background: #ffffff;", ""));
//                resultsUserquestionsHtml = Grid.render(sruqli, cols, 25, "/surveyresults.jsp?surveyid="+survey.getSurveyid()+"&panel=panel1a", "userquestionspage");
//                //Store it in the cache
//                HtmlCache.updateCache(resultsHtmlKey, 6000, resultsUserquestionsHtml);
//            } else {
//                resultsUserquestionsHtml = HtmlCache.getFromCache(resultsHtmlKey);
//            }
        }  else {
            resultsUserquestionsHtml = "<font class=\"smallfont\">The creator has chosen to hide overall aggregate results.  However, we don't allow researchers to hide aggregate results from individual blogs so those results are still available.  To see such results, find a place where this is posted and click the See How Others Voted link... you'll see how others from that blog answered.</font>";
        }

        //Determine which of the results tabs is on
        if (userwhotooksurvey!=null){
            resultstabselectedindex = 1;
        } else {
            resultstabselectedindex = 0;
        }

        //Set the results for userwhotooksurvey
        if (userwhotooksurvey!=null){
            String resultsHtmlForUserWhoTookSurveyKey = "surveyresults.jsp-resultsHtmlForUserWhoTookSurvey-surveyid"+survey.getSurveyid()+"-userid"+userwhotooksurvey.getUserid();
            String group = "PublicSurveyResults.java-surveyid-"+survey.getSurveyid();
            Object fromCache = DbcacheexpirableCache.get(resultsHtmlForUserWhoTookSurveyKey, group);
            if (fromCache!=null){
                try{resultsHtmlForUserWhoTookSurvey = (String)fromCache;}catch(Exception ex){logger.error("", ex);}
            } else {
                resultsHtmlForUserWhoTookSurvey = SurveyResultsDisplay.getHtmlForResults(survey, null, userwhotooksurvey.getUserid(), new ArrayList<Integer>(), null, true, false);
                DbcacheexpirableCache.put(resultsHtmlForUserWhoTookSurveyKey, group, resultsHtmlForUserWhoTookSurvey, DbcacheexpirableCache.expireSurveyInXHrs(survey, 3));
            }
//            if (HtmlCache.isStale(resultsHtmlForUserWhoTookSurveyKey, 6000)){
//                resultsHtmlForUserWhoTookSurvey = SurveyResultsDisplay.getHtmlForResults(survey, null, userwhotooksurvey.getUserid(), new ArrayList<Integer>(), null, true, false);
//                HtmlCache.updateCache(resultsHtmlForUserWhoTookSurveyKey, 6000, resultsHtmlForUserWhoTookSurvey);
//            } else {
//                resultsHtmlForUserWhoTookSurvey = HtmlCache.getFromCache(resultsHtmlForUserWhoTookSurveyKey);
//            }
        } else {
            //resultsHtmlForUserWhoTookSurvey = "<font class='mediumfont'>Nobody who learned of this from "+userwhotooksurvey.getFirstname()+" "+userwhotooksurvey.getLastname()+" has answered... yet.  You could be the first!</font>";
            resultsHtmlForUserWhoTookSurvey = "";
        }

        //Set results friends tab text
        if (userwhotooksurvey!=null){
            resultsfriendstabtext = Str.truncateString(userwhotooksurvey.getNickname(), 15)+"'s Friends";
        }


        //Special Facebook activities
        if (Pagez.getUserSession().getIsfacebookui()){
            String resultsYourFriendsKey = "surveyresults.jsp-resultsYourFriendsKey-surveyid"+survey.getSurveyid()+"-facebookuid"+Pagez.getUserSession().getFacebookUser().getUid();
            String group = "PublicSurveyResults.java-surveyid-"+survey.getSurveyid();
            Object fromCache = DbcacheexpirableCache.get(resultsYourFriendsKey, group);
            if (fromCache!=null){
                try{resultsYourFriends = (String)fromCache;}catch(Exception ex){logger.error("", ex);}
            } else {
                //Load facebook users
                loadFacebookUsers();
                //Generate results
                resultsshowyourfriendstab = true;
                //FacebookApiWrapperHtmlui faw = new FacebookApiWrapperHtmlui(Pagez.getUserSession());
                ArrayList<FacebookUser> friends = Pagez.getUserSession().getFacebookFriends();
                if (friends!=null && friends.size()>0){
                    StringBuffer facebookquery = new StringBuffer();
                    facebookquery.append(" ( ");
                    for (Iterator it = friends.iterator(); it.hasNext(); ) {
                        FacebookUser facebookUser = (FacebookUser)it.next();
                        facebookquery.append("facebookuserid="+facebookUser.getUid());
                        if (it.hasNext()){
                            facebookquery.append(" OR ");
                        }
                    }
                    facebookquery.append(" ) ");
                    ArrayList<Integer> onlyincluderesponsesfromtheseuserids = new ArrayList<Integer>();
                    List fbusers = HibernateUtil.getSession().createQuery("from User WHERE "+facebookquery.toString()).list();
                    for (Iterator iterator = fbusers.iterator(); iterator.hasNext();) {
                        User fbuser = (User) iterator.next();
                        onlyincluderesponsesfromtheseuserids.add(fbuser.getUserid());
                    }
                    resultsYourFriends = SurveyResultsDisplay.getHtmlForResults(survey, null, 0, onlyincluderesponsesfromtheseuserids, null, true, false);
                } else {
                    resultsYourFriends = "<font class='mediumfont'>None of your friends have joined this one... yet.</font>";
                }
                //Update cache
                DbcacheexpirableCache.put(resultsYourFriendsKey, group, resultsYourFriends, DbcacheexpirableCache.expireSurveyInXHrs(survey, 3));
            }
//            if (HtmlCache.isStale(resultsYourFriendsKey, 6000)){
//                //Load facebook users
//                loadFacebookUsers();
//                //Generate results
//                resultsshowyourfriendstab = true;
//                //FacebookApiWrapperHtmlui faw = new FacebookApiWrapperHtmlui(Pagez.getUserSession());
//                ArrayList<FacebookUser> friends = Pagez.getUserSession().getFacebookFriends();
//                if (friends!=null && friends.size()>0){
//                    StringBuffer facebookquery = new StringBuffer();
//                    facebookquery.append(" ( ");
//                    for (Iterator it = friends.iterator(); it.hasNext(); ) {
//                        FacebookUser facebookUser = (FacebookUser)it.next();
//                        facebookquery.append("facebookuserid="+facebookUser.getUid());
//                        if (it.hasNext()){
//                            facebookquery.append(" OR ");
//                        }
//                    }
//                    facebookquery.append(" ) ");
//                    ArrayList<Integer> onlyincluderesponsesfromtheseuserids = new ArrayList<Integer>();
//                    List fbusers = HibernateUtil.getSession().createQuery("from User WHERE "+facebookquery.toString()).list();
//                    for (Iterator iterator = fbusers.iterator(); iterator.hasNext();) {
//                        User fbuser = (User) iterator.next();
//                        onlyincluderesponsesfromtheseuserids.add(fbuser.getUserid());
//                    }
//                    resultsYourFriends = SurveyResultsDisplay.getHtmlForResults(survey, null, 0, onlyincluderesponsesfromtheseuserids, null, true, false);
//                } else {
//                    resultsYourFriends = "<font class='mediumfont'>None of your friends have joined this one... yet.</font>";
//                }
//                //Update the cache
//                HtmlCache.updateCache(resultsYourFriendsKey, 6000, resultsYourFriends);
//            } else {
//                resultsYourFriends = HtmlCache.getFromCache(resultsYourFriendsKey);
//            }



        }




    }




    private void loadFacebookUsers(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        facebookuserswhotooksurvey = new ArrayList<PublicSurveyFacebookFriendListitem>();
        facebookuserswhodidnottakesurvey = new TreeMap<String, String>();
        if (survey!=null){
            //Go to facebook and get a list of the logged-in user's friends
            //FacebookApiWrapperHtmlui faw = new FacebookApiWrapperHtmlui(Pagez.getUserSession());
            ArrayList<FacebookUser> friends = Pagez.getUserSession().getFacebookFriends();
            if (friends.size()>0){
                //Build sql to pull up those users that are in the dneero db
                StringBuffer sql = new StringBuffer();
                sql.append(" ( ");
                for (Iterator<FacebookUser> iterator = friends.iterator(); iterator.hasNext();) {
                    FacebookUser facebookUser = iterator.next();
                    sql.append(" facebookuserid='"+UserInputSafe.clean(facebookUser.getUid())+"' ");
                    if(iterator.hasNext()){
                        sql.append(" OR ");
                    }
                }
                sql.append(" ) ");
                List users = HibernateUtil.getSession().createQuery("from User WHERE "+sql).setCacheable(true).list();
                //Now I have a list of all friends from facebook and a list of users who are friends from dNeero
                //I need to create lists of those who've joined (and therefore must be dNeero users) and a list of those who haven't (and may be dneero users)
                //Iterate all facebook users because they'll fall into one of the two camps
                for (Iterator<FacebookUser> iterator = friends.iterator(); iterator.hasNext();) {
                    FacebookUser facebookUser = iterator.next();
                    //See if this facebookUser is a dneero user and if they've taken the survey
                    boolean isdneerouser = false;
                    int userid = 0;
                    boolean hastakensurvey = false;
                    int responseid = 0;
                    for (Iterator iterator2 = users.iterator(); iterator2.hasNext();) {
                        User user = (User) iterator2.next();
                        if (user.getFacebookuserid()>0 && String.valueOf(user.getFacebookuserid()).equals(facebookUser.getUid())){
                            Blogger blogger = Blogger.get(user.getBloggerid());
                            for (Iterator<Response> iterator1 = blogger.getResponses().iterator(); iterator1.hasNext();) {
                                Response response = iterator1.next();
                                if (response.getSurveyid()==survey.getSurveyid()){
                                    hastakensurvey = true;
                                    responseid = response.getResponseid();
                                    break;
                                }
                            }
                            isdneerouser = true;
                            userid = user.getUserid();
                            break;
                        }
                    }
                    //If they've taken the survey
                    if (hastakensurvey){
                        PublicSurveyFacebookFriendListitem psffli = new PublicSurveyFacebookFriendListitem();
                        psffli.setFacebookUser(facebookUser);
                        psffli.setUserid(userid);
                        psffli.setResponseid(responseid);
                        facebookuserswhotooksurvey.add(psffli);
                    }
                    //Otherwise they've not taken the survey
                    if (!hastakensurvey){
                        facebookuserswhodidnottakesurvey.put(facebookUser.getFirst_name()+" "+facebookUser.getLast_name(), facebookUser.getUid());
                    }
                }
            }
        }
    }


    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey=survey;
    }

    public User getUserwhotooksurvey() {
        return userwhotooksurvey;
    }

    public void setUserwhotooksurvey(User userwhotooksurvey) {
        this.userwhotooksurvey=userwhotooksurvey;
    }

    public String getResultsHtml() {
        return resultsHtml;
    }

    public void setResultsHtml(String resultsHtml) {
        this.resultsHtml=resultsHtml;
    }

    public String getResultsHtmlForUserWhoTookSurvey() {
        return resultsHtmlForUserWhoTookSurvey;
    }

    public void setResultsHtmlForUserWhoTookSurvey(String resultsHtmlForUserWhoTookSurvey) {
        this.resultsHtmlForUserWhoTookSurvey=resultsHtmlForUserWhoTookSurvey;
    }

    public String getResultsYourFriends() {
        return resultsYourFriends;
    }

    public void setResultsYourFriends(String resultsYourFriends) {
        this.resultsYourFriends=resultsYourFriends;
    }

    public String getResultsfriendstabtext() {
        return resultsfriendstabtext;
    }

    public void setResultsfriendstabtext(String resultsfriendstabtext) {
        this.resultsfriendstabtext=resultsfriendstabtext;
    }

    public boolean getResultsshowyourfriendstab() {
        return resultsshowyourfriendstab;
    }

    public void setResultsshowyourfriendstab(boolean resultsshowyourfriendstab) {
        this.resultsshowyourfriendstab=resultsshowyourfriendstab;
    }

    public List<PublicSurveyFacebookFriendListitem> getFacebookuserswhotooksurvey() {
        return facebookuserswhotooksurvey;
    }

    public void setFacebookuserswhotooksurvey(List<PublicSurveyFacebookFriendListitem> facebookuserswhotooksurvey) {
        this.facebookuserswhotooksurvey=facebookuserswhotooksurvey;
    }

    public TreeMap<String, String> getFacebookuserswhodidnottakesurvey() {
        return facebookuserswhodidnottakesurvey;
    }

    public void setFacebookuserswhodidnottakesurvey(TreeMap<String, String> facebookuserswhodidnottakesurvey) {
        this.facebookuserswhodidnottakesurvey=facebookuserswhodidnottakesurvey;
    }

    public int getResultstabselectedindex() {
        return resultstabselectedindex;
    }

    public void setResultstabselectedindex(int resultstabselectedindex) {
        this.resultstabselectedindex=resultstabselectedindex;
    }

    public String getResultsUserquestionsHtml() {
        return resultsUserquestionsHtml;
    }

    public void setResultsUserquestionsHtml(String resultsUserquestionsHtml) {
        this.resultsUserquestionsHtml=resultsUserquestionsHtml;
    }
}
