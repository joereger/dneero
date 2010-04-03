package com.dneero.instantnotify;

import com.dneero.dao.Blogger;
import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.email.EmailTemplateProcessor;
import com.dneero.finders.FindBloggersForSurvey;
import com.dneero.systemprops.BaseUrl;
import com.dneero.threadpool.ThreadPool;
import com.dneero.ui.SurveyEnhancer;
import com.dneero.util.Str;
import com.dneero.xmpp.SendXMPPMessage;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: May 24, 2007
 * Time: 2:08:21 PM
 */
public class InstantNotifyOfNewSurvey implements Runnable {

    private static ThreadPool tp;
    private int surveyid;

    public InstantNotifyOfNewSurvey(int surveyid){
        this.surveyid = surveyid;
    }


    public void run(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("run() called");

        Survey survey = Survey.get(surveyid);

        //If it's not open, don't notify
        if (survey.getStatus()!=Survey.STATUS_OPEN){
            return;
        }

        //If it's Access Code only, don't notify
        if (survey.getIsaccesscodeonly()){
            return;
        }

        //Vars to hold our strings in the email
        StringBuffer listofsurveysHtml = new StringBuffer();
        StringBuffer listofsurveysTxt = new StringBuffer();
        //Create text for email
        SurveyEnhancer surveyEnhancer = new SurveyEnhancer(survey);
        String url = BaseUrl.get(false, survey.getPlid()) + "survey.jsp?surveyid="+survey.getSurveyid();
        //Html
        listofsurveysHtml.append("<br><br><a href=\""+url+"\">" + survey.getTitle() + "</a>");
        if (!survey.getDescription().equals("")){
            listofsurveysHtml.append("<br>"+survey.getDescription());
        }
        //Txt
        listofsurveysTxt.append("\n\n" + survey.getTitle());
        if (!survey.getDescription().equals("")){
            listofsurveysTxt.append("\n" + survey.getDescription());
        }
        listofsurveysTxt.append("\n" + url);
        //Instant messaging text
        String instantmessage = "dNeero.com: Earn on Conversation: \"" + Str.truncateString(survey.getDescription(), 50)+ "\" at Url: " + url;
        //Create the args array to hold the dynamic stuff
        String[] args = new String[10];
        args[0] = "";
        args[1] = listofsurveysHtml.toString();
        args[2] = listofsurveysTxt.toString();

        //Find bloggers who qualify
        FindBloggersForSurvey sbfs = new FindBloggersForSurvey(survey);
        List<Blogger> bloggers = sbfs.getBloggers();
        for (Iterator<Blogger> iterator = bloggers.iterator(); iterator.hasNext();) {
            Blogger blogger = iterator.next();
            User user = User.get(blogger.getUserid());
            if (user.getIsenabled()){
                if (user.getInstantnotifybyemailison()){
                    //Send email
                    EmailTemplateProcessor.sendMail("Instant Notification: New Conversation for "+ user.getNickname(), "bloggernotifyofnewsurveys", user, args);
                }
                if (user.getInstantnotifyxmppison() && !user.getInstantnotifyxmppusername().equals("")){
                    //Send XMPP
                    SendXMPPMessage sxmppmsg = new SendXMPPMessage(user.getInstantnotifyxmppusername(), instantmessage);
                    sxmppmsg.send();
                }
            }
        }
        logger.debug("done processing");
    }


    public void sendNotifications(){
        if (tp==null){
            tp = new ThreadPool(15);
        }
        tp.assign(this);
    }



}
