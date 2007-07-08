package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Jsf;
import com.dneero.util.Str;
import com.dneero.util.Time;
import com.dneero.sir.SocialInfluenceRatingPercentile;
import com.dneero.scheduledjobs.SystemStats;

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
    private List<Blog> blogs;
    private List<Panel> panels;
    private int panelid;
    private String msg;
    private int socialinfluenceratingpercentile;
    private int socialinfluenceratingpercentile90days;
    private String charityamtdonatedForscreen;

    public PublicProfile(){
        load();
    }
    
    public String beginView(){
        return "publicprofile";
    }

    public void load(){
        Logger logger = Logger.getLogger(this.getClass().getName());

        user = null;
        blogger = null;
        if (com.dneero.util.Num.isinteger(Jsf.getRequestParam("userid"))){
            user = User.get(Integer.parseInt(Jsf.getRequestParam("userid")));
            if (user!=null && user.getUserid()>0 && user.getBloggerid()>0){
                blogger = Blogger.get(user.getBloggerid());
            }
        }
        if (user==null || user.getUserid()==0 || blogger==null || blogger.getBloggerid()==0){
            if (com.dneero.util.Num.isinteger(Jsf.getRequestParam("bloggerid"))){
                blogger = Blogger.get(Integer.parseInt(Jsf.getRequestParam("bloggerid")));
                if (blogger!=null && blogger.getBloggerid()>0 && blogger.getUserid()>0){
                    user = User.get(blogger.getUserid());
                }
            }
        }

        charityamtdonatedForscreen = "$"+Str.formatForMoney(user.getCharityamtdonated());

        listitems = new ArrayList<PublicProfileListitem>();
        if (blogger!=null && blogger.getResponses()!=null){
            for (Iterator<Response> iterator = blogger.getResponses().iterator(); iterator.hasNext();) {
                Response response1 = iterator.next();
                Survey survey = Survey.get(response1.getSurveyid());
                PublicProfileListitem li = new PublicProfileListitem();
                li.setSurvey(survey);
                li.setResponse(response1);
                listitems.add(li);
            }
        }
        blogs = new ArrayList<Blog>();
        if (blogger!=null && blogger.getBlogs()!=null){
            for (Iterator<Blog> iterator = blogger.getBlogs().iterator(); iterator.hasNext();) {
                Blog blog = iterator.next();
                blogs.add(blog);
            }
        }
        panels = new ArrayList<Panel>();
        if (blogger!=null && blogger.getPanelmemberships()!=null){
            for (Iterator<Panelmembership> iterator = blogger.getPanelmemberships().iterator(); iterator.hasNext();) {
                Panelmembership panelmembership = (Panelmembership)iterator.next();
                panels.add(Panel.get(panelmembership.getPanelid()));
            }
        }
        if (blogger!=null && blogger.getBloggerid()>0){
            socialinfluenceratingpercentile = SocialInfluenceRatingPercentile.getPercentileOfRanking(SystemStats.getTotalbloggers(), blogger.getSocialinfluenceratingranking());
            socialinfluenceratingpercentile90days = SocialInfluenceRatingPercentile.getPercentileOfRanking(SystemStats.getTotalbloggers(), blogger.getSocialinfluenceratingranking90days());
        } else {
            socialinfluenceratingpercentile = 0;
            socialinfluenceratingpercentile90days = 0;
        }
    }

    public String addToPanel(){
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
            panel.setResearcherid(Jsf.getUserSession().getUser().getResearcherid());
            try{panel.save();}catch (Exception ex){logger.error(ex);}
        }
        //Check to see if blogger is already in panel... if so, don't add
        if (!ResearcherFindBloggers.isBloggerInPanel(panel, blogger)){
            Panelmembership pm = new Panelmembership();
            pm.setBloggerid(blogger.getBloggerid());
            pm.setPanelid(panelid);
            try{pm.save();}catch(Exception ex){logger.error(ex);}
            bloggersadded = bloggersadded + 1;
            logger.debug("creating panelmembership for bloggerid="+blogger.getBloggerid()+" in panelid="+panelid);
        }
        try{panel.refresh();}catch(Exception ex){logger.error(ex);}
        if (bloggersadded>0){
            msg = "The Blogger was added to the panel called: "+panel.getName();
        } else {
            msg = "The Blogger was already in the panel called: "+panel.getName();
        }
        return "publicprofile";
    }

    public LinkedHashMap getPanelids(){
        LinkedHashMap out = new LinkedHashMap();
        List results = HibernateUtil.getSession().createQuery("from Panel where researcherid='"+Jsf.getUserSession().getUser().getResearcherid()+"'").list();
        for (Iterator iterator = results.iterator(); iterator.hasNext();) {
            Panel panel = (Panel) iterator.next();
            out.put(Str.truncateString(panel.getName(), 40), panel.getPanelid());
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


    public List<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
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

    public int getSocialinfluenceratingpercentile90days() {
        return socialinfluenceratingpercentile90days;
    }

    public void setSocialinfluenceratingpercentile90days(int socialinfluenceratingpercentile90days) {
        this.socialinfluenceratingpercentile90days = socialinfluenceratingpercentile90days;
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
}