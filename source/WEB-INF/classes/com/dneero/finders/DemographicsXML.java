package com.dneero.finders;

import com.dneero.dao.Blogger;
import com.dneero.dao.Demographic;
import com.dneero.dao.Pl;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Util;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;

import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Sep 5, 2006
 * Time: 10:31:17 AM
 *
 * Abstraction of the XML workings of the survey criteria xml field
 *
 */
public class DemographicsXML {

    private TreeMap<Demographic, ArrayList<String>> dmg = new TreeMap<Demographic, ArrayList<String>>(new DemographicComparator());
    private Document doc;
    private int plid;
    private String rawXML;

    public DemographicsXML(String rawXML, Pl pl){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("DemographicsXML instanciated");
        this.plid = pl.getPlid();
        this.rawXML = rawXML;
        init();
    }

    public DemographicsXML(Pl pl){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("DemographicsXML instanciated");
        this.plid = pl.getPlid();
        this.rawXML = "";
        init();
    }

    private void init(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("DemographicsXML init() called");
        //Preselect
        preSelectAll();
        //Pull in the xml
        if (rawXML!=null && !rawXML.equals("")){
            SAXBuilder builder = new SAXBuilder();
            try{
                doc = builder.build(new java.io.ByteArrayInputStream(rawXML.getBytes()));
            } catch (Exception ex){
                logger.error("",ex);
            }
        }
        //Check for a null doc
        nullDocCheck();
        //Populate the dmg with values from the XML
        Iterator keyValuePairs = dmg.entrySet().iterator();
        for (int i = 0; i < dmg.size(); i++){
            Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
            Demographic demographic = (Demographic)mapentry.getKey();
            ArrayList<String> values = (ArrayList<String>)mapentry.getValue();
            //Current values are from preSelectAll()/the private label... I want to replace with those from XML
            String[] tmpArray = loadValueOfArrayFromXML("demographicid_"+demographic.getDemographicid());
            //Only replace the values in dmg if the values from XML aren't empty
            if (tmpArray!=null){
                ArrayList<String> arryTmp = Util.stringArrayToArrayList(tmpArray);
                dmg.put(demographic, arryTmp);
            }
        }
    }



    private void preSelectAll(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{
            List<Demographic> demographics = HibernateUtil.getSession().createCriteria(Demographic.class)
                                           .add(Restrictions.eq("plid", plid))
                                           .setCacheable(true)
                                           .list();
            for (Iterator<Demographic> demographicIterator = demographics.iterator(); demographicIterator.hasNext();) {
                Demographic demographic = demographicIterator.next();
                ArrayList<String> possiblevalues = DemographicsUtil.convert(demographic.getPossiblevalues());
                dmg.put(demographic, possiblevalues);
            }
        } catch (Exception ex){
            logger.error("", ex);
        }
    }



    private void nullDocCheck(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (doc==null){
            //Start a new doc
            doc = new Document();
            Element crit = new Element("demographics");
            doc.addContent(crit);
        }
        if (!doc.getRootElement().getName().equals("demographics")){
            logger.debug("Root element was not demographics, overwriting document.");
            doc = new Document();
            Element crit = new Element("demographics");
            doc.addContent(crit);
        }
    }

    public String getAsString(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        nullDocCheck();
        //Build XML from the dmg object
        Iterator keyValuePairs = dmg.entrySet().iterator();
        for (int i = 0; i < dmg.size(); i++){
            Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
            Demographic demographic = (Demographic)mapentry.getKey();
            ArrayList<String> values = (ArrayList<String>)mapentry.getValue();
            //Set the XML node... node name is demographicid
            setValueOfArrayNode("demographicid_"+demographic.getDemographicid(), Util.arrayListToStringArray(values));
        }
        return Util.jdomXmlDocAsString(doc);
    }

    public boolean isUserQualified(User user) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (user==null){return false;}
        if (user.getBloggerid()==0){return false;}
        Blogger blogger = Blogger.get(user.getBloggerid());
        if (blogger==null || blogger.getBloggerid()==0){return false;}
        if (blogger!=null){
            DemographicsXML userDemographicXML = new DemographicsXML(blogger.getDemographicsxml(), Pl.get(plid));
            try{
                //Iterate (Demographics, ArrayList<String>) in dmg
                Iterator keyValuePairs = dmg.entrySet().iterator();
                for (int i = 0; i < dmg.size(); i++){
                    Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
                    Demographic demographic = (Demographic)mapentry.getKey();
                    ArrayList<String> selectedValues = (ArrayList<String>)mapentry.getValue();
                    //See if any of these values is selected in user xml
                    boolean foundAnAcceptableValueInUserXML = false;
                    if (selectedValues!=null){
                        for (Iterator<String> it = selectedValues.iterator(); it.hasNext();) {
                            String s = it.next();
                            if (userDemographicXML.isValueSelected(demographic, s)){
                                foundAnAcceptableValueInUserXML = true;
                            }
                        }
                    }
                    //Return
                    if (!foundAnAcceptableValueInUserXML){
                        logger.debug("userid="+user.getUserid()+" not qualified because of '"+demographic.getName()+"' for plid="+plid);
                        return false;
                    }
                }

//                List<Demographic> demographics = HibernateUtil.getSession().createCriteria(Demographic.class)
//                                                 .add(Restrictions.eq("plid", plid))
//                                                 .setCacheable(true)
//                                                 .list();
//                if (demographics!=null && demographics.size()>0){
//                    for (Iterator<Demographic> demographicIterator = demographics.iterator(); demographicIterator.hasNext();) {
//                        Demographic demographic = demographicIterator.next();
//                        if (!areAllPossibleValuesSelected(demographic)){
//                            //Iterate all values selected in this xml object (not the user's xml and not all possible values from the Demographic)
//                            ArrayList<String> selectedValues = getValues(demographic.getDemographicid());
//                            boolean foundAnAcceptableValueInUserXML = false;
//                            if (selectedValues!=null){
//                                for (Iterator<String> it = selectedValues.iterator(); it.hasNext();) {
//                                    String s = it.next();
//                                    if (userDemographicXML.isValueSelected(demographic, s)){
//                                        foundAnAcceptableValueInUserXML = true;
//                                    }
//                                }
//                            } else {
//                                //No value selected in this object xml... now what?  Nobody qualifies?  Or everybody qualifies?
//                                foundAnAcceptableValueInUserXML = false; //Nobody qualifies... survey was setup incorrectly
//                            }
//                            //Return
//                            if (!foundAnAcceptableValueInUserXML){
//                                return false;
//                            }
//                        }
//                    }
//                }


            } catch (Exception ex){
                logger.error("", ex);
            }
        }
        return true;
    }


    public boolean areAllPossibleValuesSelected(Demographic demographic){
        Logger logger = Logger.getLogger(this.getClass().getName());
        ArrayList<String> allPossibleValues = DemographicsUtil.convert(demographic.getPossiblevalues());
        ArrayList<String> selectedValues = getValues(demographic.getDemographicid());
        //High level checking without iterating
        if (selectedValues==null && allPossibleValues!=null){
            return false;
        }
        if (selectedValues==null && allPossibleValues==null){
            return true;
        }
        if (selectedValues!=null && allPossibleValues!=null){
            if (selectedValues.size()==0 && allPossibleValues.size()==0){
                return true;
            }
            if (selectedValues.size()==0 && allPossibleValues.size()>0){
                return false;
            }
        }
        //Gotta iterate
        if (selectedValues!=null && allPossibleValues!=null){
            for (Iterator<String> it = allPossibleValues.iterator(); it.hasNext();) {
                String allVal = it.next();
                if (!selectedValues.contains(allVal)){
                    return false;
                }
            }
        }
        //Made it this far so all possible values are accounted for
        return true;
    }

    public boolean isValueSelected(Demographic demographic, String selectedValue){
        Logger logger = Logger.getLogger(this.getClass().getName());
        ArrayList<String> selectedValues = getValues(demographic.getDemographicid());
        if (selectedValues!=null){
            if (selectedValues.contains(selectedValue)){
                return true;
            }
        }
        //Made it this far, didn't find it
        return false;
    }

    public Document getXMLDoc() {
        return doc;
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

    public ArrayList<String> getValues(int demographicid){
        Logger logger = Logger.getLogger(this.getClass().getName());
        ArrayList<String> out = new ArrayList<String>();
        Demographic d = Demographic.get(demographicid);
        if (d!=null && d.getDemographicid()==demographicid){
            //Get the ArrayList from dmg using the demographic as the key
            out = dmg.get(d);
        }
        return out;
    }


    public void setValues(int demographicid, ArrayList<String> values){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (values==null){values = new ArrayList<String>();}
        Demographic d = Demographic.get(demographicid);
        if (d!=null && d.getDemographicid()==demographicid){
            //Put values to dmg using the demographic as the key
            dmg.put(d, values);
        }
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



        Iterator keyValuePairs = dmg.entrySet().iterator();
        for (int i = 0; i < dmg.size(); i++){
            Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
            Demographic demographic = (Demographic)mapentry.getKey();
            ArrayList<String> selectedValues = (ArrayList<String>)mapentry.getValue();
            out.append("<tr>");
            out.append("<td valign=\"top\">");
            out.append("<font class=\"formfieldnamefont\">");
            out.append(demographic.getName());
            out.append("</font>");
            out.append("</td>");
            out.append("<td valign=\"top\">");
            out.append("<font class=\"smallfont\">");
            for (Iterator it = selectedValues.iterator(); it.hasNext(); ) {
                String val = (String)it.next();
                out.append(val);
                if (it.hasNext()){
                    out.append(", ");
                }
            }
            out.append("</font>");
            out.append("</td>");
            out.append("</tr>");
        }







        out.append("</table>");
        return out.toString();
    }



}