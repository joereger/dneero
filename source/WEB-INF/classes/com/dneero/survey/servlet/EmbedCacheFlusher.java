package com.dneero.survey.servlet;

import org.apache.log4j.Logger;
import com.dneero.cache.providers.CacheFactory;

/**
 * User: Joe Reger Jr
 * Date: Jul 6, 2008
 * Time: 8:43:47 AM
 */
public class EmbedCacheFlusher {

    public static void flushCache(int surveyid, int userid){
        Logger logger = Logger.getLogger(EmbedCacheFlusher.class);

        CacheFactory.getCacheProvider().flush("surveyflashservlet-s"+surveyid+"-u"+userid+"-ispreview"+false, "embeddedsurveycache"+"/"+"surveyid-"+surveyid);
        CacheFactory.getCacheProvider().flush("surveyflashservlet-s"+surveyid+"-u"+userid+"-ispreview"+true,  "embeddedsurveycache"+"/"+"surveyid-"+surveyid);
        CacheFactory.getCacheProvider().flush("surveyjavascriptservlet-s"+surveyid+"-u"+userid+"-ispreview"+true+"-makeHttpsIfSSLIsOn"+true,   "embeddedsurveycache"+"/"+"surveyid-"+surveyid);
        CacheFactory.getCacheProvider().flush("surveyjavascriptservlet-s"+surveyid+"-u"+userid+"-ispreview"+true+"-makeHttpsIfSSLIsOn"+false,  "embeddedsurveycache"+"/"+"surveyid-"+surveyid);
        CacheFactory.getCacheProvider().flush("surveyjavascriptservlet-s"+surveyid+"-u"+userid+"-ispreview"+false+"-makeHttpsIfSSLIsOn"+true,  "embeddedsurveycache"+"/"+"surveyid-"+surveyid);
        CacheFactory.getCacheProvider().flush("surveyjavascriptservlet-s"+surveyid+"-u"+userid+"-ispreview"+false+"-makeHttpsIfSSLIsOn"+false, "embeddedsurveycache"+"/"+"surveyid-"+surveyid);
        CacheFactory.getCacheProvider().flush("surveyflashfacebookservlet-s"+surveyid+"-u"+userid+"-ispreview"+false, "embeddedsurveycache"+"/"+"surveyid-"+surveyid);
        CacheFactory.getCacheProvider().flush("surveyflashfacebookservlet-s"+surveyid+"-u"+userid+"-ispreview"+true,  "embeddedsurveycache"+"/"+"surveyid-"+surveyid);
      

    }

}
