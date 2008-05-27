package com.dneero.startup;

import com.dneero.dao.Errorlogging;
import com.dneero.dao.hibernate.HibernateUtil;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: May 27, 2008
 * Time: 9:07:12 AM
 */
public class Log4jLevels {


    public static void setLevels(){
        Logger logger = Logger.getLogger(Log4jLevels.class);
        try{
            List errorloggings = HibernateUtil.getSession().createQuery("from Errorlogging").setMaxResults(2500).list();
            for (Iterator iterator=errorloggings.iterator(); iterator.hasNext();) {
                try{
                    Errorlogging errorlogging=(Errorlogging) iterator.next();
                    Logger lgr = Logger.getLogger(errorlogging.getClassname());
                    Level level = Level.ERROR;
                    if (errorlogging.getLevel().equals("DEBUG")){
                        level = Level.DEBUG;
                    } else if (errorlogging.getLevel().equals("INFO")){
                        level = Level.INFO;
                    } else if (errorlogging.getLevel().equals("WARN")){
                        level = Level.WARN;
                    } else if (errorlogging.getLevel().equals("ERROR")){
                        level = Level.ERROR;
                    } else if (errorlogging.getLevel().equals("FATAL")){
                        level = Level.FATAL;
                    } else if (errorlogging.getLevel().equals("ALL")){
                        level = Level.ALL;
                    } else if (errorlogging.getLevel().equals("OFF")){
                        level = Level.OFF;
                    }
                    lgr.setLevel(level);
                } catch (Exception ex){
                    logger.error("", ex);
                }
            }
        } catch (Exception ex){
            logger.error("", ex);
        }
    }


}
