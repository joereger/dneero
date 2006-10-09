package com.dneero.survey.servlet;

import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.dao.Blog;
import com.dneero.dao.hibernate.HibernateUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

/**
 * User: Joe Reger Jr
 * Date: Jul 5, 2006
 * Time: 2:08:26 PM
 */
public class RecordImpression {

    public static void record(Survey survey, HttpServletRequest request){
        Logger logger = Logger.getLogger(RecordImpression.class);

        String referer = request.getHeader("referer");
        if (referer==null){
            referer="";
        }
        logger.debug("referer=" + referer);

        String ip = request.getRemoteAddr();
        if (ip==null){
            ip="";
        }
        logger.debug("ip=" + ip);

        //Find user
        User user = null;
        if (request.getParameter("u")!=null && com.dneero.util.Num.isinteger(request.getParameter("u"))){
            user = User.get(Integer.parseInt(request.getParameter("u")));
        }

        //Find blogid
        Blog blog=null;
        if (user!=null && user.getBlogger()!=null && referer!=null && !referer.equals("")){
            logger.debug("user.getBlogger() not null");
            for (Iterator it = user.getBlogger().getBlogs().iterator(); it.hasNext(); ) {
                Blog blogTmp = (Blog)it.next();
                logger.debug("blogTmp.getUrl()="+blogTmp.getUrl());
                logger.debug("referer.indexOf(blogTmp.getUrl())="+referer.indexOf(blogTmp.getUrl()));
                if (referer.indexOf(blogTmp.getUrl())>=0){
                    logger.debug("setting blog=blogTmp");
                    blog = blogTmp;
                }
            }
        }


        //Create an IAO
        ImpressionActivityObject iao = new ImpressionActivityObject();
        iao.setSurveyid(survey.getSurveyid());
        iao.setReferer(referer);
        iao.setIp(ip);
        if (blog!=null){
            logger.debug("iao: blog.getBlogid()="+blog.getBlogid());
            iao.setBlogid(blog.getBlogid());
        } else {
            logger.debug("iao: blog is null");
            iao.setBlogid(0);
        }

        //Write iao to db
        //@todo cache impression activity for performance gain
        //I've already built the iao object so that it's easy to put into a simple memory list and then parse on some interval, writing to the database
        ImpressionActivityObjectStorage.store(iao);
    }

}
