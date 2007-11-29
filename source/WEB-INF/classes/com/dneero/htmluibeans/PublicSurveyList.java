package com.dneero.htmluibeans;

import com.dneero.util.*;
import com.dneero.dao.Survey;
import com.dneero.dao.Blogger;
import com.dneero.dao.Response;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.finders.FindSurveysForBlogger;
import com.dneero.facebook.FacebookSurveyThatsBeenTaken;
import com.dneero.facebook.FacebookUser;
import com.dneero.facebook.FacebookApiWrapper;
import com.dneero.htmlui.Pagez;
import com.dneero.money.CurrentBalanceCalculator;
import org.apache.log4j.Logger;

import java.util.*;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class PublicSurveyList implements Serializable {

    private ArrayList<SurveyListItem> surveys;
    private ArrayList<FacebookSurveyThatsBeenTaken> facebookSurveyThatsBeenTakens;
    private String[] facebookfriendsselected;
    private TreeMap<String, String> facebookusersnotaddedapp = new TreeMap<String, String>();
    private List<PublicSurveyFacebookFriendListitem> facebookuserswhoaddedapp = new ArrayList<PublicSurveyFacebookFriendListitem>();
    private String rndstr;
    //private ArrayList<BloggerCompletedsurveysListitem> completedsurveys;
    private boolean facebookjustaddedapp = false;
    private String invitefriendsurl = "";
    private String currentbalance = "$0.00";
    private String pendingearnings = "$0.00";
    private double currentbalanceDbl = 0.0;
    private double pendingearningsDbl = 0.0;

    public PublicSurveyList() {

    }


    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("instanciating PublicSurveyList");
        //If user is logged-in only show them their surveys
//        if (Pagez.getUserSession().getIsloggedin()){
//
//
//        //Otherwise, get all open surveys
//        } else {

            rndstr = RandomString.randomAlphanumeric(5);

            FindSurveysForBlogger fsfb = null;
            if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getUser()!=null && Pagez.getUserSession().getUser().getBloggerid()>0){
                fsfb = new FindSurveysForBlogger(Blogger.get(Pagez.getUserSession().getUser().getBloggerid()));
            }


            surveys = new ArrayList<SurveyListItem>();
            List results = HibernateUtil.getSession().createQuery("from Survey where status='"+Survey.STATUS_OPEN+"' order by willingtopayperrespondent desc").list();
            for (Iterator iterator = results.iterator(); iterator.hasNext();) {
                Survey survey = (Survey) iterator.next();

                SurveyListItem bsli = new SurveyListItem();
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
                if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getUser()!=null && Pagez.getUserSession().getUser().getBloggerid()>0){
                    Blogger blogger = Blogger.get(Pagez.getUserSession().getUser().getBloggerid());
                    for (Iterator<Response> iterator2 = blogger.getResponses().iterator(); iterator2.hasNext();) {
                        Response response = iterator2.next();
                        if (response.getSurveyid()==survey.getSurveyid()){
                            bsli.setLoggedinuserhasalreadytakensurvey(true);
                        }
                    }
                }

                //See if user is qualified
                boolean bloggerqualifies = false;
                boolean bloggerqualifiesisunknown = false;
                logger.debug("about to set isloggedinuserqualified");
                if (!bsli.getLoggedinuserhasalreadytakensurvey()){
                    if (Pagez.getUserSession().getIsloggedin() && Pagez.getUserSession().getUser()!=null && Pagez.getUserSession().getUser().getBloggerid()>0){
                        //Iterate surveys this blogger qualifies for
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
                        bloggerqualifiesisunknown = true;
                        bsli.setIsbloggerqualifiedstring("");
                    }
                } else {
                    logger.debug("already taken");
                    bsli.setIsbloggerqualifiedstring("You've Already Taken It");
                }
                
                //Only add if user qualifies or if it's unknown if they qualify (i.e. they're not logged-in)
                if (bloggerqualifies || bloggerqualifiesisunknown){
                    surveys.add(bsli);
                }
            }

            //Facebook stuff
            if (Pagez.getRequest().getParameter("addedapp")!=null && Pagez.getRequest().getParameter("addedapp").equals("1")){
                facebookjustaddedapp = true;   
            }
            if (Pagez.getUserSession().getIsfacebookui()){
                //Load which friends are on dNeero and which aren't
                loadFacebookUsers();
                //Get list of friend uids
                FacebookApiWrapper faw = new FacebookApiWrapper(Pagez.getUserSession());
                //Load surveys taken by friends
                TreeMap<Integer, FacebookSurveyThatsBeenTaken> surveys = faw.getSurveysFriendsHaveTaken(Pagez.getUserSession().getFacebookFriends());
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
                //Load the account balance
                CurrentBalanceCalculator cbc = new CurrentBalanceCalculator(Pagez.getUserSession().getUser());
                currentbalanceDbl = cbc.getCurrentbalance();
                pendingearningsDbl = cbc.getPendingearnings();
                currentbalance = "$"+Str.formatForMoney(currentbalanceDbl);
                pendingearnings = "$"+Str.formatForMoney(pendingearningsDbl);

                //Invite friends link
                //invitefriendsurl = faw.inviteFriendsTodNeero();

            }

        //}
    }




    private void loadFacebookUsers(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("Start calculating which friends are on dNeero and which aren't");
        facebookusersnotaddedapp = new TreeMap<String, String>();
        facebookuserswhoaddedapp = new ArrayList<PublicSurveyFacebookFriendListitem>();
        //Go to facebook and get a list of the logged-in user's friends
        //FacebookApiWrapperHtmlui faw = new FacebookApiWrapperHtmlui(Pagez.getUserSession());
        ArrayList<FacebookUser> friends = Pagez.getUserSession().getFacebookFriends();
        if (friends.size()>0){
            //Build sql to pull up those users that are in the dneero db
//            StringBuffer sql = new StringBuffer();
//            sql.append(" ( ");
//            for (Iterator<FacebookUser> iterator = friends.iterator(); iterator.hasNext();) {
//                FacebookUser facebookUser = iterator.next();
//                sql.append(" facebookuserid='"+ UserInputSafe.clean(facebookUser.getUid())+"' ");
//                if(iterator.hasNext()){
//                    sql.append(" OR ");
//                }
//            }
//            sql.append(" ) ");
//            logger.debug("from User WHERE "+sql);
//            List users = HibernateUtil.getSession().createQuery("from User WHERE "+sql).setCacheable(true).list();
//            logger.debug("users.size()="+users.size());
            //Now I have a list of all friends from facebook and a list of users who are friends from dneero
            //I need to create lists of those who've taken the survey (and therefore must be dneero users) and a list of those who haven't (and may be dneero users)
            //Iterate all facebook users because they'll fall into one of the two camps
            for (Iterator<FacebookUser> iterator = friends.iterator(); iterator.hasNext();) {
                FacebookUser facebookUser = iterator.next();
                //See if this facebookUser is a dneero user and if they've taken the survey
//                logger.debug("facebookUser="+facebookUser.getFirst_name()+" "+facebookUser.getLast_name()+" uid="+facebookUser.getUid());
//                boolean isdneerouser = false;
//                int userid = 0;
//                int responseid = 0;
//                for (Iterator iterator2 = users.iterator(); iterator2.hasNext();) {
//                    User user = (User) iterator2.next();
//                    logger.debug("facebookUser="+facebookUser.getFirst_name()+" "+facebookUser.getLast_name()+" user.getUserid()="+user.getUserid());
//                    if (user.getFacebookuserid()>0 && Num.isinteger(facebookUser.getUid()) && user.getFacebookuserid()==Integer.parseInt(facebookUser.getUid())){
//                        logger.debug("facebookUser="+facebookUser.getFirst_name()+" "+facebookUser.getLast_name()+" appears to be a dNeero user");
//                        isdneerouser = true;
//                        userid = user.getUserid();
//                        break;
//                    }
//                }
                //If they've taken the survey
                boolean isdneerouser = false;
                if (facebookUser.getHas_added_app()){
                    isdneerouser = true;
                }
                if (isdneerouser){
                    PublicSurveyFacebookFriendListitem psffli = new PublicSurveyFacebookFriendListitem();
                    psffli.setFacebookUser(facebookUser);
                    psffli.setUserid(0);
                    psffli.setResponseid(0);
                    facebookuserswhoaddedapp.add(psffli);
                }
                //Otherwise they've not added the app
                if (!isdneerouser){
                    facebookusersnotaddedapp.put(facebookUser.getFirst_name()+" "+facebookUser.getLast_name(), facebookUser.getUid());
                }
            }
        }
    }



    public ArrayList<SurveyListItem> getSurveys() {
        return surveys;
    }

    public void setSurveys(ArrayList<SurveyListItem> surveys) {
        //logger.debug("setListitems");
        this.surveys = surveys;
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


    public String getRndstr() {
        return rndstr;
    }

    public void setRndstr(String rndstr) {
        this.rndstr = rndstr;
    }



    public boolean isFacebookjustaddedapp() {
        return facebookjustaddedapp;
    }

    public void setFacebookjustaddedapp(boolean facebookjustaddedapp) {
        this.facebookjustaddedapp = facebookjustaddedapp;
    }

    public String getInvitefriendsurl() {
        return invitefriendsurl;
    }

    public void setInvitefriendsurl(String invitefriendsurl) {
        this.invitefriendsurl=invitefriendsurl;
    }

    public String getCurrentbalance() {
        return currentbalance;
    }

    public void setCurrentbalance(String currentbalance) {
        this.currentbalance=currentbalance;
    }

    public String getPendingearnings() {
        return pendingearnings;
    }

    public void setPendingearnings(String pendingearnings) {
        this.pendingearnings=pendingearnings;
    }

    public double getCurrentbalanceDbl() {
        return currentbalanceDbl;
    }

    public void setCurrentbalanceDbl(double currentbalanceDbl) {
        this.currentbalanceDbl=currentbalanceDbl;
    }

    public double getPendingearningsDbl() {
        return pendingearningsDbl;
    }

    public void setPendingearningsDbl(double pendingearningsDbl) {
        this.pendingearningsDbl=pendingearningsDbl;
    }
}
