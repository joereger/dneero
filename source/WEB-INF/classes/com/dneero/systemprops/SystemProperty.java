package com.dneero.systemprops;

import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.Systemprop;

import java.util.*;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Nov 6, 2006
 * Time: 3:08:21 PM
 */
public class SystemProperty {

    private static HashMap<String, String> props;
    private static HashMap<String, String> descriptions;

    //Things to do to add a prop:
    //1) Add a public static var here
    //2) Put the values into props and descriptions
    //3) Edit /sysadmin/systemprops.xhtml to include input for the prop
    //4) Edit /formbeans/SysadminSystemProps.java to include saving of the prop

    public static String PROP_BASEURL = "PROP_BASEURL";
    public static String PROP_SENDXMPP = "PROP_SENDXMPP";
    public static String PROP_SMTPOUTBOUNDSERVER = "PROP_SMTPOUTBOUNDSERVER";

    private static void loadAllPropsAndDefaultValues(){
        if (props==null){
            props = new HashMap<String, String>();
        }
        if (descriptions==null){
            descriptions = new HashMap<String, String>();
        }
        props.put(PROP_BASEURL,"http://dneero.com/");
        descriptions.put(PROP_BASEURL,"The base url that this instance is installed at.  Include the trailing slash.  Ex: http://dneero.com/");

        props.put(PROP_SENDXMPP, "0");
        descriptions.put(PROP_SENDXMPP, "0 or 1.  Whether or not to send XMPP notifications from this installation.");        

        props.put(PROP_SMTPOUTBOUNDSERVER, "localhost");
        descriptions.put(PROP_SMTPOUTBOUNDSERVER, "Smtp server name or ip address to use to send email.  Default is 'localhost'");        

    }



    //Edits below this line not needed to add a prop


    public static String getProp(String nameOfPropToGet){
        if (props==null){
            refreshAllProps();
        }
        if (props.containsKey(nameOfPropToGet)){
            return props.get(nameOfPropToGet);
        }
        Logger logger = Logger.getLogger(SystemProperty.class);
        logger.error("SystemProperty.getProp() called for "+nameOfPropToGet+" but no value was available.");
        return "";
    }

    public static void setProp(String name, String value){
        Logger logger = Logger.getLogger(SystemProperty.class);
        //Update an existing prop from the database with the same name
        List dbprops = HibernateUtil.getSession().createQuery("from Systemprop").list();
        for (Iterator iterator = dbprops.iterator(); iterator.hasNext();) {
            Systemprop systemprop = (Systemprop) iterator.next();
            if (systemprop.getName().equals(name)){
                systemprop.setValue(value);
                try{systemprop.save();}catch(Exception ex){logger.error(ex);}
            }
        }
        //None exists in the database so create one
        Systemprop systemprop = new Systemprop();
        systemprop.setName(name);
        systemprop.setValue(value);
        try{systemprop.save();}catch(Exception ex){logger.error(ex);}
        //Now refresh
        refreshAllProps();
    }

    public static String getDescription(String name){
        if (descriptions.containsKey(name)){
            return descriptions.get(name);
        }
        return "";
    }

    public static ArrayList<String> getAllPropertyNames(){
        ArrayList<String> out = new ArrayList<String>();
        Iterator keyValuePairs = props.entrySet().iterator();
        for (int i = 0; i < props.size(); i++){
            Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
            String name = (String)mapentry.getKey();
            String value = (String)mapentry.getValue();
            out.add(name);
        }
        return out;
    }

    private static void refreshAllProps(){
        props = new HashMap<String, String>();
        loadAllPropsAndDefaultValues();
        loadPropsFromDb();
    }



    private static void loadPropsFromDb(){
        if (props==null){
            props = new HashMap<String, String>();
        }
        List results = HibernateUtil.getSession().createQuery("from Systemprop").list();
        for (Iterator iterator = results.iterator(); iterator.hasNext();) {
            Systemprop systemprop = (Systemprop) iterator.next();
            props.put(systemprop.getName(), systemprop.getValue());
        }
    }


    public static HashMap<String, String> getProps() {
        return props;
    }

    public static void setProps(HashMap<String, String> props) {
        SystemProperty.props = props;
    }
}
