package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.session.UserSession;
import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;
import com.dneero.dao.Researcher;
import com.dneero.dao.Creditcard;
import com.dneero.dao.Userrole;
import com.dneero.money.PaymentMethod;

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class ResearcherBilling {

    private String ccnum;
    private int cctype;
    private String cvv2;
    private int ccexpmo;
    private int ccexpyear;
    private String postalcode;
    private String ccstate;
    private String street;
    private String cccity;
    private String firstname;
    private String lastname;
    private String ipaddress;
    private String merchantsessionid;


    Logger logger = Logger.getLogger(this.getClass().getName());

    public ResearcherBilling(){
        load();
    }

    public void load(){
        logger.debug("load called");
        UserSession userSession = Jsf.getUserSession();
        if (userSession.getUser()!=null && userSession.getUser().getResearcherid()>0){
            Researcher researcher = Researcher.get(userSession.getUser().getResearcherid());

            if(userSession.getUser().getChargemethodcreditcardid()>0){
                Creditcard cc = Creditcard.get(userSession.getUser().getChargemethodcreditcardid());
                ccnum = cc.getCcnum();
                cctype = cc.getCctype();
                cvv2 = cc.getCvv2();
                ccexpmo = cc.getCcexpmo();
                ccexpyear = cc.getCcexpyear();
                postalcode = cc.getPostalcode();
                ccstate = cc.getState();
                street = cc.getStreet();
                cccity = cc.getCity();
                firstname = cc.getFirstname();
                lastname = cc.getLastname();
                ipaddress = cc.getIpaddress();
                merchantsessionid = cc.getMerchantsessionid();
            }


        }


    }

    public String saveAction(){

        UserSession userSession = Jsf.getUserSession();



        if (userSession.getUser().getResearcherid()>0){


            //Start validation
            //@todo better validation
            if (ccnum.equals("")){
                Jsf.setFacesMessage("researcherdetails:ccnum", "You've chosen to be paid via credit card so you must provide a credit card number.");
                return "";
            }
            //End validation



            Creditcard cc = new Creditcard();
            if(userSession.getUser().getChargemethodcreditcardid()>0){
                cc = Creditcard.get(userSession.getUser().getChargemethodcreditcardid());
            }
            cc.setCcexpmo(ccexpmo);
            cc.setCcexpyear(ccexpyear);
            cc.setCcnum(ccnum);
            cc.setCctype(cctype);
            cc.setCity(cccity);
            cc.setCvv2(cvv2);
            cc.setFirstname(firstname);
            cc.setIpaddress(Jsf.getRemoteAddr());
            cc.setLastname(lastname);
            cc.setMerchantsessionid(Jsf.getSessionID());
            cc.setPostalcode(postalcode);
            cc.setState(ccstate);
            cc.setStreet(street);
            cc.setUserid(userSession.getUser().getUserid());
            try{
                cc.save();
            } catch (GeneralException gex){
                Jsf.setFacesMessage("Error saving record: "+gex.getErrorsAsSingleString());
                logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                return null;
            }



            userSession.getUser().setChargemethod(PaymentMethod.PAYMENTMETHODCREDITCARD);
            userSession.getUser().setChargemethodcreditcardid(cc.getCreditcardid());

            try{
                userSession.getUser().save();
            } catch (GeneralException gex){
                Jsf.setFacesMessage("Error saving record: "+gex.getErrorsAsSingleString());
                logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                return null;
            }



            userSession.getUser().refresh();


            return "accountmain";

        } else {
            Jsf.setFacesMessage("UserSession.getUser() is null.  Please log in.");
            return null;
        }
    }


    public LinkedHashMap getCreditcardtypes(){
        LinkedHashMap out = new LinkedHashMap();
        out.put("Visa", Creditcard.CREDITCARDTYPE_VISA);
        out.put("Master Card", Creditcard.CREDITCARDTYPE_MASTERCARD);
        out.put("American Express", Creditcard.CREDITCARDTYPE_AMEX);
        out.put("Discover", Creditcard.CREDITCARDTYPE_DISCOVER);
        return out;
    }

    public LinkedHashMap getMonthsForCreditcard(){
        LinkedHashMap out = new LinkedHashMap();
        out.put("Jan(01)", 1);
        out.put("Feb(02)", 2);
        out.put("Mar(03)", 3);
        out.put("Apr(04)", 4);
        out.put("May(05)", 5);
        out.put("Jun(06)", 6);
        out.put("Jul(07)", 7);
        out.put("Aug(08)", 8);
        out.put("Sep(09)", 9);
        out.put("Oct(10)", 10);
        out.put("Nov(11)", 11);
        out.put("Dec(12)", 12);
        return out;
    }

    public LinkedHashMap getYearsForCreditcard(){
        LinkedHashMap out = new LinkedHashMap();
        out.put("2006", 2006);
        out.put("2007", 2007);
        out.put("2008", 2008);
        out.put("2009", 2009);
        out.put("2010", 2010);
        out.put("2011", 2011);
        out.put("2012", 2012);
        out.put("2013", 2013);
        out.put("2014", 2014);
        out.put("2015", 2015);
        out.put("2016", 2016);
        out.put("2017", 2017);
        return out;
    }

    public void setChargemethods(){

    }




    public String getCcnum() {
        return ccnum;
    }

    public void setCcnum(String ccnum) {
        this.ccnum = ccnum;
    }

    public int getCctype() {
        return cctype;
    }

    public void setCctype(int cctype) {
        this.cctype = cctype;
    }

    public String getCvv2() {
        return cvv2;
    }

    public void setCvv2(String cvv2) {
        this.cvv2 = cvv2;
    }

    public int getCcexpmo() {
        return ccexpmo;
    }

    public void setCcexpmo(int ccexpmo) {
        this.ccexpmo = ccexpmo;
    }

    public int getCcexpyear() {
        return ccexpyear;
    }

    public void setCcexpyear(int ccexpyear) {
        this.ccexpyear = ccexpyear;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getCcstate() {
        return ccstate;
    }

    public void setCcstate(String ccstate) {
        this.ccstate = ccstate;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCccity() {
        return cccity;
    }

    public void setCccity(String cccity) {
        this.cccity = cccity;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getMerchantsessionid() {
        return merchantsessionid;
    }

    public void setMerchantsessionid(String merchantsessionid) {
        this.merchantsessionid = merchantsessionid;
    }
}
