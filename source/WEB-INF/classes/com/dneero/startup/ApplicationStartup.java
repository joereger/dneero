package com.dneero.startup;

import com.dneero.util.WebAppRootDir;
import com.dneero.dao.hibernate.HibernateUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
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
public class ApplicationStartup extends HttpServlet {

    //@todo implement ApplicationStartup as a ServletContextListener for better app server portability

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            //Must be here so that Tomcat considers this to be a servlet and will load it at Tomcat startup
        }

        public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            //Must be here so that Tomcat considers this to be a servlet and will load it at Tomcat startup
        }

        public void init(ServletConfig config){
            //Configure some dir stuff
            WebAppRootDir ward = new WebAppRootDir(config);
            HibernateUtil.getSession();
            logger.info("dNeero Application started!  Let's make some dinero!  WebAppRootDir = " + WebAppRootDir.getWebAppRootPath());
        }

}
