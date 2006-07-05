package com.dneero.survey.servlet;

import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.dao.Blog;

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

            //Try to figure out which blogid this is coming from
            String referer = request.getHeader("referer");
            logger.debug("referer=" + referer);

            //Find blogid
            Blog blog=null;
            if (user.getBlogger()!=null){
                for (Iterator it = user.getBlogger().getBlogs().iterator(); it.hasNext(); ) {
                    blog = (Blog)it.next();
                    if (referer.indexOf(blog.getUrl())>0){
                        break;
                    }
                }
            }

            //Record
            RecordImpression.record(survey, user, blog, request);

            out.append("<b>dNeero Survey</b>");
            out.append("<br>");
            out.append(survey.getSurveybody());
            out.append("<br>");
            out.append("<b>dNeero - blog for pay</b>");

            return out.toString();
        } else {
            return "This embedded survey link is not correctly formatted.";
        }

    }

}
