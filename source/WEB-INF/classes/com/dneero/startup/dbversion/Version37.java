package com.dneero.startup.dbversion;

import com.dneero.startup.UpgradeDatabaseOneVersion;
import com.dneero.db.Db;
import com.dneero.util.RandomString;
import org.apache.log4j.Logger;
import com.dneero.db.DbConfig;
/**
 * User: Joe Reger Jr
 * Date: Nov 26, 2006
 * Time: 11:57:46 AM
 */
public class Version37 implements UpgradeDatabaseOneVersion {

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
        int countdds = Db.RunSQLUpdate("UPDATE balance SET isreferralmoney=false", dbConfig);
        //-----------------------------------
        //-----------------------------------


        //-----------------------------------
        //-----------------------------------
        int countddfs = Db.RunSQLUpdate("UPDATE balance SET isresellermoney=false", dbConfig);
        //-----------------------------------
        //-----------------------------------



        logger.debug("doPostHibernateUpgrade() finish");
    }



}
