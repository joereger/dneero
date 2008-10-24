package com.dneero.startup.dbversion;

import com.dneero.startup.UpgradeDatabaseOneVersion;
import com.dneero.db.Db;
import org.apache.log4j.Logger;
import com.dneero.db.DbConfig;
/**
 * User: Joe Reger Jr
 * Date: Nov 26, 2006
 * Time: 11:57:46 AM
 */
public class Version33 implements UpgradeDatabaseOneVersion {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void doPreHibernateUpgrade(DbConfig dbConfig){
        logger.debug("doPreHibernateUpgrade() start");
        logger.debug("Not really doing anything.");
        logger.debug("doPreHibernateUpgrade() finish");
    }

    public void doPostHibernateUpgrade(DbConfig dbConfig){
        logger.debug("doPostHibernateUpgrade() start");


        //-----------------------------------
        //-----------------------------------
        int countdds = Db.RunSQLUpdate("UPDATE balance SET isresearchermoney=false", dbConfig);
        //-----------------------------------
        //-----------------------------------


        //-----------------------------------
        //-----------------------------------
        int countddes = Db.RunSQLUpdate("UPDATE balance SET isbloggermoney=true", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int countddsd = Db.RunSQLUpdate("UPDATE balance SET isresearchermoney=true where userid='2'", dbConfig);
        //-----------------------------------
        //-----------------------------------


        //-----------------------------------
        //-----------------------------------
        int countddesds = Db.RunSQLUpdate("UPDATE balance SET isbloggermoney=false where userid='2'", dbConfig);
        //-----------------------------------
        //-----------------------------------



        logger.debug("doPostHibernateUpgrade() finish");
    }



}
