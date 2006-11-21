package com.dneero.formbeans;

import com.dneero.util.SortableList;
import com.dneero.util.Jsf;
import com.dneero.dao.Impression;
import com.dneero.dao.Survey;
import com.dneero.money.BloggerIncomeCalculator;

import java.util.*;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class BloggerImpressions extends SortableList {

    private ArrayList<BloggerImpressionsListItem> list;
    private String surveytitle;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public BloggerImpressions(){
        super("impressionsqualifyingforpayment");

    }

    private void load(int surveyid){
        Survey survey = Survey.get(surveyid);
        if (survey!=null && Jsf.getUserSession().getUser()!=null && survey.canRead(Jsf.getUserSession().getUser())){
            surveytitle = survey.getTitle();
            list = new ArrayList();


//                List<Impression> impressions = HibernateUtil.getSession().createCriteria(Impression.class)
//                                   .add(Restrictions.eq("surveyid", survey.getSurveyid()))
//                                   .createCriteria("impressiondetails")
//                                        .add(Restrictions.eq("bloggerid", Jsf.getUserSession().getUser().getBlogger().getBloggerid()))
//                                   .list();
//

                List<Impression> impressions = BloggerIncomeCalculator.getAllImpressionsForSurvey(Jsf.getUserSession().getUser().getBlogger(), survey);
                for (Iterator<Impression> iterator1 = impressions.iterator(); iterator1.hasNext();) {
                    Impression impression = iterator1.next();
                    BloggerImpressionsListItem listitem = new BloggerImpressionsListItem();
                    listitem.setImpressionid(impression.getImpressionid());
                    listitem.setQuality(impression.getQuality());
                    listitem.setReferer(impression.getReferer());
                    listitem.setImpressionsqualifyingforpayment(impression.getImpressiondetails().size());
                    list.add(listitem);
                }



        }
    }

    public String beginView(){
        logger.debug("beginView called:");
        String tmpSurveyid = Jsf.getRequestParam("surveyid");
        if (com.dneero.util.Num.isinteger(tmpSurveyid)){
            logger.debug("beginView called: found surveyid in request param="+tmpSurveyid);
            load(Integer.parseInt(tmpSurveyid));
        }
        return "bloggerimpressions";
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }

    protected void sort(final String column, final boolean ascending) {
        //logger.debug("sort called");
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                BloggerImpressionsListItem obj1 = (BloggerImpressionsListItem)o1;
                BloggerImpressionsListItem obj2 = (BloggerImpressionsListItem)o2;
                if (column == null) {
                    return 0;
                }
                if (column.equals("impressionsqualifyingforpayment")) {
                    return ascending ? obj1.getImpressionsqualifyingforpayment()-obj2.getImpressionsqualifyingforpayment() : obj2.getImpressionsqualifyingforpayment()-obj1.getImpressionsqualifyingforpayment() ;
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

    public ArrayList<BloggerImpressionsListItem> getList() {
        return list;
    }

    public void setList(ArrayList<BloggerImpressionsListItem> list) {
        this.list = list;
    }


    public String getSurveytitle() {
        return surveytitle;
    }

    public void setSurveytitle(String surveytitle) {
        this.surveytitle = surveytitle;
    }
}