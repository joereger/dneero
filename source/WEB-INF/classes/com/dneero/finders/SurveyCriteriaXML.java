package com.dneero.finders;

import com.dneero.constants.*;
import com.dneero.dao.*;
import com.dneero.util.Num;
import com.dneero.util.Util;
import com.dneero.util.Time;
import com.dneero.util.DateDiff;
import com.dneero.sir.SocialInfluenceRatingPercentile;
import com.dneero.scheduledjobs.SystemStats;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import java.io.ByteArrayOutputStream;
import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Sep 5, 2006
 * Time: 10:31:17 AM
 *
 * Abstraction of the XML workings of the survey criteria xml field
 *
 */
public class SurveyCriteriaXML {

    private int agemin = 13;
    private int agemax = 100;
    private int blogquality = 0;
    private int blogquality90days = 0;
    private int minsocialinfluencepercentile = 100;
    private int minsocialinfluencepercentile90days = 100;
    private int dayssincelastsurvey = 0;
    private int totalsurveystakenatleast = 0;
    private int totalsurveystakenatmost = 100000;
    private String[] gender;
    private String[] ethnicity;
    private String[] maritalstatus;
    private String[] income;
    private String[] educationlevel;
    private String[] state;
    private String[] city;
    private String[] country;
    private String[] profession;
    private String[] blogfocus;
    private String[] politics;
    private String[] dneerousagemethods;
    private String[] panelids;
    private String[] superpanelids;

    private Document doc;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public SurveyCriteriaXML(String criteriaxml){

        logger.debug("SurveyCriteriaXML instanciated");

        preSelectAll();

        if (criteriaxml!=null && !criteriaxml.equals("")){
            SAXBuilder builder = new SAXBuilder();
            try{
                doc = builder.build(new java.io.ByteArrayInputStream(criteriaxml.getBytes()));
            } catch (Exception ex){
                logger.error("",ex);
            }
        }
        nullDocCheck();
        if (Num.isinteger(loadValueOfStringFromXML("agemin"))){
            agemin = Integer.parseInt(loadValueOfStringFromXML("agemin"));
        }
        if (Num.isinteger(loadValueOfStringFromXML("agemax"))){
            agemax = Integer.parseInt(loadValueOfStringFromXML("agemax"));
        }
        if (Num.isinteger(loadValueOfStringFromXML("blogquality"))){
            blogquality = Integer.parseInt(loadValueOfStringFromXML("blogquality"));
        }
        if (Num.isinteger(loadValueOfStringFromXML("blogquality90days"))){
            blogquality90days = Integer.parseInt(loadValueOfStringFromXML("blogquality90days"));
        }
        if (Num.isinteger(loadValueOfStringFromXML("minsocialinfluencepercentile"))){
            minsocialinfluencepercentile = Integer.parseInt(loadValueOfStringFromXML("minsocialinfluencepercentile"));
        }
        if (Num.isinteger(loadValueOfStringFromXML("minsocialinfluencepercentile90days"))){
            minsocialinfluencepercentile90days = Integer.parseInt(loadValueOfStringFromXML("minsocialinfluencepercentile90days"));
        }
        if (Num.isinteger(loadValueOfStringFromXML("dayssincelastsurvey"))){
            dayssincelastsurvey = Integer.parseInt(loadValueOfStringFromXML("dayssincelastsurvey"));
        }
        if (Num.isinteger(loadValueOfStringFromXML("totalsurveystakenatleast"))){
            totalsurveystakenatleast = Integer.parseInt(loadValueOfStringFromXML("totalsurveystakenatleast"));
        }
        if (Num.isinteger(loadValueOfStringFromXML("totalsurveystakenatmost"))){
            totalsurveystakenatmost = Integer.parseInt(loadValueOfStringFromXML("totalsurveystakenatmost"));
        }
        String[] tmpArray;
        tmpArray = loadValueOfArrayFromXML("gender");
        if (tmpArray!=null){ gender = tmpArray; }
        tmpArray = loadValueOfArrayFromXML("ethnicity");
        if (tmpArray!=null){ ethnicity = tmpArray; }
        tmpArray = loadValueOfArrayFromXML("maritalstatus");
        if (tmpArray!=null){ maritalstatus = tmpArray; }
        tmpArray = loadValueOfArrayFromXML("income");
        if (tmpArray!=null){ income = tmpArray; }
        tmpArray = loadValueOfArrayFromXML("educationlevel");
        if (tmpArray!=null){ educationlevel = tmpArray; }
        tmpArray = loadValueOfArrayFromXML("state");
        if (tmpArray!=null){ state = tmpArray; }
        tmpArray = loadValueOfArrayFromXML("city");
        if (tmpArray!=null){ city = tmpArray; }
        tmpArray = loadValueOfArrayFromXML("country");
        if (tmpArray!=null){ country = tmpArray; }
        tmpArray = loadValueOfArrayFromXML("profession");
        if (tmpArray!=null){ profession = tmpArray; }
        tmpArray = loadValueOfArrayFromXML("blogfocus");
        if (tmpArray!=null){ blogfocus = tmpArray; }
        tmpArray = loadValueOfArrayFromXML("politics");
        if (tmpArray!=null){ politics = tmpArray; }
        tmpArray = loadValueOfArrayFromXML("dneerousagemethods");
        if (tmpArray!=null){ dneerousagemethods = tmpArray; }
        tmpArray = loadValueOfArrayFromXML("panelids");
        if (tmpArray!=null){ panelids = tmpArray; }
        tmpArray = loadValueOfArrayFromXML("superpanelids");
        if (tmpArray!=null){ superpanelids = tmpArray; }
        
    }

    //Determine whether a particular user qualifies for this criteria
    public boolean isUserQualified(User user){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (user==null){
            return false;
        }
        if (user.getBloggerid()==0){
            return false;
        }
        Blogger blogger = Blogger.get(user.getBloggerid());
        if (blogger==null || blogger.getBloggerid()==0){
            return false;
        }
        if (blogger!=null){
            boolean surveyfitsblogger = true;
            if (surveyfitsblogger && !Util.arrayContains(gender, blogger.getGender())){
                surveyfitsblogger = false;
                logger.debug("does not qualify because of gender.");
            }
            if (surveyfitsblogger && !Util.arrayContains(ethnicity, blogger.getEthnicity())){
                surveyfitsblogger = false;
                logger.debug("does not qualify because of ethnicity.");
            }
            if (surveyfitsblogger && !Util.arrayContains(maritalstatus, blogger.getMaritalstatus())){
                surveyfitsblogger = false;
                logger.debug("does not qualify because of maritalstatus.");
            }
            if (surveyfitsblogger && !Util.arrayContains(income, blogger.getIncomerange())){
                surveyfitsblogger = false;
                logger.debug("does not qualify because of incomerange.");
            }
            if (surveyfitsblogger && !Util.arrayContains(educationlevel, blogger.getEducationlevel())){
                surveyfitsblogger = false;
                logger.debug("does not qualify because of educationlevel.");
            }
            if (surveyfitsblogger && !Util.arrayContains(state, blogger.getState())){
                surveyfitsblogger = false;
                logger.debug("does not qualify because of state.");
            }
            if (surveyfitsblogger && !Util.arrayContains(city, blogger.getCity())){
                surveyfitsblogger = false;
                logger.debug("does not qualify because of city.");
            }
            if (surveyfitsblogger && !Util.arrayContains(country, blogger.getCountry())){
                surveyfitsblogger = false;
                logger.debug("does not qualify because of country.");
            }
            if (surveyfitsblogger && !Util.arrayContains(profession, blogger.getProfession())){
                surveyfitsblogger = false;
                logger.debug("does not qualify because of profession.");
            }
            if (surveyfitsblogger && !Util.arrayContains(politics, blogger.getPolitics())){
                surveyfitsblogger = false;
                logger.debug("does not qualify because of politics.");
            }
            //Panel
            if (surveyfitsblogger && panelids!=null && panelids.length>0){
                boolean isuserinpanel = false;
                for (Iterator<Panelmembership> iterator = blogger.getPanelmemberships().iterator(); iterator.hasNext();) {
                    Panelmembership panelmembership = iterator.next();
                    if (Util.arrayContains(panelids, String.valueOf(panelmembership.getPanelid()))){
                        isuserinpanel = true;
                        break;
                    }
                }
                if (!isuserinpanel){
                    surveyfitsblogger = false;
                    logger.debug("does not qualify because of panelid.");
                }
            }
            //SuperPanel
            if (surveyfitsblogger && superpanelids!=null && superpanelids.length>0){
                boolean isuserinpanel = false;
                for (Iterator<Panelmembership> iterator = blogger.getPanelmemberships().iterator(); iterator.hasNext();) {
                    Panelmembership panelmembership = iterator.next();
                    if (Util.arrayContains(superpanelids, String.valueOf(panelmembership.getPanelid()))){
                        isuserinpanel = true;
                        break;
                    }
                }
                if (!isuserinpanel){
                    surveyfitsblogger = false;
                    logger.debug("does not qualify because of superpanelid.");
                }
            }
            //Now check the age requirements
            if (surveyfitsblogger && blogger.getBirthdate().before(   Time.subtractYear(Calendar.getInstance(), agemax).getTime()    )){
                surveyfitsblogger = false;
                logger.debug("does not qualify because birthdate is before.");
            }
            if (surveyfitsblogger && blogger.getBirthdate().after(   Time.subtractYear(Calendar.getInstance(), agemin).getTime()    )){
                surveyfitsblogger = false;
                logger.debug("does not qualify because birthdate is after.");
            }
            //Quality
            if (surveyfitsblogger && blogger.getQuality()<blogquality){
                surveyfitsblogger = false;
                logger.debug("does not qualify because of blog quality.");
            }
            //Quality 90 days
            if (surveyfitsblogger && blogger.getQuality90days()<blogquality90days){
                surveyfitsblogger = false;
                logger.debug("does not qualify because of blog quality 90 days.");
            }
            //Social Influence Rating
            if (surveyfitsblogger){
                int maxranking = SocialInfluenceRatingPercentile.getRankingOfGivenPercentile(SystemStats.getTotalbloggers(), minsocialinfluencepercentile);
                if (blogger.getSocialinfluenceratingranking()>maxranking){
                    surveyfitsblogger = false;
                    logger.debug("does not qualify because of socialinfluenceranking.  maxranking="+maxranking+" blogger.getSocialinfluenceratingranking()="+blogger.getSocialinfluenceratingranking());
                }
            }
            //Social Influence Rating 90 days
            if (surveyfitsblogger){
                int maxranking90days = SocialInfluenceRatingPercentile.getRankingOfGivenPercentile(SystemStats.getTotalbloggers(), minsocialinfluencepercentile90days);
                if (blogger.getSocialinfluenceratingranking90days()>maxranking90days){
                    surveyfitsblogger = false;
                    logger.debug("does not qualify because of socialinfluenceranking90days.  maxranking90days="+maxranking90days+" blogger.getSocialinfluenceratingranking90days()="+blogger.getSocialinfluenceratingranking90days());
                }
            }
            //dneerousagemethod qualification
            if (user.getFacebookuserid()>0){
                //This is a facebook user
                if(!Util.arrayContains(dneerousagemethods, Dneerousagemethods.FACEBOOKAPPUSERS)){
                    surveyfitsblogger = false;
                    logger.debug("does not qualify because of dneerousagemethod... survey's not for facebook app users");
                }
            } else {
                //This is a dNeero.com user
                if(!Util.arrayContains(dneerousagemethods, Dneerousagemethods.DNEERODOTCOMUSERS)){
                    surveyfitsblogger = false;
                    logger.debug("does not qualify because of dneerousagemethod... survey's not for dneero.com users");
                }
            }
            //Venue focus
            if (surveyfitsblogger){
                //Only consider focus for non-facebook users
                if (user.getFacebookuserid()<=0){
                    //Only do this check if not all of the focuses are selected
                    if (!areAllBlogfocusesSelected()){
                        //@todo eventually will want to remove this if/then loop because enough people will have logged in and setup their venues
                        if (blogger.getVenues()!=null && blogger.getVenues().size()>0){
                            boolean fulfillsfocusreqs = false;
                            for (Iterator<Venue> iterator1=blogger.getVenues().iterator(); iterator1.hasNext();) {
                                Venue venue=iterator1.next();
                                //Does at least one blog from this user fall within the
                                if (Util.arrayContains(getBlogfocus(), venue.getFocus())){
                                    fulfillsfocusreqs = true;
                                    break;
                                }
                            }
                            if (!fulfillsfocusreqs){
                                surveyfitsblogger = false;
                                logger.debug("does not qualify because of focus.");
                            }
                        }
                    }
                }
            }
            //This next stuff is very database-heavy
            boolean needtodoexpensiveresponsecalculations = false;
            if (dayssincelastsurvey>0){
                needtodoexpensiveresponsecalculations = true;
            }
            if (totalsurveystakenatleast>0){
                needtodoexpensiveresponsecalculations = true;
            }
            if (totalsurveystakenatmost!=10000){
                needtodoexpensiveresponsecalculations = true;
            }
            if (needtodoexpensiveresponsecalculations){
                //Iterate all responses to collect some data for next few qualifications
                //@todo optimize this by storing mostrecentresponsedate and surveystaken in the user or blogger table at time of convo join
                Response mostrecentresponse = null;
                int surveystaken = 0;
                for (Iterator<Response> iterator1 = blogger.getResponses().iterator(); iterator1.hasNext();) {
                    Response response = iterator1.next();
                    //Fill most recent response
                    if (mostrecentresponse==null || mostrecentresponse.getResponsedate().before(response.getResponsedate())){
                        mostrecentresponse = response;
                    }
                    //Count surveys taken
                    surveystaken = surveystaken + 1;
                }
                //Calculate dayssincelastsurvey
                int dayssincelastsurvey = Integer.MAX_VALUE;
                if (mostrecentresponse!=null){
                    dayssincelastsurvey = DateDiff.dateDiff("day", Calendar.getInstance(), Time.getCalFromDate(mostrecentresponse.getResponsedate()));
                    logger.debug("bloggerid="+blogger.getBloggerid()+" dayssincelastsurvey="+dayssincelastsurvey);
                }
                //DaysSinceLastSurvey
                if (dayssincelastsurvey>0 && dayssincelastsurvey<this.dayssincelastsurvey){
                    surveyfitsblogger = false;
                    logger.debug("does not qualify because of dayssincelastsurvey");
                }
                //Total surveys taken of at least
                if (totalsurveystakenatleast>0 && surveystaken<totalsurveystakenatleast){
                    surveyfitsblogger = false;
                    logger.debug("does not qualify because of totalsurveystakenatleast");
                }
                //Total surveys taken of at most
                if (surveystaken>totalsurveystakenatmost){
                    surveyfitsblogger = false;
                    logger.debug("does not qualify because of totalsurveystakenatmost");
                }
            }
            //If it's still in, return true
            if (surveyfitsblogger){
                return true;
            }
        }
        return false;
    }

    public boolean areAllBlogfocusesSelected(){
        return areAllOfSomethingSelected(convertToArray(Blogfocuses.get()), blogfocus);
    }

    private boolean areAllOfSomethingSelected(String[] allvalues, String[] selectedvalues){
        if (selectedvalues==null && allvalues!=null){
            return false;
        }
        if (selectedvalues==null && allvalues==null){
            return true;
        }
        if (selectedvalues!=null && allvalues!=null){
            for (int i=0; i<allvalues.length; i++) {
                String allvalue=allvalues[i];
                if (!Util.arrayContains(selectedvalues, allvalue)){
                    return false;
                }
            }
        }
        return true;
    }

    private void preSelectAll(){
        gender = convertToArray(Genders.get());
        ethnicity = convertToArray(Ethnicities.get());
        maritalstatus = convertToArray(Maritalstatuses.get());
        income = convertToArray(Incomes.get());
        educationlevel = convertToArray(Educationlevels.get());
        state = convertToArray(States.get());
        city = convertToArray(Cities.get());
        country = convertToArray(Countries.get());
        profession = convertToArray(Professions.get());
        blogfocus = convertToArray(Blogfocuses.get());
        politics = convertToArray(Politics.get());
        dneerousagemethods = convertToArray(Dneerousagemethods.get());
    }

    public static String[] convertToArray(TreeMap tmap){
        String[] out = new String[0];
        if (tmap!=null){
            out = new String[tmap.size()];
            Iterator keyValuePairs = tmap.entrySet().iterator();
            for (int i = 0; i < tmap.size(); i++){
                Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
                Object key = mapentry.getKey();
                String value = (String)mapentry.getValue();
                out[i] = value;
            }
        }
        return out;
    }

    public static String[] convertToArray(TreeSet<String> tmap){
        String[] out = new String[0];
        if (tmap!=null){
            out = new String[tmap.size()];
            int i = 0;
            for (Iterator<String> iterator=tmap.iterator(); iterator.hasNext();) {
                String s=iterator.next();
                out[i] = s;
                i = i + 1;
            }

        }
        return out;
    }

    public static String[] convertToArray(LinkedHashMap tmap){
        String[] out = new String[0];
        if (tmap!=null){
            out = new String[tmap.size()];
            Iterator keyValuePairs = tmap.entrySet().iterator();
            for (int i = 0; i < tmap.size(); i++){
                Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
                Object key = mapentry.getKey();
                String value = (String)mapentry.getValue();
                out[i] = value;
            }
        }
        return out;
    }

    private void nullDocCheck(){
        if (doc==null){
            //Start a new doc
            doc = new Document();
            Element crit = new Element("surveycriteria");
            doc.addContent(crit);
        }
        if (!doc.getRootElement().getName().equals("surveycriteria")){
            logger.debug("Root element was not surveycriteria, overwriting document.");
            doc = new Document();
            Element crit = new Element("surveycriteria");
            doc.addContent(crit);
        }
    }

    public String getSurveyCriteriaAsString(){
        nullDocCheck();
        setValueOfSimpleStringNode("agemin", String.valueOf(agemin));
        setValueOfSimpleStringNode("agemax", String.valueOf(agemax));
        setValueOfSimpleStringNode("blogquality", String.valueOf(blogquality));
        setValueOfSimpleStringNode("blogquality90days", String.valueOf(blogquality90days));
        setValueOfSimpleStringNode("minsocialinfluencepercentile", String.valueOf(minsocialinfluencepercentile));
        setValueOfSimpleStringNode("minsocialinfluencepercentile90days", String.valueOf(minsocialinfluencepercentile90days));
        setValueOfSimpleStringNode("dayssincelastsurvey", String.valueOf(dayssincelastsurvey));
        setValueOfSimpleStringNode("totalsurveystakenatleast", String.valueOf(totalsurveystakenatleast));
        setValueOfSimpleStringNode("totalsurveystakenatmost", String.valueOf(totalsurveystakenatmost));
        setValueOfArrayNode("gender", gender);
        setValueOfArrayNode("ethnicity", ethnicity);
        setValueOfArrayNode("maritalstatus", maritalstatus);
        setValueOfArrayNode("income", income);
        setValueOfArrayNode("educationlevel", educationlevel);
        setValueOfArrayNode("state", state);
        setValueOfArrayNode("city", city);
        setValueOfArrayNode("country", country);
        setValueOfArrayNode("profession", profession);
        setValueOfArrayNode("blogfocus", blogfocus);
        setValueOfArrayNode("politics", politics);
        setValueOfArrayNode("dneerousagemethods", dneerousagemethods);
        setValueOfArrayNode("panelids", panelids);
        setValueOfArrayNode("superpanelids", superpanelids);
        try {
            XMLOutputter serializer = new XMLOutputter();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            serializer.output(doc, out);
            return out.toString();
        } catch (Exception ex) {
            logger.debug("",ex);
        }
        return "";
    }

    private void setValueOfSimpleStringNode(String nodename, String value){
        if (doc.getRootElement().getChild(nodename)!=null){
            //It exists already and needs to be edited
            Element el = doc.getRootElement().getChild(nodename);
            el.setContent(new Text(value));
        } else {
            //It doesn't exist and needs to be created
            Element el = new Element(nodename);
            el.setContent(new Text(value));
            doc.getRootElement().addContent(el);
        }
    }

    private void setValueOfArrayNode(String nodename, String[] values){
        Element el = new Element(nodename);
        if (doc.getRootElement().getChild(nodename)!=null){
            //It exists already and needs to be edited
            el = doc.getRootElement().getChild(nodename);
            el.removeContent();
        } else {
            //It doesn't exist and needs to be created
            el = new Element(nodename);
            doc.getRootElement().addContent(el);
        }
        //Add values to this element
        if (values!=null){
            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                Element val = new Element("value");
                val.setContent(new Text(value));
                el.addContent(val);
            }
        }
    }

    private String loadValueOfStringFromXML(String nodename){
        if (doc.getRootElement().getChild(nodename)!=null){
            return doc.getRootElement().getChild(nodename).getValue();
        }
        return "";
    }

    private String[] loadValueOfArrayFromXML(String nodename){
        if (doc.getRootElement().getChild(nodename)!=null){
            Element node = doc.getRootElement().getChild(nodename);
            if (node.getChildren("value").size()>0){
                String[] out = new String[node.getChildren("value").size()];
                int i = 0;
                for (Iterator iterator = node.getChildren("value").iterator(); iterator.hasNext();) {
                    Element element = (Element) iterator.next();
                    out[i] = element.getValue();
                    i = i + 1;
                }
                return out;
            }
        }
        return null;
    }

    public String getAsHtml(){
        StringBuffer out = new StringBuffer();
        out.append("<table cellpadding=\"5\" cellspacing=\"0\" border=\"0\">");

        out.append("<tr>");
        out.append("<td valign=\"top\" width=\"25%\">");
        out.append("</td>");
        out.append("<td valign=\"top\">");
        out.append("</td>");
        out.append("</tr>");

        out.append("<tr>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"formfieldnamefont\">");
        out.append("Age");
        out.append("</font>");
        out.append("</td>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"smallfont\">");
        out.append(agemin+" - "+agemax);
        out.append("</font>");
        out.append("</td>");
        out.append("</tr>");

        out.append("<tr>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"formfieldnamefont\">");
        out.append("Blog Quality Of At Least");
        out.append("</font>");
        out.append("</td>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"smallfont\">");
        out.append(blogquality);
        out.append("</font>");
        out.append("</td>");
        out.append("</tr>");

        out.append("<tr>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"formfieldnamefont\">");
        out.append("Blog Quality Last 90 Days Of At Least");
        out.append("</font>");
        out.append("</td>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"smallfont\">");
        out.append(blogquality90days);
        out.append("</font>");
        out.append("</td>");
        out.append("</tr>");

        out.append("<tr>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"formfieldnamefont\">");
        out.append("Minimum Social Influence Rating");
        out.append("</font>");
        out.append("</td>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"smallfont\">");
        out.append(100-minsocialinfluencepercentile);
        out.append("</font>");
        out.append("</td>");
        out.append("</tr>");

        out.append("<tr>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"formfieldnamefont\">");
        out.append("Minimum Social Influence Rating 90 Days");
        out.append("</font>");
        out.append("</td>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"smallfont\">");
        out.append(100-minsocialinfluencepercentile90days);
        out.append("</font>");
        out.append("</td>");
        out.append("</tr>");

        out.append("<tr>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"formfieldnamefont\">");
        out.append("Days Since Last Conversation Participation");
        out.append("</font>");
        out.append("</td>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"smallfont\">");
        out.append(dayssincelastsurvey);
        out.append("</font>");
        out.append("</td>");
        out.append("</tr>");

        out.append("<tr>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"formfieldnamefont\">");
        out.append("Total Conversations Joined of at Least");
        out.append("</font>");
        out.append("</td>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"smallfont\">");
        out.append(totalsurveystakenatleast);
        out.append("</font>");
        out.append("</td>");
        out.append("</tr>");

        out.append("<tr>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"formfieldnamefont\">");
        out.append("Total Conversations Joined of at Most");
        out.append("</font>");
        out.append("</td>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"smallfont\">");
        out.append(totalsurveystakenatmost);
        out.append("</font>");
        out.append("</td>");
        out.append("</tr>");

        out.append("<tr>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"formfieldnamefont\">");
        out.append("Gender");
        out.append("</font>");
        out.append("</td>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"smallfont\">");
        for (int i = 0; i < gender.length; i++) {
            out.append(gender[i]);
            if (gender.length>(i+1)){out.append(", ");}
        }
        out.append("</font>");
        out.append("</td>");
        out.append("</tr>");

        out.append("<tr>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"formfieldnamefont\">");
        out.append("Ethnicity");
        out.append("</font>");
        out.append("</td>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"smallfont\">");
        for (int i = 0; i < ethnicity.length; i++) {
            out.append(ethnicity[i]);
            if (ethnicity.length>(i+1)){out.append(", ");}
        }
        out.append("</font>");
        out.append("</td>");
        out.append("</tr>");

        out.append("<tr>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"formfieldnamefont\">");
        out.append("Marital Status");
        out.append("</font>");
        out.append("</td>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"smallfont\">");
        for (int i = 0; i < maritalstatus.length; i++) {
            out.append(maritalstatus[i]);
            if (maritalstatus.length>(i+1)){out.append(", ");}
        }
        out.append("</font>");
        out.append("</td>");
        out.append("</tr>");

        out.append("<tr>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"formfieldnamefont\">");
        out.append("Income");
        out.append("</font>");
        out.append("</td>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"smallfont\">");
        for (int i = 0; i < income.length; i++) {
            out.append(income[i]);
            if (income.length>(i+1)){out.append(", ");}
        }
        out.append("</font>");
        out.append("</td>");
        out.append("</tr>");

        out.append("<tr>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"formfieldnamefont\">");
        out.append("Education Level");
        out.append("</font>");
        out.append("</td>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"smallfont\">");
        for (int i = 0; i < educationlevel.length; i++) {
            out.append(educationlevel[i]);
            if (educationlevel.length>(i+1)){out.append(", ");}
        }
        out.append("</font>");
        out.append("</td>");
        out.append("</tr>");

        out.append("<tr>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"formfieldnamefont\">");
        out.append("State");
        out.append("</font>");
        out.append("</td>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"smallfont\">");
        for (int i = 0; i < state.length; i++) {
            out.append(state[i]);
            if (state.length>(i+1)){out.append(", ");}
        }
        out.append("</font>");
        out.append("</td>");
        out.append("</tr>");

        out.append("<tr>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"formfieldnamefont\">");
        out.append("Nearest City");
        out.append("</font>");
        out.append("</td>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"smallfont\">");
        for (int i = 0; i < city.length; i++) {
            out.append(city[i]);
            if (city.length>(i+1)){out.append(", ");}
        }
        out.append("</font>");
        out.append("</td>");
        out.append("</tr>");

        out.append("<tr>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"formfieldnamefont\">");
        out.append("Country");
        out.append("</font>");
        out.append("</td>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"smallfont\">");
        for (int i = 0; i < country.length; i++) {
            out.append(country[i]);
            if (country.length>(i+1)){out.append(", ");}
        }
        out.append("</font>");
        out.append("</td>");
        out.append("</tr>");

        out.append("<tr>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"formfieldnamefont\">");
        out.append("Profession");
        out.append("</font>");
        out.append("</td>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"smallfont\">");
        for (int i = 0; i < profession.length; i++) {
            out.append(profession[i]);
            if (profession.length>(i+1)){out.append(", ");}
        }
        out.append("</font>");
        out.append("</td>");
        out.append("</tr>");

        out.append("<tr>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"formfieldnamefont\">");
        out.append("Focus");
        out.append("</font>");
        out.append("</td>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"smallfont\">");
        for (int i = 0; i < blogfocus.length; i++) {
            out.append(blogfocus[i]);
            if (blogfocus.length>(i+1)){out.append(", ");}
        }
        out.append("</font>");
        out.append("</td>");
        out.append("</tr>");

        out.append("<tr>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"formfieldnamefont\">");
        out.append("Politics");
        out.append("</font>");
        out.append("</td>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"smallfont\">");
        for (int i = 0; i < politics.length; i++) {
            out.append(politics[i]);
            if (politics.length>(i+1)){out.append(", ");}
        }
        out.append("</font>");
        out.append("</td>");
        out.append("</tr>");

        out.append("<tr>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"formfieldnamefont\">");
        out.append("Usage Methods");
        out.append("</font>");
        out.append("</td>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"smallfont\">");
        for (int i = 0; i < dneerousagemethods.length; i++) {
            out.append(dneerousagemethods[i]);
            if (dneerousagemethods.length>(i+1)){out.append(", ");}
        }
        out.append("</font>");
        out.append("</td>");
        out.append("</tr>");

        //@todo add panel and superpanel to requirements output as text

        out.append("</table>");
        return out.toString();
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

    public String[] getDneerousagemethods() {
        return dneerousagemethods;
    }

    public void setDneerousagemethods(String[] dneerousagemethods) {
        this.dneerousagemethods = dneerousagemethods;
    }

    public int getDayssincelastsurvey() {
        return dayssincelastsurvey;
    }

    public void setDayssincelastsurvey(int dayssincelastsurvey) {
        this.dayssincelastsurvey = dayssincelastsurvey;
    }

    public int getTotalsurveystakenatleast() {
        return totalsurveystakenatleast;
    }

    public void setTotalsurveystakenatleast(int totalsurveystakenatleast) {
        this.totalsurveystakenatleast = totalsurveystakenatleast;
    }

    public int getTotalsurveystakenatmost() {
        return totalsurveystakenatmost;
    }

    public void setTotalsurveystakenatmost(int totalsurveystakenatmost) {
        this.totalsurveystakenatmost = totalsurveystakenatmost;
    }

    public String[] getCountry() {
        return country;
    }

    public void setCountry(String[] country) {
        this.country=country;
    }

    public String[] getPanelids() {
        return panelids;
    }

    public void setPanelids(String[] panelids) {
        this.panelids=panelids;
    }

    public String[] getSuperpanelids() {
        return superpanelids;
    }

    public void setSuperpanelids(String[] superpanelids) {
        this.superpanelids=superpanelids;
    }
}
