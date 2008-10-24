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
public class Version44 implements UpgradeDatabaseOneVersion {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void doPreHibernateUpgrade(DbConfig dbConfig){
        logger.debug("doPreHibernateUpgrade() start");


        logger.debug("doPreHibernateUpgrade() finish");
    }

    public void doPostHibernateUpgrade(DbConfig dbConfig){
        logger.debug("doPostHibernateUpgrade() start");


        //-----------------------------------
        //-----------------------------------
        int countdds = Db.RunSQLUpdate("UPDATE response SET isresearcherreviewed=true", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int countddds = Db.RunSQLUpdate("UPDATE response SET issysadminreviewed=true", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int countddsss = Db.RunSQLUpdate("UPDATE response SET isresearcherrejected=false", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int countddddds = Db.RunSQLUpdate("UPDATE response SET issysadminrejected=false", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int countddsadss = Db.RunSQLUpdate("UPDATE question SET isresearcherreviewed=true", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int countddasdasds = Db.RunSQLUpdate("UPDATE question SET issysadminreviewed=true", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int countdsdfsddsss = Db.RunSQLUpdate("UPDATE question SET isresearcherrejected=false", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int countddsdfddds = Db.RunSQLUpdate("UPDATE question SET issysadminrejected=false", dbConfig);
        //-----------------------------------
        //-----------------------------------


        logger.debug("doPostHibernateUpgrade() finish");
    }


    
}