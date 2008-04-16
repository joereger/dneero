package com.dneero.finders;

import com.dneero.dao.Blogger;
import com.dneero.dao.Response;
import com.dneero.dao.Survey;
import com.dneero.dao.User;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Apr 16, 2008
 * Time: 11:14:31 AM
 */
public class FindResponses {

    public static ArrayList<Response> find(Survey survey, SurveyCriteriaXML filterCriteriaXml){
        Logger logger = Logger.getLogger(FindResponses.class);
        ArrayList<Response> out = new ArrayList<Response>();
        for (Iterator<Response> iterator = survey.getResponses().iterator(); iterator.hasNext();) {
            Response response = iterator.next();
            if (filterCriteriaXml!=null){
                Blogger blogger = Blogger.get(response.getBloggerid());
                if (blogger!=null && blogger.getBloggerid()>0){
                    User user = User.get(blogger.getUserid());
                    if (user!=null && user.getUserid()>0){
                        //If they're qualified according to the survey criteria
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
