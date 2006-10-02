package com.dneero.formbeans;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Collections;

import com.dneero.session.UserSession;
import com.dneero.util.Jsf;
import com.dneero.util.SortableList;
import com.dneero.util.Str;
import com.dneero.dao.Response;
import com.dneero.dao.Survey;
import com.dneero.dao.Payblogger;
import com.dneero.invoice.BloggerIncomeCalculator;

import javax.faces.context.FacesContext;

/**
 * User: Joe Reger Jr
 * Date: Aug 21, 2006
 * Time: 7:12:22 PM
 */
public class BloggerEarningsSurvey extends SortableList {

    private ArrayList<BloggerEarningsSurveyListPayments> list;
    private Survey survey;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public BloggerEarningsSurvey(){
        super("paymentdate");

    }

    private void load(int responseid){
        UserSession userSession = Jsf.getUserSession();
        if (userSession.getUser()!=null && userSession.getUser().getBlogger()!=null){
            Response response = Response.get(responseid);
            Survey survey = Survey.get(response.getSurveyid());
            this.survey = survey;
            if (response.canRead(Jsf.getUserSession().getUser())){
                ArrayList<Payblogger> paybloggers = BloggerIncomeCalculator.getPaybloggersForASurvey(userSession.getUser().getBlogger(), survey);
                list = new ArrayList();
                for (Iterator<Payblogger> iterator = paybloggers.iterator(); iterator.hasNext();) {
                    Payblogger payblogger = iterator.next();
                    BloggerEarningsSurveyListPayments listitem = new BloggerEarningsSurveyListPayments();
                    listitem.setAmt("$"+Str.formatForMoney(payblogger.getAmt()));
                    listitem.setPaybloggerid(payblogger.getPaybloggerid());
                    listitem.setPaymentdate(payblogger.getDate());
                    list.add(listitem);
                }
            }
        }
    }

    public String beginView(){
        logger.debug("beginView called");
        String tmpResponseid = Jsf.getRequestParam("responseid");
        if (com.dneero.util.Num.isinteger(tmpResponseid)){
            logger.debug("beginView called: found tmpResponseid in param="+tmpResponseid);
            load(Integer.parseInt(tmpResponseid));
        } else {
            logger.debug("beginView called: NOT found tmpResponseid in param="+tmpResponseid);
        }
        return "bloggerearningssurvey";
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }

    protected void sort(final String column, final boolean ascending) {
        //logger.debug("sort called");
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                BloggerEarningsSurveyListPayments obj1 = (BloggerEarningsSurveyListPayments)o1;
                BloggerEarningsSurveyListPayments obj2 = (BloggerEarningsSurveyListPayments)o2;
                if (column == null) {
                    return 0;
                }
                if (column.equals("paymentdate")) {
                    return ascending ? obj1.getPaymentdate().compareTo(obj2.getPaymentdate()) : obj2.getPaymentdate().compareTo(obj1.getPaymentdate());
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

    public ArrayList<BloggerEarningsSurveyListPayments> getList() {
        return list;
    }

    public void setList(ArrayList<BloggerEarningsSurveyListPayments> list) {
        this.list = list;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

}
