package com.dneero.facebook;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.jdom.input.DOMBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.Element;
import com.facebook.api.FacebookRestClient;
import com.dneero.systemprops.SystemProperty;

/**
 * User: Joe Reger Jr
 * Date: Aug 14, 2007
 * Time: 11:31:22 PM
 */
public class FacebookUser {
    private String first_name;
    private String last_name;
    private String uid;
    private String sex;

    public FacebookUser(int facebookuserid, String facebookSessionKey){
        refreshFromFacebookApi(facebookuserid, facebookSessionKey);
    }

    public void refreshFromFacebookApi(int facebookuserid, String facebookSessionKey){
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{
            FacebookRestClient facebookRestClient = new FacebookRestClient(SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_KEY), SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_SECRET), facebookSessionKey);
            String fql = "SELECT first_name, last_name, birthday, sex, uid FROM user WHERE uid="+facebookuserid;
            Document w3cDoc = facebookRestClient.fql_query(fql.subSequence(0,fql.length()));
            DOMBuilder builder = new DOMBuilder();
            org.jdom.Document jdomDoc = builder.build(w3cDoc);
            logger.debug("Start Facebook FQL Response: "+fql);
            XMLOutputter outp = new XMLOutputter();
            outp.output(jdomDoc, System.out);
            logger.debug(":End Facebook FQL Response");
            Element root = jdomDoc.getRootElement();
            //outputChildrenToLogger(root, 0);
            Element user = FacebookApiWrapper.getChild(root, "user");
            Element first_name = FacebookApiWrapper.getChild(user, "first_name");
            this.first_name = first_name.getTextTrim();
            Element last_name = FacebookApiWrapper.getChild(user, "last_name");
            this.last_name = last_name.getTextTrim();
            Element uid = FacebookApiWrapper.getChild(user, "uid");
            this.uid = uid.getTextTrim();
            Element sex = FacebookApiWrapper.getChild(user, "sex");
            this.sex = sex.getTextTrim();
        } catch (Exception ex){
            ex.printStackTrace();
            logger.error(ex);
        }
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
