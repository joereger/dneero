package com.dneero.htmluibeans;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.util.Num;
import com.dneero.util.Str;
import com.dneero.helpers.NicknameHelper;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 * User: Joe Reger Jr
 * Date: Feb 17, 2007
 * Time: 9:32:33 AM
 */
public class CustomercarePanelsAddpeople implements Serializable {

    private int panelid=0;
    private int surveyid=0;
    private int respondentfilterid=0;
    private int rankid=0;
    private int rankpercentofatleast=0;

    public CustomercarePanelsAddpeople(){

    }

    public void initBean(){
        if (Pagez.getRequest().getParameter("panelid")!=null && Num.isinteger(Pagez.getRequest().getParameter("panelid"))){
            panelid = Integer.parseInt(Pagez.getRequest().getParameter("panelid"));
        }
        if (Pagez.getRequest().getParameter("surveyid")!=null && Num.isinteger(Pagez.getRequest().getParameter("surveyid"))){
            surveyid = Integer.parseInt(Pagez.getRequest().getParameter("surveyid"));
        }
        if (Pagez.getRequest().getParameter("respondentfilterid")!=null && Num.isinteger(Pagez.getRequest().getParameter("respondentfilterid"))){
            respondentfilterid = Integer.parseInt(Pagez.getRequest().getParameter("respondentfilterid"));
        }
        if (Pagez.getRequest().getParameter("rankid")!=null && Num.isinteger(Pagez.getRequest().getParameter("rankid"))){
            rankid = Integer.parseInt(Pagez.getRequest().getParameter("rankid"));
        }
        if (Pagez.getRequest().getParameter("rankpercentofatleast")!=null && Num.isinteger(Pagez.getRequest().getParameter("rankpercentofatleast"))){
            rankpercentofatleast = Integer.parseInt(Pagez.getRequest().getParameter("rankpercentofatleast"));
        }
    }



    public void add() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());

    }

    public TreeMap<String, String> getSurveys(){
        TreeMap<String, String> out = new TreeMap<String, String>();
        //out.put("0", "0 - Undefined");
        //@todo allow to choose users across all surveys???
        List<Survey> surveyd = HibernateUtil.getSession().createCriteria(Survey.class)
                                           .addOrder(Order.desc("surveyid"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Survey> surveyIterator = surveyd.iterator(); surveyIterator.hasNext();) {
            Survey survey = surveyIterator.next();
            Researcher researcher = Researcher.get(survey.getResearcherid());
            User user = User.get(researcher.getUserid());
            out.put(String.valueOf(survey.getSurveyid()), Str.truncateString(NicknameHelper.getNameOrNickname(user), 20)+":"+Str.truncateString(survey.getTitle(), 80));
        }
        return out;
    }

    public TreeMap<String, String> getPanels(){
        TreeMap<String, String> out = new TreeMap<String, String>();
        //out.put("0", "0 - Undefined");
        List<Panel> panels = HibernateUtil.getSession().createCriteria(Panel.class)
                                           .add(Restrictions.eq("issystempanel", true))
                                           .addOrder(Order.desc("panelid"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Panel> panelIterator = panels.iterator(); panelIterator.hasNext();) {
            Panel panel = panelIterator.next();
            out.put(String.valueOf(panel.getPanelid()), Str.truncateString(panel.getName(), 70));
        }
        return out;
    }

    public TreeMap<String, String> getRanks(){
        TreeMap<String, String> out = new TreeMap<String, String>();
        //out.put("0", "0 - Undefined");
        List<Rank> ranks = HibernateUtil.getSession().createCriteria(Rank.class)
                                           .addOrder(Order.desc("rankid"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Rank> panelIterator = ranks.iterator(); panelIterator.hasNext();) {
            Rank rank = panelIterator.next();
            User user = User.get(rank.getUserid());
            out.put(String.valueOf(rank.getRankid()), Str.truncateString(NicknameHelper.getNameOrNickname(user), 20)+":"+Str.truncateString(rank.getName(), 70));
        }
        return out;
    }

    public TreeMap<String, String> getRankpercentofatleastOptions(){
        TreeMap<String, String> out = new TreeMap<String, String>();
        out.put(String.valueOf(0), "All People with Positive Ranking");
        out.put(String.valueOf(5), "Top 5% of Ranked People");
        out.put(String.valueOf(10), "Top 10% of Ranked People");
        out.put(String.valueOf(25), "Top 25% of Ranked People");
        out.put(String.valueOf(50), "Top 50% of Ranked People");
        out.put(String.valueOf(75), "Top 75% of Ranked People");
        out.put(String.valueOf(90), "Top 90% of Ranked People");
        return out;
    }

    public TreeMap<String, String> getRespondentfilters(){
        TreeMap<String, String> out = new TreeMap<String, String>();
        out.put("0", "Apply no Filter, Use All");
        List<Respondentfilter> respondentfilters = HibernateUtil.getSession().createCriteria(Respondentfilter.class)
                                           .addOrder(Order.desc("respondentfilterid"))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Respondentfilter> filterIterator = respondentfilters.iterator(); filterIterator.hasNext();) {
            Respondentfilter filter = filterIterator.next();
            User user = User.get(filter.getUserid());
            out.put(String.valueOf(filter.getRespondentfilterid()), Str.truncateString(NicknameHelper.getNameOrNickname(user), 20)+":"+Str.truncateString(filter.getName(), 70));
        }
        return out;
    }

    public int getPanelid() {
        return panelid;
    }

    public void setPanelid(int panelid) {
        this.panelid = panelid;
    }

    public int getSurveyid() {
        return surveyid;
    }

    public void setSurveyid(int surveyid) {
        this.surveyid = surveyid;
    }

    public int getRespondentfilterid() {
        return respondentfilterid;
    }

    public void setRespondentfilterid(int respondentfilterid) {
        this.respondentfilterid = respondentfilterid;
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
}