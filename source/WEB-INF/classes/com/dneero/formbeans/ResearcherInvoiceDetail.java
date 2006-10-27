package com.dneero.formbeans;

import com.dneero.util.Jsf;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Aug 29, 2006
 * Time: 10:58:31 AM
 */
public class ResearcherInvoiceDetail {

    private int invoiceid;
    private ArrayList<ResearcherInvoiceDetailImpressionListItem> impressionlistitems;
    private ArrayList<ResearcherInvoiceDetailResponseListItem> responselistitems;
    private int numberofresponses;
    private int numberofimpressions;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public ResearcherInvoiceDetail(){

    }

    public void load(){
        impressionlistitems = new ArrayList<ResearcherInvoiceDetailImpressionListItem>();
        responselistitems = new ArrayList<ResearcherInvoiceDetailResponseListItem>();
        if (Jsf.getUserSession().getUser()!=null){
            //Responses
            List responses = HibernateUtil.getSession().createQuery("from Response where invoiceid='"+invoiceid+"'").list();
            numberofresponses = responses.size();
            for (Iterator iterator = responses.iterator(); iterator.hasNext();) {
                Response response = (Response) iterator.next();
                User user = User.get(Blogger.get(response.getBloggerid()).getUserid());
                ResearcherInvoiceDetailResponseListItem item = new ResearcherInvoiceDetailResponseListItem();
                item.setResponsedate(response.getResponsedate());
                item.setResponseid(response.getResponseid());
                item.setSurveyid(response.getSurveyid());
                item.setSurveytitle(response.getSurvey().getTitle());
                item.setUserid(user.getUserid());
                item.setUsername(user.getFirstname() + " " + user.getLastname());
                responselistitems.add(item);
            }
            //Impressions
            List impressions = HibernateUtil.getSession().createQuery("from Impressiondetail where invoiceid='"+invoiceid+"'").list();
            numberofimpressions = impressions.size();
            for (Iterator iterator = impressions.iterator(); iterator.hasNext();) {
                Impressiondetail impressiondetail = (Impressiondetail) iterator.next();
                Impression impression = Impression.get(impressiondetail.getImpressionid());
                Survey survey = Survey.get(impression.getSurveyid());
                ResearcherInvoiceDetailImpressionListItem item = new ResearcherInvoiceDetailImpressionListItem();
                item.setImpressionid(impressiondetail.getImpressionid());
                item.setIp(impressiondetail.getIp());
                item.setReferer(impression.getReferer());
                item.setSurveyid(survey.getSurveyid());
                item.setSurveytitle(survey.getTitle());
                item.setImpressiondate(impressiondetail.getImpressiondate());
                impressionlistitems.add(item);
            }

        }
    }

    public String beginView(){
        logger.debug("beginView called: invoiceid="+invoiceid);
        String tmpInvoiceid = Jsf.getRequestParam("invoiceid");
        if (com.dneero.util.Num.isinteger(tmpInvoiceid)){
            logger.debug("beginView called: found invoiceid in param="+tmpInvoiceid);
            Invoice invoice = Invoice.get(Integer.parseInt(tmpInvoiceid));
            if (Jsf.getUserSession().getUser()!=null && invoice.canRead(Jsf.getUserSession().getUser())){
                invoiceid = invoice.getInvoiceid();
                load();
            }

        } else {
            logger.debug("beginView called: NOT found invoiceid in param="+tmpInvoiceid);
        }
        return "researcherinvoicedetail";
    }


    public ArrayList<ResearcherInvoiceDetailImpressionListItem> getImpressionlistitems() {
        return impressionlistitems;
    }

    public void setImpressionlistitems(ArrayList<ResearcherInvoiceDetailImpressionListItem> impressionlistitems) {
        this.impressionlistitems = impressionlistitems;
    }

    public ArrayList<ResearcherInvoiceDetailResponseListItem> getResponselistitems() {
        return responselistitems;
    }

    public void setResponselistitems(ArrayList<ResearcherInvoiceDetailResponseListItem> responselistitems) {
        this.responselistitems = responselistitems;
    }


    public int getInvoiceid() {
        return invoiceid;
    }

    public void setInvoiceid(int invoiceid) {
        this.invoiceid = invoiceid;
    }


    public int getNumberofresponses() {
        return numberofresponses;
    }

    public void setNumberofresponses(int numberofresponses) {
        this.numberofresponses = numberofresponses;
    }

    public int getNumberofimpressions() {
        return numberofimpressions;
    }

    public void setNumberofimpressions(int numberofimpressions) {
        this.numberofimpressions = numberofimpressions;
    }
}
