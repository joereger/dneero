package com.dneero.display;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentTypes;
import com.dneero.finders.FindResponses;
import com.dneero.finders.SurveyCriteriaXML;
import com.dneero.htmluibeans.ResearcherRankPeopleComparatorPoints;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;

/**
 * User: Joe Reger Jr
 * Date: Jul 14, 2006
 * Time: 12:34:47 PM
 */
public class SurveyResultsUserQuestions {



    public static ArrayList<SurveyResultsUserQuestionsListitem> getUserQuestionResults(Survey survey, Blogger blogger, int referredbyuserid, ArrayList<Integer> onlyincluderesponsesfromtheseuserids, SurveyCriteriaXML filterCriteriaXML){
        ArrayList<SurveyResultsUserQuestionsListitem> out = new ArrayList<SurveyResultsUserQuestionsListitem>();
        Logger logger = Logger.getLogger(SurveyResultsDisplay.class);

        //Get all responses to this survey
        ArrayList<Response> allresponses = FindResponses.find(survey, filterCriteriaXML);
        //Starting set to display is all responses, then we'll remove some
        ArrayList<Response> responsestodisplay = allresponses;
        //If referredbyuserid is included, filter out those not referred by this userid
        if (referredbyuserid>0){
            ArrayList<Response> responsesTmp = new ArrayList<Response>();
            for (Iterator<Response> iterator1 = responsestodisplay.iterator(); iterator1.hasNext();) {
                Response tmpResponse = iterator1.next();
                if (tmpResponse.getReferredbyuserid()==referredbyuserid){
                    responsesTmp.add(tmpResponse);
                }
            }
            responsestodisplay = responsesTmp;
        }
        //If onlyincluderesponsesfromtheseuserids filter out those not in the list
        if (onlyincluderesponsesfromtheseuserids!=null && onlyincluderesponsesfromtheseuserids.size()>0){
            ArrayList<Response> responsesTmp = new ArrayList<Response>();
            for (Iterator<Response> iterator1 = responsestodisplay.iterator(); iterator1.hasNext();) {
                Response tmpResponse = iterator1.next();
                int userid = Blogger.get(tmpResponse.getBloggerid()).getUserid();
                boolean isinlistofuserids = false;
                for (Iterator it = onlyincluderesponsesfromtheseuserids.iterator(); it.hasNext(); ) {
                    int onlyincluderesponsesfromtheseuserid = (Integer)it.next();
                    if (userid==onlyincluderesponsesfromtheseuserid){
                        isinlistofuserids = true;
                        break;
                    }
                }
                if (isinlistofuserids){
                    responsesTmp.add(tmpResponse);
                }
            }
            responsestodisplay = responsesTmp;
        }
        //Generate an arraylist of responseidstodisplay to be used in the HQL query below
        ArrayList<Integer> responseidstodisplay = new ArrayList<Integer>();
        for (Iterator<Response> tmpResponses = responsestodisplay.iterator(); tmpResponses.hasNext();) {
            Response tmpResponse = tmpResponses.next();
            responseidstodisplay.add(tmpResponse.getResponseid());
        }

        //Iterate each question now
        for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
            Question question = iterator.next();
            if (question.getIsuserquestion()){
                logger.debug("found question.getQuestionid()="+question.getQuestionid());
                Component component = ComponentTypes.getComponentByID(question.getComponenttype(), question, blogger);
                logger.debug("found component.getName()="+component.getName());
                User user = User.get(question.getUserid());
//                out.append("<font class=\"smallfont\"><b><a href=\"/profile.jsp?userid="+user.getUserid()+"\">"+user.getFirstname()+" "+user.getLastname()+"</a> wanted to know:</b></font>");
//                out.append("<br/>");
//                out.append("<b>"+question.getQuestion()+"</b>");
//                out.append("<br/>");


                //Find all responses to this question
                List<Questionresponse>  questionresponses = new ArrayList<Questionresponse>();
                try{
                    logger.debug("About to execute SurveyResultsDisplay questionresponses query");
                    if (responseidstodisplay!=null && responseidstodisplay.size()>0){
                        questionresponses = HibernateUtil.getSession().createCriteria(Questionresponse.class)
                           .add(Restrictions.eq("questionid", question.getQuestionid()))
                           .add(Restrictions.in("responseid", responseidstodisplay))
                           .setCacheable(true)
                           .list();
                    }
                    logger.debug("Done executing SurveyResultsDisplay questionresponses query");
                } catch (Exception ex){
                    logger.error("", ex);
                }

                //Create the result for this question
                String htmlForResult = component.getHtmlForResult(questionresponses);

                //Add to the ArrayList
                SurveyResultsUserQuestionsListitem sruqli = new SurveyResultsUserQuestionsListitem();
                sruqli.setQuestion(question);
                sruqli.setUser(user);
                sruqli.setHtmlForResult(htmlForResult);
                out.add(sruqli);
            }
        }
        //Order the sruqli with a custom comparator... most responses listed first
        try{
            Collections.sort(out, new SurveyResultsUserQuestionsComparator());
        } catch (Exception ex){logger.error("", ex);}
        return out;
    }


}