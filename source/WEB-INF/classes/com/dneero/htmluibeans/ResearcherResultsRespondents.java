package com.dneero.htmluibeans;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;
import com.dneero.dao.Survey;
import com.dneero.dao.Response;
import com.dneero.dao.Blogger;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;

import com.dneero.util.SortableList;
import com.dneero.htmlui.Pagez;
import com.dneero.helpers.NicknameHelper;

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


    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        survey = Survey.get(Pagez.getUserSession().getCurrentSurveyid());
        if (com.dneero.util.Num.isinteger(Pagez.getRequest().getParameter("surveyid"))){
            Pagez.getUserSession().setCurrentSurveyid(Integer.parseInt(Pagez.getRequest().getParameter("surveyid")));
            survey = Survey.get((Integer.parseInt(Pagez.getRequest().getParameter("surveyid"))));
        }
        list = new ArrayList<ResearcherResultsRespondentsListitem>();
        if (survey!=null && survey.getSurveyid()>0){
            List<Response> rsps = HibernateUtil.getSession().createCriteria(Response.class)
                                               .add(Restrictions.eq("surveyid", survey.getSurveyid()))
                                               .addOrder(Order.desc("responsedate"))
                                               .setCacheable(true)
                                               .list();
            if (rsps!=null){
                for (Iterator<Response> iterator = rsps.iterator(); iterator.hasNext();) {
                //for (Iterator<Response> iterator = survey.getResponses().iterator(); iterator.hasNext();) {
                    Response response = iterator.next();
                    Blogger blogger = Blogger.get(response.getBloggerid());
                    User user = User.get(blogger.getUserid());
                    ResearcherResultsRespondentsListitem li = new ResearcherResultsRespondentsListitem();
                    li.setBloggerid(blogger.getBloggerid());
                    li.setFirstname(NicknameHelper.getNameOrNickname(user));
                    li.setLastname(NicknameHelper.getNameOrNickname(user));
                    li.setResponsedate(response.getResponsedate());
                    li.setResponseid(response.getResponseid());
                    li.setUser(user);
                    list.add(li);
                }
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
            initBean();
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
