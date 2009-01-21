package com.dneero.htmluibeans;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.finders.FindResponses;
import com.dneero.finders.SurveyCriteriaXML;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.util.Num;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Feb 17, 2007
 * Time: 9:32:33 AM
 */
public class CustomercarePanelsAddpeopleByRankconfirm implements Serializable {

    private int panelid=0;
    private int rankid=0;
    private int rankpercentofatleast=0;
    private int respondentfilterid;
    private Panel panel;
    private int numberofnewrespondents;
    private String bloggeridstoaddcommasep="";

    public CustomercarePanelsAddpeopleByRankconfirm(){

    }

    public void initBean(){
        if (Pagez.getRequest().getParameter("panelid")!=null && Num.isinteger(Pagez.getRequest().getParameter("panelid"))){
            panelid = Integer.parseInt(Pagez.getRequest().getParameter("panelid"));
            panel = Panel.get(panelid);
        }
        if (Pagez.getRequest().getParameter("rankid")!=null && Num.isinteger(Pagez.getRequest().getParameter("rankid"))){
            rankid = Integer.parseInt(Pagez.getRequest().getParameter("rankid"));
        }
        if (Pagez.getRequest().getParameter("rankpercentofatleast")!=null && Num.isinteger(Pagez.getRequest().getParameter("rankpercentofatleast"))){
            rankpercentofatleast = Integer.parseInt(Pagez.getRequest().getParameter("rankpercentofatleast"));
        }
        if (Pagez.getRequest().getParameter("respondentfilterid")!=null && Num.isinteger(Pagez.getRequest().getParameter("respondentfilterid"))){
            respondentfilterid = Integer.parseInt(Pagez.getRequest().getParameter("respondentfilterid"));
        }
        findRespondents();
    }

    private void findRespondents(){
        if (rankid>0){
            Rank rank = Rank.get(rankid);
            if (!rank.canEdit(Pagez.getUserSession().getUser())){
                return;
            }
            ArrayList<Object[]> stats = (ArrayList<Object[]>) HibernateUtil.getSession().createQuery("select userid, sum(points) as summ, avg(normalizedpoints) from Rankuser where rankid='"+rank.getRankid()+"' group by userid").list();
            //Iterate through and get max/min points
            int maxpoints = 0;
            int minpoints = 0;
            int maxpositivepoints = 0;
            int minpositivepoints = 0;
            for (Iterator iterator = stats.iterator(); iterator.hasNext();) {
                Object[] res = (Object[])iterator.next();
                int userid = (Integer)res[0];
                long pointsLng = (Long)res[1];
                int points = Integer.parseInt(String.valueOf(pointsLng));
                Double avgnormDbl = (Double)res[2];
                User user = User.get(userid);
                Double avgnormalizedpointsDbl = Double.parseDouble(String.valueOf(avgnormDbl*100));
                if (points>maxpoints || (maxpoints==0 && points<0)){
                    maxpoints=points;
                }
                if (points<minpoints || (minpoints==0 && points>0)){
                    minpoints=points;
                }
                if (points>maxpositivepoints){
                    maxpositivepoints = points;
                }
                if (points<minpositivepoints || (minpositivepoints==0 && points>0)){
                    minpositivepoints = points;
                }
            }
            //Iterate through and pull out users
            numberofnewrespondents = 0;
            ArrayList<Integer> userids = new ArrayList<Integer>();
            for (Iterator iterator = stats.iterator(); iterator.hasNext();) {
                Object[] res = (Object[])iterator.next();
                int userid = (Integer)res[0];
                long pointsLng = (Long)res[1];
                double points = Double.parseDouble(String.valueOf(pointsLng));
                Double avgnormDbl = (Double)res[2];
                Double avgnormalizedpointsDbl = Double.parseDouble(String.valueOf(avgnormDbl*100));
                //Calculate the percentage in the rating they stand at
                double rangesize = maxpositivepoints - minpositivepoints;
                double pointsshifted = points - minpositivepoints;
                double percent = 100;
                if (rangesize>0){
                    percent = (pointsshifted/rangesize)*100;
                }
                //Setup a user obj
                User user = null;
                //Determine whether to add
                boolean shouldadd=true;
                //Check points positive/neg
                if (shouldadd){
                    if (points<=0){
                        shouldadd=false;
                    }
                }
                //Check points percentage
                if (shouldadd){
                    if (rankpercentofatleast>0 && percent<rankpercentofatleast){
                        shouldadd=false;
                    }
                }
                //Check demographic
                if (shouldadd){
                    user = User.get(userid);
                    if (respondentfilterid>0){
                        Respondentfilter filter = Respondentfilter.get(respondentfilterid);
                        SurveyCriteriaXML scXML = new SurveyCriteriaXML(filter.getCriteriaxml());
                        if (!scXML.isUserQualified(user)){
                            shouldadd=false;
                        }
                    }
                }
                //Check panel
                if (shouldadd){
                    if (isInPanel(panel, user.getBloggerid())){
                        shouldadd=false;
                    }
                }
                //Finally, do the add
                if (shouldadd){
                    numberofnewrespondents = numberofnewrespondents + 1;
                    if (numberofnewrespondents>1){
                        bloggeridstoaddcommasep = bloggeridstoaddcommasep + ",";
                    }
                    bloggeridstoaddcommasep = bloggeridstoaddcommasep + user.getBloggerid();
                }
            }

        }
    }

    private boolean isInPanel(Panel panel, int bloggerid){
        if (panel.canEdit(Pagez.getUserSession().getUser())){
            for (Iterator<Panelmembership> panelIterator = panel.getPanelmemberships().iterator(); panelIterator.hasNext();){
                Panelmembership panelmembership = panelIterator.next();
                if (panelmembership.getBloggerid()==bloggerid){
                    return true;
                }
            }
        }
        return false;
    }


    public void add() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        String[] bloggerids = bloggeridstoaddcommasep.split(",");
        Panel panel = Panel.get(panelid);
        for (int i = 0; i < bloggerids.length; i++) {
            String bloggeridStr = bloggerids[i];
            if (Num.isinteger(bloggeridStr)){
                int bloggerid = Integer.parseInt(bloggeridStr);
                if (!isInPanel(panel, bloggerid)){
                    Panelmembership panelmembership = new Panelmembership();
                    panelmembership.setBloggerid(bloggerid);
                    panelmembership.setPanelid(panelid);
                    panelmembership.setIssysadminrejected(false);
                    panelmembership.setIssysadminreviewed(false);
                    try{panelmembership.save();}catch(Exception ex){logger.error("", ex);}
                }
            }
        }
        try{panel.refresh();}catch(Exception ex){logger.error("", ex);}
    }

    public int getRankid() {
        return rankid;
    }

    public void setRankid(int rankid) {
        this.rankid = rankid;
    }

    public int getRankpercentofatleast() {
        return rankpercentofatleast;
    }

    public void setRankpercentofatleast(int rankpercentofatleast) {
        this.rankpercentofatleast = rankpercentofatleast;
    }

    public int getRespondentfilterid() {
        return respondentfilterid;
    }

    public void setRespondentfilterid(int respondentfilterid) {
        this.respondentfilterid = respondentfilterid;
    }

    public String getBloggeridstoaddcommasep() {
        return bloggeridstoaddcommasep;
    }

    public void setBloggeridstoaddcommasep(String bloggeridstoaddcommasep) {
        this.bloggeridstoaddcommasep = bloggeridstoaddcommasep;
    }

    public int getNumberofnewrespondents() {
        return numberofnewrespondents;
    }

    public void setNumberofnewrespondents(int numberofnewrespondents) {
        this.numberofnewrespondents = numberofnewrespondents;
    }

    public Panel getPanel() {
        return panel;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }

    public int getPanelid() {
        return panelid;
    }

    public void setPanelid(int panelid) {
        this.panelid = panelid;
    }
}