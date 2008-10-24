package com.dneero.startup;

import com.dneero.db.Db;
import com.dneero.db.DbConfig;
import com.dneero.db.DbFactory;
import com.dneero.systemprops.InstanceProperties;
import com.dneero.util.ErrorDissect;
import com.dneero.util.Num;
import com.dneero.util.Time;
import org.apache.log4j.Logger;

import java.util.Calendar;

/**
 * User: Joe Reger Jr
 * Date: Nov 26, 2006
 * Time: 11:37:28 AM
 */
public class DbVersionCheck {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public static int EXECUTE_PREHIBERNATE = 0;
    public static int EXECUTE_POSTHIBERNATE = 1;

    public void doCheck(int execute_pre_or_post){
        Logger logger = Logger.getLogger(this.getClass().getName());
        //START HA-JDBC CRAP...  IGNORE
        if (InstanceProperties.getIshajdbcon()!=null && InstanceProperties.getIshajdbcon().equals("1")){
            //HA-JDBC is in the hizzle so check all four databases individually
            System.out.println("DNEERO: Checking Db1");
            if (InstanceProperties.getDbIsActive1()!=null && InstanceProperties.getDbIsActive1().equals("1")){
                DbConfig dbConfig = new DbConfig(InstanceProperties.getDbConnectionUrl1(), InstanceProperties.getDbDriverName1(), InstanceProperties.getDbUsername1(), InstanceProperties.getDbPassword1());
                doCheckSingleDbConfig(execute_pre_or_post, dbConfig, "com.dneero.startup.dbversion.");
            }
            System.out.println("DNEERO: Checking Db2");
            if (InstanceProperties.getDbIsActive2()!=null && InstanceProperties.getDbIsActive2().equals("1")){
                DbConfig dbConfig = new DbConfig(InstanceProperties.getDbConnectionUrl2(), InstanceProperties.getDbDriverName2(), InstanceProperties.getDbUsername2(), InstanceProperties.getDbPassword2());
                doCheckSingleDbConfig(execute_pre_or_post, dbConfig, "com.dneero.startup.dbversion.");
            }
            System.out.println("DNEERO: Checking Db3");
            if (InstanceProperties.getDbIsActive3()!=null && InstanceProperties.getDbIsActive3().equals("1")){
                DbConfig dbConfig = new DbConfig(InstanceProperties.getDbConnectionUrl3(), InstanceProperties.getDbDriverName3(), InstanceProperties.getDbUsername3(), InstanceProperties.getDbPassword3());
                doCheckSingleDbConfig(execute_pre_or_post, dbConfig, "com.dneero.startup.dbversion.");
            }
            System.out.println("DNEERO: Checking Db4");
            if (InstanceProperties.getDbIsActive4()!=null && InstanceProperties.getDbIsActive4().equals("1")){
                DbConfig dbConfig = new DbConfig(InstanceProperties.getDbConnectionUrl4(), InstanceProperties.getDbDriverName4(), InstanceProperties.getDbUsername4(), InstanceProperties.getDbPassword4());
                doCheckSingleDbConfig(execute_pre_or_post, dbConfig, "com.dneero.startup.dbversion.");
            }
        //END HA-JDBC CRAP...  IGNORE
        } else {
            //First check the default database using the main version stuff
            doCheckSingleDbConfig(execute_pre_or_post, DbFactory.getDefaultDbConfig(), "com.dneero.startup.dbversion.");
            //Now do the Dbcache database
            doCheckSingleDbConfig(execute_pre_or_post, DbFactory.getDefaultDbConfigForDbcache(), "com.dneero.startup.dbversiondbcache.");
        }
    }

    private void doCheckSingleDbConfig(int execute_pre_or_post, DbConfig dbConfig, String packagename){
            Logger logger = Logger.getLogger(this.getClass().getName());
            if (dbConfig!=null){
                logger.debug("doCheckSingleDbConfig(execute_pre_or_post="+execute_pre_or_post+", dbConfig="+dbConfig.toString()+")");
            } else {
                logger.debug("doCheckSingleDbConfig(execute_pre_or_post="+execute_pre_or_post+", dbConfig=null(will use default from DbFactory)))");
            }
            //Calculate the max version that exists in the code classes
            int maxVer = 0;
            while(true){
                try{
                    //Try to create an object
                    UpgradeDatabaseOneVersion upg = (UpgradeDatabaseOneVersion)(Class.forName(packagename+"Version"+maxVer).newInstance());
                } catch (ClassNotFoundException ex){
                    //If class isn't found, break, but be sure to decrement the maxVer
                    //logger.debug("No class found: Class.forName(\"com.dneero.startup.dbversion.Version"+maxVer+")");
                    maxVer = maxVer - 1;
                    break;
                } catch (Throwable e){
                    //Also decrement the maxVer if anything else happens
                    maxVer = maxVer - 1;
                    logger.error("", e);
                    break;
                }
                maxVer = maxVer + 1;
            }
            RequiredDatabaseVersion.setMaxversion(maxVer);

            //Make sure we have the database table to support the database version status
            if (!checkThatDatabaseVersionTableExists(dbConfig)){
                createDbVersionTable(dbConfig);
            }

            //Get the highest version stored in the database version table
            int currentDatabaseVersion = getMaxVersionFromDatabase(dbConfig);

            //Boolean to keep working
            boolean keepWorking = true;


            //Compare to the current required database version
            while (currentDatabaseVersion<=RequiredDatabaseVersion.getMaxversion() && keepWorking) {
                try{
                    //Do the upgrade
                    UpgradeDatabaseOneVersion upg = (UpgradeDatabaseOneVersion)(Class.forName(packagename+"Version"+ (currentDatabaseVersion+1)).newInstance());
                    System.out.println("Start upgrade database to version " + (currentDatabaseVersion+1));
                    if (execute_pre_or_post==EXECUTE_PREHIBERNATE){
                        upg.doPreHibernateUpgrade(dbConfig);
                        //updateDatabase((currentDatabaseVersion+1));
                    } else {
                        upg.doPostHibernateUpgrade(dbConfig);
                        //Note that I only update the database version in the post-hibernate run
                        updateDatabase((currentDatabaseVersion+1), dbConfig);
                    }
                    System.out.println("End upgrade database to version " + (currentDatabaseVersion+1));
                } catch (ClassNotFoundException ex){
                    //Class isn't found, report it and exit
                    //logger.error("",ex);
                    //ex.printStackTrace();
                    keepWorking = false;
                    //RequiredDatabaseVersion.setError(RequiredDatabaseVersion.getError() + ErrorDissect.dissect(ex));
                    //logger.debug("Error: Upgrade database to version " + (currentDatabaseVersion+1) + ".  Class not found.");
                } catch (Exception e){
                    //Some other sort of error
                    System.out.println("dNeero UpgradeCheckAtStartup.java: Error upgrading Db:" + e.getMessage());
                    e.printStackTrace();
                    logger.error("", e);
                    keepWorking = false;
                    RequiredDatabaseVersion.setError(RequiredDatabaseVersion.getError() + ErrorDissect.dissect(e));
                    logger.debug("Error: Upgrade database to version " + (currentDatabaseVersion+1) + " had issues recorded.");
                }
                //Increment and see if we can upgrade to this version
                currentDatabaseVersion = currentDatabaseVersion + 1;
            }

            if (execute_pre_or_post==EXECUTE_POSTHIBERNATE){
                if ((currentDatabaseVersion-1)==RequiredDatabaseVersion.getMaxversion()){
                    //Successful upgrade through all versions
                    RequiredDatabaseVersion.setHavecorrectversion(true);
                    System.out.println("The correct database version is installed.  Version: " + (currentDatabaseVersion-1));
                }
            }

        }




        private static void updateDatabase(int version, DbConfig dbConfig){
            try{
                //Update the database version
                //-----------------------------------
                //-----------------------------------
                int identity = Db.RunSQLInsert("INSERT INTO databaseversion(version, date) VALUES('"+version+"', '"+ Time.dateformatfordb(Calendar.getInstance())+"')", dbConfig);
                //-----------------------------------
                //-----------------------------------
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        private static int getMaxVersionFromDatabase(DbConfig dbConfig){
            try{
                //Go to the database and see what we've got.
                //-----------------------------------
                //-----------------------------------
                String[][] rstVersion= Db.RunSQL("SELECT max(version) FROM databaseversion", dbConfig);
                //-----------------------------------
                //-----------------------------------
                if (rstVersion!=null && rstVersion.length>0){
                    if (Num.isinteger(rstVersion[0][0])){
                        RequiredDatabaseVersion.setCurrentversion(Integer.parseInt(rstVersion[0][0]));
                        return Integer.parseInt(rstVersion[0][0]);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                RequiredDatabaseVersion.setError(RequiredDatabaseVersion.getError() + ErrorDissect.dissect(e));
            }
            return -1;
        }

        private static boolean checkThatDatabaseVersionTableExists(DbConfig dbConfig){
            try{
                //-----------------------------------
                //-----------------------------------
                String[][] rstT = Db.RunSQL("SELECT COUNT(*) FROM databaseversion", dbConfig);
                //-----------------------------------
                //-----------------------------------
                if (rstT!=null && rstT.length>0){
                    return true;
                }
                return false;
            } catch (Exception e){
                return false;
            }
        }

        private static void createDbVersionTable(DbConfig dbConfig){
            try{
                //-----------------------------------
                //-----------------------------------
                int count = Db.RunSQLUpdate("CREATE TABLE `databaseversion` (`databaseversionid` int(11) NOT NULL auto_increment, `version` int(11) NOT NULL default '0', `date` datetime default null, PRIMARY KEY  (`databaseversionid`)) ENGINE=MyISAM DEFAULT CHARSET=latin1;", dbConfig);
                //-----------------------------------
                //-----------------------------------
            } catch (Exception e){
                e.printStackTrace();
            }
        }



}
