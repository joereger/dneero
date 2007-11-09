package com.dneero.htmluibeans;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;

import com.dneero.util.Str;
import com.dneero.htmlui.Pagez;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Nov 18, 2006
 * Time: 9:07:21 AM
 */
public class PublicProfileImpressions implements Serializable {

    private Survey survey;
    private ArrayList<ResearcherResultsImpressionsListitem> list;
    private Response response;
    private Blogger blogger;
    private User user;

    public PublicProfileImpressions(){

    }


    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("loadSurvey called");

        if (com.dneero.util.Num.isinteger(Pagez.getRequest().getParameter("responseid"))){
            logger.debug("beginView called: found responseid in request param="+Pagez.getRequest().getParameter("responseid"));
            response = Response.get(Integer.parseInt(Pagez.getRequest().getParameter("responseid")));
            blogger = Blogger.get(response.getBloggerid());
            user = User.get(blogger.getUserid());
        }

        survey = Survey.get(Pagez.getUserSession().getCurrentSurveyid());
        list = new ArrayList<ResearcherResultsImpressionsListitem>();

        if (response!=null && blogger!=null && survey!=null){


                List<Impression> impressions = HibernateUtil.getSession().createCriteria(Impression.class)
                                   .add( Restrictions.eq("surveyid", survey.getSurveyid()))
                                   .list();
                for (Iterator<Impression> iterator1 = impressions.iterator(); iterator1.hasNext();) {
                    Impression impression = iterator1.next();

                    ResearcherResultsImpressionsListitem robj = new ResearcherResultsImpressionsListitem();
                    robj.setImpressionspaidandtobepaid(impression.getImpressionspaid() + impression.getImpressionstobepaid());
                    robj.setReferer(impression.getReferer());
                    robj.setReferertruncated(Str.truncateString(impression.getReferer(), 45));
                    robj.setImpressionquality(String.valueOf(impression.getQuality()));
                    list.add(robj);

                }

        }



        logger.debug("done loading survey");
 
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }


    public ArrayList<ResearcherResultsImpressionsListitem> getList() {
        return list;
    }

    public void setList(ArrayList<ResearcherResultsImpressionsListitem> list) {
        this.list = list;
    }


    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Blogger getBlogger() {
        return blogger;
    }

    public void setBlogger(Blogger blogger) {
        this.blogger = blogger;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
