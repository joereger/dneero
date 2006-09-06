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
import java.util.List;
import java.util.Iterator;

import com.dneero.util.Num;

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
        gender = loadValueOfArrayFromXML("gender");
        ethnicity = loadValueOfArrayFromXML("ethnicity");
        maritalstatus = loadValueOfArrayFromXML("maritalstatus");
        income = loadValueOfArrayFromXML("income");
        educationlevel = loadValueOfArrayFromXML("educationlevel");
        state = loadValueOfArrayFromXML("state");
        city = loadValueOfArrayFromXML("city");
        profession = loadValueOfArrayFromXML("profession");
        blogfocus = loadValueOfArrayFromXML("blogfocus");
        politics = loadValueOfArrayFromXML("politics");
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
                logger.debug(nodename + " new Element('value')="+value);
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
        return new String[0];
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
}
