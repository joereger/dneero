package com.dneero.db.simple;


import com.dneero.db.DbImplementation;
import com.dneero.db.DbConfig;
import com.dneero.systemprops.InstanceProperties;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

public class Db implements DbImplementation {

  public static final String name = "simple";
  public static final int defaultRecordstoreturn=50000;

  private boolean driverHasBeenConfigured = false;
  private int configCount = 0;
  private DbConfig dbConfig;

  public Db(DbConfig dbConfig){
        Logger logger = Logger.getLogger(this.getClass().getName());
        this.dbConfig = dbConfig;
        logger.debug("created Db.java ('simple') with dbConfig="+dbConfig);
  }

  public String getName(){
      return name;
  }

  /**
   * Get a connection object.
   */
  public Connection getConnection(){
      Logger logger = Logger.getLogger(this.getClass().getName());
        try{
            if (!driverHasBeenConfigured || InstanceProperties.getHaveNewConfigToTest()){
                System.out.println("dNeero: simple driverHasBeenConfigured is false or we have a new dbconfig to test.");
                System.out.println("dNeero: dbConfig.getDriver()="+dbConfig.getDriver());
                Class.forName(dbConfig.getDriver()).newInstance();
                driverHasBeenConfigured = true;
                configCount++;
            }
            try{
                //Return the connection
                //System.out.println("dNeero: about to call DriverManager.getConnection() with dbConfig="+dbConfig);
                return DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUsername(), dbConfig.getPassword());
            } catch (Exception e){
                System.out.println("dNeero: Error getting connection from the dataSource ds.");
                e.printStackTrace();
            }
        } catch (Exception e){
            System.out.println("dNeero: Error setting up.");
            e.printStackTrace();
        }
        System.out.println("dNeero: Returning null connection.");
        return null;

  }

  public void shutdownDriver() {

    }

  public String printDriverStats() throws Exception {
        StringBuffer mb = new StringBuffer();
        mb.append("<br><br>Got nothin' for ya.");
        return mb.toString();

    }

  /*
  * Run SQL, return a String Array
  */
  public String[][] RunSQL(String sql, int recordstoreturn) {
    Logger logger = Logger.getLogger("com.dneero.db.simple.Db");
    //reger.core.Util.logtodb("SQL: " + sql);
    //System.out.println("Reger.com: RunSql called: " + sql);

    Connection conn=null;
    Statement stmt=null;
    ResultSet rs=null;
    String[][] results=null;
    try{


            conn = getConnection();
            if(conn != null){
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);

                //Get metadata
                Vector rows = new Vector();
                int columnCount = rs.getMetaData().getColumnCount();
                int recCount=0;
                boolean tmp = rs.next();
                if (recordstoreturn<0){
                    recordstoreturn=defaultRecordstoreturn;
                }
                while(tmp && recCount < recordstoreturn){
                    String[] row = new String[columnCount];
                    for(int c=0; c<columnCount; c++){
                        row[c] = rs.getString(c+1);
                        if(row[c] == null){
                            row[c]="";
                        } else {
                            row[c] = row[c].trim();
                        }
                    }
                    rows.addElement(row);
                    recCount++;
                    tmp = rs.next();
                }

                //Put records into a string array
                results = new String[rows.size()][];
                for(int i=0; i < results.length; i++){
                    results[i] = (String[]) rows.get(i);
                }
            } else {
                logger.error("conn==null. sql=" + sql);
            }


    }catch(Exception e) {
          logger.error("Error in Db code", e);
    } finally {
        try {
            //Close the connections
            if(rs!=null) rs.close();
            if(stmt!=null) stmt.close();
            if(conn!=null) conn.close();
        }catch(Exception e) {
              e.printStackTrace();
        }
    }

    //Return results to caller
    return results;
  }

  /**
  * Overload to allow just sql, with no specification of # records to return
  */
  public String[][] RunSQL(String sql) {
          return RunSQL(sql, defaultRecordstoreturn);
  }



  //Run Update SQL, return the number of rows affected
  public int RunSQLUpdate(String sql){
     Logger logger = Logger.getLogger("com.dneero.db.simple.Db");

      int count=0;
    Connection conn=null;
    Statement stmt=null;
      try{


            conn = getConnection();
            if(conn != null){
                stmt = conn.createStatement();
                count = stmt.executeUpdate(sql);
            } else {
                logger.error("conn==null. sql=" + sql);
            }


    }catch(Exception e) {
      logger.error("Error in Db code", e);
      return 0;
    } finally {
        try {
            //Close the connections
            if(stmt!=null) stmt.close();
            if(conn!=null) conn.close();
        }catch(Exception e) {
              e.printStackTrace();
        }
    }
    //Return the count to the caller
      return count;
  }


  public int RunSQLInsert(String sql){
    return RunSQLInsert(sql, true);
  }



  //Run Insert SQL, return the unique autonumber of the row inserted
  public int RunSQLInsert(String sql, boolean loggingison){
      Logger logger = Logger.getLogger("com.dneero.db.simple.Db");
      int count=0;
    int myidentity=0;
    Connection conn=null;
    Statement stmt=null;
    ResultSet tmpRst=null;
      try{


            conn = getConnection();
            if(conn != null)  {
                stmt = conn.createStatement();
                count = stmt.executeUpdate(sql);
                tmpRst = stmt.executeQuery("SELECT LAST_INSERT_ID()");
                if(tmpRst.next()) {
                    myidentity = tmpRst.getInt(1);
                }
                if(tmpRst!=null) tmpRst.close();
            } else {
                if (loggingison){logger.error("conn==null. sql=" + sql);}
            }


    }catch(Exception e) {
      if (loggingison){logger.error("Error in Db code", e);}
      return 0;
    } finally {
        try {
            //Close the connections
            if(tmpRst!=null) tmpRst.close();
            if(stmt!=null) stmt.close();
            if(conn!=null) conn.close();
        }catch(Exception e) {
              e.printStackTrace();
        }
    }
      //Return the identity to the caller
    return myidentity;

  }


}