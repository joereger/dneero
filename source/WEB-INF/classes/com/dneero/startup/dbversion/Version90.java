package com.dneero.startup.dbversion;

import com.dneero.db.Db;
import com.dneero.db.DbConfig;
import com.dneero.startup.UpgradeDatabaseOneVersion;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Nov 26, 2006
 * Time: 11:57:46 AM
 */
public class Version90 implements UpgradeDatabaseOneVersion {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void doPreHibernateUpgrade(DbConfig dbConfig){
        logger.debug("doPreHibernateUpgrade() start");


        logger.debug("doPreHibernateUpgrade() finish");
    }

    public void doPostHibernateUpgrade(DbConfig dbConfig){
        logger.debug("doPostHibernateUpgrade() start");

        //-----------------------------------
        //-----------------------------------
        int count1 = Db.RunSQLUpdate("UPDATE survey SET isanonymousresponseallowed=false", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int count2 = Db.RunSQLUpdate("UPDATE pl SET isanonymousresponseallowed=false", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int count3 = Db.RunSQLUpdate("UPDATE user SET isanonymous=false", dbConfig);
        //-----------------------------------
        //-----------------------------------


        logger.debug("doPostHibernateUpgrade() finish");
    }







}