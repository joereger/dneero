package com.dneero.finders;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Content;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format;
import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;

import com.dneero.util.Num;
import com.dneero.util.Jsf;

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

    private Document doc;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public SurveyCriteriaXML(String criteriaxml){
        preSelectAll();

        if (criteriaxml!=null && !criteriaxml.equals("")){
            SAXBuilder builder = new SAXBuilder();
            try{
                doc = builder.build(new java.io.ByteArrayInputStream(criteriaxml.getBytes()));
            } catch (Exception ex){
                logger.error(ex);
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
        tmpArray = loadValueOfArrayFromXML("profession");
        if (tmpArray!=null){ profession = tmpArray; }
        tmpArray = loadValueOfArrayFromXML("blogfocus");
        if (tmpArray!=null){ blogfocus = tmpArray; }
        tmpArray = loadValueOfArrayFromXML("politics");
        if (tmpArray!=null){ politics = tmpArray; }
        
    }

    private void preSelectAll(){
        gender = convertToArray((TreeMap) Jsf.getManagedBean("genders"));
        ethnicity = convertToArray((LinkedHashMap)Jsf.getManagedBean("ethnicities"));
        maritalstatus = convertToArray((LinkedHashMap)Jsf.getManagedBean("maritalstatuses"));
        income = convertToArray((LinkedHashMap)Jsf.getManagedBean("incomes"));
        educationlevel = convertToArray((LinkedHashMap)Jsf.getManagedBean("educationlevels"));
        state = convertToArray((LinkedHashMap)Jsf.getManagedBean("states"));
        city = convertToArray((LinkedHashMap)Jsf.getManagedBean("cities"));
        profession = convertToArray((TreeMap)Jsf.getManagedBean("professions"));
        blogfocus = convertToArray((TreeMap)Jsf.getManagedBean("blogfocuses"));
        politics = convertToArray((LinkedHashMap)Jsf.getManagedBean("politics"));
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
            logger.error("Root element was not surveycriteria, overwriting document.");
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
        setValueOfArrayNode("gender", gender);
        setValueOfArrayNode("ethnicity", ethnicity);
        setValueOfArrayNode("maritalstatus", maritalstatus);
        setValueOfArrayNode("income", income);
        setValueOfArrayNode("educationlevel", educationlevel);
        setValueOfArrayNode("state", state);
        setValueOfArrayNode("city", city);
        setValueOfArrayNode("profession", profession);
        setValueOfArrayNode("blogfocus", blogfocus);
        setValueOfArrayNode("politics", politics);
        try {
            XMLOutputter serializer = new XMLOutputter();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            serializer.output(doc, out);
            return out.toString();
        } catch (Exception ex) {
            logger.error(ex);
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
        out.append("Blog Focus");
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
}
