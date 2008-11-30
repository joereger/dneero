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
import com.dneero.helpers.UserInputSafe;
import com.dneero.mail.MailtypeSimple;

/**
 * User: Joe Reger Jr
 * Date: Nov 26, 2006
 * Time: 11:57:46 AM
 */
public class Version50 implements UpgradeDatabaseOneVersion {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void doPreHibernateUpgrade(DbConfig dbConfig){
        logger.debug("doPreHibernateUpgrade() start");



        logger.debug("doPreHibernateUpgrade() finish");
    }

    public void doPostHibernateUpgrade(DbConfig dbConfig){
        logger.debug("doPostHibernateUpgrade() start");

        //-----------------------------------
        //-----------------------------------
        String[][] rstSi= Db.RunSQL("SELECT supportissueid, userid, datetime, status, subject from supportissue order by supportissueid asc", 100000, DbFactory.getDefaultDbConfig());
        //-----------------------------------
        //-----------------------------------
        if (rstSi!=null && rstSi.length>0){
            for(int i=0; i<rstSi.length; i++){
                int supportissueid = Integer.parseInt(rstSi[i][0]);
                int userid = Integer.parseInt(rstSi[i][1]);
                Calendar datetime = Time.dbstringtocalendar(rstSi[i][2]);
                int status = Integer.parseInt(rstSi[i][3]);
                String subject = rstSi[i][4];


                //-----------------------------------
                //-----------------------------------
                int mailid = Db.RunSQLInsert("INSERT INTO mail(userid, date, isread, isflaggedforcustomercare, subject) VALUES('"+userid+"', '"+Time.dateformatfordb(datetime)+"', true, false, '"+UserInputSafe.clean(subject)+"')", DbFactory.getDefaultDbConfig());
                //-----------------------------------
                //-----------------------------------


                //-----------------------------------
                //-----------------------------------
                String[][] rstSicomm= Db.RunSQL("SELECT supportissuecommid, supportissueid, datetime, isfromdneeroadmin, notes from supportissuecomm where supportissueid='"+supportissueid+"' order by supportissuecommid asc", 100000, DbFactory.getDefaultDbConfig());
                //-----------------------------------
                //-----------------------------------
                if (rstSicomm!=null && rstSicomm.length>0){
                    for(int j=0; j<rstSicomm.length; j++){
                        int supportissuecommid = Integer.parseInt(rstSicomm[j][0]);
                        Calendar datetimeComm = Time.dbstringtocalendar(rstSicomm[j][2]);
                        boolean isfromdneeroadmin = false;
                        if (rstSicomm[j][3].equals("true")){
                            isfromdneeroadmin = true;
                        }
                        String notes = rstSicomm[j][4];

                        //-----------------------------------
                        //-----------------------------------
                        int mailchildid = Db.RunSQLInsert("INSERT INTO mailchild(mailid, mailtypeid, date, isfromcustomercare, var1, var2, var3, var4, var5) VALUES('"+mailid+"', '"+MailtypeSimple.TYPEID+"', '"+Time.dateformatfordb(datetimeComm)+"', "+isfromdneeroadmin+", '"+UserInputSafe.clean(notes)+"', '', '', '', '')", DbFactory.getDefaultDbConfig());
                        //-----------------------------------
                        //-----------------------------------
                         
                    }
                }


            }
        }


        logger.debug("doPostHibernateUpgrade() finish");
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