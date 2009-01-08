package com.dneero.helpers;

import org.hibernate.criterion.Restrictions;
import org.apache.log4j.Logger;

import java.util.List;

import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.Venue;

/**
 * User: Joe Reger Jr
 * Date: Jan 5, 2009
 * Time: 3:35:04 PM
 */
public class VenueUtils {

    public static boolean isVenueUrlInUse(String url){
        String urlClean = url.trim();
        urlClean = cleanForSaveToDb(urlClean);
        List<Venue> venues = HibernateUtil.getSession().createCriteria(Venue.class)
                               .add(Restrictions.eq("url", urlClean))
                               .add(Restrictions.eq("isactive", true))
                               .setCacheable(true)
                               .list();
        if (venues!=null && venues.size()>0){
            return true; 
        }
        return false;
    }

    public static Venue getNewOrInactive(String url){
        String urlClean = url.trim();
        urlClean = cleanForSaveToDb(urlClean);
        List<Venue> venues = HibernateUtil.getSession().createCriteria(Venue.class)
                               .add(Restrictions.eq("url", urlClean))
                               .add(Restrictions.eq("isactive", false))
                               .setCacheable(true)
                               .list();
        if (venues!=null && venues.size()>0){
            return venues.get(0);
        }
        return new Venue();
    }

    public static String cleanForSaveToDb(String url){
        String out = url;
        out = removeHttp(out);
        out = removeSlash(out);
        return out;
    }

    private static String removeHttp(String url){
        String out = url;
        if (url!=null){
            url = url.trim();
            if (url.indexOf("http://")>-1){
                out = url.trim().replace("http://", "");
                return out;
            }
            if (url.indexOf("https://")>-1){
                out = url.trim().replace("https://", "");
                return out;
            }
        }
        return out;
    }

    private static String removeSlash(String url){
        Logger logger = Logger.getLogger(VenueUtils.class);
        String out = url;
        if (url!=null && url.length()>0){
            url = url.trim();
            logger.debug("url="+url);
            logger.debug("url.substring(url.length()-1, url.length())="+url.substring(url.length()-1, url.length()));
            if (url.substring(url.length()-1, url.length()).equals("/") || url.substring(url.length()-1, url.length()).equals("\\")){
                logger.debug("end slash found... will try to truncate it");
                url = url.substring(0, url.length()-1);
                return url;
            }
        }
        logger.debug("out="+out);
        return out;
    }

    public static String stringToMatchForImpressions(String url){
        String out = url;
        if (out!=null){
            out = out.trim();
            //remove www
            if (out.substring(0,3).equals("www.")){
                out = out.substring(4, out.length());
            }
        }
        return out;
    }

}
