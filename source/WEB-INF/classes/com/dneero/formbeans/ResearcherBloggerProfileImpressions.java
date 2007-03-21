package com.dneero.formbeans;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Jsf;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Nov 18, 2006
 * Time: 9:07:21 AM
 */
public class ResearcherBloggerProfileImpressions implements Serializable {

    private Survey survey;
    private ArrayList<ResearcherResultsImpressionsListitem> list;
    private Response response;
    private Blogger blogger;
    private User user;

    public ResearcherBloggerProfileImpressions(){

    }
    
    public String beginView(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        String tmpResponseid = Jsf.getRequestParam("responseid");
        if (com.dneero.util.Num.isinteger(tmpResponseid)){
            logger.debug("beginView called: found responseid in request param="+tmpResponseid);
            response = Response.get(Integer.parseInt(tmpResponseid));
            blogger = Blogger.get(response.getBloggerid());
            user = User.get(blogger.getUserid());
            load();
        }
        return "researcherbloggerprofileimpressions";
    }

    public void load(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("loadSurvey called");
        survey = Survey.get(Jsf.getUserSession().getCurrentSurveyid());
        list = new ArrayList<ResearcherResultsImpressionsListitem>();

        if (response!=null && blogger!=null && survey!=null){


                List<Impression> impressions = HibernateUtil.getSession().createCriteria(Impression.class)
                                   .add( Restrictions.eq("surveyid", survey.getSurveyid()))
                                   .list();
                for (Iterator<Impression> iterator1 = impressions.iterator(); iterator1.hasNext();) {
                    Impression impression = iterator1.next();

                    for (Iterator<Blog> iterator = blogger.getBlogs().iterator(); iterator.hasNext();) {
                        Blog blog = iterator.next();
                        if (impression.getBlog()!=null && blog.getBlogid()==impression.getBlog().getBlogid()){
                            ResearcherResultsImpressionsListitem robj = new ResearcherResultsImpressionsListitem();
                            robj.setBlogtitle(impression.getBlog().getTitle());
                            robj.setBlogurl(impression.getBlog().getUrl());
                            robj.setImpressionsqualifyingforpayment(impression.getImpressionsqualifyingforpayment());
                            robj.setReferer(impression.getReferer());
                            list.add(robj);
                        }
                    }

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
