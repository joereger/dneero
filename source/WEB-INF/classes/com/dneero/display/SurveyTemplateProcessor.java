package com.dneero.display;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentTypes;
import com.dneero.util.Str;
import com.dneero.helpers.NicknameHelper;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: Joe Reger Jr
 * Date: Jul 6, 2006
 * Time: 1:36:39 PM
 */
public class SurveyTemplateProcessor {

    private Survey survey;
    private Blogger blogger;
    private Response response;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public SurveyTemplateProcessor(Survey survey, Blogger blogger){
        this.survey = survey;
        this.blogger = blogger;
        if (survey!=null && blogger!=null){
            logger.debug("blogger.getBloggerid()="+blogger.getBloggerid());
            logger.debug("survey.getSurveyid()="+survey.getSurveyid());
            List results = HibernateUtil.getSession().createQuery("from Response where bloggerid='"+blogger.getBloggerid()+"' and surveyid='"+survey.getSurveyid()+"'").list();
            logger.debug("results.size()="+results.size());
            for (Iterator iterator = results.iterator(); iterator.hasNext();) {
                Response resp = (Response) iterator.next();
                if (response==null){
                    response=resp;
                }
                //Choose the most recent response by this blogger... there should generally only be one
                if (response.getResponsedate().before(resp.getResponsedate())){
                    response=resp;
                }
            }
        } else {
            logger.debug("survey==null and blogger==null");
        }
        if (response==null){
            logger.debug("response==null");
        } else {
            logger.debug("response!=null responseid="+response.getResponseid());
        }
    }

    public SurveyTemplateProcessor(Survey survey, Blogger blogger, Response response){
        this.survey = survey;
        this.blogger = blogger;
        this.response = response;
    }

    public String getSurveyForTaking(boolean makeHttpsIfSSLIsOn){
        StringBuffer out = new StringBuffer();
        String template = survey.getTemplate();
        if (template==null){
            template = "";
        }
        if (template.equals("")){
            template = getAutoGeneratedTemplateForSurvey(survey);
        }
        template = appendExtraQuestionsIfNecessary(survey, template);

        Pattern p = Pattern.compile("\\<\\$(.|\\n)*?\\$\\>");
        Matcher m = p.matcher(template);
        while(m.find()) {
            ComponentTypes ct = new ComponentTypes();
            Component component = ct.getByTagSyntax(m.group(), blogger, survey);
            if (component!=null){
                m.appendReplacement(out, Str.cleanForAppendreplacement(component.getHtmlForInput(response) + "<br>"));
            }
        }
        try{
            m.appendTail(out);
        } catch (Exception e){
            //Do nothing... just null pointer
        }
        return "<div style=\"background : #ffffff; border: 0px solid #ffffff; padding : 5px; width : 400px; overflow : auto;\">"+out.toString()+"</div>";
    }


    public String getSurveyForDisplay(boolean makeHttpsIfSSLIsOn, boolean displayEvenIfSysadminRejected){
        StringBuffer out = new StringBuffer();

        //If this response is rejected, don't allow display
        if (!displayEvenIfSysadminRejected){
            if (blogger!=null && response!=null){
                if (response.getIssysadminrejected()){
                    return "<p>This conversation is not currently available.</p>";
                }
            }
        }

        String template = survey.getTemplate();
        if (template==null){
            template = "";
        }
        if (template.equals("")){
            template = getAutoGeneratedTemplateForSurvey(survey);
        }
        template = appendExtraQuestionsIfNecessary(survey, template);

        Pattern p = Pattern.compile("\\<\\$(.|\\n)*?\\$\\>");
        Matcher m = p.matcher(template);
        while(m.find()) {
            ComponentTypes ct = new ComponentTypes();
            Component component = ct.getByTagSyntax(m.group(), blogger, survey);
            if (component!=null){
                m.appendReplacement(out, "<p>"+Str.cleanForAppendreplacement(component.getHtmlForDisplay(response) + "</p>"));
            }
        }
        try{
            m.appendTail(out);
        } catch (Exception e){
            //Do nothing... just null pointer
        }
        //Add Userquestion stuff for display
        List<Questionresponse> responses = new ArrayList<Questionresponse>();
        if (blogger!=null && response!=null){
            responses = HibernateUtil.getSession().createQuery("from Questionresponse where bloggerid='"+blogger.getBloggerid()+"' and responseid='"+response.getResponseid()+"'").list();
            if (responses!=null){
                logger.debug("responses.size()="+responses.size());
                for (Iterator<Questionresponse> qrIt=responses.iterator(); qrIt.hasNext();) {
                    Questionresponse questionresponse=qrIt.next();
                    Question question = Question.get(questionresponse.getQuestionid());
                    if (question.getIsuserquestion()){
                        Component component = ComponentTypes.getComponentByID(question.getComponenttype(), question, blogger);
                        User userwhocreatedquestion = User.get(question.getUserid());
                        out.append("<p>");
                        out.append(NicknameHelper.getNameOrNickname(userwhocreatedquestion)+" asked:");
                        out.append("</p>");
                        out.append("<p>");
                        out.append(component.getHtmlForDisplay(response));
                        out.append("</p>");
                    }
                }
            }
        }

        return out.toString().trim();
    }



    public static String getAutoGeneratedTemplateForSurvey(Survey survey){
        StringBuffer out = new StringBuffer();
        HibernateUtil.getSession().saveOrUpdate(survey);
        for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
            Question question = iterator.next();
            if (!question.getIsuserquestion()){
                out.append("\n"+"<p>");
                out.append("<$question_"+question.getQuestionid()+"$>");
                out.append("</p>");
            }
        }
        return out.toString();
    }

    public static String appendExtraQuestionsIfNecessary(Survey survey, String currentTemplate){
        StringBuffer out = new StringBuffer();
        Logger logger = Logger.getLogger(SurveyTemplateProcessor.class);
        if (currentTemplate!=null){
            out.append(currentTemplate);
        }
        for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
            Question question = iterator.next();
            if (!question.getIsuserquestion()){
                String qtag = "<$question_"+question.getQuestionid()+"$>";
                if (currentTemplate==null || currentTemplate.indexOf(qtag)<0){
                    out.append("\n"+"<p>");
                    out.append("<$question_"+question.getQuestionid()+"$>");
                    out.append("</p>");
                }
            }
        }
        return out.toString();
    }


    public Response getResponse() {
        return response;
    }

    public Blogger getBlogger() {
        return blogger;
    }

    public Survey getSurvey() {
        return survey;
    }
}
