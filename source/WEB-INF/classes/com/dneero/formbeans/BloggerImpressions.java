package com.dneero.formbeans;

import com.dneero.util.Jsf;
import com.dneero.dao.Impression;
import com.dneero.dao.Survey;
import com.dneero.dao.Blogger;
import com.dneero.money.UserImpressionFinder;

import java.util.*;
import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class BloggerImpressions  implements Serializable {

    private ArrayList<BloggerImpressionsListItem> list;
    private String surveytitle;

    public BloggerImpressions(){

    }

    public String beginView(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("beginView called:");
        String tmpSurveyid = Jsf.getRequestParam("surveyid");
        if (com.dneero.util.Num.isinteger(tmpSurveyid)){
            logger.debug("beginView called: found surveyid in request param="+tmpSurveyid);
            load(Integer.parseInt(tmpSurveyid));
        }
        return "bloggerimpressions";
    }

    private void load(int surveyid){
        Survey survey = Survey.get(surveyid);
        if (survey!=null && Jsf.getUserSession().getUser()!=null && survey.canRead(Jsf.getUserSession().getUser())){
            surveytitle = survey.getTitle();
            list = new ArrayList();

                List<Impression> impressions = UserImpressionFinder.getAllImpressionsForSurvey(Blogger.get(Jsf.getUserSession().getUser().getBloggerid()), survey);
                for (Iterator<Impression> iterator1 = impressions.iterator(); iterator1.hasNext();) {
                    Impression impression = iterator1.next();
                    BloggerImpressionsListItem listitem = new BloggerImpressionsListItem();
                    listitem.setImpressionid(impression.getImpressionid());
                    listitem.setQuality(impression.getQuality());
                    listitem.setReferer(impression.getReferer());
                    listitem.setImpressionspaidandtobepaid(impression.getImpressionspaid() +  impression.getImpressionstobepaid());
                    listitem.setImpressionstotal(impression.getImpressionstotal());
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
                BloggerImpressionsListItem obj1 = (BloggerImpressionsListItem)o1;
                BloggerImpressionsListItem obj2 = (BloggerImpressionsListItem)o2;
                if (column == null) {
                    return 0;
                }
                if (column.equals("impressionspaidandtobepaid")) {
                    return ascending ? obj1.getImpressionspaidandtobepaid()-obj2.getImpressionspaidandtobepaid() : obj2.getImpressionspaidandtobepaid()-obj1.getImpressionspaidandtobepaid() ;
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
