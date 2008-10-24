package com.dneero.startup.dbversion;

import com.dneero.startup.UpgradeDatabaseOneVersion;
import com.dneero.db.Db;
import com.dneero.util.Str;
import org.apache.log4j.Logger;
import com.dneero.db.DbConfig;
/**
 * User: Joe Reger Jr
 * Date: Nov 26, 2006
 * Time: 11:57:46 AM
 */
public class Version27 implements UpgradeDatabaseOneVersion {

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
        String[][] rstUser= Db.RunSQL("SELECT questionresponseid, value FROM questionresponse", dbConfig);
        //-----------------------------------
        //-----------------------------------
        if (rstUser!=null && rstUser.length>0){
            for(int i=0; i<rstUser.length; i++){
                //-----------------------------------
                //-----------------------------------
                int count = Db.RunSQLUpdate("UPDATE questionresponse SET value='"+ Str.cleanForSQL(rstUser[i][1].trim()) +"' WHERE questionresponseid='"+rstUser[i][0]+"'", dbConfig);
                //-----------------------------------
                //-----------------------------------
            }
        }

        logger.debug("doPostHibernateUpgrade() finish");
    }



}
