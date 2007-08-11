package com.dneero.startup.dbversion;

import com.dneero.startup.UpgradeDatabaseOneVersion;
import com.dneero.db.Db;
import com.dneero.dao.Response;
import com.dneero.util.Num;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Nov 26, 2006
 * Time: 11:57:46 AM
 */
public class Version23 implements UpgradeDatabaseOneVersion {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void doPreHibernateUpgrade(){
        logger.debug("doPreHibernateUpgrade() start");
        logger.debug("Not really doing anything.");
        logger.debug("doPreHibernateUpgrade() finish");
    }

    public void doPostHibernateUpgrade(){
        logger.debug("doPostHibernateUpgrade() start");

            //-----------------------------------
            //-----------------------------------
            int countdssa = Db.RunSQLUpdate("UPDATE response SET poststatus='"+ Response.POSTATUS_POSTED +"'");
            //-----------------------------------
            //-----------------------------------

            //-----------------------------------
            //-----------------------------------
            int countdsa = Db.RunSQLUpdate("UPDATE response SET ispaid=true");
            //-----------------------------------
            //-----------------------------------

            //-----------------------------------
            //-----------------------------------
            int countdsdsfa = Db.RunSQLUpdate("UPDATE impression SET responseid='0'");
            //-----------------------------------
            //-----------------------------------

            //-----------------------------------
            //-----------------------------------
            String[][] impressions = Db.RunSQL("SELECT impressionid, surveyid, userid FROM impression");
            //-----------------------------------
            //-----------------------------------
            if (impressions!=null && impressions.length>0){
                for(int i=0; i<impressions.length; i++){
                    if(Num.isinteger(impressions[i][1])){
                        if (Num.isinteger(impressions[i][2])){
                            //We have integer surveyid and userid
                            int impressionid = Integer.parseInt(impressions[i][0]);
                            int surveyid = Integer.parseInt(impressions[i][1]);
                            int userid = Integer.parseInt(impressions[i][2]);
                            //Go get the bloggerid
                            int bloggerid=0;
                            //-----------------------------------
                            //-----------------------------------
                            String[][] bloggers = Db.RunSQL("SELECT bloggerid FROM user WHERE userid='"+userid+"'");
                            //-----------------------------------
                            //-----------------------------------
                            if (bloggers!=null && bloggers.length>0){
                                if (Num.isinteger(bloggers[0][0])){
                                    bloggerid = Integer.parseInt(bloggers[0][0]);
                                }
                            }
                            if (bloggerid>0){
                                //Use the bloggerid and surveyid to find the responseid
                                int responseid=0;
                                //-----------------------------------
                                //-----------------------------------
                                String[][] response= Db.RunSQL("SELECT responseid FROM response WHERE bloggerid='"+bloggerid+"' AND surveyid="+surveyid);
                                //-----------------------------------
                                //-----------------------------------
                                if (response!=null && response.length>0){
                                    if (Num.isinteger(response[0][0])){
                                        responseid = Integer.parseInt(response[0][0]);
                                    }
                                }
                                if (responseid>0){
                                    //Update the record
                                    //-----------------------------------
                                    //-----------------------------------
                                    int count = Db.RunSQLUpdate("UPDATE impression SET responseid='"+responseid+"' WHERE impressionid='"+impressionid+"'");
                                    //-----------------------------------
                                    //-----------------------------------
                                }
                            }
                        }
                    }
                }
            }


        logger.debug("doPostHibernateUpgrade() finish");
    }


    //Sample sql statements

    //-----------------------------------
    //-----------------------------------
    //int count = Db.RunSQLUpdate("CREATE TABLE `pltemplate` (`pltemplateid` int(11) NOT NULL auto_increment, logid int(11), plid int(11), type int(11), templateid int(11), PRIMARY KEY  (`pltemplateid`)) ENGINE=MyISAM DEFAULT CHARSET=latin1;");
    //-----------------------------------
    //-----------------------------------

    //-----------------------------------
    //-----------------------------------
    //int count = Db.RunSQLUpdate("ALTER TABLE megachart CHANGE daterangesavedsearchid daterangesavedsearchid int(11) NOT NULL default '0'");
    //-----------------------------------
    //-----------------------------------

    //-----------------------------------
    //-----------------------------------
    //int count = Db.RunSQLUpdate("ALTER TABLE account DROP gps");
    //-----------------------------------
    //-----------------------------------

    //-----------------------------------
    //-----------------------------------
    //int count = Db.RunSQLUpdate("ALTER TABLE megalogtype ADD isprivate int(11) NOT NULL default '0'");
    //-----------------------------------
    //-----------------------------------

    //-----------------------------------
    //-----------------------------------
    //int count = Db.RunSQLUpdate("DROP TABLE megafielduser");
    //-----------------------------------
    //-----------------------------------

    //-----------------------------------
    //-----------------------------------
    //int count = Db.RunSQLUpdate("CREATE INDEX name_of_index ON table (field1, field2)");
    //-----------------------------------
    //-----------------------------------

    //-----------------------------------
    //-----------------------------------
    //int count2 = Db.RunSQLUpdate("UPDATE survey SET embedlink='\u0001'");
    //-----------------------------------
    //-----------------------------------
}
