package com.dneero.formbeans;

import com.dneero.util.SortableList;
import com.dneero.util.Jsf;
import com.dneero.util.Str;
import com.dneero.session.UserSession;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.Response;
import com.dneero.dao.Survey;
import com.dneero.dao.Impressiondetail;
import com.dneero.dao.Impression;
import com.dneero.invoice.BloggerIncomeCalculator;

import java.util.*;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class BloggerImpressionDetails extends SortableList {

    private ArrayList<BloggerImpressionDetailsListItem> list;
    private String surveytitle;
    private String referer;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public BloggerImpressionDetails(){
        super("date");

    }

    private void load(int impressionid){
        Impression impression = Impression.get(impressionid);
        Survey survey = Survey.get(impression.getSurveyid());
        if (impression!=null && survey!=null && Jsf.getUserSession().getUser()!=null && impression.canRead(Jsf.getUserSession().getUser())){
            surveytitle = survey.getTitle();
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
    }

    public String beginView(){
        //logger.debug("beginView called:");
        String tmpImpressionid = Jsf.getRequestParam("impressionid");
        if (com.dneero.util.Num.isinteger(tmpImpressionid)){
            logger.debug("beginView called: found impressionid in request param="+tmpImpressionid);
            load(Integer.parseInt(tmpImpressionid));
        }
        return "bloggerimpressiondetails";
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }

    protected void sort(final String column, final boolean ascending) {
        //logger.debug("sort called");
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                BloggerImpressionDetailsListItem obj1 = (BloggerImpressionDetailsListItem)o1;
                BloggerImpressionDetailsListItem obj2 = (BloggerImpressionDetailsListItem)o2;
                if (column == null) {
                    return 0;
                }
                if (column.equals("date")) {
                    //@todo comparator to order by date
                    //return ascending ? obj1.getImpressiondate().after(obj2.getImpressiondate()) : obj2.getImpressiondate().after(obj1.getImpressiondate());
                    return 0;
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

    public ArrayList<BloggerImpressionDetailsListItem> getList() {
        return list;
    }

    public void setList(ArrayList<BloggerImpressionDetailsListItem> list) {
        this.list = list;
    }


    public String getSurveytitle() {
        return surveytitle;
    }

    public void setSurveytitle(String surveytitle) {
        this.surveytitle = surveytitle;
    }


    public String getReferer() {
        return referer;
    }

    public void setReferer(String referer) {
        this.referer = referer;
    }
}
