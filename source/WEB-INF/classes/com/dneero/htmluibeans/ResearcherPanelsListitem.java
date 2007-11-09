package com.dneero.htmluibeans;

import com.dneero.dao.Panel;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Feb 9, 2007
 * Time: 11:14:03 AM
 */
public class ResearcherPanelsListitem implements Serializable {

    public Panel panel;
    public int numberofmembers;

    public ResearcherPanelsListitem(){}


    public int getNumberofmembers() {
        return numberofmembers;
    }

    public void setNumberofmembers(int numberofmembers) {
        this.numberofmembers = numberofmembers;
    }

    public Panel getPanel() {
        return panel;
    }

    public void setPanel(Panel panel) {
        this.panel = panel;
    }
}
