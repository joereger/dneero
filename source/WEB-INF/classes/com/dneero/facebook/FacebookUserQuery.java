package com.dneero.facebook;

import com.facebook.api.FacebookRestClient;
import com.dneero.systemprops.SystemProperty;
import org.w3c.dom.Document;
import org.jdom.input.DOMBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.Element;
import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.List;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Aug 14, 2007
 * Time: 2:30:33 PM
 */
public class FacebookUserQuery {

    private String first_name;
    private String last_name;
    private Calendar birthday;
    private boolean ismale;

    public FacebookUserQuery(String facebookSessionKey){
        Logger logger = Logger.getLogger(this.getClass().getName());
        //Extra api call on this one
        try{
            FacebookRestClient facebookRestClient = new FacebookRestClient(SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_KEY), SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_SECRET), facebookSessionKey);
            int facebookuserid = facebookRestClient.users_getLoggedInUser();
            runQuery(facebookSessionKey, facebookuserid);
        } catch (Exception ex){
            logger.error(ex);
        }
    }

    public FacebookUserQuery(String facebookSessionKey, int facebookuserid){
        runQuery(facebookSessionKey, facebookuserid);
    }

    private void runQuery(String facebookSessionKey, int facebookuserid){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{
            FacebookRestClient facebookRestClient = new FacebookRestClient(SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_KEY), SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_SECRET), facebookSessionKey);
            String fql = "SELECT first_name, last_name, birthday, sex FROM user WHERE uid="+facebookuserid;
            Document w3cDoc = facebookRestClient.fql_query(fql.subSequence(0,fql.length()));
            logger.debug("Start Facebook FQL Response: "+fql);
            DOMBuilder builder = new DOMBuilder();
            org.jdom.Document jdomDoc = builder.build(w3cDoc);
            XMLOutputter outp = new XMLOutputter();
            outp.output(jdomDoc, System.out);

            Element root = jdomDoc.getRootElement();

            outputChildrenToLogger(root, 0);

            Element user = getChild(root, "user");

            Element first_name = getChild(user, "first_name");
            this.first_name = first_name.getTextTrim();

            Element last_name = getChild(user, "last_name");
            this.last_name = last_name.getTextTrim();

            logger.debug(":End Facebook FQL Response");
        } catch (Exception ex){
            ex.printStackTrace();
            logger.error(ex);
        }
    }

    private Element getChild(Element el, String name){
        List allChildren = el.getChildren();
        for (Iterator iterator = allChildren.iterator(); iterator.hasNext();) {
            Element element = (Element) iterator.next();
            if (element.getName().equals(name)){
                return element;
            }
        }
        return null;
    }

    private void outputChildrenToLogger(Element el, int level){
        Logger logger = Logger.getLogger(this.getClass().getName());
        level = level + 1;
        String indent = "";
        for(int i=0; i<level; i++){
            indent = indent + "-";
        }
        List allChildren = el.getChildren();
        for (Iterator iterator = allChildren.iterator(); iterator.hasNext();) {
            Element element = (Element) iterator.next();
            logger.debug(indent + " " + element.getName());
            outputChildrenToLogger(element, level);
        }
    }


    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public Calendar getBirthday() {
        return birthday;
    }

    public boolean getIsmale() {
        return ismale;
    }

}
