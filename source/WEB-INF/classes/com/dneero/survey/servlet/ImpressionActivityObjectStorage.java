package com.dneero.survey.servlet;

import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.*;
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

        //Find user
        User user = null;
        if (iao.getUserid()>0){
            user = User.get(iao.getUserid());
        }

        //Find blogid
        Blog blog=null;
        if (iao.getReferer()!=null && !iao.getReferer().equals("")){
            if (user!=null && user.getBlogger()!=null){
                for (Iterator it = user.getBlogger().getBlogs().iterator(); it.hasNext(); ) {
                    Blog tmpBlog = (Blog)it.next();
                    logger.debug("tmpBlog.getUrl()="+ tmpBlog.getUrl());
                    logger.debug("referer.indexOf(tmpBlog.getUrl())="+iao.getReferer().indexOf(tmpBlog.getUrl()));
                    if (iao.getReferer().indexOf(tmpBlog.getUrl())>=0){
                        logger.debug("found the blogid for this impression: blogid="+ tmpBlog.getBlogid());
                        blog = tmpBlog;
                    }
                }
            } else {
                //The user is null, no userid was passed on url line
                //@todo at this point i could pull up *all* blogs for *all* users and see if I can find anything... expensive though
            }
        } else {
            //The referer is blank
        }

        //Find bloggerid
        int bloggerid = 0;
        if (blog!=null && blog.getBloggerid()>0){
            bloggerid = blog.getBloggerid();
        }

        //See if there's an existing impression to append this to, if not create one
        Impression impression = null;
        List<Impression> impressions = HibernateUtil.getSession().createQuery("from Impression where surveyid='"+iao.getSurveyid()+"' and referer='"+iao.getReferer()+"'").list();
        if (impressions.size()>0){
            for (Iterator it = impressions.iterator(); it.hasNext(); ) {
                impression = (Impression)it.next();
                impression.setTotalimpressions(impression.getTotalimpressions()+1);
            }
        } else {
            impression = new Impression();
            impression.setFirstseendate(new Date());
            impression.setSurveyid(iao.getSurveyid());
            impression.setTotalimpressions(1);
            impression.setReferer(iao.getReferer());
        }
        try{impression.save();} catch (GeneralException gex){logger.error(gex);}

        //Record the impressiondetail
        Impressiondetail impressiondetail = new Impressiondetail();
        impressiondetail.setImpressionid(impression.getImpressionid());
        impressiondetail.setImpressiondate(new Date());
        impressiondetail.setIp(iao.getIp());
        impressiondetail.setQualifiesforpaymentstatus(Impressiondetail.QUALIFIESFORPAYMENTSTATUS_PENDING);
        impressiondetail.setBloggerid(bloggerid);
        
        //impression.getImpressiondetails().add(impressiondetail);
        try{impressiondetail.save();} catch (GeneralException gex){logger.error(gex);}

        //@todo is this the only thing that causes an entry in joinblogimpression? good for testing but is this necessary in the real world? especially once i start caching things?
        if (blog!=null && blog.getBlogid()>0){
            blog.getImpressions().add(impression);
            try{blog.save();} catch (GeneralException gex){logger.error(gex);}
        }

    }

}
