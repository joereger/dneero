package com.dneero.formbeans;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Iterator;

import com.dneero.session.UserSession;
import com.dneero.session.Roles;
import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;
import com.dneero.dao.*;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class ResearcherDetails {

    //Form props
    private String billingname;
    private String billingaddress1;
    private String billingaddress2;
    private String billingcity;
    private String billingstate;
    private String billingzip;
    private int billingpaymentmethod;
    private String ccnum;
    private int ccexpmonth;
    private int ccexpyear;

    //Other props
    private int userid;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public ResearcherDetails(){
        UserSession userSession = Jsf.getUserSession();
        if (userSession.getUser()!=null && userSession.getUser().getResearcher()!=null){
            Researcher researcher = userSession.getUser().getResearcher();
            userid = researcher.getUserid();
        }
    }

    public void load(){
        logger.debug("load called");
        UserSession userSession = Jsf.getUserSession();
        if (userSession.getUser()!=null && userSession.getUser().getResearcher()!=null && userSession.getUser().getResearcher().getResearcherbilling()!=null){
            Researcherbilling researcherbilling = userSession.getUser().getResearcher().getResearcherbilling();

            billingname = researcherbilling.getBillingname();
            billingaddress1 = researcherbilling.getBillingaddress1();
            billingaddress2 = researcherbilling.getBillingaddress2();
            billingcity = researcherbilling.getBillingcity();
            billingstate = researcherbilling.getBillingstate();
            billingzip = researcherbilling.getBillingzip();
            billingpaymentmethod = researcherbilling.getBillingpaymentmethod();
            ccnum = researcherbilling.getCcnum();
            ccexpmonth = researcherbilling.getCcexpmonth();
            ccexpyear = researcherbilling.getCcexpyear();
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

            researcher.setUserid(userSession.getUser().getUserid());


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
                try{
                    role.save();
                } catch (GeneralException gex){
                    Jsf.setFacesMessage("Error saving role record: "+gex.getErrorsAsSingleString());
                    logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                    return null;
                }
            }

            Researcherbilling researcherBilling = userSession.getUser().getResearcher().getResearcherbilling();
            if (researcherBilling==null){
                researcherBilling = new Researcherbilling();
            }

            researcherBilling.setResearcherid(researcher.getResearcherid());
            researcherBilling.setBillingname(billingname);
            researcherBilling.setBillingaddress1(billingaddress1);
            researcherBilling.setBillingaddress2(billingaddress2);
            researcherBilling.setBillingcity(billingcity);
            researcherBilling.setBillingstate(billingstate);
            researcherBilling.setBillingzip(billingzip);
            researcherBilling.setBillingpaymentmethod(billingpaymentmethod);
            researcherBilling.setCcnum(ccnum);
            researcherBilling.setCcexpmonth(ccexpmonth);
            researcherBilling.setCcexpyear(ccexpyear);
            try{
                researcherBilling.save();
            } catch (GeneralException gex){
                logger.debug("Save research billing failed: " + gex.getErrorsAsSingleString());
                String message = "Researcher Details save failed: " + gex.getErrorsAsSingleString();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO, message, message));
                return null;
            }



            userSession.getUser().refresh();

            if (isnewresearcher){
                return "success_newresearcher";
            } else {
                return "success";
            }
        } else {
            Jsf.setFacesMessage("UserSession.getUser() is null.  Please log in.");
            return null;
        }
    }



    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getBillingname() {
        return billingname;
    }

    public void setBillingname(String billingname) {
        this.billingname = billingname;
    }

    public String getBillingaddress1() {
        return billingaddress1;
    }

    public void setBillingaddress1(String billingaddress1) {
        this.billingaddress1 = billingaddress1;
    }

    public String getBillingaddress2() {
        return billingaddress2;
    }

    public void setBillingaddress2(String billingaddress2) {
        this.billingaddress2 = billingaddress2;
    }

    public String getBillingcity() {
        return billingcity;
    }

    public void setBillingcity(String billingcity) {
        this.billingcity = billingcity;
    }

    public String getBillingstate() {
        return billingstate;
    }

    public void setBillingstate(String billingstate) {
        this.billingstate = billingstate;
    }

    public String getBillingzip() {
        return billingzip;
    }

    public void setBillingzip(String billingzip) {
        this.billingzip = billingzip;
    }

    public int getBillingpaymentmethod() {
        return billingpaymentmethod;
    }

    public void setBillingpaymentmethod(int billingpaymentmethod) {
        this.billingpaymentmethod = billingpaymentmethod;
    }

    public String getCcnum() {
        return ccnum;
    }

    public void setCcnum(String ccnum) {
        this.ccnum = ccnum;
    }

    public int getCcexpmonth() {
        return ccexpmonth;
    }

    public void setCcexpmonth(int ccexpmonth) {
        this.ccexpmonth = ccexpmonth;
    }

    public int getCcexpyear() {
        return ccexpyear;
    }

    public void setCcexpyear(int ccexpyear) {
        this.ccexpyear = ccexpyear;
    }
}
