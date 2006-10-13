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
    private String chargemethodccnum;
    private String chargemethodccexpmo;
    private String chargemethodccexpyear;


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
            chargemethodccnum = userSession.getUser().getChargemethodccnum();
            chargemethodccexpmo = userSession.getUser().getChargemethodccexpmo();
            chargemethodccexpyear = userSession.getUser().getChargemethodccexpyear();
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
                if (chargemethodccnum==null || chargemethodccexpmo==null || chargemethodccexpyear==null){
                    Jsf.setFacesMessage("researcherdetails:chargemethodccnum", "You've chosen to be paid via credit card so you must provide a credit card number.");
                    return "";
                }
                if (chargemethodccnum.equals("")){
                    Jsf.setFacesMessage("researcherdetails:chargemethodccnum", "You've chosen to be paid via credit card so you must provide a credit card number.");
                    return "";
                }
                if (!Num.isinteger(chargemethodccexpyear)){
                    Jsf.setFacesMessage("researcherdetails:chargemethodccexpyear", "Year must be a valid number over 2006.");
                    return "";
                }
                if (!Num.isinteger(chargemethodccexpmo)){
                    Jsf.setFacesMessage("researcherdetails:chargemethodccexpmo", "Month must be a valid number.");
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


            userSession.getUser().setChargemethod(chargemethod);
            userSession.getUser().setChargemethodccexpmo(chargemethodccexpmo);
            userSession.getUser().setChargemethodccexpyear(chargemethodccexpyear);
            userSession.getUser().setChargemethodccnum(chargemethodccnum);

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
                return "success";
            }
        } else {
            Jsf.setFacesMessage("UserSession.getUser() is null.  Please log in.");
            return null;
        }
    }

    public LinkedHashMap getChargemethods(){
        LinkedHashMap out = new LinkedHashMap();
        out.put("Invoice Me", 0);
        out.put("Credit Card", 1);
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

    public String getChargemethodccnum() {
        return chargemethodccnum;
    }

    public void setChargemethodccnum(String chargemethodccnum) {
        this.chargemethodccnum = chargemethodccnum;
    }

    public String getChargemethodccexpmo() {
        return chargemethodccexpmo;
    }

    public void setChargemethodccexpmo(String chargemethodccexpmo) {
        this.chargemethodccexpmo = chargemethodccexpmo;
    }

    public String getChargemethodccexpyear() {
        return chargemethodccexpyear;
    }

    public void setChargemethodccexpyear(String chargemethodccexpyear) {
        this.chargemethodccexpyear = chargemethodccexpyear;
    }
}
