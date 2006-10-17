package com.dneero.formbeans;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.dneero.session.UserSession;
import com.dneero.session.Roles;
import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;
import com.dneero.util.Num;
import com.dneero.dao.*;
import com.dneero.money.PaymentMethod;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class ResearcherDetails {

    private int chargemethod;
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

    public ResearcherDetails(){
        load();
    }

    public void load(){
        logger.debug("load called");
        UserSession userSession = Jsf.getUserSession();
        if (userSession.getUser()!=null && userSession.getUser().getBlogger()!=null){
            Researcher researcher = userSession.getUser().getResearcher();

            chargemethod = userSession.getUser().getChargemethod();

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

        Researcher researcher;
        boolean isnewresearcher = false;
        if (userSession.getUser()!=null && userSession.getUser().getResearcher()!=null){
            researcher =  userSession.getUser().getResearcher();
        } else {
            researcher = new Researcher();
            isnewresearcher = true;
        }

        if (userSession.getUser()!=null){


            //Start validation
            if (chargemethod==PaymentMethod.PAYMENTMETHODCREDITCARD){
                //@todo better validation
                if (ccnum.equals("")){
                    Jsf.setFacesMessage("researcherdetails:ccnum", "You've chosen to be paid via credit card so you must provide a credit card number.");
                    return "";
                }
            }
            //End validation








            researcher.setUserid(userSession.getUser().getUserid());
            userSession.getUser().setResearcher(researcher);

            try{
                researcher.save();
            } catch (GeneralException gex){
                Jsf.setFacesMessage("Error saving record: "+gex.getErrorsAsSingleString());
                logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                return null;
            }


            boolean hasroleassigned = false;
            if (userSession.getUser()!=null && userSession.getUser().getUserroles()!=null){
                for (Iterator iterator = userSession.getUser().getUserroles().iterator(); iterator.hasNext();) {
                    Userrole role =  (Userrole)iterator.next();
                    if (role.getRoleid()== Roles.RESEARCHER){
                        hasroleassigned = true;
                    }
                }
            }
            if (!hasroleassigned && userSession.getUser()!=null){
                Userrole role = new Userrole();
                role.setUserid(userSession.getUser().getUserid());
                role.setRoleid(Roles.RESEARCHER);
                userSession.getUser().getUserroles().add(role);
                try{
                    role.save();
                } catch (GeneralException gex){
                    Jsf.setFacesMessage("Error saving role record: "+gex.getErrorsAsSingleString());
                    logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                    return null;
                }
            }



            Creditcard cc = new Creditcard();
            if(chargemethod==PaymentMethod.PAYMENTMETHODCREDITCARD){
                if(userSession.getUser().getPaymethodcreditcardid()>0){
                    cc = Creditcard.get(userSession.getUser().getPaymethodcreditcardid());
                }
                cc.setCcexpmo(ccexpmo);
                cc.setCcexpyear(ccexpyear);
                cc.setCcnum(ccnum);
                cc.setCctype(cctype);
                cc.setCity(cccity);
                cc.setCvv2(cvv2);
                cc.setFirstname(firstname);
                //@todo set IP Address for paypal
                cc.setIpaddress("192.168.1.1");
                cc.setLastname(lastname);
                //@todo set merchant sessionid for paypal
                cc.setMerchantsessionid("12345");
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
            }


            userSession.getUser().setChargemethod(chargemethod);
            if(chargemethod==PaymentMethod.PAYMENTMETHODCREDITCARD){
                userSession.getUser().setChargemethodcreditcardid(cc.getCreditcardid());
            }

            try{
                userSession.getUser().save();
            } catch (GeneralException gex){
                Jsf.setFacesMessage("Error saving record: "+gex.getErrorsAsSingleString());
                logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                return null;
            }



            userSession.getUser().refresh();

            if (isnewresearcher){
                return "success_newresearcher";
            } else {
                return "accountmain";
            }
        } else {
            Jsf.setFacesMessage("UserSession.getUser() is null.  Please log in.");
            return null;
        }
    }

    public LinkedHashMap getChargemethods(){
        LinkedHashMap out = new LinkedHashMap();
        out.put("Invoice Me", PaymentMethod.PAYMENTMETHODMANUAL);
        out.put("Credit Card", PaymentMethod.PAYMENTMETHODCREDITCARD);
        return out;
    }

    public LinkedHashMap getCreditcardtypes(){
        LinkedHashMap out = new LinkedHashMap();
        out.put("Visa", Creditcard.CREDITCARDTYPE_VISA);
        out.put("Master Card", Creditcard.CREDITCARDTYPE_MASTERCARD);
        out.put("American Express", Creditcard.CREDITCARDTYPE_AMEX);
        out.put("Discover", Creditcard.CREDITCARDTYPE_DISCOVER);
        return out;
    }

    public void setChargemethods(){

    }


    public int getChargemethod() {
        return chargemethod;
    }

    public void setChargemethod(int chargemethod) {
        this.chargemethod = chargemethod;
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
