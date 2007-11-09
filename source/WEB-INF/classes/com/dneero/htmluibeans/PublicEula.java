package com.dneero.htmluibeans;

import com.dneero.eula.EulaHelper;
import com.dneero.dao.User;
import com.dneero.dao.Usereula;

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

    public PublicEula(){

    }



    public void initBean(){
        eula = EulaHelper.getMostRecentEula().getEula();
    }


    public String getEula() {
        return eula;
    }

    public void setEula(String eula) {
        this.eula = eula;
    }
}
