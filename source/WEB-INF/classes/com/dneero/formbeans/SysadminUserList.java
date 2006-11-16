package com.dneero.formbeans;

import com.dneero.util.SortableList;
import com.dneero.util.Num;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.User;
import com.dneero.dao.Survey;

import java.util.List;
import java.util.Comparator;
import java.util.Collections;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class SysadminUserList extends SortableList {

    Logger logger = Logger.getLogger(this.getClass().getName());
    private List users;
    private String searchuserid="";
    private String searchfirstname="";
    private String searchlastname="";
    private String searchemail="";

    public SysadminUserList() {
        super("userid");
        logger.debug("instanciated");
        load();
    }

    private void load(){
        logger.debug("load()");
        logger.debug("searchfirstname="+searchfirstname);
        logger.debug("searchlastname="+searchlastname);
        logger.debug("searchemail="+searchemail);
        Criteria crit = HibernateUtil.getSession().createCriteria(User.class);
        crit.add(Restrictions.gt("userid", 0));
        if (searchuserid!=null && !searchuserid.equals("") && Num.isinteger(searchuserid)){
            crit.add(Restrictions.eq("userid", searchuserid));
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
        users = crit.list();
    }
    
    public String search(){
        load();
        return "userlist";
    }

    public List getUsers() {
        //logger.debug("getSurveys");
        sort(getSort(), isAscending());
        return users;
    }

    public void setUsers(List users) {
        //logger.debug("setSurveys");
        this.users = users;
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }

    protected void sort(final String column, final boolean ascending) {
        //logger.debug("sort called");
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                User user1 = (User)o1;
                User user2 = (User)o2;

                if (column == null) {
                    return 0;
                }
                if (column.equals("email")) {
                    return ascending ? user1.getEmail().compareTo(user2.getEmail()) : user2.getEmail().compareTo(user1.getEmail());
                } else if (column.equals("firstname")) {
                    return ascending ? user1.getFirstname().compareTo(user2.getFirstname()) : user2.getFirstname().compareTo(user1.getFirstname());
                } else if (column.equals("userid")){
                    return ascending ? user2.getUserid()-user1.getUserid() : user1.getUserid()-user2.getUserid() ;
                } else {
                    return 0;
                }
            }
        };

        //sort and also set our model with the new sort, since using DataTable with
        //ListDataModel on front end
        if (users != null && !users.isEmpty()) {
            //logger.debug("sorting users and initializing ListDataModel");
            Collections.sort(users, comparator);
        }
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
}
