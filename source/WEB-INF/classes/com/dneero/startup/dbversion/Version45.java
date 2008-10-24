package com.dneero.startup.dbversion;

import com.dneero.db.Db;
import com.dneero.incentive.IncentiveCash;
import com.dneero.startup.UpgradeDatabaseOneVersion;
import org.apache.log4j.Logger;

import java.util.HashMap;
import com.dneero.db.DbConfig;
/**
 * User: Joe Reger Jr
 * Date: Nov 26, 2006
 * Time: 11:57:46 AM
 */
public class Version45 implements UpgradeDatabaseOneVersion {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void doPreHibernateUpgrade(DbConfig dbConfig){
        logger.debug("doPreHibernateUpgrade() start");


        logger.debug("doPreHibernateUpgrade() finish");
    }

    public void doPostHibernateUpgrade(DbConfig dbConfig){
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

        HashMap<Integer, Integer> surveyToIncentives = new HashMap<Integer, Integer>();

        //-----------------------------------
        //-----------------------------------
        String[][] rstSrv= Db.RunSQL("SELECT surveyid, willingtopayperrespondent FROM survey", dbConfig);
        //-----------------------------------
        //-----------------------------------
        if (rstSrv!=null && rstSrv.length>0){
            for(int i=0; i<rstSrv.length; i++){
                logger.error("  ");
                logger.error("  ");
                logger.error("Start Processing surveyid="+rstSrv[i][0]);

                //-----------------------------------
                //-----------------------------------
                int identity = Db.RunSQLInsert("INSERT INTO surveyincentive(surveyid, type) VALUES('"+rstSrv[i][0]+"', '"+IncentiveCash.ID+"')", dbConfig);
                //-----------------------------------
                //-----------------------------------

                //Storing (surveyid, surveyincentiveid)
                System.out.println("surveyToIncentives.put("+rstSrv[i][0]+", "+identity+")");
                surveyToIncentives.put(Integer.parseInt(rstSrv[i][0]), identity);

                //-----------------------------------
                //-----------------------------------
                int identtty = Db.RunSQLInsert("INSERT INTO surveyincentiveoption(surveyincentiveid, name, value) VALUES('"+identity+"', '"+IncentiveCash.WILLINGTOPAYPERRESPONSE+"', '"+rstSrv[i][1]+"')", dbConfig);
                //-----------------------------------
                //-----------------------------------

                logger.error("End Processing surveyid="+rstSrv[i][0]);
                logger.error("  ");
                logger.error("  ");
            }
        }

        //-----------------------------------
        //-----------------------------------
        String[][] rstResp= Db.RunSQL("SELECT responseid, surveyid FROM response ORDER BY responseid asc", dbConfig);
        //-----------------------------------
        //-----------------------------------
        if (rstResp!=null && rstResp.length>0){
            for(int i=0; i<rstResp.length; i++){
                logger.error("Start processing responseid="+rstResp[i][0]);
                int responseid = Integer.parseInt(rstResp[i][0]);
                int surveyid = Integer.parseInt(rstResp[i][1]);
                int surveyincentiveid = 0;
                if (surveyToIncentives.containsKey(surveyid)){
                    surveyincentiveid = surveyToIncentives.get(surveyid);
                } else {
                    logger.error("No surveyincentiveid found in surveyToIncentives for surveyid="+surveyid);
                }

                //-----------------------------------
                //-----------------------------------
                int count = Db.RunSQLUpdate("UPDATE response SET surveyincentiveid='"+surveyincentiveid+"' WHERE responseid='"+responseid+"'", dbConfig);
                //-----------------------------------
                //-----------------------------------

                logger.error("End processing responseid="+rstResp[i][0]);
            }
        }





        logger.debug("doPostHibernateUpgrade() finish");
    }


   
}