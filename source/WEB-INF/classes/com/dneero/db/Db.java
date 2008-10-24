package com.dneero.db;

import org.apache.log4j.Logger;

import java.sql.*;

import com.dneero.systemprops.InstanceProperties;


public class Db {

  Logger logger = Logger.getLogger(this.getClass().getName());
  private static boolean haveValidConfig = false;
  private static String typeofdriver= "simple";

  public static Connection getConnection(){
        return getConnection(null);
  }
  public static Connection getConnection(DbConfig dbConfig){
        return DbFactory.get(typeofdriver, dbConfig).getConnection();
  }

  public static boolean testConfig(){
    return testConfig(null);
  }
  public static boolean testConfig(DbConfig dbConfig){
       Logger logger = Logger.getLogger(Db.class);
       logger.debug("testConfig() called");
       //if (!haveValidConfig && InstanceProperties.getHaveNewConfigToTest()){
           //logger.debug("!haveValidConfig && InstanceProperties.getHaveNewConfigToTest()");
            //-----------------------------------
            //-----------------------------------
            String[][] rstTest= DbFactory.get(typeofdriver, dbConfig).RunSQL("SELECT 1");
            //-----------------------------------
            //-----------------------------------
            if (rstTest!=null && rstTest.length>0){
                haveValidConfig = true;
                logger.debug("haveValidConfig = true");
            } else {
                haveValidConfig = false;
                logger.debug("haveValidConfig = false");   
            }
        //}
        InstanceProperties.setHaveNewConfigToTest(false);
        logger.debug("testConfig() returning haveValidConfig = "+haveValidConfig);
        return haveValidConfig;
    }
  
  /*
  * Run SQL, return a String Array
  */
  public static String[][] RunSQL(String sql, int recordstoreturn) {
    return RunSQL(sql, recordstoreturn, null);
  }
  public static String[][] RunSQL(String sql, int recordstoreturn, DbConfig dbConfig) {
    if (InstanceProperties.getHaveNewConfigToTest()){
        testConfig(dbConfig);
    }
    if (getHaveValidConfig()){
        return DbFactory.get(typeofdriver, dbConfig).RunSQL(sql, recordstoreturn);
    } else {
        return new String[0][];
    }
  }
  
  public static String[][] RunSQL(String sql) {
    return RunSQL(sql, null);
  }
  public static String[][] RunSQL(String sql, DbConfig dbConfig) {
  		return RunSQL(sql, 50000, dbConfig);
  }

  public static int RunSQLUpdate(String sql){
      return RunSQLUpdate(sql, null);
  }
  public static int RunSQLUpdate(String sql, DbConfig dbConfig){
    if (InstanceProperties.getHaveNewConfigToTest()){
        testConfig(dbConfig);
    }
    if (getHaveValidConfig()){
       return DbFactory.get(typeofdriver, dbConfig).RunSQLUpdate(sql);
    } else {
        return 0;
    }
  }


  public static int RunSQLInsert(String sql){
    return RunSQLInsert(sql, null);
  }
  public static int RunSQLInsert(String sql, DbConfig dbConfig){
    return RunSQLInsert(sql, true, dbConfig);
  }

  public static int RunSQLInsert(String sql, boolean isloggingon){
      return RunSQLInsert(sql, isloggingon, null);
  }
  public static int RunSQLInsert(String sql, boolean isloggingon, DbConfig dbConfig){
    if (InstanceProperties.getHaveNewConfigToTest()){
        testConfig(dbConfig);
    }
    if (getHaveValidConfig()){
       return DbFactory.get(typeofdriver, dbConfig).RunSQLInsert(sql, isloggingon);
    } else {
        return 0;
    }
  }

  public static String printDriverStats() throws Exception {
    return printDriverStats(null);
  }
  public static String printDriverStats(DbConfig dbConfig) throws Exception {
        return DbFactory.get(typeofdriver, dbConfig).printDriverStats();
  }

  public static void recordToSqlDebugCache(String sql){
      Logger logger = Logger.getLogger("com.dneero.db.Db");
      logger.debug(sql);
  }


    public static boolean getHaveValidConfig() {
        return haveValidConfig;
    }
}