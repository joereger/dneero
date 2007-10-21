package com.dneero.formbeans;

import com.dneero.util.Jsf;
import com.dneero.util.Num;
import com.dneero.util.SortableList;
import com.dneero.util.Str;
import com.dneero.finders.SurveyCriteriaXML;
import com.dneero.finders.FindBloggersForSurvey;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.sir.SocialInfluenceRatingPercentile;
import com.dneero.scheduledjobs.SystemStats;

import java.util.*;
import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Feb 8, 2007
 * Time: 12:23:05 PM
 */
public class ResearcherFindBloggers implements Serializable {

    private List listitems;
    private int panelid;
    private String msg;

    private int agemin = 13;
    private int agemax = 100;
    private int blogquality = 0;
    private int blogquality90days = 0;
    private int minsocialinfluencepercentile = 100;
    private int minsocialinfluencepercentile90days = 100;
    private String[] gender;
    private String[] ethnicity;
    private String[] maritalstatus;
    private String[] income;
    private String[] educationlevel;
    private String[] state;
    private String[] city;
    private String[] profession;
    private String[] blogfocus;
    private String[] politics;


   

    public ResearcherFindBloggers(){

    }

    public String beginView(){
        load();
        return "researcherfindbloggers";
    }

    private void load(){
        if (Jsf.getRequestParam("panelid")!=null && Num.isinteger(Jsf.getRequestParam("panelid"))){
            panelid = Integer.parseInt(Jsf.getRequestParam("panelid"));
        }
        gender = SurveyCriteriaXML.convertToArray((TreeMap) Jsf.getManagedBean("genders"));
        ethnicity = SurveyCriteriaXML.convertToArray((LinkedHashMap)Jsf.getManagedBean("ethnicities"));
        maritalstatus = SurveyCriteriaXML.convertToArray((LinkedHashMap)Jsf.getManagedBean("maritalstatuses"));
        income = SurveyCriteriaXML.convertToArray((LinkedHashMap)Jsf.getManagedBean("incomes"));
        educationlevel = SurveyCriteriaXML.convertToArray((LinkedHashMap)Jsf.getManagedBean("educationlevels"));
        state = SurveyCriteriaXML.convertToArray((LinkedHashMap)Jsf.getManagedBean("states"));
        city = SurveyCriteriaXML.convertToArray((LinkedHashMap)Jsf.getManagedBean("cities"));
        profession = SurveyCriteriaXML.convertToArray((TreeMap)Jsf.getManagedBean("professions"));
        blogfocus = SurveyCriteriaXML.convertToArray((TreeMap)Jsf.getManagedBean("blogfocuses"));
        politics = SurveyCriteriaXML.convertToArray((LinkedHashMap)Jsf.getManagedBean("politics"));

        if (Jsf.getRequestParam("panelid")!=null && Num.isinteger(Jsf.getRequestParam("panelid"))){
            panelid = Integer.parseInt(Jsf.getRequestParam("panelid"));
        }
    }

 



    public String search(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        SurveyCriteriaXML surveyCriteriaXML = new SurveyCriteriaXML("");
        surveyCriteriaXML.setAgemin(agemin);
        surveyCriteriaXML.setAgemax(agemax);
        surveyCriteriaXML.setBlogquality(blogquality);
        surveyCriteriaXML.setBlogquality90days(blogquality90days);
        surveyCriteriaXML.setMinsocialinfluencepercentile(minsocialinfluencepercentile);
        surveyCriteriaXML.setMinsocialinfluencepercentile90days(minsocialinfluencepercentile90days);
        surveyCriteriaXML.setGender(gender);
        surveyCriteriaXML.setEthnicity(ethnicity);
        surveyCriteriaXML.setMaritalstatus(maritalstatus);
        surveyCriteriaXML.setIncome(income);
        surveyCriteriaXML.setEducationlevel(educationlevel);
        surveyCriteriaXML.setState(state);
        surveyCriteriaXML.setCity(city);
        surveyCriteriaXML.setProfession(profession);
        surveyCriteriaXML.setBlogfocus(blogfocus);
        surveyCriteriaXML.setPolitics(politics);

        FindBloggersForSurvey finder = new FindBloggersForSurvey(surveyCriteriaXML);
        ArrayList bloggers = (ArrayList)finder.getBloggers();
        listitems = new ArrayList();
        for (Iterator it = bloggers.iterator(); it.hasNext(); ) {
            Blogger blogger = (Blogger)it.next();
            int socialinfluenceratingpercentile = SocialInfluenceRatingPercentile.getPercentileOfRanking(SystemStats.getTotalbloggers(), blogger.getSocialinfluenceratingranking());
            ResearcherFindBloggersListitem li = new ResearcherFindBloggersListitem();
            li.setBlogger(blogger);
            li.setUser(User.get(blogger.getUserid()));
            li.setSocialinfluenceratingpercentile(socialinfluenceratingpercentile);
            listitems.add(li);
            logger.debug("added bloggerid: "+blogger.getBloggerid());
        }
        if (listitems.size()<=0){
            msg = "No bloggers were found using the specified criteria.  However, we're always growing and adding new bloggers so check back soon!";    
        }
        return "researcherfindbloggers";
    }

    public String addAllToPanel(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("addAllToPanel() called");
        logger.debug("agemin: "+agemin);
        logger.debug("agemax: "+agemax);
        logger.debug("panelid: "+panelid);
        SurveyCriteriaXML surveyCriteriaXML = new SurveyCriteriaXML("");
        surveyCriteriaXML.setAgemin(agemin);
        surveyCriteriaXML.setAgemax(agemax);
        surveyCriteriaXML.setBlogquality(blogquality);
        surveyCriteriaXML.setBlogquality90days(blogquality90days);
        surveyCriteriaXML.setMinsocialinfluencepercentile(minsocialinfluencepercentile);
        surveyCriteriaXML.setMinsocialinfluencepercentile90days(minsocialinfluencepercentile90days);
        surveyCriteriaXML.setGender(gender);
        surveyCriteriaXML.setEthnicity(ethnicity);
        surveyCriteriaXML.setMaritalstatus(maritalstatus);
        surveyCriteriaXML.setIncome(income);
        surveyCriteriaXML.setEducationlevel(educationlevel);
        surveyCriteriaXML.setState(state);
        surveyCriteriaXML.setCity(city);
        surveyCriteriaXML.setProfession(profession);
        surveyCriteriaXML.setBlogfocus(blogfocus);
        surveyCriteriaXML.setPolitics(politics);

        FindBloggersForSurvey finder = new FindBloggersForSurvey(surveyCriteriaXML);
        ArrayList bloggers = (ArrayList)finder.getBloggers();
        int bloggersadded = 0;
        int totalbloggers = bloggers.size();
        if (panelid>0){
            Panel panel = Panel.get(panelid);
            for (Iterator it = bloggers.iterator(); it.hasNext(); ) {
                Blogger blogger = (Blogger)it.next();
                //Check to see if blogger is already in panel... if so, don't add
                if (!isBloggerInPanel(panel, blogger)){
                    Panelmembership pm = new Panelmembership();
                    pm.setBloggerid(blogger.getBloggerid());
                    pm.setPanelid(panelid);
                    try{pm.save();}catch(Exception ex){logger.error("",ex);}
                    bloggersadded = bloggersadded + 1;
                    logger.debug("creating panelmembership for bloggerid="+blogger.getBloggerid()+" in panelid="+panelid);
                }
            }
            try{panel.refresh();}catch(Exception ex){logger.error("",ex);}
        }
        if (bloggersadded>0){
            msg = bloggersadded + " new bloggers added to the panel.";   
        } else {
            msg = "No new bloggers found here that weren't already in the panel.";
        }

        return "researcherfindbloggers";
    }
    
    public static boolean isBloggerInPanel(Panel panel, Blogger blogger){
        for (Iterator<Panelmembership> iterator = panel.getPanelmemberships().iterator(); iterator.hasNext();) {
            Panelmembership panelmembership = iterator.next();
            if (panelmembership.getBloggerid()==blogger.getBloggerid()){
                return true;
            }
        }
        return false;
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

    public String resetSearch(){
        load();
        listitems = null;
        return "researcherfindbloggers";
    }

    public List getListitems() {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("getListitems");
        sort("userlastname", true);
        return listitems;
    }

    public void setListitems(List listitems) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("setListitems");
        this.listitems = listitems;
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }

    protected void sort(final String column, final boolean ascending) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("sort called");
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                ResearcherFindBloggersListitem obj1 = (ResearcherFindBloggersListitem)o1;
                ResearcherFindBloggersListitem obj2 = (ResearcherFindBloggersListitem)o2;
                if (column == null) {
                    return 0;
                }
                if (obj1!=null && obj2!=null && column.equals("bloggerid")){
                    return ascending ? obj2.getBlogger().getBloggerid()-obj1.getBlogger().getBloggerid() : obj1.getBlogger().getBloggerid()-obj2.getBlogger().getBloggerid() ;
                } else if (obj1!=null && obj2!=null && column.equals("userlastname")){
                    return ascending ? obj1.getUser().getLastname().compareTo(obj2.getUser().getLastname()) : obj2.getUser().getLastname().compareTo(obj1.getUser().getLastname());
                } else {
                    return 0;
                }
            }
        };

        //sort and also set our model with the new sort, since using DataTable with
        //ListDataModel on front end
        if (listitems != null && !listitems.isEmpty()) {
            logger.debug("sorting listitems and initializing ListDataModel");
            Collections.sort(listitems, comparator);
        }
    }


    public int getPanelid() {
        return panelid;
    }

    public void setPanelid(int panelid) {
        this.panelid = panelid;
    }


    public int getAgemin() {
        return agemin;
    }

    public void setAgemin(int agemin) {
        this.agemin = agemin;
    }

    public int getAgemax() {
        return agemax;
    }

    public void setAgemax(int agemax) {
        this.agemax = agemax;
    }

    public int getBlogquality() {
        return blogquality;
    }

    public void setBlogquality(int blogquality) {
        this.blogquality = blogquality;
    }

    public int getBlogquality90days() {
        return blogquality90days;
    }

    public void setBlogquality90days(int blogquality90days) {
        this.blogquality90days = blogquality90days;
    }

    public String[] getGender() {
        return gender;
    }

    public void setGender(String[] gender) {
        this.gender = gender;
    }

    public String[] getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String[] ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String[] getMaritalstatus() {
        return maritalstatus;
    }

    public void setMaritalstatus(String[] maritalstatus) {
        this.maritalstatus = maritalstatus;
    }

    public String[] getIncome() {
        return income;
    }

    public void setIncome(String[] income) {
        this.income = income;
    }

    public String[] getEducationlevel() {
        return educationlevel;
    }

    public void setEducationlevel(String[] educationlevel) {
        this.educationlevel = educationlevel;
    }

    public String[] getState() {
        return state;
    }

    public void setState(String[] state) {
        this.state = state;
    }

    public String[] getCity() {
        return city;
    }

    public void setCity(String[] city) {
        this.city = city;
    }

    public String[] getProfession() {
        return profession;
    }

    public void setProfession(String[] profession) {
        this.profession = profession;
    }

    public String[] getBlogfocus() {
        return blogfocus;
    }

    public void setBlogfocus(String[] blogfocus) {
        this.blogfocus = blogfocus;
    }

    public String[] getPolitics() {
        return politics;
    }

    public void setPolitics(String[] politics) {
        this.politics = politics;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getMinsocialinfluencepercentile() {
        return minsocialinfluencepercentile;
    }

    public void setMinsocialinfluencepercentile(int minsocialinfluencepercentile) {
        this.minsocialinfluencepercentile = minsocialinfluencepercentile;
    }

    public int getMinsocialinfluencepercentile90days() {
        return minsocialinfluencepercentile90days;
    }

    public void setMinsocialinfluencepercentile90days(int minsocialinfluencepercentile90days) {
        this.minsocialinfluencepercentile90days = minsocialinfluencepercentile90days;
    }
}
