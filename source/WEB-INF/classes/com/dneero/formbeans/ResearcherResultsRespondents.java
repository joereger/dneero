package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.dao.Survey;
import com.dneero.dao.Response;
import com.dneero.dao.Blogger;
import com.dneero.dao.User;
import com.dneero.util.Jsf;
import com.dneero.util.SortableList;

import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Nov 18, 2006
 * Time: 9:06:40 AM
 */
public class ResearcherResultsRespondents extends SortableList {


    Logger logger = Logger.getLogger(this.getClass().getName());
    private Survey survey;
    private List list;

    public ResearcherResultsRespondents(){
        super("responsedate");
        logger.debug("instanciating");
        load();
    }

    private void load(){
        survey = Survey.get(Jsf.getUserSession().getCurrentSurveyid());
        list = new ArrayList<ResearcherResultsRespondentsListitem>();
        if (survey!=null && survey.getSurveyid()>0){
            for (Iterator<Response> iterator = survey.getResponses().iterator(); iterator.hasNext();) {
                Response response = iterator.next();
                Blogger blogger = Blogger.get(response.getBloggerid());
                User user = User.get(blogger.getUserid());
                ResearcherResultsRespondentsListitem li = new ResearcherResultsRespondentsListitem();
                li.setBloggerid(blogger.getBloggerid());
                li.setFirstname(user.getFirstname());
                li.setLastname(user.getLastname());
                li.setResponsedate(response.getResponsedate());
                li.setResponseid(response.getResponseid());
                list.add(li);
            }
        }
        logger.debug("list.size()="+list.size());
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }

    protected void sort(final String column, final boolean ascending) {
        logger.debug("sort called");
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                Response obj1 = (Response)o1;
                Response obj2 = (Response)o2;
                if (column == null) {
                    return 0;
                }
                if (obj1!=null && obj2!=null && column.equals("responseid")) {
                    //return ascending ? obj1.getResponseid().compareTo(obj2.getTitle()) : obj2.getTitle().compareTo(obj1.getTitle());
                } else {
                    return 0;
                }
                return 0;
            }
        };

        //sort and also set our model with the new sort, since using DataTable with
        //ListDataModel on front end
        if (list != null && !list.isEmpty()) {
            logger.debug("sorting objs and initializing ListDataModel");
            Collections.sort(list, comparator);
        }
    }


    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public List getList() {
        logger.debug("getList() called");
        if (list==null){
            load();
        }
        sort("responseid", isAscending());
        if (list==null){
            logger.debug("list is still null");
        } else {
            logger.debug("list.size()="+list.size());
        }
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }
}