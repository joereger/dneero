package com.dneero.survey.servlet;

import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.dao.Blog;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jul 5, 2006
 * Time: 2:08:26 PM
 */
public class RecordImpression {

    public static void record(Survey survey, HttpServletRequest request){
        Logger logger = Logger.getLogger(RecordImpression.class);

        String referer = request.getHeader("referer");
        logger.debug("referer=" + referer);

        //Find user
        User user = null;
        if (request.getParameter("userid")!=null && com.dneero.util.Num.isinteger(request.getParameter("userid"))){
            user = User.get(Integer.parseInt(request.getParameter("userid")));
        }

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

        //Create an IAO
        ImpressionActivityObject iao = new ImpressionActivityObject();
        iao.setSurveyid(survey.getSurveyid());
        if (blog!=null){
            iao.setBlogid(blog.getBlogid());
        } else {
            iao.setBlogid(0);
        }

        //Write iao to db
        //@todo cache impression activity for performance gain
        //I've already built the iao object so that it's easy to put into a simple memory list and then parse on some interval, writing to the database
        ImpressionActivityObjectStorage.store(iao);
    }

}
