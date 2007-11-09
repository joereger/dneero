package com.dneero.htmluibeans;

import com.dneero.dao.Panel;
import com.dneero.util.Jsf;
import com.dneero.util.Num;
import com.dneero.htmlui.Pagez;
import org.apache.log4j.Logger;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Feb 17, 2007
 * Time: 9:32:33 AM
 */
public class ResearcherPanelsEdit implements Serializable {

    private Panel panel;

    public ResearcherPanelsEdit(){

    }



    public void initBean(){
        if (Pagez.getRequest().getParameter("panelid")!=null && Num.isinteger(Pagez.getRequest().getParameter("panelid"))){
            panel = Panel.get(Integer.parseInt(Pagez.getRequest().getParameter("panelid")));
        }
    }



    public String edit(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if(panel.getName()!=null && !panel.getName().equals("")){
            try{panel.save();}catch(Exception ex){logger.error("",ex);}
        }
        Pagez.sendRedirect("/jsp/researcher/panels.jsp");
        return "";
    }


    public Panel getPanel() {
        return panel;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }
}
