package com.dneero.startup.dbversion;

import com.dneero.startup.UpgradeDatabaseOneVersion;
import com.dneero.db.Db;
import com.dneero.util.RandomString;
import com.dneero.util.Time;
import org.apache.log4j.Logger;
import com.dneero.db.DbConfig;
import com.dneero.sir.SocialInfluenceRating;

import java.util.Calendar;

/**
 * User: Joe Reger Jr
 * Date: Nov 26, 2006
 * Time: 11:57:46 AM
 */
public class Version62 implements UpgradeDatabaseOneVersion {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void doPreHibernateUpgrade(DbConfig dbConfig){
        logger.debug("doPreHibernateUpgrade() start");


        logger.debug("doPreHibernateUpgrade() finish");
    }

    public void doPostHibernateUpgrade(DbConfig dbConfig){
        logger.debug("doPostHibernateUpgrade() start");

        //-----------------------------------
        //-----------------------------------
        int countdddss = Db.RunSQLUpdate("ALTER TABLE blogger DROP socialinfluencerating", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int counts = Db.RunSQLUpdate("ALTER TABLE blogger DROP socialinfluenceratingranking", dbConfig);
        //-----------------------------------
        //-----------------------------------



        logger.debug("doPostHibernateUpgrade() finish");
    }



}