package com.dneero.htmluibeans;

import com.dneero.dao.Eula;
import com.dneero.dao.Pl;
import com.dneero.eula.EulaHelper;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.privatelabel.PlFinder;
import com.dneero.util.GeneralException;
import com.dneero.util.Num;
import com.dneero.util.Time;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Nov 10, 2006
 * Time: 3:02:56 PM
 */
public class SysadminEditEula implements Serializable {

    private String eula;
    private int eulaid;
    private String date;
    private int plid;

    public SysadminEditEula(){

    }



    public void initBean(){
        Pl pl = PlFinder.getDefaultPl();
        plid = pl.getPlid();
        if (Num.isinteger(Pagez.getRequest().getParameter("plid"))){
            plid = Integer.parseInt(Pagez.getRequest().getParameter("plid"));
            pl = Pl.get(plid);
        }
        eula = EulaHelper.getMostRecentEula(pl).getEula();
        eulaid = EulaHelper.getMostRecentEula(pl).getEulaid();
        date = Time.dateformatcompactwithtime(Time.getCalFromDate(EulaHelper.getMostRecentEula(pl).getDate()));
    }

    public void edit() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        Pl pl = Pl.get(plid);
        if (!eula.equals(EulaHelper.getMostRecentEula(pl).getEula())){
            Eula eulaObj = new Eula();
            eulaObj.setDate(new Date());
            eulaObj.setEula(eula);
            eulaObj.setPlid(plid);
            try{
                eulaObj.save();
            } catch (GeneralException gex){
                logger.error(gex);
                logger.debug("agree failed: " + gex.getErrorsAsSingleString());
                Pagez.getUserSession().setMessage("Error... please try again.");
                throw new ValidationException("Error with db.");
            }
            EulaHelper.refreshMostRecentEula(pl);
        }
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

    public int getPlid() {
        return plid;
    }

    public void setPlid(int plid) {
        this.plid = plid;
    }
}
