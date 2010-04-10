package com.dneero.htmluibeans;

import com.dneero.dao.User;
import com.dneero.dao.Usereula;
import com.dneero.eula.EulaHelper;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.util.GeneralException;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Nov 10, 2006
 * Time: 2:48:46 PM
 */
public class LoginAgreeNewEula implements Serializable {

    private String eula;
    private String init;

    public LoginAgreeNewEula(){

    }

    public void initBean(){
        eula = EulaHelper.getMostRecentEula(Pagez.getUserSession().getPl()).getEula();
    }

    public void agree() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        User user = Pagez.getUserSession().getUser();

        if (!eula.equals(EulaHelper.getMostRecentEula(Pagez.getUserSession().getPl()).getEula())){
            //@todo LoginAgreeNewEula validate eula
            //vex.addValidationError("The end user license can't be edited.");
            //eula = EulaHelper.getMostRecentEula(Pagez.getUserSession().getPl()).getEula();
            //throw vex;
        }

        Usereula usereula = new Usereula();
        usereula.setDate(new Date());
        usereula.setEulaid(EulaHelper.getMostRecentEula(Pagez.getUserSession().getPl()).getEulaid());
        usereula.setUserid(user.getUserid());
        usereula.setIp(Pagez.getRequest().getRemoteAddr());
        try{
            usereula.save();
        } catch (GeneralException gex){
            logger.error(gex);
            logger.debug("agree failed: " + gex.getErrorsAsSingleString());
            vex.addValidationError("Error... please try again.");
            throw vex;
        }
        Pagez.getUserSession().setIseulaok(true);




    }


    public String getInit() {
        return init;
    }

    public void setInit(String init) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (init!=null && init.equals("doinit")){
            logger.debug("init = doinit so calling load()");
            initBean();
        } else {
            logger.debug("init null or not doinit");
        }
    }

    public String getEula() {
        return eula;
    }

    public void setEula(String eula) {
        this.eula = eula;
    }
}
