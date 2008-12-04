package com.dneero.htmluibeans;

import com.dneero.util.Num;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.User;
import com.dneero.dao.Iptrack;
import com.dneero.htmlui.Pagez;
import com.dneero.iptrack.IptrackAnalyzer;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.Serializable;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class CustomercareIptrack implements Serializable {

    private List<Iptrack> iptracks;
    private String searchuserid="";
    private String searchactivitytypeid="";
    private String searchip="";
    private String searchoctet1="";
    private String searchoctet2="";
    private String searchoctet3="";
    private String searchoctet4="";

    public CustomercareIptrack() {

    }



    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("load()");

        

        //if (Pagez.getRequest().getParameter("action")!=null && !Pagez.getRequest().getParameter("action").equals("")){
            Criteria crit = HibernateUtil.getSession().createCriteria(Iptrack.class);
            if (searchuserid!=null && !searchuserid.equals("") && Num.isinteger(searchuserid)){
                crit.add(Restrictions.eq("userid", Integer.parseInt(searchuserid)));
            } else {
                crit.add(Restrictions.gt("userid", 0));
            }
            if (searchactivitytypeid!=null && !searchactivitytypeid.equals("")  && Num.isinteger(searchactivitytypeid)){
                crit.add(Restrictions.eq("activitytypeid", Integer.parseInt(searchactivitytypeid)));
            }
            if (searchip!=null && !searchip.equals("")){
                crit.add(Restrictions.like("ip", "%"+searchip+"%"));
            }
            if (searchoctet1!=null && !searchoctet1.equals("") && Num.isinteger(searchoctet1)){
                crit.add(Restrictions.eq("octet1", Integer.parseInt(searchoctet1)));
            }
            if (searchoctet2!=null && !searchoctet2.equals("") && Num.isinteger(searchoctet2)){
                crit.add(Restrictions.eq("octet2", Integer.parseInt(searchoctet2)));
            }
            if (searchoctet3!=null && !searchoctet3.equals("") && Num.isinteger(searchoctet3)){
                crit.add(Restrictions.eq("octet3", Integer.parseInt(searchoctet3)));
            }
            if (searchoctet4!=null && !searchoctet4.equals("") && Num.isinteger(searchoctet4)){
                crit.add(Restrictions.eq("octet4", Integer.parseInt(searchoctet4)));
            }
            iptracks= (List<Iptrack>)crit.addOrder(Order.desc("iptrackid")).list();
//        } else {
//            iptracks= new ArrayList<Iptrack>();
//        }
    }

    public List<Iptrack> getIptracks() {
        return iptracks;
    }

    public void setIptracks(List<Iptrack> iptracks) {
        this.iptracks=iptracks;
    }

    public String getSearchuserid() {
        return searchuserid;
    }

    public void setSearchuserid(String searchuserid) {
        this.searchuserid=searchuserid;
    }

    public String getSearchactivitytypeid() {
        return searchactivitytypeid;
    }

    public void setSearchactivitytypeid(String searchactivitytypeid) {
        this.searchactivitytypeid=searchactivitytypeid;
    }

    public String getSearchip() {
        return searchip;
    }

    public void setSearchip(String searchip) {
        this.searchip=searchip;
    }

    public String getSearchoctet1() {
        return searchoctet1;
    }

    public void setSearchoctet1(String searchoctet1) {
        this.searchoctet1=searchoctet1;
    }

    public String getSearchoctet2() {
        return searchoctet2;
    }

    public void setSearchoctet2(String searchoctet2) {
        this.searchoctet2=searchoctet2;
    }

    public String getSearchoctet3() {
        return searchoctet3;
    }

    public void setSearchoctet3(String searchoctet3) {
        this.searchoctet3=searchoctet3;
    }

    public String getSearchoctet4() {
        return searchoctet4;
    }

    public void setSearchoctet4(String searchoctet4) {
        this.searchoctet4=searchoctet4;
    }
}