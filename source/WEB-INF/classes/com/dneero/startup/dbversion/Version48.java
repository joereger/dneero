package com.dneero.startup.dbversion;

import com.dneero.dao.Survey;
import com.dneero.dao.Surveyincentive;
import com.dneero.dao.Surveyincentiveoption;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.db.Db;
import com.dneero.incentive.IncentiveCash;
import com.dneero.startup.UpgradeDatabaseOneVersion;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.Calendar;

import com.dneero.db.DbConfig;
import com.dneero.db.DbFactory;
import com.dneero.util.Num;
import com.dneero.util.Time;
import com.dneero.util.DateDiff;
import com.dneero.survey.servlet.ImpressionsByDayUtil;

/**
 * User: Joe Reger Jr
 * Date: Nov 26, 2006
 * Time: 11:57:46 AM
 */
public class Version48 implements UpgradeDatabaseOneVersion {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void doPreHibernateUpgrade(DbConfig dbConfig){
        logger.debug("doPreHibernateUpgrade() start");



        logger.debug("doPreHibernateUpgrade() finish");
    }

    public void doPostHibernateUpgrade(DbConfig dbConfig){
        logger.debug("doPostHibernateUpgrade() start");

        //NOTE THAT I'M CONNECTING TO TWO DBs HERE!!!!
        //I'M PASSING DIFFERENT dbConfig's TO Db.RunSQL()

        //Only want to run this once
        int responseswithmigrateddata = 0;
        //-----------------------------------
        //-----------------------------------
        String[][] rstResponseMigrated= Db.RunSQL("SELECT count(*) from response where isdatamigrated=true", 100000, DbFactory.getDefaultDbConfig());
        //-----------------------------------
        //-----------------------------------
        if (rstResponseMigrated!=null && rstResponseMigrated.length>0){
            for(int i=0; i<rstResponseMigrated.length; i++){
                if (Num.isinteger(rstResponseMigrated[i][0])){
                    responseswithmigrateddata = responseswithmigrateddata + Integer.parseInt(rstResponseMigrated[i][0]);
                }
            }
        }
        if (responseswithmigrateddata==0){
            logger.error("Setting Response.isdatamigrated=false for all records");
            //-----------------------------------
            //-----------------------------------
            int count2 = Db.RunSQLUpdate("UPDATE response SET isdatamigrated=false", DbFactory.getDefaultDbConfig());
            //-----------------------------------
            //-----------------------------------
        }

        //Keep processing until there are no unprocessed records
        int totalresponses = -1;
        while(totalresponses!=0){
            totalresponses = 0;
            //-----------------------------------
            //-----------------------------------
            String[][] rstResponseCount= Db.RunSQL("SELECT count(*) from response where isdatamigrated=false", 100000, DbFactory.getDefaultDbConfig());
            //-----------------------------------
            //-----------------------------------
            if (rstResponseCount!=null && rstResponseCount.length>0){
                for(int i=0; i<rstResponseCount.length; i++){
                    if (Num.isinteger(rstResponseCount[i][0])){
                        totalresponses = Integer.parseInt(rstResponseCount[i][0]);
                    }
                }
            }
            //Process records
            logger.error("Calling doStuff() because totalresponses="+totalresponses);
            doStuff();
        }


        logger.debug("doPostHibernateUpgrade() finish");
    }

    private void doStuff(){
        try{
            Calendar startTime = Calendar.getInstance();

            int totalresponses = 0;
            //-----------------------------------
            //-----------------------------------
            String[][] rstResponseCount= Db.RunSQL("SELECT count(*) from response where isdatamigrated=false", 100000, DbFactory.getDefaultDbConfig());
            //-----------------------------------
            //-----------------------------------
            if (rstResponseCount!=null && rstResponseCount.length>0){
                for(int i=0; i<rstResponseCount.length; i++){
                    if (Num.isinteger(rstResponseCount[i][0])){
                        totalresponses = totalresponses + Integer.parseInt(rstResponseCount[i][0]);
                    }
                }
            }

            int responsesprocessedalready = 0;
            //-----------------------------------
            //-----------------------------------
            String[][] rstResponses= Db.RunSQL("SELECT responseid from response where isdatamigrated=false order by responseid asc", 100000, DbFactory.getDefaultDbConfig());
            //-----------------------------------
            //-----------------------------------
            if (rstResponses!=null && rstResponses.length>0){
                for(int i=0; i<rstResponses.length; i++){
                    responsesprocessedalready = responsesprocessedalready + 1;
                    if (Num.isinteger(rstResponses[i][0])){
                        int responseid = Integer.parseInt(rstResponses[i][0]);
                        int logeveryX = 1000;
                        if (responseid % logeveryX == 0){
                            logger.error("Processing responseid="+responseid);
                        }
                        int impressionstotal = 0;
                        int impressionstobepaid = 0;
                        int impressionspaid = 0;
                        ImpressionsByDayUtil ibdu = new ImpressionsByDayUtil("");
                        //-----------------------------------
                        //-----------------------------------
                        String[][] rstImp= Db.RunSQL("SELECT SUM(impressionstotal), SUM(impressionstobepaid), SUM(impressionspaid) from impression where responseid='"+responseid+"'", 100000, DbFactory.getDefaultDbConfigForImpressions());
                        //-----------------------------------
                        //-----------------------------------
                        if (rstImp!=null && rstImp.length>0){
                            for(int j=0; j<rstImp.length; j++){
                                if (Num.isinteger(rstImp[j][0])){
                                    impressionstotal = impressionstotal + Integer.parseInt(rstImp[j][0]);
                                }
                                if (Num.isinteger(rstImp[j][1])){
                                    impressionstobepaid = impressionstobepaid + Integer.parseInt(rstImp[j][1]);
                                }
                                if (Num.isinteger(rstImp[j][2])){
                                    impressionspaid = impressionspaid + Integer.parseInt(rstImp[j][2]);
                                }
                            }
                        }

                        //-----------------------------------
                        //-----------------------------------
                        String[][] rstImpByDay= Db.RunSQL("SELECT impressionsbyday from impression where responseid='"+responseid+"'", 100000, DbFactory.getDefaultDbConfigForImpressions());
                        //-----------------------------------
                        //-----------------------------------
                        if (rstImpByDay!=null && rstImpByDay.length>0){
                            for(int j=0; j<rstImpByDay.length; j++){
                                if (rstImpByDay[j][0]!=null){
                                    ImpressionsByDayUtil ibduForThisImpression = new ImpressionsByDayUtil(rstImpByDay[j][0]);
                                    ibdu.add(ibduForThisImpression);
                                }
                            }
                        }

                        if (responseid % logeveryX == 0){
                            logger.error("Saving responseid="+responseid+" impressionstotal="+impressionstotal+" impressionttobepaid="+impressionstobepaid+" impressionspaid="+impressionspaid+" impressionsbyday="+ibdu.getAsString());
                        }

                        //-----------------------------------
                        //-----------------------------------
                        int count2 = Db.RunSQLUpdate("UPDATE response SET impressionstotal='"+impressionstotal+"', impressionstobepaid='"+impressionstobepaid+"', impressionspaid='"+impressionspaid+"', impressionsbyday='"+ibdu.getAsString()+"', isdatamigrated=true where responseid='"+responseid+"'", DbFactory.getDefaultDbConfig());
                        //-----------------------------------
                        //-----------------------------------

                        if (responseid % logeveryX == 0){
                            Calendar estimatedCompletion = estimatedCompletion(startTime, totalresponses, responsesprocessedalready);
                            if (estimatedCompletion!=null){
                                logger.error("estimatedCompletion: "+ Time.dateformatcompactwithtime(estimatedCompletion));
                            }
                        }
                    }

                }
            }
        } catch (Exception ex){
            logger.error("", ex);
        }
    }

    private Calendar estimatedCompletion(Calendar startTime, int totalresponses, int responsesprocessedalready){
        try{
            int secondsprocessing =DateDiff.dateDiff("second", Calendar.getInstance(), startTime);
            logger.error("secondsprocessing="+secondsprocessing);
            double secondsperresponse = (Double.parseDouble(String.valueOf(secondsprocessing)))/(Double.parseDouble(String.valueOf(responsesprocessedalready)));
            logger.error("secondsperresponse="+secondsperresponse);
            int remainingresponsestoprocess = totalresponses - responsesprocessedalready;
            logger.error("remainingresponsestoprocess="+remainingresponsestoprocess);
            double remainingseconds = (Double.parseDouble(String.valueOf(remainingresponsestoprocess))) * secondsperresponse;
            logger.error("remainingseconds="+remainingseconds);
            double remainingminutes = remainingseconds / Double.parseDouble(String.valueOf(60));
            logger.error("remainingminutes="+remainingminutes);
            double remaininghours = remainingminutes / Double.parseDouble(String.valueOf(60));
            logger.error("remaininghours="+remaininghours);
            int remainingminutesInt = new Double(remainingminutes).intValue();
            Calendar estimatedCompletion = Time.xMinutesAgoEnd(Calendar.getInstance(), (-1)*remainingminutesInt);
            return estimatedCompletion;
        } catch (Exception ex){
            logger.error("", ex);
        }
        return null;
    }


    //Sample sql statements

    //-----------------------------------
    //-----------------------------------
    //int count = Db.RunSQLUpdate("CREATE TABLE `pltemplate` (`pltemplateid` int(11) NOT NULL auto_increment, logid int(11), plid int(11), type int(11), templateid int(11), PRIMARY KEY  (`pltemplateid`)) ENGINE=MyISAM DEFAULT CHARSET=latin1;", dbConfig);
    //-----------------------------------
    //-----------------------------------

    //-----------------------------------
    //-----------------------------------
    //int count = Db.RunSQLUpdate("ALTER TABLE megachart CHANGE daterangesavedsearchid daterangesavedsearchid int(11) NOT NULL default '0'", dbConfig);
    //-----------------------------------
    //-----------------------------------

    //-----------------------------------
    //-----------------------------------
    //int count = Db.RunSQLUpdate("ALTER TABLE account DROP gps", dbConfig);
    //-----------------------------------
    //-----------------------------------

    //-----------------------------------
    //-----------------------------------
    //int count = Db.RunSQLUpdate("ALTER TABLE megalogtype ADD isprivate int(11) NOT NULL default '0'", dbConfig);
    //-----------------------------------
    //-----------------------------------

    //-----------------------------------
    //-----------------------------------
    //int count = Db.RunSQLUpdate("DROP TABLE megafielduser", dbConfig);
    //-----------------------------------
    //-----------------------------------

    //-----------------------------------
    //-----------------------------------
    //int count = Db.RunSQLUpdate("CREATE INDEX name_of_index ON table (field1, field2)", dbConfig);
    //-----------------------------------
    //-----------------------------------

    //-----------------------------------
    //-----------------------------------
    //int count2 = Db.RunSQLUpdate("UPDATE survey SET embedlink='\u0001'", dbConfig);
    //-----------------------------------
    //-----------------------------------
}