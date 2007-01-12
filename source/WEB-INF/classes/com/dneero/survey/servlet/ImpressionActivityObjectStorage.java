package com.dneero.survey.servlet;

import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.*;
import com.dneero.util.GeneralException;
import com.dneero.util.Num;
import com.dneero.util.Time;

import java.util.List;
import java.util.Iterator;
import java.util.Date;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

/**
 * User: Joe Reger Jr
 * Date: Jul 5, 2006
 * Time: 2:14:00 PM
 */
public class ImpressionActivityObjectStorage {

    public static void store(ImpressionActivityObject iao){
        Logger logger = Logger.getLogger(ImpressionActivityObjectStorage.class);

        //Find user
        User user = null;
        if (iao.getUserid()>0){
            user = User.get(iao.getUserid());
        }

        //Find blog as indicated by referer
        Blog blog=null;
        if (iao.getReferer()!=null && !iao.getReferer().equals("")){
            blog = findBlogFromReferer(iao.getReferer(), user);
        } else {
            //The referer is blank
        }


        //Find bloggerid
        int bloggerid = 0;
        if (blog!=null && blog.getBloggerid()>0){
            bloggerid = blog.getBloggerid();
        }

        //Find the survey
        Survey survey = null;
        int surveyimpressionsthatqualifyforpayment = 0;
        if (iao.getSurveyid()>0){
            survey = Survey.get(iao.getSurveyid());
            if (survey!=null && survey.getSurveyid()>0){
                Object obj = HibernateUtil.getSession().createQuery("select sum(impressionsqualifyingforpayment) from Impression where surveyid='"+survey.getSurveyid()+"'").uniqueResult();
                if (obj!=null && Num.isinteger(String.valueOf(obj))){
                    surveyimpressionsthatqualifyforpayment = (Integer)obj;
                }
            } else {
                //Error, survey not found, don't record
                logger.debug("Surveyid="+iao.getSurveyid()+" not found so aborting impression save.");
                return;
            }
        }

        //Find number of impressions on this blog qualify for payment
        int blogimpressionsthatqualifyforpayment = getImpressionsThatQualifyForPayment(blog, survey);

        //See if this impressiondetail qualifies for payment
        int qualifiesforpaymentstatus = Impressiondetail.QUALIFIESFORPAYMENTSTATUS_TRUE;
        String qualifiesforpaymentstatusreason = "";
        if (surveyimpressionsthatqualifyforpayment>survey.getMaxdisplaystotal()){
            qualifiesforpaymentstatus = Impressiondetail.QUALIFIESFORPAYMENTSTATUS_FALSE;
            qualifiesforpaymentstatusreason = "The survey already reached its maximum number of survey displays across all blogs.";
        }
        if (blogimpressionsthatqualifyforpayment+1>survey.getMaxdisplaysperblog()){
            qualifiesforpaymentstatus = Impressiondetail.QUALIFIESFORPAYMENTSTATUS_FALSE;
            qualifiesforpaymentstatusreason = "This blog has already displayed the survey the maximum number of times for a blog.";
        }

        //logger.debug("iao.getDate()="+iao.getDate().toString());
        //logger.debug("iao.getDate()="+ Time.dateformatfordb(Time.getCalFromDate(iao.getDate())));

        //See if there's an existing impression to append this to, if not create one
        Impression impression = null;
        List<Impression> impressions = HibernateUtil.getSession().createQuery("from Impression where surveyid='"+iao.getSurveyid()+"' and referer='"+iao.getReferer()+"'").list();
        if (impressions.size()>0){
            for (Iterator it = impressions.iterator(); it.hasNext(); ) {
                impression = (Impression)it.next();
                //Only increment if this qualifies
                if (qualifiesforpaymentstatus==Impressiondetail.QUALIFIESFORPAYMENTSTATUS_TRUE){
                    impression.setImpressionsqualifyingforpayment(impression.getImpressionsqualifyingforpayment()+1);
                }
            }
        } else {
            impression = new Impression();
            impression.setFirstseen(iao.getDate());
            impression.setSurveyid(iao.getSurveyid());
            //Only increment if this qualifies
            if (qualifiesforpaymentstatus==Impressiondetail.QUALIFIESFORPAYMENTSTATUS_TRUE){
                impression.setImpressionsqualifyingforpayment(1);
            } else {
                impression.setImpressionsqualifyingforpayment(0);
            }
            impression.setReferer(iao.getReferer());
        }
        try{impression.save();} catch (GeneralException gex){logger.error(gex);}

        //Record the impressiondetail
        Impressiondetail impressiondetail = new Impressiondetail();
        impressiondetail.setImpressionid(impression.getImpressionid());
        impressiondetail.setImpressiondate(iao.getDate());
        impressiondetail.setIp(iao.getIp());
        impressiondetail.setQualifiesforpaymentstatus(qualifiesforpaymentstatus);
        impressiondetail.setBloggerid(bloggerid);
        impressiondetail.setQualifiesforpaymentstatusreason(qualifiesforpaymentstatusreason);
        
        //impression.getImpressiondetails().add(impressiondetail);
        try{impressiondetail.save();} catch (GeneralException gex){logger.error(gex);}

        //@todo is this the only thing that causes an entry in joinblogimpression? good for testing but is this necessary in the real world? especially once i start caching things?
        if (blog!=null && blog.getBlogid()>0){
            blog.getImpressions().add(impression);
            try{blog.save();} catch (GeneralException gex){logger.error(gex);}
        }

    }

    public static Blog findBlogFromReferer(String referer){
        return findBlogFromReferer(referer, null);
    }

    public static Blog findBlogFromReferer(String referer, User user){
        Logger logger = Logger.getLogger(ImpressionActivityObjectStorage.class);
        if (referer!=null && !referer.equals("")){
            //Strip ending slash
            logger.debug("referer.substring(referer.length()-1, referer.length())="+referer.substring(referer.length()-1, referer.length()));
            if (referer.substring(referer.length()-1, referer.length()).equals("/")){
                logger.debug("removing slash from referer: referer="+referer);
                referer = referer.substring(0, referer.length()-1);
            } else {
                logger.debug("not removing slash from referer: referer="+referer);
            }
            if (user!=null && user.getBloggerid()>0){
                for (Iterator it = Blogger.get(user.getBloggerid()).getBlogs().iterator(); it.hasNext(); ) {
                    Blog blog = (Blog)it.next();
                    logger.debug("blog.getUrl()="+ blog.getUrl());
                    logger.debug("referer.indexOf(blog.getUrl())="+referer.indexOf(blog.getUrl()));
                    if (referer.indexOf(blog.getUrl())>=0){
                        logger.debug("found the blogid for this impression: blogid="+ blog.getBlogid());
                        return blog;
                    }
                }
            } else {
                //The user is null, no userid was passed on url line
                List<Blog> blogs = HibernateUtil.getSession().createCriteria(Blog.class)
                                                   .add(Restrictions.like("url", "%"+referer+"%"))
                                                   .setCacheable(true)
                                                   .list();
                for (Iterator<Blog> iterator = blogs.iterator(); iterator.hasNext();) {
                    Blog blog = iterator.next();
                    logger.debug("blog.getUrl()="+ blog.getUrl());
                    logger.debug("referer.indexOf(blog.getUrl())="+referer.indexOf(blog.getUrl()));
                    if (referer.indexOf(blog.getUrl())>=0){
                        logger.debug("found the blogid for this impression: blogid="+ blog.getBlogid());
                        return blog;
                    }
                }
            }
        }
        return null;
    }

    public static int getImpressionsThatQualifyForPayment(Blog blog, Survey survey){
        int blogimpressionsthatqualifyforpayment = 0;
        if (blog!=null && blog.getBlogid()>0){
            blog = Blog.get(blog.getBlogid());
            for (Iterator it2 = blog.getImpressions().iterator(); it2.hasNext(); ) {
                Impression impression = (Impression)it2.next();
                //Add the impressions that already exist... note that i'm only looking at the impressionsqualifyingforpayment var so that var must be kept up-to-date
                if (survey.getSurveyid()==impression.getSurveyid()){
                    blogimpressionsthatqualifyforpayment = blogimpressionsthatqualifyforpayment + impression.getImpressionsqualifyingforpayment();
                }
            }
        }
        return blogimpressionsthatqualifyforpayment;
    }

}
