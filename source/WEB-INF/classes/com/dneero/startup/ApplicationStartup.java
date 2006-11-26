package com.dneero.startup;

import com.dneero.util.WebAppRootDir;
import com.dneero.util.Jsf;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.systemprops.SystemProperty;
import com.dneero.db.DbConfig;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.*;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.ee.servlet.QuartzInitializerServlet;

/**
 * User: Joe Reger Jr
 * Date: Apr 17, 2006
 * Time: 10:50:54 AM
 */
public class ApplicationStartup implements ServletContextListener {

    private static boolean ishibernateinitialized = false;
    private static boolean iswabapprooddirdiscovered = false;
    private static boolean isdatabasereadyforapprun = false;
    private static boolean isappstarted = false;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void contextInitialized(ServletContextEvent cse) {
       System.out.println("DNEERO: Application initialized");
       //Configure some dir stuff
        WebAppRootDir ward = new WebAppRootDir(cse.getServletContext());
        iswabapprooddirdiscovered = true;
        //Connect to database
        if (DbConfig.haveValidConfig()){
            //Run pre-hibernate db upgrades
            DbVersionCheck dbvcPre = new DbVersionCheck();
            dbvcPre.doCheck(DbVersionCheck.EXECUTE_PREHIBERNATE);
            //Set up hibernate
            HibernateUtil.getSession();
            ishibernateinitialized = true;
            //Run post-hibernate db upgrades
            DbVersionCheck dbvcPost = new DbVersionCheck();
            dbvcPost.doCheck(DbVersionCheck.EXECUTE_POSTHIBERNATE);
            //Check to make sure we're good to go
            if (RequiredDatabaseVersion.getHavecorrectversion()){
                isdatabasereadyforapprun = true;
                isappstarted = true;
            }
        } else {
            logger.info("DbConfig.haveValidConfig()=false");
        }
        //Report to log and XMPP
        logger.info("WebAppRootDir = " + WebAppRootDir.getWebAppRootPath());
        logger.info("dNeero Application Started!  Let's make some dinero!");
        SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SYSADMINS, "dNeero Application started!");
        xmpp.send();
    }

    public void contextDestroyed(ServletContextEvent cse) {
       SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SYSADMINS, "dNeero Application shut down!");
       xmpp.send();
       System.out.println("DNEERO: Application shut down!");
    }

    public static boolean getIswabapprooddirdiscovered() {
        return iswabapprooddirdiscovered;
    }

    public static boolean getIshibernateinitialized() {
        return ishibernateinitialized;
    }

    public static boolean getIsdatabasereadyforapprun() {
        return isdatabasereadyforapprun;
    }

    public static boolean getIsappstarted() {
        return isappstarted;
    }


}
