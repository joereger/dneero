package com.dneero.formbeans;

import org.apache.log4j.Logger;

import java.util.*;
import java.io.Serializable;

import com.dneero.util.Jsf;
import com.dneero.util.SortableList;
import com.dneero.util.Num;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.Panelmembership;
import com.dneero.dao.Blogger;
import com.dneero.dao.User;

/**
 * User: Joe Reger Jr
 * Date: Feb 8, 2007
 * Time: 12:22:47 PM
 */
public class ResearcherPanelsListBloggers implements Serializable {

    private List listitems;
    private int panelid = 0;

    public ResearcherPanelsListBloggers() {


    }

    public String beginView(){
        load();   
        return "researcherpanelslistbloggersinpanel";
    }

    private void load(){
        Logger logger = Logger.getLogger(this.getClass().getName());

        if (Jsf.getRequestParam("panelid")!=null && Num.isinteger(Jsf.getRequestParam("panelid"))){
            panelid = Integer.parseInt(Jsf.getRequestParam("panelid"));
        }

        if (Jsf.getUserSession()!=null && Jsf.getUserSession().getUser()!=null && Jsf.getUserSession().getUser().getResearcherid()>0){
            logger.debug("userSession, user and researcher not null");
            logger.debug("into loop for panelid="+panelid);
            load();
        }

        listitems = new ArrayList<ResearcherPanelsListBloggersListitem>();
        List panelmembers = HibernateUtil.getSession().createQuery("from Panelmembership where panelid='"+panelid+"'").list();
        for (Iterator iterator = panelmembers.iterator(); iterator.hasNext();) {
            Panelmembership panelmembership = (Panelmembership) iterator.next();
            ResearcherPanelsListBloggersListitem li = new ResearcherPanelsListBloggersListitem();
            li.setPanelmembership(panelmembership);
            li.setBlogger(Blogger.get(panelmembership.getBloggerid()));
            li.setUser(User.get(Blogger.get(panelmembership.getBloggerid()).getUserid()));
            listitems.add(li);
            
        }
    }

    public String removeFromPanel(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (Jsf.getRequestParam("panelmembershipid")!=null && Num.isinteger(Jsf.getRequestParam("panelmembershipid"))){
            Panelmembership panelmembership = Panelmembership.get(Integer.parseInt(Jsf.getRequestParam("panelmembershipid")));
            try{panelmembership.delete();}catch(Exception ex){logger.debug(ex);}
            load();
        }
        return "researcherpanelslistbloggersinpanel";
    }




    public List getListitems() {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("getListitems");
        sort("userlastname", true);
        return listitems;
    }

    public void setListitems(List listitems) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("setListitems");
        this.listitems = listitems;
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }

    protected void sort(final String column, final boolean ascending) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("sort called");
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                ResearcherPanelsListBloggersListitem obj1 = (ResearcherPanelsListBloggersListitem)o1;
                ResearcherPanelsListBloggersListitem obj2 = (ResearcherPanelsListBloggersListitem)o2;
                if (column == null) {
                    return 0;
                }
                if (obj1!=null && obj2!=null && column.equals("bloggerid")){
                    return ascending ? obj2.getBlogger().getBloggerid()-obj1.getBlogger().getBloggerid() : obj1.getBlogger().getBloggerid()-obj2.getBlogger().getBloggerid() ;
                } else if (obj1!=null && obj2!=null && column.equals("userlastname")){
                    return ascending ? obj1.getUser().getLastname().compareTo(obj2.getUser().getLastname()) : obj2.getUser().getLastname().compareTo(obj1.getUser().getLastname());
                } else {
                    return 0;
                }
            }
        };

        //sort and also set our model with the new sort, since using DataTable with
        //ListDataModel on front end
        if (listitems != null && !listitems.isEmpty()) {
            logger.debug("sorting listitems and initializing ListDataModel");
            Collections.sort(listitems, comparator);
        }
    }


}
