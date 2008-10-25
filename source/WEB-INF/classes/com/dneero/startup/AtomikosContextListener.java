package com.dneero.startup;

/**
 * User: Joe Reger Jr
 * Date: Oct 25, 2008
 * Time: 12:13:32 PM
 */
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;

import com.atomikos.icatch.jta.UserTransactionManager;


public class AtomikosContextListener implements ServletContextListener {
    
    private UserTransactionManager utm;

    public void contextInitialized(ServletContextEvent event) {
        try {
            utm = new UserTransactionManager();
            utm.init();
            System.out.println("initialized transaction manager");
        }
        catch (Exception ex) {
            utm = null;
            throw new RuntimeException("cannot initialize UserTransactionManager", ex);
        }
    }

    public void contextDestroyed(ServletContextEvent event) {
        if (utm != null) {
            utm.close();
            System.out.println("shut down transaction manager");
        }
    }
}

