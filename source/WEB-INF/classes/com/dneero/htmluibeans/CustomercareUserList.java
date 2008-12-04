package com.dneero.htmluibeans;

import com.dneero.util.SortableList;
import com.dneero.util.Num;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.User;
import com.dneero.htmlui.Pagez;

import java.util.List;
import java.util.Comparator;
import java.util.Collections;
import java.util.ArrayList;
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
public class CustomercareUserList implements Serializable {

    private List<User> users;
    private String searchuserid="";
    private String searchfirstname="";
    private String searchlastname="";
    private String searchemail="";
    private boolean searchfacebookers=false;
    private String searchreferredbyuserid="";

    public CustomercareUserList() {

    }



    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("load()");
        logger.debug("searchfirstname="+searchfirstname);
        logger.debug("searchlastname="+searchlastname);
        logger.debug("searchemail="+searchemail);
        if (Pagez.getRequest().getParameter("action")!=null && !Pagez.getRequest().getParameter("action").equals("")){

            Criteria crit = HibernateUtil.getSession().createCriteria(User.class);
            if (searchuserid!=null && !searchuserid.equals("") && Num.isinteger(searchuserid)){
                crit.add(Restrictions.eq("userid", Integer.parseInt(searchuserid)));
            } else {
                crit.add(Restrictions.gt("userid", 0));
            }
            if (searchfirstname!=null && !searchfirstname.equals("")){
                crit.add(Restrictions.like("firstname", "%"+searchfirstname+"%"));
            }
            if (searchlastname!=null && !searchlastname.equals("")){
                crit.add(Restrictions.like("lastname", "%"+searchlastname+"%"));
            }
            if (searchemail!=null && !searchemail.equals("")){
                crit.add(Restrictions.like("email", "%"+searchemail+"%"));
            }
            if(searchfacebookers){
                crit.add(Restrictions.gt("facebookuserid", 0));
            }
            if (searchreferredbyuserid!=null && !searchreferredbyuserid.equals("") && Num.isinteger(searchreferredbyuserid)){
                crit.add(Restrictions.eq("referredbyuserid", Integer.parseInt(searchreferredbyuserid)));
            }
            users = (List<User>)crit.addOrder(Order.desc("userid")).list();
        } else {
            users = new ArrayList<User>();
        }

    }


    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users=users;
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }




    public String getSearchfirstname() {
        return searchfirstname;
    }

    public void setSearchfirstname(String searchfirstname) {
        this.searchfirstname = searchfirstname;
    }

    public String getSearchlastname() {
        return searchlastname;
    }

    public void setSearchlastname(String searchlastname) {
        this.searchlastname = searchlastname;
    }

    public String getSearchemail() {
        return searchemail;
    }

    public void setSearchemail(String searchemail) {
        this.searchemail = searchemail;
    }

    public String getSearchuserid() {
        return searchuserid;
    }

    public void setSearchuserid(String searchuserid) {
        this.searchuserid = searchuserid;
    }

    public boolean getSearchfacebookers() {
        return searchfacebookers;
    }

    public void setSearchfacebookers(boolean searchfacebookers) {
        this.searchfacebookers = searchfacebookers;
    }

    public String getSearchreferredbyuserid() {
        return searchreferredbyuserid;
    }

    public void setSearchreferredbyuserid(String searchreferredbyuserid) {
        this.searchreferredbyuserid=searchreferredbyuserid;
    }
}
