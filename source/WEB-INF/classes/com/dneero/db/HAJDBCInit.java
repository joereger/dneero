package com.dneero.db;

import net.sf.hajdbc.sql.DriverDatabaseClusterMBean;
import net.sf.hajdbc.Database;

import javax.management.JMX;
import javax.management.ObjectName;
import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;
import java.util.Set;
import java.util.Iterator;
import java.sql.Connection;
import java.sql.DriverManager;

import com.dneero.systemprops.InstanceProperties;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Sep 29, 2008
 * Time: 7:24:47 AM
 */
public class HAJDBCInit {

    public static void init() throws Exception {
        Logger logger = Logger.getLogger(HAJDBCInit.class);
        if (1==1){
            return;
        }
        logger.debug("Start HAJDBCInit.init()");

        //The config file feeds the database setup.  It tells ha-jdbc which databases exist, what
        //their login credentials and connect urls are, and whether they should be active. emote

        //@todo How to handle dbs that are offline but should be part of the cluster according to the config file?

        //Safeguard.  Only do something if usehajdbc is on.
        if (InstanceProperties.getIshajdbcon()==null || !InstanceProperties.getIshajdbcon().equals("1")){
            logger.debug("InstanceProperties.getIshajdbcon()!=1 so aborting HAJDBCInit.init()");
            return;
        }

        //Call the driver itself
        //logger.debug("rstTest start");
        //String[][] rstTest= Db.RunSQL("SELECT 1");
        //logger.debug("rstTest end");

        //Call the driver itself
        logger.debug("Class.forName() start");
        try{
            //Class.forName("net.sf.hajdbc.sql.Driver");
            Connection conn = DriverManager.getConnection("jdbc:ha-jdbc:cluster1","root","password");
        } catch (Exception ex){
            logger.error("", ex);
        }
        logger.debug("Class.forName() end");

        //Debug
        logger.debug(getAllDatabases());

        //Connect to MBean
        String clusterId = "cluster1";
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = ObjectName.getInstance("net.sf.hajdbc", "cluster", clusterId);
        DriverDatabaseClusterMBean cluster = JMX.newMBeanProxy(server, name, DriverDatabaseClusterMBean.class);

        //Remove all databases from the HA-JDBC driver
        try{
            Set<String> activeDatabases = cluster.getActiveDatabases();
            for (Iterator<String> stringIterator=activeDatabases.iterator(); stringIterator.hasNext();) {
                String db=stringIterator.next();
                logger.debug("Found active database: "+db);
                logger.debug("Will deactivate: "+db);
                cluster.deactivate(db);
                logger.debug("Will remove: "+db);
                cluster.remove(db);
                logger.debug("Database removed: "+db);
            }
            logger.debug(getAllDatabases());
        } catch (Exception ex){
            logger.error("", ex);
        }
        try{
            Set<String> inactiveDatabases = cluster.getInactiveDatabases();
            for (Iterator<String> stringIterator=inactiveDatabases.iterator(); stringIterator.hasNext();) {
                String db=stringIterator.next();
                logger.debug("Found inactive database: "+db);
                logger.debug("Will remove: "+db);
                cluster.remove(db);
                logger.debug("Database removed: "+db);
            }
            logger.debug(getAllDatabases());
        } catch (Exception ex){
            logger.error("", ex);
        }

        //Add the databases listed in the config file, activate them
        if (InstanceProperties.getDbIsActive1()!=null && InstanceProperties.getDbIsActive1().equals("1")){
            String databaseid = "database1";
            logger.debug("Will attempt to add: "+databaseid);
            cluster.add(databaseid, InstanceProperties.getDbDriverName1(), InstanceProperties.getDbConnectionUrl1());
            logger.debug("Done adding: "+databaseid);
            Database db = cluster.getDatabase(databaseid);
            db.setUser(InstanceProperties.getDbUsername1());
            db.setPassword(InstanceProperties.getDbPassword1());
            logger.debug("Will attempt to activate: "+databaseid);
            cluster.activate(databaseid);
            logger.debug("Done activating: "+databaseid);
        }


        if (InstanceProperties.getDbIsActive2()!=null && InstanceProperties.getDbIsActive2().equals("1")){
            String databaseid = "database2";
            logger.debug("Will attempt to add: "+databaseid);
            cluster.add(databaseid, InstanceProperties.getDbDriverName2(), InstanceProperties.getDbConnectionUrl2());
            logger.debug("Done adding: "+databaseid);
            Database db = cluster.getDatabase(databaseid);
            db.setUser(InstanceProperties.getDbUsername2());
            db.setPassword(InstanceProperties.getDbPassword2());
            logger.debug("Will attempt to activate: "+databaseid);
            cluster.activate(databaseid);
            logger.debug("Done activating: "+databaseid);
        }

        if (InstanceProperties.getDbIsActive3()!=null && InstanceProperties.getDbIsActive3().equals("1")){
            String databaseid = "database3";
            logger.debug("Will attempt to add: "+databaseid);
            cluster.add(databaseid, InstanceProperties.getDbDriverName3(), InstanceProperties.getDbConnectionUrl3());
            logger.debug("Done adding: "+databaseid);
            Database db = cluster.getDatabase(databaseid);
            db.setUser(InstanceProperties.getDbUsername3());
            db.setPassword(InstanceProperties.getDbPassword3());
            logger.debug("Will attempt to activate: "+databaseid);
            cluster.activate(databaseid);
            logger.debug("Done activating: "+databaseid);
        }

        if (InstanceProperties.getDbIsActive4()!=null && InstanceProperties.getDbIsActive4().equals("1")){
            String databaseid = "database4";
            logger.debug("Will attempt to add: "+databaseid);
            cluster.add(databaseid, InstanceProperties.getDbDriverName4(), InstanceProperties.getDbConnectionUrl4());
            logger.debug("Done adding: "+databaseid);
            Database db = cluster.getDatabase(databaseid);
            db.setUser(InstanceProperties.getDbUsername4());
            db.setPassword(InstanceProperties.getDbPassword4());
            logger.debug("Will attempt to activate: "+databaseid);
            cluster.activate(databaseid);
            logger.debug("Done activating: "+databaseid);
        }

        logger.debug(getAllDatabases());

        //InstanceProperties.setHaveNewConfigToTest(true);
        //InstanceProperties.setHaveValidConfig(false);

        logger.debug("End HAJDBCInit.init()");

    }

    public static String getAllDatabases(){
        return "getAllDatabases() disabled";
        //return "Active: "+getActiveDatabases()+" Inactive: "+getInActiveDatabases();
    }

    public static String getActiveDatabases(){
        Logger logger = Logger.getLogger(HAJDBCInit.class);
        StringBuffer out = new StringBuffer();
        try{
        String clusterId = "cluster1";
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = ObjectName.getInstance("net.sf.hajdbc", "cluster", clusterId);
        DriverDatabaseClusterMBean cluster = JMX.newMBeanProxy(server, name, DriverDatabaseClusterMBean.class);
        Set<String> activeDatabases = cluster.getActiveDatabases();
        for (Iterator<String> stringIterator=activeDatabases.iterator(); stringIterator.hasNext();) {
            String db=stringIterator.next();
            String isalive = "dead";
            if (cluster.isAlive(db)){
                isalive = "live";
            }
            out.append(db+"("+isalive+")");
            if (stringIterator.hasNext()){
                out.append(",");
            }
        }
        } catch (Exception ex){
            logger.error("", ex);
        }
        return out.toString();
    }

    public static String getInActiveDatabases(){
        Logger logger = Logger.getLogger(HAJDBCInit.class);
        StringBuffer out = new StringBuffer();
        try{
        String clusterId = "cluster1";
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName name = ObjectName.getInstance("net.sf.hajdbc", "cluster", clusterId);
        DriverDatabaseClusterMBean cluster = JMX.newMBeanProxy(server, name, DriverDatabaseClusterMBean.class);
        Set<String> inactiveDatabases = cluster.getInactiveDatabases();
        for (Iterator<String> stringIterator=inactiveDatabases.iterator(); stringIterator.hasNext();) {
            String db=stringIterator.next();
            String isalive = "dead";
            if (cluster.isAlive(db)){
                isalive = "live";
            }
            out.append(db+"("+isalive+")");
            if (stringIterator.hasNext()){
                out.append(",");
            }
        }
        } catch (Exception ex){
            logger.error("", ex);
        }
        return out.toString();
    }




}
