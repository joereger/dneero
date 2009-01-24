package com.dneero.helpers;

import com.dneero.dao.Response;
import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.dao.Blogger;
import com.dneero.dao.hibernate.HibernateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.hibernate.criterion.Restrictions;

/**
 * User: Joe Reger Jr
 * Date: Jan 24, 2009
 * Time: 10:38:14 AM
 */
public class FastGetUserStuff {

    public static ArrayList<Survey> getSurveys(User user){
        return getSurveys(Blogger.get(user.getBloggerid()));
    }

    public static ArrayList<Survey> getSurveys(Blogger blogger){
        ArrayList<Survey> surveys = new ArrayList<Survey>();
        if (blogger!=null && blogger.getBloggerid()>0){
            ArrayList<Integer> surveyids = new ArrayList<Integer>();
            for (Iterator<Response> rspsIt=blogger.getResponses().iterator(); rspsIt.hasNext();) {
                Response response=rspsIt.next();
                surveyids.add(response.getSurveyid());
            }
            List<Survey> svys = HibernateUtil.getSession().createCriteria(Survey.class)
                                               .add(Restrictions.in("surveyid", surveyids))
                                               .setCacheable(true)
                                               .list();
            if (svys!=null && svys.size()>0){
                for (Iterator<Survey> svysIt=svys.iterator(); svysIt.hasNext();) {
                    Survey survey=svysIt.next();
                    surveys.add(survey);
                }
            }
        }
        return surveys;
    }


}
