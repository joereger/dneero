package com.dneero.startup.dbversion;

import com.dneero.startup.UpgradeDatabaseOneVersion;
import com.dneero.db.Db;
import com.dneero.money.SurveyMoneyStatus;
import com.dneero.util.Str;
import com.dneero.util.RandomString;
import org.apache.log4j.Logger;
import com.dneero.db.DbConfig;
/**
 * User: Joe Reger Jr
 * Date: Nov 26, 2006
 * Time: 11:57:46 AM
 */
public class Version36 implements UpgradeDatabaseOneVersion {

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
        int countdds = Db.RunSQLUpdate("UPDATE survey SET resellercode=''", dbConfig);
        //-----------------------------------
        //-----------------------------------


        
        //-----------------------------------
        //-----------------------------------
        String[][] rstUser= Db.RunSQL("SELECT userid FROM user", dbConfig);
        //-----------------------------------
        //-----------------------------------
        if (rstUser!=null && rstUser.length>0){
            for(int i=0; i<rstUser.length; i++){
                //-----------------------------------
                //-----------------------------------
                int countddsd = Db.RunSQLUpdate("UPDATE user SET resellercode='"+ RandomString.randomAlphanumericAllUpperCaseNoOsOrZeros(7) +"' where userid='"+rstUser[i][0]+"'", dbConfig);
                //-----------------------------------
                //-----------------------------------
            }
        }

        
       //-----------------------------------
        //-----------------------------------
        int countdddsd = Db.RunSQLUpdate("UPDATE user SET resellerpercent='0.0'", dbConfig);
        //-----------------------------------
        //-----------------------------------


        logger.debug("doPostHibernateUpgrade() finish");
    }



}
