package com.dneero.htmluibeans;

import com.dneero.dao.Panel;

import com.dneero.util.Num;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import org.apache.log4j.Logger;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Feb 17, 2007
 * Time: 9:32:33 AM
 */
public class CustomercarePanelsEdit implements Serializable {

    private Panel panel;

    public CustomercarePanelsEdit(){

    }



    public void initBean(){
        if (Pagez.getRequest().getParameter("panelid")!=null && Num.isinteger(Pagez.getRequest().getParameter("panelid"))){
            panel = Panel.get(Integer.parseInt(Pagez.getRequest().getParameter("panelid")));
        }
    }



    public void edit() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if(panel.getName()!=null && !panel.getName().equals("")){
            try{panel.save();}catch(Exception ex){logger.error("",ex);}
        }
    }


    public Panel getPanel() {
        return panel;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }
}