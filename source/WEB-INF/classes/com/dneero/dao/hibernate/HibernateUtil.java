package com.dneero.dao.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.HibernateException;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.hibernate.cfg.Configuration;
import org.apache.log4j.Logger;
import org.jboss.cache.transaction.DummyTransactionManager;

import java.io.File;
import java.io.Serializable;


import com.dneero.systemprops.InstanceProperties;
import com.dneero.systemprops.WebAppRootDir;
import com.dneero.startup.ApplicationStartup;
import com.dneero.db.Db;

import javax.naming.NamingException;

public class HibernateUtil {

    private static SessionFactory sessionFactory;
    private static final ThreadLocal session = new ThreadLocal();
    private static boolean isinitializingsessionrightnow = false;



    private static void initializeSession(){
        Logger logger = Logger.getLogger(HibernateUtil.class);
        logger.info("Starting HibernateUtil.initializeSession()");
        if (ApplicationStartup.getIswabapprooddirdiscovered()){
            if (Db.getHaveValidConfig()){
                try {
                    //Create a configuration object
                    Configuration conf = new Configuration();
                    //Add config file
                    String pathConfig = WebAppRootDir.getWebAppRootPath() + "WEB-INF/classes/hibernate.cfg.xml";
                    conf.configure(new File(pathConfig));
                    //conf.addPackage("com.dneero.dao.hibernate");
                    //conf.addAnnotatedClass(Banner.class);
                    //Set up database connection
                    //conf.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
                    //conf.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLInnoDBDialect");
                    conf.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLMyISAMDialect");
                    conf.setProperty("hibernate.connection.username", InstanceProperties.getDbUsername());
                    conf.setProperty("hibernate.connection.url", InstanceProperties.getDbConnectionUrl());
                    conf.setProperty("hibernate.connection.password", InstanceProperties.getDbPassword());
                    conf.setProperty("hibernate.connection.driver_class", InstanceProperties.getDbDriverName());

                    //Misc
                    //conf.setProperty("hibernate.current_session_context_class", "thread");
                    //Turn on by setting instance prop hibernateShowSql="1"
                    if (InstanceProperties.getHibernateShowSql()){
                        conf.setProperty("hibernate.show_sql", "true");
                    } else {
                        conf.setProperty("hibernate.show_sql", "false");
                    }
                    conf.setProperty("hibernate.generate_statistics", "false");                    

                    //Connection pool
                    conf.setProperty("hibernate.c3p0.min_size", String.valueOf(InstanceProperties.getDbMinIdle()));
                    conf.setProperty("hibernate.c3p0.max_size", String.valueOf(InstanceProperties.getDbMaxActive()));
                    conf.setProperty("hibernate.c3p0.timeout", String.valueOf(InstanceProperties.getDbMaxWait()));
                    conf.setProperty("hibernate.c3p0.max_statements", "50");


                    //Second level cache
                    conf.setProperty("hibernate.cache.use_second_level_cache", "true");
                    //@If on jboss use cache.provider_class=org.jboss.ejb3.entity.TreeCacheProviderHook - see http://docs.jboss.com/jbossas/guides/clusteringguide/r2/en/html_single/#clustering-intro 1.4.2.2
                    //conf.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.OSCacheProvider");
                    conf.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.TreeCacheProvider");
                    //conf.setProperty("hibernate.cache.provider_class", "org.jboss.ejb3.entity.TreeCacheProviderHook");
                    conf.setProperty("hibernate.cache.use_structured_entries", "true");
                    conf.setProperty("hibernate.cache.use_query_cache", "true");
                    conf.setProperty("hibernate.cache.usage", "transactional");

                    //Session context mgr
                    //conf.setProperty("hibernate.current_session_context_class", "thread");

                    //Update the schema in the database
                    try{
                        SchemaUpdate schemaUpdate = new SchemaUpdate(conf);
                        schemaUpdate.execute(true, true);
                    } catch (Exception e){
                        logger.error("Error updating schema from hibernate to database", e);
                    }
                    // Create the SessionFactory
                    sessionFactory = conf.buildSessionFactory();

                    logger.info("HibernateUtil Session Initialized. Let's rock some data abstration!");
                    logger.info("HibernateUtil: username:"+ InstanceProperties.getDbUsername());
                } catch (Throwable ex) {
                    logger.error("Initial Hibernate SessionFactory creation failed.", ex);
                    throw new ExceptionInInitializerError(ex);
                }
            }
        }
        logger.info("Ending HibernateUtil.initializeSession()");
    }




    public static Session getSession() throws HibernateException {
        Logger logger = Logger.getLogger(HibernateUtil.class);
        if (sessionFactory==null){
            if (!isinitializingsessionrightnow){
                isinitializingsessionrightnow  = true;
                try{initializeSession();}catch(Exception ex){logger.error("",ex);}
                isinitializingsessionrightnow = false;
            } else {
                return null;
            }
        }
        if (sessionFactory!=null){
                Session s = (Session) HibernateUtil.session.get();
                if (s==null||!s.isOpen()) {
                    s = sessionFactory.openSession();
                    session.set(s);
                }
                return s;
        }
        return null;
    }

    public static void closeSession() throws HibernateException {
        Logger logger = Logger.getLogger(HibernateUtil.class);
        try{
            Session s = (Session)session.get();
            session.set(null);
            if (s!=null && s.isOpen()){
                s.close();
            }
        } catch (Exception ex){
            logger.error("",ex);
        }
    }

    public static void killSessionFactory(){
        sessionFactory.close();
        sessionFactory = null;
    }


    public static Serializable getIdentifier(Object obj){
        return getSession().getIdentifier(obj);
    }


    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void startSession(){
        HibernateUtil.getSession().beginTransaction();
    }

    public static void endSession(){
        Logger logger = Logger.getLogger(HibernateUtil.class);
        //End Hibernate Session
        try{
            HibernateUtil.getSession().getTransaction().commit();
            HibernateUtil.closeSession();
        } catch (HibernateException hex){
            logger.debug("HibernateException found in save()");
            logger.error("HibernateException", hex);
            HibernateUtil.getSession().getTransaction().rollback();
            HibernateUtil.closeSession();
        }
    }
}
