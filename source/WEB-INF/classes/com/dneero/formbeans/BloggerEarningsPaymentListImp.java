package com.dneero.formbeans;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Collections;

import com.dneero.session.UserSession;
import com.dneero.util.Jsf;
import com.dneero.util.SortableList;
import com.dneero.dao.Response;

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
        UserSession userSession = Jsf.getUserSession();
        if (userSession.getUser()!=null && userSession.getUser().getBlogger()!=null){
            list = new ArrayList();
            for (Iterator<Response> iterator = userSession.getUser().getBlogger().getResponses().iterator(); iterator.hasNext();) {
                Response response = iterator.next();
                BloggerEarningsPaymentListImpressions listitem = new BloggerEarningsPaymentListImpressions();
                //@todo populate listitem
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
