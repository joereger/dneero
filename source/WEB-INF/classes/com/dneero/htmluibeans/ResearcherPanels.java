package com.dneero.htmluibeans;

import org.apache.log4j.Logger;

import java.util.*;
import java.io.Serializable;


import com.dneero.util.Time;
import com.dneero.util.Num;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.Panel;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;

/**
 * User: Joe Reger Jr
 * Date: Feb 8, 2007
 * Time: 12:22:24 PM
 */
public class ResearcherPanels implements Serializable {


    private List listitems;
    private String newpanelname = "My Panel";
    private String msg = "";

    public ResearcherPanels() {

    }




    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (Pagez.getUserSession()!=null && Pagez.getUserSession().getUser()!=null && Pagez.getUserSession().getUser().getResearcherid()>0){
            logger.debug("userSession, user and researcher not null");
            logger.debug("into loop for userSession.getUser().getResearcher().getResearcherid()="+Pagez.getUserSession().getUser().getResearcherid());
            listitems = new ArrayList<ResearcherPanelsListitem>();
            List items = HibernateUtil.getSession().createQuery("from Panel where researcherid='"+Pagez.getUserSession().getUser().getResearcherid()+"' order by panelid asc").list();
            for (Iterator iterator = items.iterator(); iterator.hasNext();) {
                Panel panel = (Panel) iterator.next();
                ResearcherPanelsListitem li = new ResearcherPanelsListitem();
                li.setPanel(panel);
                li.setNumberofmembers(panel.getPanelmemberships().size());
                listitems.add(li);
                logger.debug("added panelid="+panel.getPanelid());
            }
        }
    }

    public void createNewPanel() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (newpanelname==null || newpanelname.equals("")){
            newpanelname = "My Panel ("+Time.dateformatdate(Calendar.getInstance())+")";
        }
        //Don't allow duplicate panel names
        int duplicatecount = 0;
        for (Iterator iterator = listitems.iterator(); iterator.hasNext();) {
            ResearcherPanelsListitem researcherPanelsListitem = (ResearcherPanelsListitem) iterator.next();
            Panel p = researcherPanelsListitem.getPanel();
            if (p.getName().equals(newpanelname)){
                duplicatecount = duplicatecount + 1;
            }
        }
        if (duplicatecount>0){
            newpanelname = newpanelname + " #" + (duplicatecount+1);
        }
        Panel panel = new Panel();
        panel.setCreatedate(new Date());
        panel.setName(newpanelname);
        panel.setDescription("");
        panel.setIssystempanel(false);
        panel.setResearcherid(Pagez.getUserSession().getUser().getResearcherid());
        try{panel.save();}catch (Exception ex){logger.error("",ex);}
        initBean();
    }

    public void deletePanel() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (Num.isinteger(Pagez.getRequest().getParameter("panelid"))){
            Panel panel = Panel.get(Integer.parseInt(Pagez.getRequest().getParameter("panelid")));
            if (panel.canEdit(Pagez.getUserSession().getUser())){
                try{
                    panel.delete();
                    initBean();
                }catch (Exception ex){
                    logger.error("",ex);
                    vex.addValidationError("Error deleting panel.");
                    throw vex;
                }
            }
        }
    }


    public List getListitems() {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("getListitems");
        //sort(getSort(), isAscending());
        sort("name", true);
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
                ResearcherPanelsListitem obj1 = (ResearcherPanelsListitem)o1;
                ResearcherPanelsListitem obj2 = (ResearcherPanelsListitem)o2;
                if (column == null) {
                    return 0;
                }
                if (obj1!=null && obj2!=null && column.equals("name")) {
                    return ascending ? obj1.getPanel().getName().compareTo(obj2.getPanel().getName()) : obj2.getPanel().getName().compareTo(obj1.getPanel().getName());
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

    public String getNewpanelname() {
        return newpanelname;
    }

    public void setNewpanelname(String newpanelname) {
        this.newpanelname = newpanelname;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
