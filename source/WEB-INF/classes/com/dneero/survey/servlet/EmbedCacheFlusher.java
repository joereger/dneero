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

        //@todo why am i not flushing a group here? "embeddedsurveycache"+"/"+"surveyid-"+surveyid

        CacheFactory.getCacheProvider("DbcacheProvider").flush("surveyflashservlet-s"+surveyid+"-u"+userid+"-ispreview"+false, "embeddedsurveycache"+"/"+"surveyid-"+surveyid);
        CacheFactory.getCacheProvider("DbcacheProvider").flush("surveyflashservlet-s"+surveyid+"-u"+userid+"-ispreview"+true,  "embeddedsurveycache"+"/"+"surveyid-"+surveyid);
        CacheFactory.getCacheProvider("DbcacheProvider").flush("surveyjavascriptservlet-s"+surveyid+"-u"+userid+"-ispreview"+true+"-makeHttpsIfSSLIsOn"+true,   "embeddedsurveycache"+"/"+"surveyid-"+surveyid);
        CacheFactory.getCacheProvider("DbcacheProvider").flush("surveyjavascriptservlet-s"+surveyid+"-u"+userid+"-ispreview"+true+"-makeHttpsIfSSLIsOn"+false,  "embeddedsurveycache"+"/"+"surveyid-"+surveyid);
        CacheFactory.getCacheProvider("DbcacheProvider").flush("surveyjavascriptservlet-s"+surveyid+"-u"+userid+"-ispreview"+false+"-makeHttpsIfSSLIsOn"+true,  "embeddedsurveycache"+"/"+"surveyid-"+surveyid);
        CacheFactory.getCacheProvider("DbcacheProvider").flush("surveyjavascriptservlet-s"+surveyid+"-u"+userid+"-ispreview"+false+"-makeHttpsIfSSLIsOn"+false, "embeddedsurveycache"+"/"+"surveyid-"+surveyid);
        CacheFactory.getCacheProvider("DbcacheProvider").flush("surveyflashfacebookservlet-s"+surveyid+"-u"+userid+"-ispreview"+false, "embeddedsurveycache"+"/"+"surveyid-"+surveyid);
        CacheFactory.getCacheProvider("DbcacheProvider").flush("surveyflashfacebookservlet-s"+surveyid+"-u"+userid+"-ispreview"+true,  "embeddedsurveycache"+"/"+"surveyid-"+surveyid);

        CacheFactory.getCacheProvider("DbcacheProvider").flush("surveyflashservlet-v2-s"+surveyid+"-u"+userid+"-ispreview"+false, "embeddedsurveycache"+"/"+"surveyid-"+surveyid);
        CacheFactory.getCacheProvider("DbcacheProvider").flush("surveyflashservlet-v2-s"+surveyid+"-u"+userid+"-ispreview"+true,  "embeddedsurveycache"+"/"+"surveyid-"+surveyid);
        CacheFactory.getCacheProvider("DbcacheProvider").flush("surveyflashservletxmlresponse-v2-s"+surveyid+"-u"+userid+"-ispreview"+false, "embeddedsurveycache"+"/"+"surveyid-"+surveyid);
        CacheFactory.getCacheProvider("DbcacheProvider").flush("surveyflashservletxmlresponse-v2-s"+surveyid+"-u"+userid+"-ispreview"+true,  "embeddedsurveycache"+"/"+"surveyid-"+surveyid);
        CacheFactory.getCacheProvider("DbcacheProvider").flush("surveyjavascriptservlet-v2-s"+surveyid+"-u"+userid+"-ispreview"+true+"-makeHttpsIfSSLIsOn"+true,   "embeddedsurveycache"+"/"+"surveyid-"+surveyid);
        CacheFactory.getCacheProvider("DbcacheProvider").flush("surveyjavascriptservlet-v2-s"+surveyid+"-u"+userid+"-ispreview"+true+"-makeHttpsIfSSLIsOn"+false,  "embeddedsurveycache"+"/"+"surveyid-"+surveyid);
        CacheFactory.getCacheProvider("DbcacheProvider").flush("surveyjavascriptservlet-v2-s"+surveyid+"-u"+userid+"-ispreview"+false+"-makeHttpsIfSSLIsOn"+true,  "embeddedsurveycache"+"/"+"surveyid-"+surveyid);
        CacheFactory.getCacheProvider("DbcacheProvider").flush("surveyjavascriptservlet-v2-s"+surveyid+"-u"+userid+"-ispreview"+false+"-makeHttpsIfSSLIsOn"+false, "embeddedsurveycache"+"/"+"surveyid-"+surveyid);
        CacheFactory.getCacheProvider("DbcacheProvider").flush("surveyflashfacebookservlet-v2-s"+surveyid+"-u"+userid+"-ispreview"+false, "embeddedsurveycache"+"/"+"surveyid-"+surveyid);
        CacheFactory.getCacheProvider("DbcacheProvider").flush("surveyflashfacebookservlet-v2-s"+surveyid+"-u"+userid+"-ispreview"+true,  "embeddedsurveycache"+"/"+"surveyid-"+surveyid);



    }

    public static void flushCache(int surveyid){
        Logger logger = Logger.getLogger(EmbedCacheFlusher.class);
        CacheFactory.getCacheProvider("DbcacheProvider").flush("embeddedsurveycache"+"/"+"surveyid-"+surveyid);
    }

}
