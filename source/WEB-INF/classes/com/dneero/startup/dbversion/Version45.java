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

/**
 * User: Joe Reger Jr
 * Date: Nov 26, 2006
 * Time: 11:57:46 AM
 */
public class Version45 implements UpgradeDatabaseOneVersion {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void doPreHibernateUpgrade(){
        logger.debug("doPreHibernateUpgrade() start");


        logger.debug("doPreHibernateUpgrade() finish");
    }

    public void doPostHibernateUpgrade(){
        logger.debug("doPostHibernateUpgrade() start");

//        List<Survey> surveys = HibernateUtil.getSession().createCriteria(Survey.class)
//                               .list();
//        for (Iterator<Survey> iterator = surveys.iterator(); iterator.hasNext();) {
//            Survey survey = iterator.next();
//            logger.error("  ");
//            logger.error("  ");
//            logger.error("Start Processing surveyid="+survey.getSurveyid());
//            //Create the survey incentive
//            Surveyincentive si = new Surveyincentive();
//            si.setSurveyid(survey.getSurveyid());
//            si.setType(IncentiveCash.ID);
//            try{si.save();} catch(Exception ex){logger.error("",ex);}
//            //Create the option
//            Surveyincentiveoption sio = new Surveyincentiveoption();
//            sio.setSurveyincentiveid(si.getSurveyincentiveid());
//            sio.setName(IncentiveCash.WILLINGTOPAYPERRESPONSE);
//            sio.setValue(String.valueOf(survey.getWillingtopayperrespondent()));
//            try{sio.save();} catch(Exception ex){logger.error("",ex);}
//            try{si.refresh();} catch(Exception ex){logger.error("",ex);}
//            //Refresh the survey
//            try{survey.refresh();}catch(Exception ex){logger.error("",ex);}
//            logger.error("End Processing surveyid="+survey.getSurveyid());
//            logger.error("  ");
//            logger.error("  ");
//        }

        //-----------------------------------
        //-----------------------------------
        String[][] rstSrv= Db.RunSQL("SELECT surveyid, willingtopayperrespondent FROM survey");
        //-----------------------------------
        //-----------------------------------
        if (rstSrv!=null && rstSrv.length>0){
            for(int i=0; i<rstSrv.length; i++){
                logger.error("  ");
                logger.error("  ");
                logger.error("Start Processing surveyid="+rstSrv[i][0]);

                //-----------------------------------
                //-----------------------------------
                int identity = Db.RunSQLInsert("INSERT INTO surveyincentive(surveyid, type) VALUES('"+rstSrv[i][0]+"', '"+IncentiveCash.ID+"')");
                //-----------------------------------
                //-----------------------------------

                //-----------------------------------
                //-----------------------------------
                int identtty = Db.RunSQLInsert("INSERT INTO surveyincentiveoption(surveyincentiveid, name, value) VALUES('"+identity+"', '"+IncentiveCash.WILLINGTOPAYPERRESPONSE+"', '"+rstSrv[i][1]+"')");
                //-----------------------------------
                //-----------------------------------

                logger.error("End Processing surveyid="+rstSrv[i][0]);
                logger.error("  ");
                logger.error("  ");
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