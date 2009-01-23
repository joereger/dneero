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
public class Version61 implements UpgradeDatabaseOneVersion {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void doPreHibernateUpgrade(DbConfig dbConfig){
        logger.debug("doPreHibernateUpgrade() start");


        logger.debug("doPreHibernateUpgrade() finish");
    }

    public void doPostHibernateUpgrade(DbConfig dbConfig){
        logger.debug("doPostHibernateUpgrade() start");

        //-----------------------------------
        //-----------------------------------
        int count01 = Db.RunSQLUpdate("UPDATE user SET sirpoints='0.0'", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int count02 = Db.RunSQLUpdate("UPDATE user SET sirrank='0'", dbConfig);
        //-----------------------------------
        //-----------------------------------

        Calendar cal = Time.xYearsAgoStart(Calendar.getInstance(), 5);
        //-----------------------------------
        //-----------------------------------
        int count03 = Db.RunSQLUpdate("UPDATE user SET sirdate='"+ Time.dateformatfordb(cal)+"'", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int count04 = Db.RunSQLUpdate("UPDATE user SET sirdebug=''", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int count05 = Db.RunSQLUpdate("UPDATE user SET siralgorithm='"+ SocialInfluenceRating.ALGORITHM +"'", dbConfig);
        //-----------------------------------
        //-----------------------------------

        logger.debug("doPostHibernateUpgrade() finish");
    }



}