package com.dneero.formbeans;

import org.apache.log4j.Logger;

import java.util.*;

import com.dneero.session.UserSession;
import com.dneero.util.Jsf;
import com.dneero.util.SortableList;
import com.dneero.dao.*;
import com.dneero.invoice.BloggerIncomeCalculator;

import javax.faces.context.FacesContext;

/**
 * User: Joe Reger Jr
 * Date: Aug 24, 2006
 * Time: 12:01:00 PM
 */
public class BloggerEarningsPaymentListImp extends SortableList {

    private ArrayList<BloggerEarningsPaymentListImpressions> list;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public BloggerEarningsPaymentListImp(){
        super("surveyname");

        int paybloggerid = 0;
        String tmpPaybloggerid = Jsf.getRequestParam("paybloggerid");
        if (com.dneero.util.Num.isinteger(tmpPaybloggerid)){
            logger.debug("beginView called: found tmpPaybloggerid in param="+tmpPaybloggerid);
            paybloggerid = Integer.parseInt(tmpPaybloggerid);
        } else {
            logger.debug("beginView called: NOT found tmpPaybloggerid in param="+tmpPaybloggerid);
        }

        UserSession userSession = Jsf.getUserSession();
        if (paybloggerid>0 && userSession.getUser()!=null && userSession.getUser().getBlogger()!=null){
            Payblogger payblogger = Payblogger.get(paybloggerid);
            if (payblogger.canRead(Jsf.getUserSession().getUser())){

                ArrayList<Impressiondetail> impressiondetails = BloggerIncomeCalculator.getImpressiondetailsForAPayblogger(payblogger);

                //Create a counting map of <surveyid, impressioncount>
                HashMap<Integer, Integer> surveyVsImpressions = new HashMap<Integer, Integer>();
                for (Iterator<Impressiondetail> iterator = impressiondetails.iterator(); iterator.hasNext();) {
                    Impressiondetail impressiondetail = iterator.next();
                    int surveyid = Impression.get(impressiondetail.getImpressionid()).getSurveyid();
                    if (surveyVsImpressions.containsKey(surveyid)){
                        surveyVsImpressions.put(surveyid, surveyVsImpressions.get(surveyid)+1);
                    } else {
                        surveyVsImpressions.put(surveyid, 1);
                    }
                }

                //Iterare map to create output
                list = new ArrayList();
                Iterator keyValuePairs = surveyVsImpressions.entrySet().iterator();
                for (int i = 0; i < surveyVsImpressions.size(); i++){
                    Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
                    Integer key = (Integer)mapentry.getKey();
                    Integer value = (Integer)mapentry.getValue();
                    Survey survey = Survey.get(key);

                    BloggerEarningsPaymentListImpressions listitem = new BloggerEarningsPaymentListImpressions();
                    listitem.setAmt((value/1000) * survey.getWillingtopaypercpm());
                    listitem.setImpressions(value);
                    listitem.setSurveyname(survey.getTitle());
                    list.add(listitem);
                }
            }
        }
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }

    protected void sort(final String column, final boolean ascending) {
        //logger.debug("sort called");
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                BloggerEarningsPaymentListImpressions obj1 = (BloggerEarningsPaymentListImpressions)o1;
                BloggerEarningsPaymentListImpressions obj2 = (BloggerEarningsPaymentListImpressions)o2;
                if (column == null) {
                    return 0;
                }
                if (column.equals("surveyname")) {
                    return ascending ? obj1.getSurveyname().compareTo(obj2.getSurveyname()) : obj2.getSurveyname().compareTo(obj1.getSurveyname());
                } else {
                    return 0;
                }
            }
        };

        //sort and also set our model with the new sort, since using DataTable with
        //ListDataModel on front end
        if (list != null && !list.isEmpty()) {
            //logger.debug("sorting surveys and initializing ListDataModel");
            Collections.sort(list, comparator);
        }
    }

    public ArrayList<BloggerEarningsPaymentListImpressions> getList() {
        return list;
    }

    public void setList(ArrayList<BloggerEarningsPaymentListImpressions> list) {
        this.list = list;
    }


}
