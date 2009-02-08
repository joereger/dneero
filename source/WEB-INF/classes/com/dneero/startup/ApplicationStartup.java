package com.dneero.startup;

import com.dneero.dao.Pl;
import com.dneero.dao.hibernate.HibernateSessionQuartzCloser;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.hibernate.HibernateUtilDbcache;
import com.dneero.dao.hibernate.HibernateUtilImpressions;
import com.dneero.pageperformance.PagePerformanceUtil;
import com.dneero.scheduledjobs.SystemStats;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.systemprops.SystemProperty;
import com.dneero.systemprops.WebAppRootDir;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.db.Db;
import com.dneero.cache.providers.CacheFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.LoggerFactory;


import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


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
    private static Scheduler scheduler = null;

    public void contextInitialized(ServletContextEvent cse) {
       System.out.println("DNEERO: Application initialized");
        printBug();
        //Shut down mbeans, if they're running
        shutdownCacheMBean();
        //Configure some dir stuff
        WebAppRootDir ward = new WebAppRootDir(cse.getServletContext());
        iswabapprooddirdiscovered = true;
        //Init Ha JDBC
        //System.out.println("DNEERO: Will call HAJDBCInit.init()");
        //try{HAJDBCInit.init();} catch (Exception ex){logger.error("", ex);}
        //Run pre-hibernate db upgrades
        System.out.println("DNEERO: Start with DbVersion PreHibernate Check");
        DbVersionCheck dbvcPre = new DbVersionCheck();
        dbvcPre.doCheck(DbVersionCheck.EXECUTE_PREHIBERNATE);
        System.out.println("DNEERO: End with DbVersion PreHibernate Check");
        //Test the database config
        System.out.println("DNEERO: Will test the database config");
        Db.testConfig();
        System.out.println("DNEERO: Done testing the database config");
        //Connect to database
        if (Db.getHaveValidConfig()){
            //Set up hibernate
            System.out.println("DNEERO: Start Initialize Hibernate");
            HibernateUtil.getSession();
            System.out.println("DNEERO: End Initialize Hibernate");
            System.out.println("DNEERO: Start Initialize HibernateDbcache");
            HibernateUtilDbcache.getSession();
            System.out.println("DNEERO: Done initializing HibernateDbcache");
            System.out.println("DNEERO: Start Initialize HibernateImpressions");
            HibernateUtilImpressions.getSession();
            System.out.println("DNEERO: Done initializing HibernateImpressions");
            ishibernateinitialized = true;
            //Run post-hibernate db upgrades
            System.out.println("DNEERO: Start DbVersion PostHibernate Check");
            DbVersionCheck dbvcPost = new DbVersionCheck();
            dbvcPost.doCheck(DbVersionCheck.EXECUTE_POSTHIBERNATE);
            System.out.println("DNEERO: Done with DbVersion PostHibernate Check");
            //Check to make sure we're good to go
            if (RequiredDatabaseVersion.getHavecorrectversion()){
                isdatabasereadyforapprun = true;
                isappstarted = true;
            }
        } else {
            logger.info("InstanceProperties.getHaveValidConfig()=false");
        }
        //If the database is running
        if (isdatabasereadyforapprun){
            System.out.println("DNEERO: isdatabasereadyforapprun=true");
            //Start Main DB Hibernate session
            System.out.println("DNEERO: Will Start Main DB Hibernate Session");
            HibernateUtil.startSession();
            System.out.println("DNEERO: Started Main DB Hibernate Sessions");
            //Make sure we have at least one PL
            guaranteeAtLeastOnePlExists();
            //Load SystemProps
            SystemProperty.refreshAllProps();
            System.out.println("DNEERO: SystemProperties loaded");
            //Set logging levels
            Log4jLevels.setLevels();
            System.out.println("DNEERO: Log4jLevels set");
            System.out.println("DNEERO: Will Close Main Db Hibernate Session");
            HibernateUtil.endSession();
            System.out.println("DNEERO: Closed Main Db Hibernate Session");
            //End Main DB Hibernate session

            //Refresh SystemStats
            System.out.println("DNEERO: Start SystemStats refresh");
            SystemStats ss = new SystemStats();
            try{ss.execute(null);}catch(Exception ex){logger.error("",ex);}
            System.out.println("DNEERO: Done SystemStats refresh");

            //Start DbCache DB Hibernate session
            System.out.println("DNEERO: Will Start DbCache DB Hibernate Session");
            HibernateUtilDbcache.startSession();
            System.out.println("DNEERO: Started DbCache DB Hibernate Sessions");
            System.out.println("DNEERO: Will Close DbCache Db Hibernate Session");
            HibernateUtilDbcache.endSession();
            System.out.println("DNEERO: Closed DbCache Db Hibernate Session");
            //End DbCache DB Hibernate session

            //Start Impressions DB Hibernate session
            System.out.println("DNEERO: Will Start Impressions DB Hibernate Session");
            HibernateUtilImpressions.startSession();
            System.out.println("DNEERO: Started Impressions DB Hibernate Sessions");
            System.out.println("DNEERO: Will Close Impressions Db Hibernate Session");
            HibernateUtilImpressions.endSession();
            System.out.println("DNEERO: Closed Impressions Db Hibernate Session");
            //End Impressions DB Hibernate session

            //Init nondao cache
            System.out.println("DNEERO: Start init CacheFactory");
            try{CacheFactory.getCacheProvider().get("", "");}catch(Exception ex){logger.error("",ex);}
            System.out.println("DNEERO: End init CacheFactory");
            //End init nondao cache

            //Init Quartz
            System.out.println("DNEERO: Start Init Quartz");
            initQuartz(cse.getServletContext());
            //Init Quartz listener
            try{
                SchedulerFactory schedFact = new StdSchedulerFactory();
                schedFact.getScheduler().addGlobalJobListener(new HibernateSessionQuartzCloser());
            } catch (Exception ex){logger.error("",ex);}
            System.out.println("DNEERO: Done Init Quartz");
        } else {
            System.out.println("DNEERO: isdatabasereadyforapprun=false");
            logger.info("Database not ready.");    
        }
        //Report to log and XMPP
        System.out.println("DNEERO: WebAppRootDir = " + WebAppRootDir.getWebAppRootPath());
        System.out.println("DNEERO: App started!  Letz make sum dinero... cos we needs teh vacation!");
        SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SYSADMINS, "dNeero Application started! ("+WebAppRootDir.getUniqueContextId()+")");
        xmpp.send();

    }

    public void contextDestroyed(ServletContextEvent cse) {
        //Record the last of the impressions dude
        try{com.dneero.scheduledjobs.ImpressionActivityObjectQueue task = new com.dneero.scheduledjobs.ImpressionActivityObjectQueue();
            task.execute(null);} catch (Exception ex){logger.error("",ex);}
        //Record last of pageperformance numbers
        try{PagePerformanceUtil.recordAndFlush();}catch(Exception ex){logger.error("", ex);}
        //Notify sysadmins
        SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SYSADMINS, "dNeero Application shut down! ("+WebAppRootDir.getUniqueContextId()+")");
        xmpp.send();
        //Shut down Hibernate
        try{
            HibernateUtil.closeSession();
            HibernateUtil.killSessionFactory();
        } catch (Exception ex){logger.error("",ex);}
        try{
            HibernateUtilDbcache.closeSession();
            HibernateUtilDbcache.killSessionFactory();
        } catch (Exception ex){logger.error("",ex);}
        try{
            HibernateUtilImpressions.closeSession();
            HibernateUtilImpressions.killSessionFactory();
        } catch (Exception ex){logger.error("",ex);}
        //Shut down MBeans
        shutdownCacheMBean();
        //Log it
        System.out.println("DNEERO: Application shut down! ("+InstanceProperties.getInstancename()+")");
    }

    public static void initQuartz(ServletContext sc){
        //
        //If there are errors in this code, check the org.quartz.ee.servlet.QuartzInitializerServlet
        //I grabbed this code from there instead of having the app server call it from web.xml
        //Potential problem with web.xml is that I may not add my listeners quickly enough.
        //
        Logger logger = Logger.getLogger(ApplicationStartup.class);
        logger.debug("Quartz Initializing");
        String QUARTZ_FACTORY_KEY = "org.quartz.impl.StdSchedulerFactory.KEY";
		StdSchedulerFactory factory;
		try {

			String configFile = null;
			if (configFile != null) {
				factory = new StdSchedulerFactory(configFile);
			} else {
				factory = new StdSchedulerFactory();
			}

			// Should the Scheduler being started now or later
			String startOnLoad = null;
			if (startOnLoad == null || (Boolean.valueOf(startOnLoad).booleanValue())) {
				// Start now
				scheduler = factory.getScheduler();
				scheduler.start();
				logger.debug("Quartz Scheduler has been started");
			} else {
				logger.debug("Quartz Scheduler has not been started - Use scheduler.start()");
			}

			logger.debug("Quartz Scheduler Factory stored in servlet context at key: " + QUARTZ_FACTORY_KEY);
			sc.setAttribute(QUARTZ_FACTORY_KEY, factory);

		} catch (Exception e) {
			logger.error("Quartz failed to initialize", e);
		}
    }

    public static void shutdownCacheMBean(){
        Logger logger = Logger.getLogger(ApplicationStartup.class);
        try{
            ArrayList servers = MBeanServerFactory.findMBeanServer(null);
            for (Iterator it = servers.iterator(); it.hasNext(); ) {
                try{
                    MBeanServer mBeanServer = (MBeanServer)it.next();
                    //List of beans to log
                    Set mBeanNames = mBeanServer.queryNames(null, null);
                    for (Iterator iterator = mBeanNames.iterator(); iterator.hasNext();) {
                        ObjectName objectName = (ObjectName) iterator.next();
                        //logger.debug("MBean -> Name:"+objectName.getCanonicalName()+" Domain:"+objectName.getDomain());
                        if (objectName.getCanonicalName().indexOf("dNeero-TreeCache-Cluster")>-1){
                            try{
                                logger.info("Unregistering MBean: "+objectName.getCanonicalName());
                                mBeanServer.unregisterMBean(objectName);
                            } catch (Exception ex){
                                logger.error("",ex);
                            }
                        }
                    }
                } catch (Exception ex){
                    logger.error("",ex);
                }
            }
        } catch (Exception ex){
            logger.error("",ex);
        }
    }

    public static void shutdownMBean(String name){
        Logger logger = Logger.getLogger(ApplicationStartup.class);
        try{
            ArrayList servers = MBeanServerFactory.findMBeanServer(null);
            for (Iterator it = servers.iterator(); it.hasNext(); ) {
                try{
                    MBeanServer mBeanServer = (MBeanServer)it.next();
                    //Do the remove
                    ObjectName tcObject = new ObjectName(name);
                    if (mBeanServer.isRegistered(tcObject)){
                        logger.info(tcObject.getCanonicalName()+" was already registered");
                        try{
                            logger.info("Unregistering MBean: "+tcObject.getCanonicalName());
                            mBeanServer.unregisterMBean(tcObject);
                        } catch (Exception ex){
                            logger.error("",ex);
                        }
                    } else {
                        logger.info(tcObject.getCanonicalName()+" was *not* already registered");
                    }
                } catch (Exception ex){
                    logger.error("",ex);
                }
            }
        } catch (Exception ex){
            logger.error("",ex);
        }
    }

    private static void guaranteeAtLeastOnePlExists(){
        Logger logger = Logger.getLogger(ApplicationStartup.class);
        List pls = HibernateUtil.getSession().createQuery("from Pl").list();
        if (pls==null || pls.size()<=0){
            Pl pl = new Pl();
            pl.setName("dNeero.com");
            pl.setSubdomain("");
            pl.setCustomdomain1("");
            pl.setCustomdomain2("");
            pl.setCustomdomain3("");
            pl.setEmailhtmlfooter("");
            pl.setEmailhtmlheader("");
            pl.setWebhtmlfooter("");
            pl.setWebhtmlheader("");
            pl.setIshttpson(false);
            pl.setNameforui("dNeero.com");
            pl.setTwitterusername("");
            pl.setTwitterpassword("");
            pl.setHomepagetemplate("");
            pl.setPeers("0");
            try{pl.save();}catch(Exception ex){logger.error(ex);}
        }
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

    public static void printBug(){
        StringBuffer out = new StringBuffer();
        out.append("\n\n\n"+"           ,  .'''''.  ...    ''''',  .'           \n" +
                "            ','     ,.MMMM;.;'      '.             \n" +
                "             ;;    ;MMMMMMMMM;     ;;'             \n" +
                "            :'M:  ;MMMMM  MMMM;.  :M':             \n" +
                "            : M:  MMMMM   MMMMM:  :M  .           \n" +
                "           .' M:  MMMMM  MMMMMM:  :M. ;           \n" +
                "           ; :M'  :MMMMMMMMMMMM'  'M: :           \n" +
                "           : :M: .;\"MMMMMMMMM\":;. ,M: :           \n" +
                "           :  ::,MMM;.M\":::M.;MMM ::' :           \n" +
                "         ,.;    ;MMMMMM;:MMMMMMMM:    :,.         \n" +
                "         MMM.;.,MMMMMMMM;MMMMMMMM;.,;.MMM         \n" +
                "         M':''':MMMMMMMMM;MMMMMMMM: \"': M         \n" +
                "         M.:   ;MMMMMMMMMMMMMMMMMM;   : M         \n" +
                "         :::   MMMMMMMMMMM;MMMMMMMM   ::M         \n" +
                "        ,'';   MMMMMMMMMMMM:MMMMMMM   :'\".         \n" +
                "      ,'   :   MMMMMMMMMMMM:MMMMMMM   :   '.       \n" +
                "     '     :  'MMMMMMMMMMMMM:MMMMMM   ;     '     \n" +
                "     ,.....;.. MMMMMMMMMMMMM:MMMMMM ..:....;.     \n" +
                "     :MMMMMMMM MMMMMMMMMMMMM:MMMMMM MMMMMMMM:     \n" +
                "     :MM\"\"\":\"\" MMMMMMMMMMMMM:MMMMMM \"\": \"'MM:     \n" +
                "      MM:   :  MMMMMMMMMMMMM:MMMMMM  ,'  :MM       \n" +
                "      'MM   :  :MMMMMMMMMMMM:MMMMM:  :   ;M:       \n" +
                "       :M;  :  'MMMMMMMMMMMMMMMMMM'  :  ;MM       \n" +
                "       :MM. :   :MMMMMMMMMM;MMMMM:   :  MM:       \n" +
                "        :M: :    MMMMMMMMM'MMMMMM'   : :MM'       \n" +
                "        'MM :    \"MMMMMMM:;MMMMM\"   ,' ;M\"         \n" +
                "         'M  :    \"\"\"\"\":;;;\"\"\"\"\"    :  M:         \n" +
                "         ;'  :     \"MMMMMMMM;.\"     :  \"\".         \n" +
                "       ,;    :      :MMMMMMM:;.     :    '.       \n" +
                "      :'     :    ,MM''\"\"\"\"'':M:    :     ';       \n" +
                "     ;'      :    ;M'         MM.   :       ;.     \n" +
                "   ,'        :    \"            \"'   :        '.   \n" +
                "   '        :'                       '        ''   \n" +
                " .          :                        '          ' \n" +
                "'          ;                          ;          ' \n" +
                "          ;                            ' "+"\n\n\n\n");
        out.append("dNeero: NEW AND IMPROVED... NOW WITH MORE BUGS!"+"\n\n\n");

        System.out.print(out.toString());
    }


}
