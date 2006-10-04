package com.dneero.dao.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.HibernateException;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.hibernate.event.*;
import org.hibernate.cfg.Configuration;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.Serializable;


import com.dneero.db.DbConfig;
import com.dneero.util.GeneralException;
import com.dneero.util.WebAppRootDir;
import com.dneero.dao.hibernate.eventlisteners.RegerPostLoadEventListener;
import com.dneero.dao.hibernate.eventlisteners.RegerPreInsertEventListener;
import com.dneero.dao.hibernate.eventlisteners.RegerPreDeleteEventListener;
import com.dneero.dao.hibernate.eventlisteners.RegerPreUpdateEventListener;

public class HibernateUtil {

    private static SessionFactory sessionFactory;
    private static final ThreadLocal session = new ThreadLocal();

    static {
        Logger logger = Logger.getLogger(HibernateUtil.class);
        if (DbConfig.haveValidConfig()){
            try {
                //Create a configuration object
                Configuration conf = new Configuration();
                //Add config file
                String pathConfig = WebAppRootDir.getWebAppRootPath() + "WEB-INF/classes/hibernate.cfg.xml";
                conf.configure(new File(pathConfig));
                //conf.addPackage("com.dneero.dao.hibernate");
                //conf.addAnnotatedClass(Banner.class);
                //Set up database connection
                conf.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
                conf.setProperty("hibernate.connection.username", DbConfig.getDbUsername());
                conf.setProperty("hibernate.connection.url", DbConfig.getDbConnectionUrl());
                conf.setProperty("hibernate.connection.password", DbConfig.getDbPassword());
                conf.setProperty("hibernate.connection.driver_class", DbConfig.getDbDriverName());

                //Misc
                //conf.setProperty("hibernate.current_session_context_class", "thread");
                conf.setProperty("hibernate.show_sql", "true");
                conf.setProperty("hibernate.generate_statistics", "true");

                //Save or update validation listener
                PreDeleteEventListener[] stack1 = { new RegerPreDeleteEventListener() };
                conf.getEventListeners().setPreDeleteEventListeners(stack1);
                PreUpdateEventListener[] stack2 = { new RegerPreUpdateEventListener() };
                conf.getEventListeners().setPreUpdateEventListeners(stack2);
                PreInsertEventListener[] stack3 = { new RegerPreInsertEventListener() };
                conf.getEventListeners().setPreInsertEventListeners(stack3);
                PostLoadEventListener[] stack4 = { new RegerPostLoadEventListener() };
                conf.getEventListeners().setPostLoadEventListeners(stack4);

                //Interceptor(s)
                //conf.setInterceptor(new HibernateInterceptor());

                //Connection pool
                conf.setProperty("hibernate.c3p0.min_size", String.valueOf(DbConfig.getDbMinIdle()));
                conf.setProperty("hibernate.c3p0.max_size", String.valueOf(DbConfig.getDbMaxActive()));
                conf.setProperty("hibernate.c3p0.timeout", String.valueOf(DbConfig.getDbMaxWait()));
                conf.setProperty("hibernate.c3p0.max_statements", "50");

                //Second level cache
                conf.setProperty("hibernate.cache.use_second_level_cache", "true");
                conf.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.OSCacheProvider");
                conf.setProperty("hibernate.cache.use_structured_entries", "true");
                conf.setProperty("hibernate.cache.use_query_cache", "true");
                conf.setProperty("hibernate.cache.usage", "nonstrict-read-write");

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
                logger.info("HibernateUtil: username:"+DbConfig.getDbUsername());
            } catch (Throwable ex) {
                logger.error("Initial Hibernate SessionFactory creation failed.", ex);
                throw new ExceptionInInitializerError(ex);
            }
        }
    }



    public static Session getSession() throws HibernateException {
        if (DbConfig.haveValidConfig()){
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
        if (DbConfig.haveValidConfig()){
            try{
                Session s = (Session)session.get();
                session.set(null);
                if (s!=null && s.isOpen()){
                    s.close();
                }
            } catch (Exception ex){
                logger.error(ex);
            }
        }
    }


    public static Serializable getIdentifier(Object obj){
        return getSession().getIdentifier(obj);
    }




}
