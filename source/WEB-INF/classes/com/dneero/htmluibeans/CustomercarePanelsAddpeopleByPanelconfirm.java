package com.dneero.htmluibeans;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.finders.SurveyCriteriaXML;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.util.Num;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Feb 17, 2007
 * Time: 9:32:33 AM
 */
public class CustomercarePanelsAddpeopleByPanelconfirm implements Serializable {

    private int panelid=0;
    private int panelidtoaddfrom=0;
    private int respondentfilterid;
    private Panel panel;
    private int numberofrespondents;
    private int numberofnewrespondents;
    private String bloggeridstoaddcommasep="";

    public CustomercarePanelsAddpeopleByPanelconfirm(){

    }

    public void initBean(){
        if (Pagez.getRequest().getParameter("panelid")!=null && Num.isinteger(Pagez.getRequest().getParameter("panelid"))){
            panelid = Integer.parseInt(Pagez.getRequest().getParameter("panelid"));
            panel = Panel.get(panelid);
        }
        if (Pagez.getRequest().getParameter("panelidtoaddfrom")!=null && Num.isinteger(Pagez.getRequest().getParameter("panelidtoaddfrom"))){
            panelidtoaddfrom = Integer.parseInt(Pagez.getRequest().getParameter("panelidtoaddfrom"));
        }
        if (Pagez.getRequest().getParameter("respondentfilterid")!=null && Num.isinteger(Pagez.getRequest().getParameter("respondentfilterid"))){
            respondentfilterid = Integer.parseInt(Pagez.getRequest().getParameter("respondentfilterid"));
        }
        findRespondents();
    }

    private void findRespondents(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (panelidtoaddfrom>0){

            Panel panel = Panel.get(panelid);

            SurveyCriteriaXML scXML = null;
            if (respondentfilterid>0){
                Respondentfilter filter = Respondentfilter.get(respondentfilterid);
                Researcher researcher = Researcher.get(panel.getResearcherid());
                User user = User.get(researcher.getUserid());
                Pl pl = Pl.get(user.getPlid());
                scXML = new SurveyCriteriaXML(filter.getCriteriaxml(), pl);
            }

            numberofnewrespondents = 0;
            bloggeridstoaddcommasep = "";

            //Go get all users from the panel
            List<Panelmembership> panelmemberships = HibernateUtil.getSession().createCriteria(Panelmembership.class)
                                               .add(Restrictions.eq("panelid", panelidtoaddfrom))
                                               .setCacheable(true)
                                               .list();
            numberofrespondents = panelmemberships.size();
            for (Iterator<Panelmembership> panelmembershipIterator=panelmemberships.iterator(); panelmembershipIterator.hasNext();) {
                Panelmembership panelmembership=panelmembershipIterator.next();
                Blogger blogger = Blogger.get(panelmembership.getBloggerid());
                User user = User.get(blogger.getUserid());
                boolean usershouldbeadded = true;
                if (scXML!=null){
                    logger.error("we have an scXML");
                    if (!scXML.isUserQualified(user)){
                        usershouldbeadded = false;
                        logger.debug("usershouldbeadded = false");
                    } else {
                        usershouldbeadded = true;
                        logger.debug("usershouldbeadded = true");
                    }
                }
                if (usershouldbeadded){
                    if (!isInPanel(panel, blogger.getBloggerid())){
                        numberofnewrespondents = numberofnewrespondents + 1;
                        if (numberofnewrespondents>1){
                            bloggeridstoaddcommasep = bloggeridstoaddcommasep + ",";
                        }
                        bloggeridstoaddcommasep = bloggeridstoaddcommasep + blogger.getBloggerid();
                    }
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

    public int getPanelidtoaddfrom() {
        return panelidtoaddfrom;
    }

    public void setPanelidtoaddfrom(int panelidtoaddfrom) {
        this.panelidtoaddfrom=panelidtoaddfrom;
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

    public int getNumberofrespondents() {
        return numberofrespondents;
    }

    public void setNumberofrespondents(int numberofrespondents) {
        this.numberofrespondents=numberofrespondents;
    }
}