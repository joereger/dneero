package com.dneero.startup.dbversion;

import com.dneero.db.Db;
import com.dneero.db.DbConfig;
import com.dneero.db.DbFactory;
import com.dneero.startup.UpgradeDatabaseOneVersion;
import com.dneero.util.Str;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Nov 26, 2006
 * Time: 11:57:46 AM
 */
public class Version74 implements UpgradeDatabaseOneVersion {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void doPreHibernateUpgrade(DbConfig dbConfig){
        logger.debug("doPreHibernateUpgrade() start");


        logger.debug("doPreHibernateUpgrade() finish");
    }

    public void doPostHibernateUpgrade(DbConfig dbConfig){
        logger.debug("doPostHibernateUpgrade() start");


        //-----------------------------------
        //-----------------------------------
        int count1 = Db.RunSQLUpdate("UPDATE pl SET isanybodyallowedtocreatesurveys=true", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int count2 = Db.RunSQLUpdate("UPDATE pl SET isanybodyallowedtocreatetwitasks=true", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int count3 = Db.RunSQLUpdate("UPDATE pl SET isbloggerdemographicrequired=true", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int count4 = Db.RunSQLUpdate("UPDATE pl SET isemailactivationrequired=true", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int count5 = Db.RunSQLUpdate("UPDATE pl SET isemailon=true", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int count6 = Db.RunSQLUpdate("UPDATE pl SET ispaidallowed=true", dbConfig);
        //-----------------------------------
        //-----------------------------------

        logger.debug("doPostHibernateUpgrade() finish");
    }

    private boolean nicknameAlreadyExists(String nickname, int userid){
        //-----------------------------------
        //-----------------------------------
        String[][] rstSi= Db.RunSQL("SELECT userid from user where nickname='"+Str.cleanForSQL(nickname.trim().toLowerCase())+"' and userid<>'"+userid+"'", 100000, DbFactory.getDefaultDbConfig());
        //-----------------------------------
        //-----------------------------------
        if (rstSi!=null && rstSi.length>0){
            //logger.debug("userid="+userid+" nicknameAlreadyExists("+nickname+") returning TRUE");
            return true;
        }
        //logger.debug("userid="+userid+" nicknameAlreadyExists("+nickname+") returning FALSE");
        return false;
    }



}