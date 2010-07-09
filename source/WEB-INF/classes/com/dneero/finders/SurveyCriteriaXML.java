package com.dneero.finders;

import com.dneero.constants.Dneerousagemethods;
import com.dneero.dao.*;
import com.dneero.scheduledjobs.SystemStats;
import com.dneero.sir.SocialInfluenceRatingPercentile;
import com.dneero.util.DateDiff;
import com.dneero.util.Num;
import com.dneero.util.Time;
import com.dneero.util.Util;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;

import java.util.Calendar;
import java.util.Iterator;

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
    private int minsocialinfluencepercentile = 100;
    private int dayssincelastsurvey = 0;
    private int totalsurveystakenatleast = 0;
    private int totalsurveystakenatmost = 100000;
    private String[] dneerousagemethods;
    private String[] panelids;
    private String[] superpanelids;
    private DemographicsXML demographicsXML;
    private int plid;

    private Document doc;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public SurveyCriteriaXML(String criteriaxml, Pl pl){
        logger.debug("SurveyCriteriaXML instanciated");

        this.plid = pl.getPlid();

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

        try{
            Element demNode = doc.getRootElement().getChild("demographics");
            if (demNode!=null){
                Document demoDoc = new Document();
                Element el = new Element("demographics");
                el.addContent(demNode);
                demoDoc.addContent(el);
                String asStr = Util.jdomXmlDocAsString(demoDoc);
                logger.debug("asStr="+asStr);
                demographicsXML = new DemographicsXML(asStr, pl);
            } else {
                logger.debug("creating demographicsXML with blank string");
                demographicsXML = new DemographicsXML("", pl);
            }
        } catch (Exception ex){
            logger.error("",ex);
        }

        if (Num.isinteger(loadValueOfStringFromXML("agemin"))){
            agemin = Integer.parseInt(loadValueOfStringFromXML("agemin"));
        }
        if (Num.isinteger(loadValueOfStringFromXML("agemax"))){
            agemax = Integer.parseInt(loadValueOfStringFromXML("agemax"));
        }
        if (Num.isinteger(loadValueOfStringFromXML("minsocialinfluencepercentile"))){
            minsocialinfluencepercentile = Integer.parseInt(loadValueOfStringFromXML("minsocialinfluencepercentile"));
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
        String[] tmpArray = null;
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

            //Demographic fields
            if (!demographicsXML.isUserQualified(user)){
                surveyfitsblogger = false;
            }

            //Panel
            if (surveyfitsblogger && panelids!=null && panelids.length>0 && Util.stringArrayContainsInteger(panelids)){
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
                    logger.debug("does not qualify because of panelid="+Util.stringArrayAsString(panelids));
                }
            }
            //SuperPanel
            if (surveyfitsblogger && superpanelids!=null && superpanelids.length>0 && Util.stringArrayContainsInteger(superpanelids)){
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
                    logger.debug("does not qualify because of superpanelid="+Util.stringArrayAsString(superpanelids));
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
            //Social Influence Rating
            if (surveyfitsblogger){
                if (minsocialinfluencepercentile<100){
                    int maxranking = SocialInfluenceRatingPercentile.getRankingOfGivenPercentile(SystemStats.getTotalusers(), minsocialinfluencepercentile);
                    if (user.getSirrank()>maxranking || user.getSirrank()==0){
                        surveyfitsblogger = false;
                        logger.debug("does not qualify because of socialinfluenceranking. minsocialinfluencepercentile="+minsocialinfluencepercentile+"  maxranking="+maxranking+" user.getSirrank()="+user.getSirrank());
                    }
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
//            if (surveyfitsblogger){
//                //Only consider focus for non-facebook users
//                if (user.getFacebookuserid()<=0){
//                    //Only do this check if not all of the focuses are selected
//                    if (!areAllBlogfocusesSelected()){
//                        //@todo eventually will want to remove this if/then loop because enough people will have logged in and setup their venues
//                        if (blogger.getVenues()!=null && blogger.getVenues().size()>0){
//                            boolean fulfillsfocusreqs = false;
//                            for (Iterator<Venue> iterator1=blogger.getVenues().iterator(); iterator1.hasNext();) {
//                                Venue venue=iterator1.next();
//                                //Does at least one blog from this user fall within the
//                                if (Util.arrayContains(getBlogfocus(), venue.getFocus())){
//                                    fulfillsfocusreqs = true;
//                                    break;
//                                }
//                            }
//                            if (!fulfillsfocusreqs){
//                                surveyfitsblogger = false;
//                                logger.debug("does not qualify because of focus.");
//                            }
//                        }
//                    }
//                }
//            }
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
        //return areAllOfSomethingSelected(Util.convertToArray(Blogfocuses.get()), blogfocus);
        return true;
    }


    private void preSelectAll(){
        dneerousagemethods = Util.convertToArray(Dneerousagemethods.get());
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

        //logger.debug("__________");
        //logger.debug("getSurveyCriteriaAsString()----- doc after null doc check");
        //logger.debug("doc="+Util.jdomXmlDocAsString(doc));

        setValueOfSimpleStringNode("agemin", String.valueOf(agemin));
        setValueOfSimpleStringNode("agemax", String.valueOf(agemax));
        setValueOfSimpleStringNode("minsocialinfluencepercentile", String.valueOf(minsocialinfluencepercentile));
        setValueOfSimpleStringNode("dayssincelastsurvey", String.valueOf(dayssincelastsurvey));
        setValueOfSimpleStringNode("totalsurveystakenatleast", String.valueOf(totalsurveystakenatleast));
        setValueOfSimpleStringNode("totalsurveystakenatmost", String.valueOf(totalsurveystakenatmost));
        setValueOfArrayNode("dneerousagemethods", dneerousagemethods);
        setValueOfArrayNode("panelids", panelids);
        setValueOfArrayNode("superpanelids", superpanelids);

        //logger.debug("getSurveyCriteriaAsString()----- doc after setting basic fields");
        //logger.debug("doc="+Util.jdomXmlDocAsString(doc));

        Document demDoc = demographicsXML.getXMLDoc();
        //logger.debug("getSurveyCriteriaAsString()----- demographicsXML");
        //logger.debug("demDoc="+Util.jdomXmlDocAsString(demDoc));
        Element demoEl = new Element("demographics");
        demoEl.addContent(demDoc.getRootElement().cloneContent());
        //logger.debug("getSurveyCriteriaAsString()----- demoEl");
        //logger.debug("demoEl="+demoEl.toString());
        doc.getRootElement().addContent(demoEl);
        //logger.debug("getSurveyCriteriaAsString()----- doc at end");
        //logger.debug("doc="+Util.jdomXmlDocAsString(doc));

        return Util.jdomXmlDocAsString(doc);
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
        out.append("Minimum Social Influence Percentile");
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
        out.append("Days Since Last Participation");
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
        out.append("Total Joins of at Least");
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
        out.append("Total Joins of at Most");
        out.append("</font>");
        out.append("</td>");
        out.append("<td valign=\"top\">");
        out.append("<font class=\"smallfont\">");
        out.append(totalsurveystakenatmost);
        out.append("</font>");
        out.append("</td>");
        out.append("</tr>");



        out.append("<tr>");
        out.append("<td valign=\"top\" colspan=\"2\">");
        out.append(demographicsXML.getAsHtml());
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


    public DemographicsXML getDemographicsXML() {
        return demographicsXML;
    }

    public void setDemographicsXML(DemographicsXML demographicsXML) {
        this.demographicsXML = demographicsXML;
    }

    public int getMinsocialinfluencepercentile() {
        return minsocialinfluencepercentile;
    }

    public void setMinsocialinfluencepercentile(int minsocialinfluencepercentile) {
        this.minsocialinfluencepercentile = minsocialinfluencepercentile;
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
