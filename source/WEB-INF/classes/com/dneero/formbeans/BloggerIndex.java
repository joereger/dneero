package com.dneero.formbeans;

import com.dneero.util.Jsf;
import com.dneero.util.Str;
import com.dneero.dao.Impressiondetail;
import com.dneero.dao.Responsepending;
import com.dneero.dao.Survey;
import com.dneero.dao.Blogger;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.display.SurveyResponseParser;
import com.dneero.display.components.def.ComponentException;

import java.util.List;
import java.util.Iterator;

import org.hibernate.criterion.Restrictions;

/**
 * User: Joe Reger Jr
 * Date: Nov 22, 2006
 * Time: 9:48:52 AM
 */
public class BloggerIndex {

    private boolean userhasresponsependings = false;
    private String responsependingmsg = "";

    public BloggerIndex(){

        List<Responsepending> responsependings = HibernateUtil.getSession().createCriteria(Responsepending.class)
                               .add(Restrictions.eq("userid", Jsf.getUserSession().getUser().getUserid()))
                               .setCacheable(true)
                               .list();
        for (Iterator<Responsepending> iterator = responsependings.iterator(); iterator.hasNext();) {
            Responsepending responsepending = iterator.next();
            userhasresponsependings = true;
            //If we have a blogger at this point, commit the pending response
            if (Jsf.getUserSession().getUser().getBloggerid()>0){
                Survey survey = Survey.get(responsepending.getSurveyid());
                try{
                    SurveyResponseParser srp = new SurveyResponseParser(responsepending.getResponseasstring());
                    BloggerSurveyTake.createResponse(survey,  srp, Blogger.get(Jsf.getUserSession().getUser().getBloggerid()));
                    responsependingmsg = responsependingmsg + "You just earned $"+ Str.formatForMoney(survey.getWillingtopayperrespondent())+"! We have successfully committed your response to '"+survey.getTitle()+"'!  But don't forget to post this survey to your blog.  To do so, click Earnings and choose this survey.  On that page you'll find a 'Post To Blog' tab that explains how to do so." + "<br/><br/>";
                } catch (ComponentException cex){
                    responsependingmsg = responsependingmsg + "There was an error committing your response to the survey '"+survey.getTitle()+": " + cex.getErrorsAsSingleString() + "<br/><br/>";
                }
                //Delete the responsepending, now that it's been handled
                responsepending.delete();
            }
        }
    }


    public boolean isUserhasresponsependings() {
        return userhasresponsependings;
    }

    public void setUserhasresponsependings(boolean userhasresponsependings) {
        this.userhasresponsependings = userhasresponsependings;
    }

    public String getResponsependingmsg() {
        return responsependingmsg;
    }

    public void setResponsependingmsg(String responsependingmsg) {
        this.responsependingmsg = responsependingmsg;
    }
}
