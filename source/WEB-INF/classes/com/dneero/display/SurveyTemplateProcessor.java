package com.dneero.display;

import com.dneero.dao.Survey;
import com.dneero.dao.Question;
import com.dneero.dao.Blogger;
import com.dneero.dao.Response;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Str;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentTypes;
import com.dneero.systemprops.BaseUrl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

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
            List results = HibernateUtil.getSession().createQuery("from Response where bloggerid='"+blogger.getBloggerid()+"' and surveyid='"+survey.getSurveyid()+"'").list();
            for (Iterator iterator = results.iterator(); iterator.hasNext();) {
                Response resp = (Response) iterator.next();
                if (response==null){
                    response = resp;
                }
                //Choose the most recent response by this blogger... there should generally only be one
                if (response.getResponsedate().before(resp.getResponsedate())){
                    response = resp;
                }
            }
        }
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
            Component component = ct.getByTagSyntax(m.group(), blogger);
            if (component!=null){
                m.appendReplacement(out, Str.cleanForAppendreplacement(component.getHtmlForInput() + "<br>"));
            }
        }
        try{
            m.appendTail(out);
        } catch (Exception e){
            //Do nothing... just null pointer
        }
        return wrapInStandardSurveyWrapper(out.toString(), false, makeHttpsIfSSLIsOn);
    }


    public String getSurveyForDisplay(boolean makeHttpsIfSSLIsOn){
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
            Component component = ct.getByTagSyntax(m.group(), blogger);
            if (component!=null){
                m.appendReplacement(out, "<p>"+Str.cleanForAppendreplacement(component.getHtmlForDisplay(response) + "</p>"));
            }
        }
        try{
            m.appendTail(out);
        } catch (Exception e){
            //Do nothing... just null pointer
        }
        return out.toString().trim();
    }

    public String wrapInStandardSurveyWrapper(String in, boolean includeFooter, boolean makeHttpsIfSSLIsOn){
        StringBuffer out = new StringBuffer();
        String baseurl = BaseUrl.get(makeHttpsIfSSLIsOn);
        if(includeFooter){
            out.append("<!-- Start dNeero Survey -->\n" +
                    "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" background=\""+baseurl+"images/surveyinblog/dneero-survey-bg.gif\" width=\"100%\" style=\"border: 2px solid #b4b4b4; background-repeat: repeat-x;\">\n" +
                    "\t<tr>\n" +
                    "\t\t<td valign=\"top\" colspan=\"7\" style=\"padding: 15px;\">\n" +
                    "\t\t\t<!-- Start Survey Questions -->\n" +
                    in +
                    "\n" +
                    "\t\t\t<!-- End Survey Questions -->\n" +
                    "\t\t</td>\n" +
                    "\t</tr>\n" +
                    "\t<tr>\t\n" +
                    "\t\t<td valign=\"bottom\" align=\"right\" bgcolor=\"#ffffff\" width=\"50%\">\n" +
                    "&nbsp;\n"+
                    "\t\t</td>\n" +
                    "\t\t<td valign=\"bottom\" align=\"right\" bgcolor=\"#ffffff\">\n" +
                    "\t\t\t<a href='"+baseurl+"surveytake.jsf?surveyid="+survey.getSurveyid()+"'><img src=\""+baseurl+"images/surveyinblog/dneero-survey-questionmark.gif\" border=\"0\"></a>\n" +
                    "\t\t</td>\n" +
                    "\t\t<td valign=\"bottom\" align=\"right\" bgcolor=\"#ffffff\" width=\"7\">\n" +
                    "\t\t\t<img src=\""+baseurl+"images/surveyinblog/dneero-survey-dots.gif\" border=\"0\">\n" +
                    "\t\t</td>\n" +
                    "\t\t<td valign=\"bottom\" align=\"right\" bgcolor=\"#ffffff\">\n" +
                    "\t\t\t<a href='"+baseurl+"surveyanswers.jsf?surveyid="+survey.getSurveyid()+"'><img src=\""+baseurl+"images/surveyinblog/dneero-survey-people.gif\" border=\"0\"></a>\n" +
                    "\t\t</td>\n" +
                    "\t\t<td valign=\"bottom\" align=\"right\" bgcolor=\"#ffffff\" width=\"7\">\n" +
                    "\t\t\t<img src=\""+baseurl+"images/surveyinblog/dneero-survey-dots.gif\" border=\"0\">\n" +
                    "\t\t</td>\n" +
                    "\t\t<td valign=\"bottom\" align=\"right\" bgcolor=\"#ffffff\">\n" +
                    "\t\t\t<a href='"+baseurl+"surveydisclosure.jsf?surveyid="+survey.getSurveyid()+"'><img src=\""+baseurl+"images/surveyinblog/dneero-survey-logo.gif\" border=\"0\"></a>\n" +
                    "\t\t</td>\n" +
                    "\t\t<td valign=\"bottom\" align=\"right\" bgcolor=\"#ffffff\" width=\"50%\">\n" +
                    "&nbsp;\n"+
                    "\t\t</td>\n" +
                    "\t</tr>\n" +
                    "</table>\n" +
                    "<!-- End dNeero Survey -->");
         } else {
            out.append(in);
         }
         return out.toString();
    }

    public static String getAutoGeneratedTemplateForSurvey(Survey survey){
        StringBuffer out = new StringBuffer();
        HibernateUtil.getSession().saveOrUpdate(survey);
        for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
            Question question = iterator.next();
            out.append("<p>"+"\n");
            out.append("<$question_"+question.getQuestionid()+"$>"+"\n");
            out.append("</p>"+"\n");
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
            String qtag = "<$question_"+question.getQuestionid()+"$>";
            if (currentTemplate==null || currentTemplate.indexOf(qtag)<0){
                if (out.length()>0){
                    out.append("<p>"+"\n");
                }
                out.append("<$question_"+question.getQuestionid()+"$>"+"\n");
                if (out.length()>0){
                    out.append("</p>"+"\n");
                }
            }
        }
        return out.toString();
    }


}
