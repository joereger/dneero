package com.dneero.finders;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Apr 16, 2008
 * Time: 11:14:31 AM
 */
public class FindResponses {

    public static ArrayList<Response> find(Survey surveyTheyTook, SurveyCriteriaXML filterCriteriaXml){
        Logger logger = Logger.getLogger(FindResponses.class);
        ArrayList<Response> out = new ArrayList<Response>();


        List<Response> responses = HibernateUtil.getSession().createQuery("from Response where surveyid='"+surveyTheyTook.getSurveyid()+"'").setCacheable(true).list();
        //for (Iterator<Response> iterator = surveyTheyTook.getResponses().iterator(); iterator.hasNext();) {
        for (Iterator<Response> iterator = responses.iterator(); iterator.hasNext();) {
            Response response = iterator.next();
            if (filterCriteriaXml!=null){
                Blogger blogger = Blogger.get(response.getBloggerid());
                if (blogger!=null && blogger.getBloggerid()>0){
                    User user = User.get(blogger.getUserid());
                    if (user!=null && user.getUserid()>0){
                        //If they're qualified according to the surveyTheyTook criteria
                        if (filterCriteriaXml.isUserQualified(user)){
                            out.add(response);
                        }
                    }
                }
            } else {
                //Since we're not using an scXml criteria we don't need to eval the blogger... efficiency baby
                out.add(response);
            }
        }
        return out;
    }

}
