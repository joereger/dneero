package com.dneero.formbeans;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.session.UserSession;
import com.dneero.util.Jsf;
import com.dneero.util.SortableList;
import com.dneero.util.Str;
import com.dneero.invoice.BloggerIncomeCalculator;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class BloggerCompletedsurveys extends SortableList {

    private ArrayList<BloggerEarningsListSurveys> list;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public BloggerCompletedsurveys(){
        super("title");
        UserSession userSession = Jsf.getUserSession();
        if (userSession.getUser()!=null && userSession.getUser().getBlogger()!=null){
            list = new ArrayList();
            HibernateUtil.getSession().saveOrUpdate(userSession.getUser().getBlogger());
            for (Iterator<Response> iterator = userSession.getUser().getBlogger().getResponses().iterator(); iterator.hasNext();) {
                Response response = iterator.next();
                Survey survey = Survey.get(response.getSurveyid());
                BloggerEarningsListSurveys listitem = new BloggerEarningsListSurveys();
                listitem.setAmtforresponse("$"+Str.formatForMoney(survey.getWillingtopayperrespondent()));
                listitem.setAmttotal("$"+Str.formatForMoney(BloggerIncomeCalculator.getBloggerTotalPossibleIncomeForSurvey(userSession.getUser().getBlogger(), survey)));
                listitem.setImpressions(BloggerIncomeCalculator.getAllImpressiondetailsForSurvey(userSession.getUser().getBlogger(), survey).size());
                listitem.setImpressionsthatqualifyforpay(BloggerIncomeCalculator.getImpressionDetailsThatQualifyForPay(userSession.getUser().getBlogger(), survey).size());
                listitem.setResponsedate(response.getResponsedate());
                listitem.setResponseid(response.getResponseid());
                listitem.setSurveyid(survey.getSurveyid());
                listitem.setSurveytitle(survey.getTitle());
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
                BloggerEarningsListSurveys obj1 = (BloggerEarningsListSurveys)o1;
                BloggerEarningsListSurveys obj2 = (BloggerEarningsListSurveys)o2;
                if (column == null) {
                    return 0;
                }
                if (column.equals("title")) {
                    return ascending ? obj1.getSurveytitle().compareTo(obj2.getSurveytitle()) : obj2.getSurveytitle().compareTo(obj1.getSurveytitle());
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

    public ArrayList<BloggerEarningsListSurveys> getList() {
        return list;
    }

    public void setList(ArrayList<BloggerEarningsListSurveys> list) {
        this.list = list;
    }



}
