package com.dneero.formbeans;

import com.dneero.util.SortableList;
import com.dneero.util.Jsf;
import com.dneero.session.UserSession;
import com.dneero.dao.*;
import com.dneero.invoice.BloggerIncomeCalculator;

import java.util.*;

import org.apache.log4j.Logger;

import javax.faces.context.FacesContext;

/**
 * User: Joe Reger Jr
 * Date: Aug 24, 2006
 * Time: 12:03:25 PM
 */
public class BloggerEarningsPaymentListSurv extends SortableList {


    private ArrayList<BloggerEarningsPaymentListSurveys> list;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public BloggerEarningsPaymentListSurv(){
        super("surveyname");

        int paybloggerid = 0;
        String tmpPaybloggerid = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("paybloggerid");
        if (com.dneero.util.Num.isinteger(tmpPaybloggerid)){
            logger.debug("beginView called: found tmpPaybloggerid in param="+tmpPaybloggerid);
            paybloggerid = Integer.parseInt(tmpPaybloggerid);
        } else {
            logger.debug("beginView called: NOT found tmpPaybloggerid in param="+tmpPaybloggerid);
        }

        UserSession userSession = Jsf.getUserSession();
        if (paybloggerid>0 && userSession.getUser()!=null && userSession.getUser().getBlogger()!=null){
            Payblogger payblogger = Payblogger.get(paybloggerid);
            ArrayList<Response> responses = BloggerIncomeCalculator.getResponsesForAPayblogger(payblogger);
            list = new ArrayList();
            for (Iterator<Response> iterator = responses.iterator(); iterator.hasNext();) {
                Response response = iterator.next();
                Survey survey = Survey.get(response.getSurveyid());
                BloggerEarningsPaymentListSurveys listitem = new BloggerEarningsPaymentListSurveys();
                listitem.setAmt(survey.getWillingtopayperrespondent());
                listitem.setSurveyname(survey.getTitle());
                list.add(listitem);
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
                BloggerEarningsPaymentListSurveys obj1 = (BloggerEarningsPaymentListSurveys)o1;
                BloggerEarningsPaymentListSurveys obj2 = (BloggerEarningsPaymentListSurveys)o2;
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

    public ArrayList<BloggerEarningsPaymentListSurveys> getList() {
        return list;
    }

    public void setList(ArrayList<BloggerEarningsPaymentListSurveys> list) {
        this.list = list;
    }

}
