package com.dneero.htmluibeans;

import org.apache.log4j.Logger;
import com.dneero.util.*;
import com.dneero.dao.Survey;
import com.dneero.dao.Twitask;
import com.dneero.money.SurveyMoneyStatus;
import com.dneero.money.TwitaskMoneyStatus;
import com.dneero.scheduledjobs.ResearcherRemainingBalanceOperations;
import com.dneero.ui.SocialBookmarkLinks;
import com.dneero.htmlui.Pagez;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherTwitaskDetailPostlaunch implements Serializable {


    private String title;
    private int status;
    private String startdate;
    private String enddate;
    private String maxpossiblespend = "0";
    private String initialcharge = "0";
    private Twitask twitask;



    public ResearcherTwitaskDetailPostlaunch(){

    }





    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("loadSurvey called");
        if (Num.isinteger(Pagez.getRequest().getParameter("twitaskid"))){
            twitask = Twitask.get((Integer.parseInt(Pagez.getRequest().getParameter("twitaskid"))));
        }
        if (twitask!=null){
            logger.debug("Found twitask in db: twitask.getTwitaskid()="+twitask.getTwitaskid()+" twitask.getQuestion()="+twitask.getQuestion());
            if (Pagez.getUserSession().getUser()!=null && twitask.canEdit(Pagez.getUserSession().getUser())){
                logger.debug("twitask.canEdit(Pagez.getUserSession().getUser())="+twitask.canEdit(Pagez.getUserSession().getUser()));
                title = twitask.getQuestion();
                status = twitask.getStatus();

                startdate = Time.dateformatcompactwithtime(Time.getCalFromDate(twitask.getStartdate()));

                TwitaskMoneyStatus sms = new TwitaskMoneyStatus(twitask);
                maxpossiblespend = "$"+Str.formatForMoney(sms.getMaxPossibleSpend());
                initialcharge = "$"+Str.formatForMoney(sms.getMaxPossibleSpend()*(ResearcherRemainingBalanceOperations.INCREMENTALPERCENTTOCHARGE/100));
            }
        }

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status=status;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate=startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate=enddate;
    }

    public String getMaxpossiblespend() {
        return maxpossiblespend;
    }

    public void setMaxpossiblespend(String maxpossiblespend) {
        this.maxpossiblespend=maxpossiblespend;
    }

    public String getInitialcharge() {
        return initialcharge;
    }

    public void setInitialcharge(String initialcharge) {
        this.initialcharge=initialcharge;
    }

    public Twitask getTwitask() {
        return twitask;
    }

    public void setTwitask(Twitask twitask) {
        this.twitask=twitask;
    }
}