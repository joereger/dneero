package com.dneero.survey.servlet;

import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.Impression;
import com.dneero.dao.Impressiondetail;
import com.dneero.dao.Blog;
import com.dneero.dao.Blogger;
import com.dneero.util.GeneralException;

import java.util.List;
import java.util.Iterator;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jul 5, 2006
 * Time: 2:14:00 PM
 */
public class ImpressionActivityObjectStorage {

    public static void store(ImpressionActivityObject iao){
        Logger logger = Logger.getLogger(ImpressionActivityObjectStorage.class);
        Impression impression = null;

        List<Impression> impressions = HibernateUtil.getSession().createQuery("from Impression where surveyid='"+iao.getSurveyid()+"' and referer='"+iao.getReferer()+"'").list();
        if (impressions.size()>0){
            for (Iterator it = impressions.iterator(); it.hasNext(); ) {
                impression = (Impression)it.next();
                impression.setTotalimpressions(impression.getTotalimpressions()+1);
                break;
            }
        } else {
            impression = new Impression();
            impression.setFirstseendate(new Date());
            impression.setSurveyid(iao.getSurveyid());
            impression.setTotalimpressions(1);
            impression.setReferer(iao.getReferer());
        }

        try{
            impression.save();
        } catch (GeneralException gex){
            logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
        }

        Impressiondetail impressiondetail = new Impressiondetail();
        impressiondetail.setImpressionid(impression.getImpressionid());
        impressiondetail.setImpressiondate(new Date());
        impressiondetail.setIp(iao.getIp());
        impressiondetail.setQualifiesforpaymentstatus(Impressiondetail.QUALIFIESFORPAYMENTSTATUS_PENDING);
        impressiondetail.setBloggerid(Blogger.get(Blog.get(iao.getBlogid()).getBloggerid()).getBloggerid());
        
        //impression.getImpressiondetails().add(impressiondetail);

        try{
            impressiondetail.save();
        } catch (GeneralException gex){
            logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
        }

        if (iao.getBlogid()>0){
            Blog blog = Blog.get(iao.getBlogid());
            if (blog!=null && blog.getBlogid()>0){
                blog.getImpressions().add(impression);
                try{
                    blog.save();
                } catch (GeneralException gex){
                    logger.error(gex);
                }
            }
        }

    }

}
