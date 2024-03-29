package com.dneero.htmluibeans;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.hibernate.NumFromUniqueResult;

import com.dneero.util.Str;
import com.dneero.util.Time;
import com.dneero.sir.SocialInfluenceRatingPercentile;
import com.dneero.scheduledjobs.SystemStats;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.helpers.IsBloggerInPanel;
import com.dneero.helpers.FastGetUserStuff;

import java.util.*;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Nov 18, 2006
 * Time: 9:07:11 AM
 */
public class PublicProfile implements Serializable {

    private Blogger blogger;
    private User user;
    private List<PublicProfileListitem> listitems;
    private List<Panel> panels;
    private List<Panel> superpanels;
    private int panelid;
    private int superpanelid;
    private String msg;
    private int socialinfluenceratingpercentile;
    private String socialinfluenceratingforscreen;
    private String charityamtdonatedForscreen;
    private int convosjoined = 0;
    private int peoplereferred = 0;
    private ArrayList<Question> userquestions;
    private ArrayList<Survey> surveys;

    public PublicProfile(){

    }


    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());

        user = null;
        blogger = null;
        logger.debug("Pagez.getRequest().getParameter(\"userid\")="+Pagez.getRequest().getParameter("userid"));
        if (com.dneero.util.Num.isinteger(Pagez.getRequest().getParameter("userid"))){
            user = User.get(Integer.parseInt(Pagez.getRequest().getParameter("userid")));
            if (user!=null && user.getUserid()>0 && user.getBloggerid()>0){
                blogger = Blogger.get(user.getBloggerid());
            }
        }
        if (user==null || user.getUserid()==0 || blogger==null || blogger.getBloggerid()==0){
            if (com.dneero.util.Num.isinteger(Pagez.getRequest().getParameter("bloggerid"))){
                blogger = Blogger.get(Integer.parseInt(Pagez.getRequest().getParameter("bloggerid")));
                if (blogger!=null && blogger.getBloggerid()>0 && blogger.getUserid()>0){
                    user = User.get(blogger.getUserid());
                }
            }
        }

        if (user==null){
            logger.debug("return because user==null");
            return;    
        }

        charityamtdonatedForscreen = "$"+Str.formatForMoney(user.getCharityamtdonated());

        if (blogger!=null && blogger.getBloggerid()>0){
            convosjoined = NumFromUniqueResult.getInt("select count(*) from Response where bloggerid='"+blogger.getBloggerid()+"'");
        } else {
            convosjoined =  0;
        }
 
        peoplereferred = NumFromUniqueResult.getInt("select count(*) from User where referredbyuserid='"+user.getUserid()+"'");


        userquestions = new ArrayList<Question>();
        List<Question> questions = HibernateUtil.getSession().createCriteria(Question.class)
                                           .add(Restrictions.eq("userid", user.getUserid()))
                                           .add(Restrictions.eq("isuserquestion", true))
                                           .add(Restrictions.eq("issysadminrejected", false))
                                           .setCacheable(true)
                                           .list();
        if (questions!=null && questions.size()>0){
            for (Iterator<Question> questionIterator=questions.iterator(); questionIterator.hasNext();) {
                Question question=questionIterator.next();
                userquestions.add(question);
            }
        }



        surveys = FastGetUserStuff.getSurveys(user);



        listitems = new ArrayList<PublicProfileListitem>();
        if (blogger!=null && blogger.getResponses()!=null){
            List<Response> rsps = HibernateUtil.getSession().createCriteria(Response.class)
                                           .add(Restrictions.eq("bloggerid", blogger.getBloggerid()))
                                           .add(Restrictions.eq("issysadminrejected", false))
                                           .addOrder(Order.desc("responseid"))
                                           .setCacheable(true)
                                           .list();
            for (Iterator<Response> iterator = rsps.iterator(); iterator.hasNext();) {
                Response response1 = iterator.next();
                Survey survey = null;
                for (Iterator<Survey> svIt=surveys.iterator(); svIt.hasNext();) {
                    Survey s1=svIt.next();
                    if (s1.getSurveyid()==response1.getSurveyid()){
                        survey=s1;
                        break;
                    }
                }
                if (survey!=null){
                    Question question = new Question();
                    for (Iterator<Question> questionIterator=userquestions.iterator(); questionIterator.hasNext();) {
                        Question q1=questionIterator.next();
                        if (q1.getSurveyid()==response1.getSurveyid()){
                            question = q1;
                        }
                    }
                    PublicProfileListitem li = new PublicProfileListitem();
                    li.setSurvey(survey);
                    li.setResponse(response1);
                    li.setUserquestion(question);
                    listitems.add(li);
                }
            }
        }

        panels = new ArrayList<Panel>();
        if (blogger!=null && blogger.getPanelmemberships()!=null){
            for (Iterator<Panelmembership> iterator = blogger.getPanelmemberships().iterator(); iterator.hasNext();) {
                Panelmembership panelmembership = (Panelmembership)iterator.next();
                panels.add(Panel.get(panelmembership.getPanelid()));
            }
        }

        if (blogger!=null && blogger.getPanelmemberships()!=null){
            String emptyStr = "";
            superpanels = new ArrayList<Panel>();
            List sps = HibernateUtil.getSession().createQuery("from Panel where issystempanel=true order by panelid desc"+emptyStr).list();
            for (Iterator iterator=sps.iterator(); iterator.hasNext();) {
                Panel panel = (Panel)iterator.next();
                if (IsBloggerInPanel.isBloggerInPanel(panel, blogger)){
                    superpanels.add(panel);
                }
            }
        }


        if (blogger!=null && blogger.getBloggerid()>0){
            if (user.getSirrank()>0){
                socialinfluenceratingpercentile = SocialInfluenceRatingPercentile.getPercentileOfRanking(SystemStats.getTotalusers(), user.getSirrank());
                if (socialinfluenceratingpercentile<1){
                    socialinfluenceratingpercentile = 1;
                }
                if (socialinfluenceratingpercentile<=50){
                    socialinfluenceratingforscreen = "Top "+(socialinfluenceratingpercentile)+"%";
                } else {
                    socialinfluenceratingforscreen = "Bottom "+(100-socialinfluenceratingpercentile)+"%";
                }
            } else {
                socialinfluenceratingforscreen = "Not Yet Calculated";
            }
        }
    }

    public void addToPanel() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        int bloggersadded = 0;
        Panel panel;
        if (panelid>0){
            panel = Panel.get(panelid);
        } else {
            //Create a new panel and add them to it
            String newpanelname = "My Panel ("+ Time.dateformatdate(Calendar.getInstance())+")";
            //@todo - Don't allow duplicate panel names
            panel = new Panel();
            panel.setCreatedate(new Date());
            panel.setName(newpanelname);
            panel.setDescription("");
            panel.setIssystempanel(false);
            panel.setResearcherid(Pagez.getUserSession().getUser().getResearcherid());
            try{panel.save();}catch (Exception ex){logger.error("",ex);}
        }
        //Check to see if blogger is already in panel... if so, don't add
        if (!IsBloggerInPanel.isBloggerInPanel(panel, blogger)){
            Panelmembership pm = new Panelmembership();
            pm.setBloggerid(blogger.getBloggerid());
            pm.setPanelid(panelid);
            pm.setIssysadminrejected(false);
            pm.setIssysadminreviewed(true);
            try{pm.save();}catch(Exception ex){logger.error("",ex);}
            bloggersadded = bloggersadded + 1;
            logger.debug("creating panelmembership for bloggerid="+blogger.getBloggerid()+" in panelid="+panelid);
        }
        try{panel.refresh();}catch(Exception ex){logger.error("",ex);}
        if (bloggersadded>0){
            msg = "The Blogger was added to the panel called: "+panel.getName();
        } else {
            msg = "The Blogger was already in the panel called: "+panel.getName();
        }
    }

    public void addToSuperPanel() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        int bloggersadded = 0;
        Panel panel;
        if (superpanelid>0){
            panel = Panel.get(superpanelid);
        } else {
            //Create a new panel and add them to it
            String newpanelname = "SuperPanel ("+ Time.dateformatdate(Calendar.getInstance())+")";
            //@todo - Don't allow duplicate panel names
            panel = new Panel();
            panel.setCreatedate(new Date());
            panel.setName(newpanelname);
            panel.setDescription("");
            panel.setIssystempanel(false);
            panel.setResearcherid(Pagez.getUserSession().getUser().getResearcherid());
            try{panel.save();}catch (Exception ex){logger.error("",ex);}
        }
        //Check to see if blogger is already in panel... if so, don't add
        if (!IsBloggerInPanel.isBloggerInPanel(panel, blogger)){
            Panelmembership pm = new Panelmembership();
            pm.setBloggerid(blogger.getBloggerid());
            pm.setPanelid(panel.getPanelid());
            pm.setIssysadminrejected(false);
            pm.setIssysadminreviewed(true);
            try{pm.save();}catch(Exception ex){logger.error("",ex);}
            bloggersadded = bloggersadded + 1;
            logger.debug("creating panelmembership for bloggerid="+blogger.getBloggerid()+" in panelid="+panelid);
        }
        try{panel.refresh();}catch(Exception ex){logger.error("",ex);}
        if (bloggersadded>0){
            msg = "The Blogger was added to the SuperPanel called: "+panel.getName();
        } else {
            msg = "The Blogger was already in the SuperPanel called: "+panel.getName();
        }
    }

    public TreeMap<String, String> getPanelids(){
        TreeMap<String, String> out = new TreeMap<String, String>();
        List results = HibernateUtil.getSession().createQuery("from Panel where researcherid='"+Pagez.getUserSession().getUser().getResearcherid()+"'").list();
        for (Iterator iterator = results.iterator(); iterator.hasNext();) {
            Panel panel = (Panel) iterator.next();
            out.put(String.valueOf(panel.getPanelid()), Str.truncateString(panel.getName(), 40));
        }
        return out;
    }

    public TreeMap<String, String> getSuperPanelids(){
        TreeMap<String, String> out = new TreeMap<String, String>();
        String emptyStr = "";
        List results = HibernateUtil.getSession().createQuery("from Panel where issystempanel=true order by panelid desc"+emptyStr).list();
        for (Iterator iterator = results.iterator(); iterator.hasNext();) {
            Panel panel = (Panel) iterator.next();
            out.put(String.valueOf(panel.getPanelid()), Str.truncateString(panel.getName(), 40));
        }
        return out;
    }

    public Blogger getBlogger() {
        return blogger;
    }

    public void setBlogger(Blogger blogger) {
        this.blogger = blogger;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<PublicProfileListitem> getListitems() {
        return listitems;
    }

    public void setListitems(List<PublicProfileListitem> listitems) {
        this.listitems = listitems;
    }

    public List<Panel> getPanels() {
        return panels;
    }

    public void setPanels(List<Panel> panels) {
        this.panels = panels;
    }

    public int getPanelid() {
        return panelid;
    }

    public void setPanelid(int panelid) {
        this.panelid = panelid;
    }

    public int getSocialinfluenceratingpercentile() {
        return socialinfluenceratingpercentile;
    }

    public void setSocialinfluenceratingpercentile(int socialinfluenceratingpercentile) {
        this.socialinfluenceratingpercentile = socialinfluenceratingpercentile;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCharityamtdonatedForscreen() {
        return charityamtdonatedForscreen;
    }

    public void setCharityamtdonatedForscreen(String charityamtdonatedForscreen) {
        this.charityamtdonatedForscreen = charityamtdonatedForscreen;
    }

    public List<Panel> getSuperpanels() {
        return superpanels;
    }

    public void setSuperpanels(List<Panel> superpanels) {
        this.superpanels=superpanels;
    }

    public int getSuperpanelid() {
        return superpanelid;
    }

    public void setSuperpanelid(int superpanelid) {
        this.superpanelid=superpanelid;
    }

    public int getConvosjoined() {
        return convosjoined;
    }

    public void setConvosjoined(int convosjoined) {
        this.convosjoined=convosjoined;
    }


    public String getSocialinfluenceratingforscreen() {
        return socialinfluenceratingforscreen;
    }

    public void setSocialinfluenceratingforscreen(String socialinfluenceratingforscreen) {
        this.socialinfluenceratingforscreen=socialinfluenceratingforscreen;
    }

    public int getPeoplereferred() {
        return peoplereferred;
    }

    public void setPeoplereferred(int peoplereferred) {
        this.peoplereferred=peoplereferred;
    }

    public ArrayList<Question> getUserquestions() {
        return userquestions;
    }

    public void setUserquestions(ArrayList<Question> userquestions) {
        this.userquestions=userquestions;
    }

    public ArrayList<Survey> getSurveys() {
        return surveys;
    }

    public void setSurveys(ArrayList<Survey> surveys) {
        this.surveys=surveys;
    }


}
