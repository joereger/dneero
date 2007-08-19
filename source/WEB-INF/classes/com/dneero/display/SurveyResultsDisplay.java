package com.dneero.display;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentTypes;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * User: Joe Reger Jr
 * Date: Jul 14, 2006
 * Time: 12:34:47 PM
 */
public class SurveyResultsDisplay {



    public static String getHtmlForResults(Survey survey, Blogger blogger, int referredbyuserid, ArrayList<Integer> onlyincluderesponsesfromtheseuserids){
        StringBuffer out = new StringBuffer();
        Logger logger = Logger.getLogger(SurveyResultsDisplay.class);
        for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
            Question question = iterator.next();
            logger.debug("found question.getQuestionid()="+question.getQuestionid());
            Component component = ComponentTypes.getComponentByID(question.getComponenttype(), question, blogger);
            logger.debug("found component.getName()="+component.getName());
            out.append("<br><br>");
            out.append("<table width=100% cellpadding=0 cellspacing=0 border=0>");
            out.append("<tr>");
            out.append("<td valign=top bgcolor=#e6e6e6>");
            out.append("<b>"+question.getQuestion()+"</b>");
            out.append("</td>");
            out.append("</tr>");
            out.append("</table>");
            out.append("<table width=100% cellpadding=0 cellspacing=0 border=0>");
            out.append("<tr>");
            out.append("<td valign=top>");
            List<Questionresponse>  questionresponses = HibernateUtil.getSession().createCriteria(Questionresponse.class)
                   .add(Restrictions.eq("questionid", question.getQuestionid()))
                   .setCacheable(true)
                   .list();
            //If referredbyuserid is included, filter out those not referred by this userid
            if (referredbyuserid>0){
                List<Questionresponse> responsesTmp = new ArrayList<Questionresponse>();
                for (Iterator<Questionresponse> iterator1 = questionresponses.iterator(); iterator1.hasNext();) {
                    Questionresponse questionresponse = iterator1.next();
                    Response response = Response.get(questionresponse.getResponseid());
                    if (response.getReferredbyuserid()==referredbyuserid){
                        responsesTmp.add(questionresponse);
                    }
                }
                questionresponses = responsesTmp;
            }
            //If onlyincluderesponsesfromtheseuserids filter out those not in the list
            if (onlyincluderesponsesfromtheseuserids!=null && onlyincluderesponsesfromtheseuserids.size()>0){
                List<Questionresponse> responsesTmp = new ArrayList<Questionresponse>();
                for (Iterator<Questionresponse> iterator1 = questionresponses.iterator(); iterator1.hasNext();) {
                    Questionresponse questionresponse = iterator1.next();
                    Response response = Response.get(questionresponse.getResponseid());
                    int userid = Blogger.get(response.getBloggerid()).getUserid();
                    boolean isinlistofuserids = false;
                    for (Iterator it = onlyincluderesponsesfromtheseuserids.iterator(); it.hasNext(); ) {
                        int onlyincluderesponsesfromtheseuserid = (Integer)it.next();
                        if (userid==onlyincluderesponsesfromtheseuserid){
                            isinlistofuserids = true;
                            break;
                        }
                    }
                    if (isinlistofuserids){
                        responsesTmp.add(questionresponse);
                    }
                }
                questionresponses = responsesTmp;
            }
            out.append(component.getHtmlForResult(questionresponses));
            out.append("</td>");
            out.append("</tr>");
            out.append("</table>");
        }
        return out.toString();
    }


}
