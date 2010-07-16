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

    private TreeMap<Integer, ArrayList<String>> dmg = new TreeMap<Integer, ArrayList<String>>();
    private Document doc;
    private int plid;
    private String rawXML;

    public DemographicsXML(String rawXML, Pl pl, boolean ifRawXMLDoesntHaveAValueUsePreselectAll){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("DemographicsXML instanciated plid="+pl.getPlid()+" rawXML="+rawXML);
        this.plid = pl.getPlid();
        this.rawXML = rawXML;
        init(ifRawXMLDoesntHaveAValueUsePreselectAll);
    }

    public DemographicsXML(Pl pl, boolean ifRawXMLDoesntHaveAValueUsePreselectAll){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("DemographicsXML instanciated via plid="+pl.getPlid()+" alone");
        this.plid = pl.getPlid();
        this.rawXML = "";
        init(ifRawXMLDoesntHaveAValueUsePreselectAll);
    }

    private void init(boolean ifRawXMLDoesntHaveAValueUsePreselectAll){
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
            int demographicid = (Integer)mapentry.getKey();
            Demographic demographic = Demographic.get(demographicid);
            ArrayList<String> values = (ArrayList<String>)mapentry.getValue();
            //Current values are from preSelectAll()/the private label... I want to replace with those from XML
            String[] tmpArray = loadValueOfArrayFromXML("demographicid_"+demographic.getDemographicid());
            //If the values from XML aren't empty, replace preSelectAll values
            if (tmpArray!=null){
                ArrayList<String> arryTmp = Util.stringArrayToArrayList(tmpArray);
                dmg.put(demographic.getDemographicid(), arryTmp);
            } else {
                //If the values from XML are empty, look to ifRawXMLDoesntHaveAValueUsePreselectAll to see whether to leave preSelectAll values or not
                if (!ifRawXMLDoesntHaveAValueUsePreselectAll){
                    dmg.put(demographic.getDemographicid(), new ArrayList<String>());
                }
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
                dmg.put(demographic.getDemographicid(), possiblevalues);
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

    private void buildDocFromDMG(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        nullDocCheck();
        //Build XML from the dmg object
        Iterator keyValuePairs = dmg.entrySet().iterator();
        for (int i = 0; i < dmg.size(); i++){
            Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
            int demographicid = (Integer)mapentry.getKey();
            Demographic demographic = Demographic.get(demographicid);
            ArrayList<String> values = (ArrayList<String>)mapentry.getValue();
            //Set the XML node... node name is demographicid
            setValueOfArrayNode("demographicid_"+demographic.getDemographicid(), Util.arrayListToStringArray(values));
        }
    }

    public String getAsString(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        buildDocFromDMG();
        return Util.jdomXmlDocAsString(doc);
    }

    public boolean isUserQualified(User user) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (user==null){return false;}
        if (user.getBloggerid()==0){return false;}
        Blogger blogger = Blogger.get(user.getBloggerid());
        if (blogger==null || blogger.getBloggerid()==0){return false;}
        if (blogger!=null){
            DemographicsXML userDemographicXML = new DemographicsXML(blogger.getDemographicsxml(), Pl.get(plid), false);
            try{
                //Iterate (Demographics, ArrayList<String>) in dmg
                Iterator keyValuePairs = dmg.entrySet().iterator();
                for (int i = 0; i < dmg.size(); i++){
                    Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
                    int demographicid = (Integer)mapentry.getKey();
                    Demographic demographic = Demographic.get(demographicid);
                    //Only check user values if all values aren't selected already
                    if (!areAllPossibleValuesSelected(demographic)){
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
                }
            } catch (Exception ex){
                logger.error("", ex);
            }
        }
        return true;
    }

    public static boolean isDemographicProfileOK(User user) {
        Logger logger = Logger.getLogger(DemographicsXML.class);
        logger.debug("isDemographicProfileOK() called");
        if (user==null){return false;}
        logger.debug("isDemographicProfileOK() userid="+user.getUserid());
        if (user.getBloggerid()==0){return false;}
        Blogger blogger = Blogger.get(user.getBloggerid());
        if (blogger==null || blogger.getBloggerid()==0){return false;}
        if (blogger!=null){

            //DemographicsXML plDemographicsXML = new DemographicsXML("", Pl.get(user.getPlid()));
            DemographicsXML userDemographicsXML = new DemographicsXML(blogger.getDemographicsxml(), Pl.get(user.getPlid()), false);
            logger.debug("isDemographicProfileOK() userDemographicsXML="+userDemographicsXML.getAsString());
            List<Demographic> demographics = HibernateUtil.getSession().createCriteria(Demographic.class)
                                             .add(Restrictions.eq("plid", user.getPlid()))
                                             .setCacheable(true)
                                             .list();
            if (demographics!=null && demographics.size()>0){
                for (Iterator<Demographic> demographicIterator = demographics.iterator(); demographicIterator.hasNext();) {
                    Demographic demographic = demographicIterator.next();
                    logger.debug("isDemographicProfileOK() demographic="+demographic.getName()+" isrequired="+demographic.getIsrequired());
                    if (demographic.getIsrequired()){
                        //if (!plDemographicsXML.areAllPossibleValuesSelected(demographic)){
                            //Iterate all values selected in this xml object (not the user's xml and not all possible values from the Demographic)
                            ArrayList<String> allPossibleValues = userDemographicsXML.getAllPossibleValues(demographic.getDemographicid());
                            boolean foundAnAcceptableValueInUserXML = false;
                            if (allPossibleValues!=null){
                                for (Iterator<String> it = allPossibleValues.iterator(); it.hasNext();) {
                                    String possibleValue = it.next();
                                    if (userDemographicsXML.isValueSelected(demographic, possibleValue)){
                                        //logger.debug("isDemographicProfileOK() possibleValue="+possibleValue+" found in userDemographicsXML");
                                        foundAnAcceptableValueInUserXML = true;
                                    } else {
                                        //logger.debug("isDemographicProfileOK() possibleValue="+possibleValue+" NOT found in userDemographicsXML");   
                                    }
                                }
                            } else {
                                //No value selected in this object xml... now what?  Nobody qualifies?  Or everybody qualifies?
                                foundAnAcceptableValueInUserXML = false; //Nobody qualifies... survey was set up incorrectly
                            }
                            //Return
                            if (!foundAnAcceptableValueInUserXML){
                                logger.debug("isDemographicProfileOK() returning FALSE due to demographic="+demographic.getName());
                                return false;
                            }
                        //}
                    }
                }
            }
        }
        logger.debug("isDemographicProfileOK() returning TRUE from bottom of page");
        return true;
    }


    public boolean areAllPossibleValuesSelected(Demographic demographic){
        Logger logger = Logger.getLogger(this.getClass().getName());
        //logger.debug("Start areAllPossibleValuesSelected(demographicid="+demographic.getDemographicid()+" name="+demographic.getName()+")");
        ArrayList<String> allPossibleValues = DemographicsUtil.convert(demographic.getPossiblevalues());
        ArrayList<String> selectedValues = getValues(demographic.getDemographicid());
        //Debug
        if (allPossibleValues!=null){
            //logger.debug("allPossibleValues ("+allPossibleValues.size()+" items)="+Util.stringArrayAsString(Util.arrayListToStringArray(allPossibleValues)));
        } else { logger.debug("allPossibleValues=null"); }
        if (selectedValues!=null){
            //logger.debug("selectedValues ("+selectedValues.size()+" items)="+Util.stringArrayAsString(Util.arrayListToStringArray(selectedValues)));
        } else { logger.debug("selectedValues=null"); }
        //High level checking without iterating
        if (selectedValues==null && allPossibleValues!=null){
            //logger.debug("areAllPossibleValuesSelected=false because (selectedValues==null && allPossibleValues!=null)");
            return false;
        }
        if (selectedValues==null && allPossibleValues==null){
            //logger.debug("areAllPossibleValuesSelected=true because (selectedValues==null && allPossibleValues==null)");
            return true;
        }
        if (selectedValues!=null && allPossibleValues!=null){
            if (selectedValues.size()==0 && allPossibleValues.size()==0){
                //logger.debug("areAllPossibleValuesSelected=true because (selectedValues.size()==0 && allPossibleValues.size()==0)");
                return true;
            }
            if (selectedValues.size()==0 && allPossibleValues.size()>0){
                //logger.debug("areAllPossibleValuesSelected=false because (selectedValues.size()==0 && allPossibleValues.size()>0)");
                return false;
            }
        }
        //Gotta iterate
        if (selectedValues!=null && allPossibleValues!=null){
            for (Iterator<String> it = allPossibleValues.iterator(); it.hasNext();) {
                String allVal = it.next();
                if (!selectedValues.contains(allVal.trim())){
                    //logger.debug("areAllPossibleValuesSelected=false because !selectedValues.contains("+allVal+")");
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
            if (selectedValues.contains(selectedValue.trim())){
                return true;
            }
        }
        //Made it this far, didn't find it
        return false;
    }

    public Document getXMLDoc() {
        buildDocFromDMG();
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
            out = dmg.get(d.getDemographicid());
        }
        return out;
    }

    public String getValue(int demographicid){
        Logger logger = Logger.getLogger(this.getClass().getName());
        ArrayList<String> vals = getValues(demographicid);
        if (vals!=null){
            for (Iterator it = vals.iterator(); it.hasNext(); ) {
                String s = (String)it.next();
                return s.trim();
            }
        }
        return "";
    }

    public ArrayList<String> getAllPossibleValues(int demographicid){
        Logger logger = Logger.getLogger(this.getClass().getName());
        ArrayList<String> out = new ArrayList<String>();
        Demographic d = Demographic.get(demographicid);
        if (d!=null && d.getDemographicid()==demographicid){
            return DemographicsUtil.convert(d.getPossiblevalues());
        }
        return out;
    }



    public void setValues(int demographicid, ArrayList<String> values){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (values==null){values = new ArrayList<String>();}
        Demographic d = Demographic.get(demographicid);
        if (d!=null && d.getDemographicid()==demographicid){
            //Put values to dmg using the demographic as the key
            dmg.put(d.getDemographicid(), values);
        }
    }

    public void setValue(int demographicid, String value){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (value==null){value = "";}
        ArrayList<String> vals = new ArrayList<String>();
        vals.add(value);
        setValues(demographicid, vals);
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
            int demographicid = (Integer)mapentry.getKey();
            Demographic demographic = Demographic.get(demographicid);
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