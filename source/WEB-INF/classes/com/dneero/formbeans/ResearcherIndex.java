package com.dneero.formbeans;

import com.dneero.util.Jsf;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Mar 12, 2007
 * Time: 2:59:18 PM
 */
public class ResearcherIndex implements Serializable {

    private boolean showmarketingmaterial = false;

    public ResearcherIndex(){
        if (Jsf.getRequestParam("showmarketingmaterial")!=null && Jsf.getRequestParam("showmarketingmaterial").equals("1")){
            showmarketingmaterial = true;
        } else {
            showmarketingmaterial = false;
        }
    }


    public boolean getShowmarketingmaterial() {
        return showmarketingmaterial;
    }

    public void setShowmarketingmaterial(boolean showmarketingmaterial) {
        this.showmarketingmaterial = showmarketingmaterial;
    }
}
