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
public class Version72 implements UpgradeDatabaseOneVersion {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void doPreHibernateUpgrade(DbConfig dbConfig){
        logger.debug("doPreHibernateUpgrade() start");


        logger.debug("doPreHibernateUpgrade() finish");
    }

    public void doPostHibernateUpgrade(DbConfig dbConfig){
        logger.debug("doPostHibernateUpgrade() start");



        //-----------------------------------
        //-----------------------------------
        String[][] rstSi= Db.RunSQL("SELECT userid, firstname, lastname, nickname from user order by userid asc", 100000, DbFactory.getDefaultDbConfig());
        //-----------------------------------
        //-----------------------------------
        if (rstSi!=null && rstSi.length>0){
            for(int i=0; i<rstSi.length; i++){
                try{
                    int userid = Integer.parseInt(rstSi[i][0]);
                    String firstname = rstSi[i][1];
                    String lastname = rstSi[i][2];
                    String nickname = rstSi[i][3];

                    logger.debug("userid="+userid+" firstname="+firstname+" lastname="+lastname+" nickname="+nickname);

                    String name = firstname + " " + lastname;

                    if (nickname.equals("")){
                        nickname = Str.onlyKeepLettersAndDigits(name).trim();
                        if (nicknameAlreadyExists(nickname, userid)){
                            //Append a number to the end and test
                            int appendtoend = 1;
                            while(nicknameAlreadyExists(nickname+appendtoend, userid) && appendtoend<25){
                                appendtoend++;
                            }
                            //Must set to the nickname that finally worked
                            nickname = nickname+appendtoend;
                        }
                    }
                    nickname = nickname.toLowerCase();

                    //logger.debug("userid="+userid+" name="+name+" nickname="+nickname);

                    //-----------------------------------
                    //-----------------------------------
                    int countddd = Db.RunSQLUpdate("UPDATE user SET name='"+ Str.cleanForSQL(name) +"', nickname='"+Str.cleanForSQL(nickname)+"' where userid='"+userid+"'", dbConfig);
                    //-----------------------------------
                    //-----------------------------------
                } catch (Exception ex){
                    logger.error("", ex);
                }
            }
        }




        //-----------------------------------
        //-----------------------------------
        int countddds = Db.RunSQLUpdate("ALTER TABLE user DROP firstname", dbConfig);
        //-----------------------------------
        //-----------------------------------

        //-----------------------------------
        //-----------------------------------
        int countdddo = Db.RunSQLUpdate("ALTER TABLE user DROP lastname", dbConfig);
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