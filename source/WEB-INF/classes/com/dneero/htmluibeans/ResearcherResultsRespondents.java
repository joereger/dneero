package com.dneero.htmluibeans;

import org.apache.log4j.Logger;
import com.dneero.dao.Survey;
import com.dneero.dao.Response;
import com.dneero.dao.Blogger;
import com.dneero.dao.User;
import com.dneero.util.Jsf;
import com.dneero.util.SortableList;

import java.util.*;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Nov 18, 2006
 * Time: 9:06:40 AM
 */
public class ResearcherResultsRespondents implements Serializable {


    private Survey survey;
    private List<ResearcherResultsRespondentsListitem> list;

    public ResearcherResultsRespondents(){

    }

    public String beginView(){
        load();
        return "researcherresultsrespondents";
    }

    private void load(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        survey = Survey.get(Pagez.getUserSession().getCurrentSurveyid());
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
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("sort called");
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                ResearcherResultsRespondentsListitem obj1 = (ResearcherResultsRespondentsListitem)o1;
                ResearcherResultsRespondentsListitem obj2 = (ResearcherResultsRespondentsListitem)o2;
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
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("getList() called");
        if (list==null){
            load();
        }
        sort("responseid", false);
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
