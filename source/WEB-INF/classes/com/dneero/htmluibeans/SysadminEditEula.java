package com.dneero.htmluibeans;

import com.dneero.eula.EulaHelper;
import com.dneero.dao.Eula;
import com.dneero.util.GeneralException;

import com.dneero.util.Time;
import com.dneero.htmlui.Pagez;
import com.mysql.jdbc.TimeUtil;

import java.util.Date;
import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Nov 10, 2006
 * Time: 3:02:56 PM
 */
public class SysadminEditEula implements Serializable {

    private String eula;
    private int eulaid;
    private String date;

    public SysadminEditEula(){

    }



    public void initBean(){
        eula = EulaHelper.getMostRecentEula().getEula();
        eulaid = EulaHelper.getMostRecentEula().getEulaid();
        date = Time.dateformatcompactwithtime(Time.getCalFromDate(EulaHelper.getMostRecentEula().getDate()));
    }

    public String edit(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (!eula.equals(EulaHelper.getMostRecentEula().getEula())){
            Eula eulaObj = new Eula();
            eulaObj.setDate(new Date());
            eulaObj.setEula(eula);
            try{
                eulaObj.save();
            } catch (GeneralException gex){
                logger.error(gex);
                logger.debug("agree failed: " + gex.getErrorsAsSingleString());
                Pagez.getUserSession().setMessage("Error... please try again.");
                return null;
            }
            EulaHelper.refreshMostRecentEula();
        }
        return "sysadminhome";
    }


    public String getEula() {
        return eula;
    }

    public void setEula(String eula) {
        this.eula = eula;
    }


    public int getEulaid() {
        return eulaid;
    }

    public void setEulaid(int eulaid) {
        this.eulaid = eulaid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
