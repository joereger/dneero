package com.dneero.formbeans;

import com.dneero.eula.EulaHelper;
import com.dneero.dao.User;
import com.dneero.dao.Usereula;
import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;

import java.util.Date;
import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Nov 10, 2006
 * Time: 2:48:46 PM
 */
public class LoginAgreeNewEula implements Serializable {

    private String eula;

    public LoginAgreeNewEula(){

    }

    public String beginView(){
        load();
        return "loginagreeneweula";
    }

    private void load(){
        eula = EulaHelper.getMostRecentEula().getEula();
    }

    public String agree(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        User user = Jsf.getUserSession().getUser();

        if (!eula.equals(EulaHelper.getMostRecentEula().getEula())){
            Jsf.setFacesMessage("eulaform:eula", "The end user license can't be edited.");
            eula = EulaHelper.getMostRecentEula().getEula();
            return null;
        }

        Usereula usereula = new Usereula();
        usereula.setDate(new Date());
        usereula.setEulaid(EulaHelper.getMostRecentEula().getEulaid());
        usereula.setUserid(user.getUserid());
        usereula.setIp(Jsf.getRemoteAddr());
        try{
            usereula.save();
        } catch (GeneralException gex){
            logger.error(gex);
            logger.debug("agree failed: " + gex.getErrorsAsSingleString());
            Jsf.setFacesMessage("eulaform:eula", "Error... please try again.");
            return null;
        }
        Jsf.getUserSession().setIseulaok(true);

        return "accountindex";



    }


    public String getEula() {
        return eula;
    }

    public void setEula(String eula) {
        this.eula = eula;
    }
}
