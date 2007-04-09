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
    private String msg = "";

    public ResearcherIndex(){

    }

    public String beginView(){
        load();
        return "researcherhome";
    }

    private void load(){
        if (Jsf.getRequestParam("showmarketingmaterial")!=null && Jsf.getRequestParam("showmarketingmaterial").equals("1")){
            showmarketingmaterial = true;
        } else {
            showmarketingmaterial = false;
        }
        ResearcherSurveyList bean = (ResearcherSurveyList)Jsf.getManagedBean("researcherSurveyList");
        bean.beginView();
    }

    public boolean getShowmarketingmaterial() {
        return showmarketingmaterial;
    }

    public void setShowmarketingmaterial(boolean showmarketingmaterial) {
        this.showmarketingmaterial = showmarketingmaterial;
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
