package com.dneero.survey.servlet;

import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.Impression;
import com.dneero.util.GeneralException;
import com.dneero.util.Jsf;

import java.util.List;
import java.util.Iterator;

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
            impression.setSurveyid(iao.getSurveyid());
            impression.setBlogid(iao.getBlogid());
            impression.setTotalimpressions(1);
        }

        impression.setReferer(iao.getReferer());

        try{
            impression.save();
        } catch (GeneralException gex){
            logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
        }
    }

}
