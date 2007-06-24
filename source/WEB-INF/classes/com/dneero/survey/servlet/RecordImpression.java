package com.dneero.survey.servlet;

import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.dao.Blog;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.scheduledjobs.ImpressionActivityObjectQueue;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.List;
import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

/**
 * User: Joe Reger Jr
 * Date: Jul 5, 2006
 * Time: 2:08:26 PM
 */
public class RecordImpression {

    public static void record(HttpServletRequest request){
        Logger logger = Logger.getLogger(RecordImpression.class);

        //Find referer
        String referer = request.getHeader("referer");
        if (referer==null){
            referer="";
        }
        logger.debug("referer=" + referer);

        //Find ip address
        String ip = request.getRemoteAddr();
        if (ip==null){
            ip="";
        }
        logger.debug("ip=" + ip);

        //Find userid
        int userid = 0;
        if (request.getParameter("u")!=null && com.dneero.util.Num.isinteger(request.getParameter("u"))){
            userid = Integer.parseInt(request.getParameter("u"));
        }
        logger.debug("userid=" + userid);

        //Find surveyid
        int surveyid = 0;
        if (request.getParameter("s")!=null && com.dneero.util.Num.isinteger(request.getParameter("s"))){
            surveyid = Integer.parseInt(request.getParameter("s"));
        }
        logger.debug("surveyid=" + surveyid);

        //Create an IAO
        ImpressionActivityObject iao = new ImpressionActivityObject();
        iao.setSurveyid(surveyid);
        iao.setReferer(referer);
        iao.setIp(ip);
        iao.setUserid(userid);
        iao.setDate(new Date());

        //Write iao to db
        //I've already built the iao object so that it's easy to put into a simple memory list and then parse on some interval, writing to the database
        //ImpressionActivityObjectStorage.store(iao);
        try{
            ImpressionActivityObjectQueue.addIao(iao);
        } catch (Exception ex){
            logger.error(ex);
        }
    }

}
