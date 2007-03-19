package com.dneero.formbeans;

import com.dneero.dao.Panel;
import com.dneero.util.Jsf;
import com.dneero.util.Num;
import org.apache.log4j.Logger;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Feb 17, 2007
 * Time: 9:32:33 AM
 */
public class ResearcherPanelsEdit implements Serializable {

    private Panel panel;

    public String beginView(){
        if (Jsf.getRequestParam("panelid")!=null && Num.isinteger(Jsf.getRequestParam("panelid"))){
            panel = Panel.get(Integer.parseInt(Jsf.getRequestParam("panelid")));
        }
        return "researcherpanelsedit";
    }

    public String edit(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if(panel.getName()!=null && !panel.getName().equals("")){
            try{panel.save();}catch(Exception ex){logger.error(ex);}
        }
        return "researcherpanels";
    }


    public Panel getPanel() {
        return panel;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }
}
