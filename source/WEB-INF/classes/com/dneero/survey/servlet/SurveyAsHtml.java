package com.dneero.survey.servlet;

import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.dao.Blog;
import com.dneero.display.SurveyTemplateProcessor;
import com.dneero.util.Str;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jun 19, 2006
 * Time: 10:42:43 AM
 */
public class SurveyAsHtml {


    public static String getHtml(Survey survey, User user, HttpServletRequest request){
        StringBuffer out = new StringBuffer();
        Logger logger = Logger.getLogger(SurveyAsHtml.class);

        if (survey!=null && user!=null){

            //@todo how is it possible to get a null referer?  i've had it happen
            String referer = request.getHeader("referer");
            logger.debug("referer=" + referer);

            //Find blogid
            Blog blog=null;
            if (user.getBlogger()!=null && referer!=null){
                for (Iterator it = user.getBlogger().getBlogs().iterator(); it.hasNext(); ) {
                    Blog blogTmp = (Blog)it.next();
                    if (referer.indexOf(blogTmp.getUrl())>0){
                        blog = blogTmp;
                        break;
                    }
                }
            }

            //Record
            RecordImpression.record(survey, user, blog, request);

            //Output
            SurveyTemplateProcessor stp = new SurveyTemplateProcessor(survey, user.getBlogger());
            out.append("<b>dNeero Survey</b>");
            out.append("<br/>");
            out.append(Str.cleanForjavascript(stp.getSurveyForDisplay()));
            out.append("<br/>");
            out.append("<b>dNeero - blog for pay</b>");

            return out.toString();
        } else {
            return "This embedded survey link is not correctly formatted.";
        }

    }

}
