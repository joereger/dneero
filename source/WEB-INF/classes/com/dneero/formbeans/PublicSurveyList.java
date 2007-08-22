package com.dneero.formbeans;

import com.dneero.util.*;
import com.dneero.dao.Survey;
import com.dneero.dao.Blogger;
import com.dneero.dao.Response;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.finders.FindSurveysForBlogger;
import com.dneero.facebook.FacebookApiWrapper;
import com.dneero.facebook.FacebookSurveyThatsBeenTaken;
import com.dneero.facebook.FacebookUser;
import com.dneero.helpers.UserInputSafe;
import org.apache.log4j.Logger;

import java.util.*;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class PublicSurveyList implements Serializable {

    private ArrayList<BloggerSurveyListItem> surveys;
    private ArrayList<FacebookSurveyThatsBeenTaken> facebookSurveyThatsBeenTakens;
    private String[] facebookfriendsselected;
    private TreeMap<String, String> facebookusersnotaddedapp = new TreeMap<String, String>();
    private List<PublicSurveyFacebookFriendListitem> facebookuserswhoaddedapp = new ArrayList<PublicSurveyFacebookFriendListitem>();

    public PublicSurveyList() {
        load();
    }

    public String beginView(){
        return "publicsurveylist";
    }

    private void load(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("instanciating PublicSurveyList");
        //If user is logged-in only show them their surveys
//        if (Jsf.getUserSession().getIsloggedin()){
//
//            BloggerSurveyList bsl = new BloggerSurveyList();
//            surveys = bsl.getSurveys();
//
//        //Otherwise, get all open surveys
//        } else {


            FindSurveysForBlogger fsfb = null;
            if (Jsf.getUserSession().getIsloggedin() && Jsf.getUserSession().getUser()!=null && Jsf.getUserSession().getUser().getBloggerid()>0){
                fsfb = new FindSurveysForBlogger(Blogger.get(Jsf.getUserSession().getUser().getBloggerid()));
            }


            surveys = new ArrayList<BloggerSurveyListItem>();
            List results = HibernateUtil.getSession().createQuery("from Survey where status='"+Survey.STATUS_OPEN+"' order by willingtopayperrespondent desc").list();
            for (Iterator iterator = results.iterator(); iterator.hasNext();) {
                Survey survey = (Survey) iterator.next();

                BloggerSurveyListItem bsli = new BloggerSurveyListItem();
                bsli.setSurveyid(survey.getSurveyid());
                bsli.setTitle(survey.getTitle());
                bsli.setDescription(survey.getDescription());
                bsli.setIscharityonly(survey.getIscharityonly());
                //logger.debug("survey: "+survey.getTitle()+" bsli.getIscharityonly()="+bsli.getIscharityonly());

                if (survey.getQuestions()!=null){
                    bsli.setNumberofquestions(String.valueOf(survey.getQuestions().size()));
                } else {
                    bsli.setNumberofquestions("0");
                }

                double maxearningNum = survey.getWillingtopayperrespondent()  +   ( (survey.getWillingtopaypercpm()*survey.getMaxdisplaysperblog())/1000 );
                bsli.setMaxearning("$"+ Str.formatForMoney(maxearningNum));

                int daysleft = DateDiff.dateDiff("day", Time.getCalFromDate(survey.getEnddate()), Calendar.getInstance());
                if (daysleft==0){
                    bsli.setDaysuntilend("Ends today!");
                } else if (daysleft==1){
                    bsli.setDaysuntilend("One day left!");
                } else {
                    bsli.setDaysuntilend(daysleft + " days left!");
                }

                //See if user has taken survey
                bsli.setLoggedinuserhasalreadytakensurvey(false);
                if (Jsf.getUserSession().getIsloggedin() && Jsf.getUserSession().getUser()!=null && Jsf.getUserSession().getUser().getBloggerid()>0){
                    Blogger blogger = Blogger.get(Jsf.getUserSession().getUser().getBloggerid());
                    for (Iterator<Response> iterator2 = blogger.getResponses().iterator(); iterator2.hasNext();) {
                        Response response = iterator2.next();
                        if (response.getSurveyid()==survey.getSurveyid()){
                            bsli.setLoggedinuserhasalreadytakensurvey(true);
                        }
                    }
                }

                //See if user is qualified
                logger.debug("about to set isloggedinuserqualified");
                if (!bsli.getLoggedinuserhasalreadytakensurvey()){
                    if (Jsf.getUserSession().getIsloggedin() && Jsf.getUserSession().getUser()!=null && Jsf.getUserSession().getUser().getBloggerid()>0){
                        //Iterate surveys this blogger qualifies for
                        boolean bloggerqualifies = false;
                        for (Iterator iter = fsfb.getSurveys().iterator(); iter.hasNext();) {
                            Survey tmpSurvey = (Survey) iter.next();
                            if (tmpSurvey.getSurveyid()==survey.getSurveyid()){
                                bloggerqualifies=true;
                            }
                        }
                        if (bloggerqualifies){
                            logger.debug("yes, qualified");
                            bsli.setIsbloggerqualifiedstring("You Qualify");
                            bsli.setIsloggedinuserqualified(true);
                        } else {
                            logger.debug("no, not qualified");
                            bsli.setIsbloggerqualifiedstring("You Don't Qualify");
                            bsli.setIsloggedinuserqualified(false);
                        }
                    } else {
                        logger.debug("unknown");
                        bsli.setIsbloggerqualifiedstring("");
                    }
                } else {
                    logger.debug("already taken");
                    bsli.setIsbloggerqualifiedstring("You've Already Taken It");
                }
                surveys.add(bsli);
            }

            //Facebook stuff
            if (Jsf.getUserSession().getIsfacebookui()){
                //Load which friends are on dNeero and which aren't
                loadFacebookUsers();
                //Get list of friend uids
                FacebookApiWrapper faw = new FacebookApiWrapper(Jsf.getUserSession());
                //Load surveys taken by friends
                TreeMap<Integer, FacebookSurveyThatsBeenTaken> surveys = faw.getSurveysFriendsHaveTaken();
                //Boil it down to an arraylist
                facebookSurveyThatsBeenTakens = new ArrayList<FacebookSurveyThatsBeenTaken>();
                Iterator keyValuePairs = surveys.entrySet().iterator();
                for (int i = 0; i < surveys.size(); i++){
                    Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
                    Integer surveyid = (Integer)mapentry.getKey();
                    FacebookSurveyThatsBeenTaken facebookSurveyThatsBeenTaken = (FacebookSurveyThatsBeenTaken)mapentry.getValue();
                    //Limit to 10 for now
                    if (i<10){
                        facebookSurveyThatsBeenTakens.add(facebookSurveyThatsBeenTaken);
                    }
                }
            }

        //}
    }


    public String tellFriends(){
        return tellFriendsOperation(facebookfriendsselected);
    }


    private String tellFriendsOperation(String[] friendstotell){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (friendstotell!=null && friendstotell.length>0){
            int numberinvited = 0;
            ArrayList<Integer> uids = new ArrayList<Integer>();
            for (int i = 0; i < friendstotell.length; i++) {
                String uid = friendstotell[i];
                if (Num.isinteger(uid)){
                    numberinvited = numberinvited + 1;
                    logger.debug("Facebookfriend to invite, uid="+uid);
                    if (numberinvited<=10){
                        uids.add(Integer.parseInt(uid));
                    }
                }
            }
            FacebookApiWrapper faw = new FacebookApiWrapper(Jsf.getUserSession());
            faw.inviteFriendsTodNeero(uids);
        }
        try{Jsf.getHttpServletResponse().sendRedirect("/surveylist.jsf"); return null;}catch(Exception ex){logger.debug(ex);}
        return "publicsurvey";
    }

    private void loadFacebookUsers(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        facebookusersnotaddedapp = new TreeMap<String, String>();
        facebookuserswhoaddedapp = new ArrayList<PublicSurveyFacebookFriendListitem>();
        //Go to facebook and get a list of the logged-in user's friends
        FacebookApiWrapper faw = new FacebookApiWrapper(Jsf.getUserSession());
        ArrayList<FacebookUser> friends = faw.getFriends();
        if (friends.size()>0){
            //Build sql to pull up those users that are in the dneero db
            StringBuffer sql = new StringBuffer();
            sql.append(" ( ");
            for (Iterator<FacebookUser> iterator = friends.iterator(); iterator.hasNext();) {
                FacebookUser facebookUser = iterator.next();
                sql.append(" facebookuserid='"+ UserInputSafe.clean(facebookUser.getUid())+"' ");
                if(iterator.hasNext()){
                    sql.append(" OR ");
                }
            }
            sql.append(" ) ");
            List users = HibernateUtil.getSession().createQuery("from User WHERE "+sql).setCacheable(true).list();
            //Now I have a list of all friends from facebook and a list of users who are friends from dneero
            //I need to create lists of those who've taken the survey (and therefore must be dneero users) and a list of those who haven't (and may be dneero users)
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
                    facebookuserswhoaddedapp.add(psffli);
                }
                //Otherwise they've not taken the survey
                if (!hastakensurvey){
                    facebookusersnotaddedapp.put(facebookUser.getFirst_name()+" "+facebookUser.getLast_name(), facebookUser.getUid());
                }
            }
            //@todo Now need to see who's not in either of these two... i.e. friends who don't have any users
        }
    }



    public ArrayList<BloggerSurveyListItem> getSurveys() {
        //logger.debug("getListitems");
        sort("title", true);
        return surveys;
    }

    public void setSurveys(ArrayList<BloggerSurveyListItem> surveys) {
        //logger.debug("setListitems");
        this.surveys = surveys;
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }

    protected void sort(final String column, final boolean ascending) {
        //logger.debug("sort called");
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                BloggerSurveyListItem survey1 = (BloggerSurveyListItem)o1;
                BloggerSurveyListItem survey2 = (BloggerSurveyListItem)o2;
                if (column == null) {
                    return 0;
                }
                if (column.equals("title")) {
                    return ascending ? survey1.getTitle().compareTo(survey2.getTitle()) : survey2.getTitle().compareTo(survey1.getTitle());
                } else {
                    return 0;
                }
            }
        };

        //sort and also set our model with the new sort, since using DataTable with
        //ListDataModel on front end
        if (surveys != null && !surveys.isEmpty()) {
            //logger.debug("sorting surveys and initializing ListDataModel");
            Collections.sort(surveys, comparator);
        }
    }


    public ArrayList<FacebookSurveyThatsBeenTaken> getFacebookSurveyThatsBeenTakens() {
        return facebookSurveyThatsBeenTakens;
    }

    public void setFacebookSurveyThatsBeenTakens(ArrayList<FacebookSurveyThatsBeenTaken> facebookSurveyThatsBeenTakens) {
        this.facebookSurveyThatsBeenTakens = facebookSurveyThatsBeenTakens;
    }

    public String[] getFacebookfriendsselected() {
        return facebookfriendsselected;
    }

    public void setFacebookfriendsselected(String[] facebookfriendsselected) {
        this.facebookfriendsselected = facebookfriendsselected;
    }

    public TreeMap<String, String> getFacebookusersnotaddedapp() {
        return facebookusersnotaddedapp;
    }

    public void setFacebookusersnotaddedapp(TreeMap<String, String> facebookusersnotaddedapp) {
        this.facebookusersnotaddedapp = facebookusersnotaddedapp;
    }

    public List<PublicSurveyFacebookFriendListitem> getFacebookuserswhoaddedapp() {
        return facebookuserswhoaddedapp;
    }

    public void setFacebookuserswhoaddedapp(List<PublicSurveyFacebookFriendListitem> facebookuserswhoaddedapp) {
        this.facebookuserswhoaddedapp = facebookuserswhoaddedapp;
    }
}
