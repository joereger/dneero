package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.dao.*;
import com.dneero.util.Jsf;
import com.dneero.session.UserSession;

import java.util.ArrayList;
import java.util.Iterator;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Nov 18, 2006
 * Time: 9:07:50 AM
 */
public class ResearcherResultsImpressionsDetails implements Serializable {

    private Survey survey;
    private ArrayList<BloggerImpressionDetailsListItem> list;
    private Impression impression;
    private String referer;



    public ResearcherResultsImpressionsDetails(){
        //@todo Refactor Non-Empty Constructor
        beginView();
    }


    public String beginView(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("beginView called:");
        String tmpImpressionid = Jsf.getRequestParam("impressionid");
        if (com.dneero.util.Num.isinteger(tmpImpressionid)){
            logger.debug("beginView called: found impressionid in request param="+tmpImpressionid);
            survey = Survey.get(Jsf.getUserSession().getCurrentSurveyid());
            impression = Impression.get(Integer.parseInt(tmpImpressionid));
            referer = impression.getReferer();
            list = new ArrayList();
            for (Iterator<Impressiondetail> iterator = impression.getImpressiondetails().iterator(); iterator.hasNext();) {
                Impressiondetail impressiondetail = iterator.next();
                BloggerImpressionDetailsListItem listitem = new BloggerImpressionDetailsListItem();
                listitem.setImpressiondate(impressiondetail.getImpressiondate());
                listitem.setImpressiondetailid(impressiondetail.getImpressiondetailid());
                listitem.setImpressionid(impression.getImpressionid());
                listitem.setIp(impressiondetail.getIp());
                listitem.setQualifiesforpaymentstatus(impressiondetail.getQualifiesforpaymentstatus());
                list.add(listitem);
            }

        }
        return "researcherresultsimpressionsdetails";
    }


    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public ArrayList<BloggerImpressionDetailsListItem> getList() {
        return list;
    }

    public void setList(ArrayList<BloggerImpressionDetailsListItem> list) {
        this.list = list;
    }

    public Impression getImpression() {
        return impression;
    }

    public void setImpression(Impression impression) {
        this.impression = impression;
    }

    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }
}
