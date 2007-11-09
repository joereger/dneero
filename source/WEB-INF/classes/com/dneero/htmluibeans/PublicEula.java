package com.dneero.htmluibeans;

import com.dneero.eula.EulaHelper;
import com.dneero.dao.User;
import com.dneero.dao.Usereula;
import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;

import java.io.Serializable;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Nov 10, 2006
 * Time: 2:48:46 PM
 */
public class PublicEula implements Serializable {

    private String eula;
    private String init;

    public PublicEula(){

    }

    public String beginView(){
        load();
        return "publiceula";
    }

    private void load(){
        eula = EulaHelper.getMostRecentEula().getEula();
    }

    public String getInit() {
        return init;
    }

    public void setInit(String init) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (init!=null && init.equals("doinit")){
            logger.debug("init = doinit so calling load()");
            load();
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
