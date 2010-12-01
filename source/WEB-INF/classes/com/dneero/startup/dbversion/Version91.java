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
public class Version91 implements UpgradeDatabaseOneVersion {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void doPreHibernateUpgrade(DbConfig dbConfig){
        logger.debug("doPreHibernateUpgrade() start");


        logger.debug("doPreHibernateUpgrade() finish");
    }

    public void doPostHibernateUpgrade(DbConfig dbConfig){
        logger.debug("doPostHibernateUpgrade() start");

        //-----------------------------------
        //-----------------------------------
        int count1 = Db.RunSQLUpdate("UPDATE survey SET customvar1=''", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int count1dsf = Db.RunSQLUpdate("UPDATE survey SET customvar2=''", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int count1rew = Db.RunSQLUpdate("UPDATE survey SET customvar3=''", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int count2 = Db.RunSQLUpdate("UPDATE pl SET customvar1ison=false", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int count2sdf = Db.RunSQLUpdate("UPDATE pl SET customvar2ison=false", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int count2rte = Db.RunSQLUpdate("UPDATE pl SET customvar3ison=false", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int count2sdgf = Db.RunSQLUpdate("UPDATE pl SET customvar1name=''", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int count2gdf = Db.RunSQLUpdate("UPDATE pl SET customvar2name=''", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int count2asd = Db.RunSQLUpdate("UPDATE pl SET customvar3name=''", dbConfig);
        //-----------------------------------
        //-----------------------------------


        logger.debug("doPostHibernateUpgrade() finish");
    }







}