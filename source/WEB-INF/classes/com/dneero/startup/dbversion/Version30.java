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
public class Version30 implements UpgradeDatabaseOneVersion {

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
        int countdds = Db.RunSQLUpdate("UPDATE researcher SET notaccuratemaxpossspend='0'", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int countdddfs = Db.RunSQLUpdate("UPDATE researcher SET notaccurateremainingpossspend='0'", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int countddfds = Db.RunSQLUpdate("UPDATE researcher SET notaccuratecurrbalance='0'", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int countddfsds = Db.RunSQLUpdate("UPDATE researcher SET notaccuratepercentofmax='0'", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int countddffds = Db.RunSQLUpdate("UPDATE researcher SET notaccurateamttocharge='0'", dbConfig);
        //-----------------------------------
        //-----------------------------------






        logger.debug("doPostHibernateUpgrade() finish");
    }



}
