package com.dneero.systemprops;

import com.dneero.util.GeneralException;
import com.dneero.util.Num;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Holds database configuration parameters
 */
public class InstanceProperties {

    private static String ishajdbcon;

    private static String dbDriverName;
    private static String dbConnectionUrl;
    private static String dbUsername;
    private static String dbPassword;

    private static String dbDriverNameDbcache;
    private static String dbConnectionUrlDbcache;
    private static String dbUsernameDbcache;
    private static String dbPasswordDbcache;

    private static String dbDriverNameImpressions;
    private static String dbConnectionUrlImpressions;
    private static String dbUsernameImpressions;
    private static String dbPasswordImpressions;

    private static String dbIsActive1;
    private static String dbDriverName1;
    private static String dbConnectionUrl1;
    private static String dbUsername1;
    private static String dbPassword1;

    private static String dbIsActive2;
    private static String dbDriverName2;
    private static String dbConnectionUrl2;
    private static String dbUsername2;
    private static String dbPassword2;

    private static String dbIsActive3;
    private static String dbDriverName3;
    private static String dbConnectionUrl3;
    private static String dbUsername3;
    private static String dbPassword3;

    private static String dbIsActive4;
    private static String dbDriverName4;
    private static String dbConnectionUrl4;
    private static String dbUsername4;
    private static String dbPassword4;

    private static String dbMaxActive;
    private static String dbMaxIdle;
    private static String dbMinIdle;
    private static String dbMaxWait;

    private static String runScheduledTasksOnThisInstance;
    private static String instancename;
    private static String hibernateShowSql;

    private static String terracottahost01;
    private static String terracottahost02;
    private static String terracottahost03;



    private static String infinispanconfigfilehibernatedbcache;
    private static String infinispanconfigfilehibernateimpressions;
    private static String infinispanconfigfileobjectcache;
    private static String infinispanconfigfilehibernate;
    private static String jgroupstcppinginitialhosts;
    private static String jgroupstcpport;



    private static String passPhrase = "pupper";
    private static boolean haveNewConfigToTest = true;
    private static boolean haveAttemptedToLoadDefaultPropsFile = false;
    private static String dbPropsInternalFilename = WebAppRootDir.getWebAppRootPath() + "conf"+File.separatorChar+"instance.props";
    private static String dbPropsExternalFilename = "dneero-"+WebAppRootDir.getUniqueContextId()+"-dbconfig.txt";

    public static void load(){
        Logger logger = Logger.getLogger(InstanceProperties.class);
        if (!haveAttemptedToLoadDefaultPropsFile){
            try {
                boolean gotFile = false;
                //Look in the webapps directory
                try{
                    File internalFile = new File(dbPropsInternalFilename);
                    if (internalFile!=null && internalFile.exists() && internalFile.canRead() && internalFile.isFile()){
                        Properties properties = new Properties();
                        properties.load(new FileInputStream(internalFile));
                        loadPropsFile(properties);
                        gotFile = true;
                        logger.debug("Got instance props from internal file ("+dbPropsInternalFilename+")");
                    }
                } catch (IOException e) {
                    //e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
                //If we don't have one in the conf directory, look to the system default dir
                if (!gotFile){
                    try{
                        File externalFile = new File("", dbPropsExternalFilename);
                        if (externalFile!=null && externalFile.exists() && externalFile.canRead() && externalFile.isFile()){
                            Properties properties = new Properties();
                            properties.load(new FileInputStream(externalFile));
                            loadPropsFile(properties);
                            gotFile = true;
                            logger.debug("Got instance props from external file (dbPropsExternalFilename)");
                        }
                    } catch (IOException e) {
                        //e.printStackTrace();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadPropsFile(Properties properties){
        try{

            ishajdbcon = properties.getProperty("ishajdbcon", "0");

            dbConnectionUrl = properties.getProperty("dbConnectionUrl", "jdbc:mysql://localhost:3306/dneero?autoReconnect=true");
            dbUsername = properties.getProperty("dbUsername", "username");
            //DesEncrypter encrypter2 = new DesEncrypter(passPhrase);
            //dbPassword = encrypter2.decrypt(properties.getProperty("dbPassword", "password"));
            dbPassword = properties.getProperty("dbPassword", "password");
            dbDriverName = properties.getProperty("dbDriverName", "com.mysql.jdbc.Driver");

            dbConnectionUrlDbcache= properties.getProperty("dbConnectionUrlDbcache", "jdbc:mysql://localhost:3306/dneerodbcache?autoReconnect=true");
            dbUsernameDbcache= properties.getProperty("dbUsernameDbcache", "username");
            //DesEncrypter encrypter2 = new DesEncrypter(passPhrase);
            //dbPasswordDbcache = encrypter2.decrypt(properties.getProperty("dbPasswordDbcache", "password"));
            dbPasswordDbcache= properties.getProperty("dbPasswordDbcache", "password");
            dbDriverNameDbcache= properties.getProperty("dbDriverNameDbcache", "com.mysql.jdbc.Driver");

            dbConnectionUrlImpressions= properties.getProperty("dbConnectionUrlImpressions", "jdbc:mysql://localhost:3306/dneeroimpressions?autoReconnect=true");
            dbUsernameImpressions= properties.getProperty("dbUsernameImpressions", "username");
            //DesEncrypter encrypter2 = new DesEncrypter(passPhrase);
            //dbPasswordImpressions = encrypter2.decrypt(properties.getProperty("dbPasswordImpressions", "password"));
            dbPasswordImpressions= properties.getProperty("dbPasswordImpressions", "password");
            dbDriverNameImpressions= properties.getProperty("dbDriverNameImpressions", "com.mysql.jdbc.Driver");

            dbIsActive1 = properties.getProperty("dbIsActive1", "0");
            dbConnectionUrl1 = properties.getProperty("dbConnectionUrl1", "jdbc:mysql://localhost:3306/dneero?autoReconnect=true");
            dbUsername1 = properties.getProperty("dbUsername1", "username");
            //DesEncrypter encrypter1 = new DesEncrypter(passPhrase);
            //dbPassword1 = encrypter1.decrypt(properties.getProperty("dbPassword1", "password"));
            dbPassword1 = properties.getProperty("dbPassword1", "password");
            dbDriverName1 = properties.getProperty("dbDriverName1", "com.mysql.jdbc.Driver");

            dbIsActive2 = properties.getProperty("dbIsActive2", "0");
            dbConnectionUrl2 = properties.getProperty("dbConnectionUrl2", "jdbc:mysql://localhost:3306/dneero?autoReconnect=true");
            dbUsername2 = properties.getProperty("dbUsername2", "username");
            //DesEncrypter encrypter2 = new DesEncrypter(passPhrase);
            //dbPassword2 = encrypter2.decrypt(properties.getProperty("dbPassword2", "password"));
            dbPassword2 = properties.getProperty("dbPassword2", "password");
            dbDriverName2 = properties.getProperty("dbDriverName2", "com.mysql.jdbc.Driver");

            dbIsActive3 = properties.getProperty("dbIsActive3", "0");
            dbConnectionUrl3 = properties.getProperty("dbConnectionUrl3", "jdbc:mysql://localhost:3306/dneero?autoReconnect=true");
            dbUsername3 = properties.getProperty("dbUsername3", "username");
            //DesEncrypter encrypter3 = new DesEncrypter(passPhrase);
            //dbPassword3 = encrypter3.decrypt(properties.getProperty("dbPassword3", "password"));
            dbPassword3 = properties.getProperty("dbPassword3", "password");
            dbDriverName3 = properties.getProperty("dbDriverName3", "com.mysql.jdbc.Driver");

            dbIsActive4 = properties.getProperty("dbIsActive4", "0");
            dbConnectionUrl4 = properties.getProperty("dbConnectionUrl4", "jdbc:mysql://localhost:4406/dneero?autoReconnect=true");
            dbUsername4 = properties.getProperty("dbUsername4", "username");
            //DesEncrypter encrypter4 = new DesEncrypter(passPhrase);
            //dbPassword4 = encrypter4.decrypt(properties.getProperty("dbPassword4", "password"));
            dbPassword4 = properties.getProperty("dbPassword4", "password");
            dbDriverName4 = properties.getProperty("dbDriverName4", "com.mysql.jdbc.Driver");

            dbMaxActive = properties.getProperty("dbMaxActive", "100");
            dbMaxIdle = properties.getProperty("dbMaxIdle", "15");
            dbMinIdle = properties.getProperty("dbMinIdle", "10");
            dbMaxWait = properties.getProperty("dbMaxWait", "10000");

            runScheduledTasksOnThisInstance = properties.getProperty("runScheduledTasksOnThisInstance", "0");
            instancename = properties.getProperty("instancename", "InstanceNotNamed");
            hibernateShowSql = properties.getProperty("hibernateShowSql", "0");

            terracottahost01 = properties.getProperty("terracottahost01", "");
            terracottahost02 = properties.getProperty("terracottahost02", "");
            terracottahost03 = properties.getProperty("terracottahost03", "");


            infinispanconfigfileobjectcache = properties.getProperty("infinispanconfigfileobjectcache", "");
            infinispanconfigfilehibernate = properties.getProperty("infinispanconfigfilehibernate", "");
            infinispanconfigfilehibernatedbcache = properties.getProperty("infinispanconfigfilehibernatedbcache", "");
            infinispanconfigfilehibernateimpressions = properties.getProperty("infinispanconfigfilehibernateimpressions", "");
            jgroupstcppinginitialhosts = properties.getProperty("jgroupstcppinginitialhosts", "localhost[7800]");
            jgroupstcpport = properties.getProperty("jgroupstcpport", "7800");

            haveAttemptedToLoadDefaultPropsFile = true;
            haveNewConfigToTest = true;
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void save() throws GeneralException {
        Logger logger = Logger.getLogger(InstanceProperties.class);
        logger.debug("save() called");

        //Make sure we test the next time around
        haveNewConfigToTest = true;
        //Properties object
        Properties properties = new Properties();

        //Ishajdbcon
        if (ishajdbcon!=null){
            properties.setProperty("ishajdbcon", ishajdbcon);
        }

        //DB
        if (dbConnectionUrl!=null){
            properties.setProperty("dbConnectionUrl", dbConnectionUrl);
        }
        if (dbUsername!=null){
            properties.setProperty("dbUsername", dbUsername);
        }
        if (dbPassword!=null){
            //DesEncrypter encrypter2 = new DesEncrypter(passPhrase);
            //String encDbPassword = encrypter2.encrypt(dbPassword);
            //properties.setProperty("dbPassword", encDbPassword);
            properties.setProperty("dbPassword", dbPassword);
        }
        if (dbDriverName!=null){
            properties.setProperty("dbDriverName", dbDriverName);
        }

        //DB Dbcache
        if (dbConnectionUrlDbcache!=null){
            properties.setProperty("dbConnectionUrlDbcache", dbConnectionUrlDbcache);
        }
        if (dbUsernameDbcache!=null){
            properties.setProperty("dbUsernameDbcache", dbUsernameDbcache);
        }
        if (dbPasswordDbcache!=null){
            //DesEncrypter encrypter2 = new DesEncrypter(passPhrase);
            //String encDbPasswordDbcache = encrypter2.encrypt(dbPasswordDbcache);
            //properties.setProperty("dbPasswordDbcache", encDbPasswordDbcache);
            properties.setProperty("dbPasswordDbcache", dbPasswordDbcache);
        }
        if (dbDriverNameDbcache!=null){
            properties.setProperty("dbDriverNameDbcache", dbDriverNameDbcache);
        }

        //DB Impressions
        if (dbConnectionUrlImpressions!=null){
            properties.setProperty("dbConnectionUrlImpressions", dbConnectionUrlImpressions);
        }
        if (dbUsernameImpressions!=null){
            properties.setProperty("dbUsernameImpressions", dbUsernameImpressions);
        }
        if (dbPasswordImpressions!=null){
            //DesEncrypter encrypter2 = new DesEncrypter(passPhrase);
            //String encDbPasswordImpressions = encrypter2.encrypt(dbPasswordImpressions);
            //properties.setProperty("dbPasswordImpressions", encDbPasswordImpressions);
            properties.setProperty("dbPasswordImpressions", dbPasswordImpressions);
        }
        if (dbDriverNameImpressions!=null){
            properties.setProperty("dbDriverNameImpressions", dbDriverNameImpressions);
        }

        //DB1
        if (dbIsActive1!=null){
            properties.setProperty("dbIsActive1", dbIsActive1);
        }
        if (dbConnectionUrl1!=null){
            properties.setProperty("dbConnectionUrl1", dbConnectionUrl1);
        }
        if (dbUsername1!=null){
            properties.setProperty("dbUsername1", dbUsername1);
        }
        if (dbPassword1!=null){
            //DesEncrypter encrypter1 = new DesEncrypter(passPhrase);
            //String encDbPassword = encrypter1.encrypt(dbPassword1);
            //properties.setProperty("dbPassword1", encDbPassword1);
            properties.setProperty("dbPassword1", dbPassword1);
        }
        if (dbDriverName1!=null){
            properties.setProperty("dbDriverName1", dbDriverName1);
        }

        //DB2
        if (dbIsActive2!=null){
            properties.setProperty("dbIsActive2", dbIsActive2);
        }
        if (dbConnectionUrl2!=null){
            properties.setProperty("dbConnectionUrl2", dbConnectionUrl2);
        }
        if (dbUsername2!=null){
            properties.setProperty("dbUsername2", dbUsername2);
        }
        if (dbPassword2!=null){
            //DesEncrypter encrypter2 = new DesEncrypter(passPhrase);
            //String encDbPassword = encrypter2.encrypt(dbPassword2);
            //properties.setProperty("dbPassword2", encDbPassword2);
            properties.setProperty("dbPassword2", dbPassword2);
        }
        if (dbDriverName2!=null){
            properties.setProperty("dbDriverName2", dbDriverName2);
        }

        //DB3
        if (dbIsActive3!=null){
            properties.setProperty("dbIsActive3", dbIsActive3);
        }
        if (dbConnectionUrl3!=null){
            properties.setProperty("dbConnectionUrl3", dbConnectionUrl3);
        }
        if (dbUsername3!=null){
            properties.setProperty("dbUsername3", dbUsername3);
        }
        if (dbPassword3!=null){
            //DesEncrypter encrypter3 = new DesEncrypter(passPhrase);
            //String encDbPassword = encrypter3.encrypt(dbPassword3);
            //properties.setProperty("dbPassword3", encDbPassword3);
            properties.setProperty("dbPassword3", dbPassword3);
        }
        if (dbDriverName3!=null){
            properties.setProperty("dbDriverName3", dbDriverName3);
        }

        //DB4
        if (dbIsActive4!=null){
            properties.setProperty("dbIsActive4", dbIsActive4);
        }
        if (dbConnectionUrl4!=null){
            properties.setProperty("dbConnectionUrl4", dbConnectionUrl4);
        }
        if (dbUsername4!=null){
            properties.setProperty("dbUsername4", dbUsername4);
        }
        if (dbPassword4!=null){
            //DesEncrypter encrypter4 = new DesEncrypter(passPhrase);
            //String encDbPassword = encrypter4.encrypt(dbPassword4);
            //properties.setProperty("dbPassword4", encDbPassword4);
            properties.setProperty("dbPassword4", dbPassword4);
        }
        if (dbDriverName4!=null){
            properties.setProperty("dbDriverName4", dbDriverName4);
        }



        if (dbMaxActive!=null){
            properties.setProperty("dbMaxActive", dbMaxActive);
        }
        if (dbMaxIdle!=null){
            properties.setProperty("dbMaxIdle", dbMaxIdle);
        }
        if (dbMinIdle!=null){
            properties.setProperty("dbMinIdle", dbMinIdle);
        }
        if (dbMaxWait!=null){
            properties.setProperty("dbMaxWait", dbMaxWait);
        }

        if (runScheduledTasksOnThisInstance!=null){
            properties.setProperty("runScheduledTasksOnThisInstance", runScheduledTasksOnThisInstance);
        }
        if (instancename!=null){
            properties.setProperty("instancename", instancename);
        }
        if (hibernateShowSql!=null){
            properties.setProperty("hibernateShowSql", hibernateShowSql);
        }

        if (terracottahost01!=null){
            properties.setProperty("terracottahost01", terracottahost01);
        }
        if (terracottahost02!=null){
            properties.setProperty("terracottahost02", terracottahost02);
        }
        if (terracottahost03!=null){
            properties.setProperty("terracottahost03", terracottahost03);
        }



        if (infinispanconfigfileobjectcache!=null){
            properties.setProperty("infinispanconfigfileobjectcache", infinispanconfigfileobjectcache);
        }
        if (infinispanconfigfilehibernate!=null){
            properties.setProperty("infinispanconfigfilehibernate", infinispanconfigfilehibernate);
        }
        if (infinispanconfigfilehibernatedbcache!=null){
            properties.setProperty("infinispanconfigfilehibernatedbcache", infinispanconfigfilehibernatedbcache);
        }
        if (infinispanconfigfilehibernateimpressions!=null){
            properties.setProperty("infinispanconfigfilehibernateimpressions", infinispanconfigfilehibernateimpressions);
        }
        if (jgroupstcppinginitialhosts!=null){
            properties.setProperty("jgroupstcppinginitialhosts", jgroupstcppinginitialhosts);
        }
        if (jgroupstcpport!=null){
            properties.setProperty("jgroupstcpport", jgroupstcpport);
        }


        boolean savedsuccessfully = false;
        String saveiomsg = "";
        try{
            //Save to the file system in the conf directory
            File fil = new File(dbPropsInternalFilename);
            if (!fil.exists()){
                fil.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(fil);
            properties.store(fos, "InstanceProperties");
            fos.close();
            fil = null;
            fos = null;
            savedsuccessfully = true;
        } catch (IOException e) {
            e.printStackTrace();
            saveiomsg = saveiomsg + e.getMessage();
        }

        try{
            //Store to default system location
            File fil2 = new File("", dbPropsExternalFilename);
            if (!fil2.exists()){
                fil2.createNewFile();
            }
            FileOutputStream fos2 = new FileOutputStream(fil2);
            properties.store(fos2, "InstanceProperties for " + WebAppRootDir.getUniqueContextId());
            fos2.close();
            fil2 = null;
            fos2 = null;
            savedsuccessfully = true;
        } catch (IOException e) {
            e.printStackTrace();
            saveiomsg = saveiomsg + e.getMessage();
        }

        //If neither save worked, alert
        if (!savedsuccessfully){
            GeneralException gex = new GeneralException("File save failed: "+saveiomsg);
            throw gex;
        }
    }




    public static String getIshajdbcon() {
        load();
        return ishajdbcon;
    }

    public static void setIshajdbcon(String ishajdbcon) {
        InstanceProperties.ishajdbcon=ishajdbcon;
    }

    //DB
    public static String getDbConnectionUrl() {
        load();
        return dbConnectionUrl;
    }

    public static void setDbConnectionUrl(String dbConnectionUrl) {
        InstanceProperties.dbConnectionUrl = dbConnectionUrl;
    }

    public static String getDbUsername() {
        load();
        return dbUsername;
    }

    public static void setDbUsername(String dbUsername) {
        InstanceProperties.dbUsername = dbUsername;
    }

    public static String getDbPassword() {
        load();
        return dbPassword;
    }

    public static void setDbPassword(String dbPassword) {
        InstanceProperties.dbPassword = dbPassword;
    }

    public static String getDbDriverName(){
        load();
        return dbDriverName;
    }

    public static void setDbDriverName(String dbDriverName) {
        InstanceProperties.dbDriverName = dbDriverName;
    }

    //DB Dbcache
    public static String getDbConnectionUrlDbcache() {
        load();
        return dbConnectionUrlDbcache;
    }

    public static void setDbConnectionUrlDbcache(String dbConnectionUrlDbcache) {
        InstanceProperties.dbConnectionUrlDbcache=dbConnectionUrlDbcache;
    }

    public static String getDbUsernameDbcache() {
        load();
        return dbUsernameDbcache;
    }

    public static void setDbUsernameDbcache(String dbUsernameDbcache) {
        InstanceProperties.dbUsernameDbcache=dbUsernameDbcache;
    }

    public static String getDbPasswordDbcache() {
        load();
        return dbPasswordDbcache;
    }

    public static void setDbPasswordDbcache(String dbPasswordDbcache) {
        InstanceProperties.dbPasswordDbcache=dbPasswordDbcache;
    }

    public static String getDbDriverNameDbcache(){
        load();
        return dbDriverNameDbcache;
    }

    public static void setDbDriverNameDbcache(String dbDriverNameDbcache) {
        InstanceProperties.dbDriverNameDbcache=dbDriverNameDbcache;
    }

    //DB Impressions
    public static String getDbConnectionUrlImpressions() {
        load();
        return dbConnectionUrlImpressions;
    }

    public static void setDbConnectionUrlImpressions(String dbConnectionUrlImpressions) {
        InstanceProperties.dbConnectionUrlImpressions=dbConnectionUrlImpressions;
    }

    public static String getDbUsernameImpressions() {
        load();
        return dbUsernameImpressions;
    }

    public static void setDbUsernameImpressions(String dbUsernameImpressions) {
        InstanceProperties.dbUsernameImpressions=dbUsernameImpressions;
    }

    public static String getDbPasswordImpressions() {
        load();
        return dbPasswordImpressions;
    }

    public static void setDbPasswordImpressions(String dbPasswordImpressions) {
        InstanceProperties.dbPasswordImpressions=dbPasswordImpressions;
    }

    public static String getDbDriverNameImpressions(){
        load();
        return dbDriverNameImpressions;
    }

    public static void setDbDriverNameImpressions(String dbDriverNameImpressions) {
        InstanceProperties.dbDriverNameImpressions=dbDriverNameImpressions;
    }


    //DB1
    public static String getDbIsActive1() {
        load();
        return dbIsActive1;
    }

    public static void setDbIsActive1(String dbIsActive1) {
        InstanceProperties.dbIsActive1=dbIsActive1;
    }

    public static String getDbConnectionUrl1() {
        load();
        return dbConnectionUrl1;
    }

    public static void setDbConnectionUrl1(String dbConnectionUrl1) {
        InstanceProperties.dbConnectionUrl1 = dbConnectionUrl1;
    }

    public static String getDbUsername1() {
        load();
        return dbUsername1;
    }

    public static void setDbUsername1(String dbUsername1) {
        InstanceProperties.dbUsername1 = dbUsername1;
    }

    public static String getDbPassword1() {
        load();
        return dbPassword1;
    }

    public static void setDbPassword1(String dbPassword1) {
        InstanceProperties.dbPassword1 = dbPassword1;
    }

    public static String getDbDriverName1(){
        load();
        return dbDriverName1;
    }

    public static void setDbDriverName1(String dbDriverName1) {
        InstanceProperties.dbDriverName1 = dbDriverName1;
    }

    //DB2
    public static String getDbIsActive2() {
        load();
        return dbIsActive2;
    }

    public static void setDbIsActive2(String dbIsActive2) {
        InstanceProperties.dbIsActive2=dbIsActive2;
    }

    public static String getDbConnectionUrl2() {
        load();
        return dbConnectionUrl2;
    }

    public static void setDbConnectionUrl2(String dbConnectionUrl2) {
        InstanceProperties.dbConnectionUrl2 = dbConnectionUrl2;
    }

    public static String getDbUsername2() {
        load();
        return dbUsername2;
    }

    public static void setDbUsername2(String dbUsername2) {
        InstanceProperties.dbUsername2 = dbUsername2;
    }

    public static String getDbPassword2() {
        load();
        return dbPassword2;
    }

    public static void setDbPassword2(String dbPassword2) {
        InstanceProperties.dbPassword2 = dbPassword2;
    }

    public static String getDbDriverName2(){
        load();
        return dbDriverName2;
    }

    public static void setDbDriverName2(String dbDriverName2) {
        InstanceProperties.dbDriverName2 = dbDriverName2;
    }


    //DB3
    public static String getDbIsActive3() {
        return dbIsActive3;
    }

    public static void setDbIsActive3(String dbIsActive3) {
        InstanceProperties.dbIsActive3=dbIsActive3;
    }

    public static String getDbConnectionUrl3() {
        load();
        return dbConnectionUrl3;
    }

    public static void setDbConnectionUrl3(String dbConnectionUrl3) {
        InstanceProperties.dbConnectionUrl3 = dbConnectionUrl3;
    }

    public static String getDbUsername3() {
        load();
        return dbUsername3;
    }

    public static void setDbUsername3(String dbUsername3) {
        InstanceProperties.dbUsername3 = dbUsername3;
    }

    public static String getDbPassword3() {
        load();
        return dbPassword3;
    }

    public static void setDbPassword3(String dbPassword3) {
        InstanceProperties.dbPassword3 = dbPassword3;
    }

    public static String getDbDriverName3(){
        load();
        return dbDriverName3;
    }

    public static void setDbDriverName3(String dbDriverName3) {
        InstanceProperties.dbDriverName3 = dbDriverName3;
    }

    //DB4
    public static String getDbIsActive4() {
        return dbIsActive4;
    }

    public static void setDbIsActive4(String dbIsActive4) {
        InstanceProperties.dbIsActive4=dbIsActive4;
    }

    public static String getDbConnectionUrl4() {
        load();
        return dbConnectionUrl4;
    }

    public static void setDbConnectionUrl4(String dbConnectionUrl4) {
        InstanceProperties.dbConnectionUrl4 = dbConnectionUrl4;
    }

    public static String getDbUsername4() {
        load();
        return dbUsername4;
    }

    public static void setDbUsername4(String dbUsername4) {
        InstanceProperties.dbUsername4 = dbUsername4;
    }

    public static String getDbPassword4() {
        load();
        return dbPassword4;
    }

    public static void setDbPassword4(String dbPassword4) {
        InstanceProperties.dbPassword4 = dbPassword4;
    }

    public static String getDbDriverName4(){
        load();
        return dbDriverName4;
    }

    public static void setDbDriverName4(String dbDriverName4) {
        InstanceProperties.dbDriverName4 = dbDriverName4;
    }



    public static int getDbMaxActive() {
        load();
        if (Num.isinteger(dbMaxActive)){
            return Integer.parseInt(dbMaxActive);
        } else {
            return 100;
        }
    }

    public static void setDbMaxActive(String dbMaxActive) {
        InstanceProperties.dbMaxActive = dbMaxActive;
    }

    public static int getDbMaxIdle() {
        load();
        if (Num.isinteger(dbMaxIdle)){
            return Integer.parseInt(dbMaxIdle);
        } else {
            return 15;
        }
    }

    public static void setDbMaxIdle(String dbMaxIdle) {
        InstanceProperties.dbMaxIdle = dbMaxIdle;
    }

    public static int getDbMinIdle() {
        load();
        if (Num.isinteger(dbMinIdle)){
            return Integer.parseInt(dbMinIdle);
        } else {
            return 10;
        }
    }

    public static void setDbMinIdle(String dbMinIdle) {
        InstanceProperties.dbMinIdle = dbMinIdle;
    }

    public static int getDbMaxWait() {
        load();
        if (Num.isinteger(dbMaxWait)){
            return Integer.parseInt(dbMaxWait);
        } else {
            return 10000;
        }
    }

    public static void setDbMaxWait(String dbMaxWait) {
        InstanceProperties.dbMaxWait = dbMaxWait;
    }


    public static String getDbPropsInternalFilename() {
        load();
        return dbPropsInternalFilename;
    }

    public static void setDbPropsInternalFilename(String dbPropsInternalFilename) {
        InstanceProperties.dbPropsInternalFilename = dbPropsInternalFilename;
    }



    public static boolean getHaveNewConfigToTest() {
        return InstanceProperties.haveNewConfigToTest;
    }


    public static void setHaveNewConfigToTest(boolean haveNewConfigToTest) {
        InstanceProperties.haveNewConfigToTest = haveNewConfigToTest;
    }

    public static boolean getRunScheduledTasksOnThisInstance(){
        load();
        if (runScheduledTasksOnThisInstance!=null && runScheduledTasksOnThisInstance.equals("1")){
            return true;
        }
        return false;
    }

    public static void setRunScheduledTasksOnThisInstance(boolean runScheduledTasksOnThisInstance){
        if (runScheduledTasksOnThisInstance){
            InstanceProperties.runScheduledTasksOnThisInstance = "1";
        } else {
            InstanceProperties.runScheduledTasksOnThisInstance = "0";
        }
    }


    public static String getInstancename() {
        load();
        return instancename;
    }

    public static void setInstancename(String instancename) {
        InstanceProperties.instancename = instancename;
    }

    public static boolean getHibernateShowSql(){
        load();
        if (hibernateShowSql!=null && hibernateShowSql.equals("1")){
            return true;
        }
        return false;
    }

    public static void setHibernateShowSql(boolean hibernateShowSql){
        if (hibernateShowSql){
            InstanceProperties.hibernateShowSql = "1";
        } else {
            InstanceProperties.hibernateShowSql = "0";
        }
    }




    public static String getTerracottahost01() {
        load();
        return terracottahost01;
    }

    public static void setTerracottahost01(String terracottahost01) {
        InstanceProperties.terracottahost01 = terracottahost01;
    }

    public static String getTerracottahost02() {
        load();
        return terracottahost02;
    }

    public static void setTerracottahost02(String terracottahost02) {
        InstanceProperties.terracottahost02 = terracottahost02;
    }

    public static String getTerracottahost03() {
        load();
        return terracottahost03;
    }

    public static void setTerracottahost03(String terracottahost03) {
        InstanceProperties.terracottahost03 = terracottahost03;
    }




    public static String getInfinispanconfigfileobjectcache() {
        load();
        return infinispanconfigfileobjectcache;
    }

    public static void setInfinispanconfigfileobjectcache(String infinispanconfigfileobjectcache) {
        InstanceProperties.infinispanconfigfileobjectcache = infinispanconfigfileobjectcache;
    }



    public static String getInfinispanconfigfilehibernate() {
        load();
        return infinispanconfigfilehibernate;
    }

    public static void setInfinispanconfigfilehibernate(String infinispanconfigfilehibernate) {
        InstanceProperties.infinispanconfigfilehibernate = infinispanconfigfilehibernate;
    }

    public static String getInfinispanconfigfilehibernatedbcache() {
        load();
        return infinispanconfigfilehibernatedbcache;
    }

    public static void setInfinispanconfigfilehibernatedbcache(String infinispanconfigfilehibernatedbcache) {
        InstanceProperties.infinispanconfigfilehibernatedbcache = infinispanconfigfilehibernatedbcache;
    }

    public static String getInfinispanconfigfilehibernateimpressions() {
        load();
        return infinispanconfigfilehibernateimpressions;
    }

    public static void setInfinispanconfigfilehibernateimpressions(String infinispanconfigfilehibernateimpressions) {
        InstanceProperties.infinispanconfigfilehibernateimpressions = infinispanconfigfilehibernateimpressions;
    }

    public static String getJgroupstcppinginitialhosts() {
        load();
        return jgroupstcppinginitialhosts;
    }

    public static void setJgroupstcppinginitialhosts(String jgroupstcppinginitialhosts) {
        InstanceProperties.jgroupstcppinginitialhosts = jgroupstcppinginitialhosts;
    }


    public static String getJgroupstcpport() {
        load();
        return jgroupstcpport;
    }

    public static void setJgroupstcpport(String jgroupstcpport) {
        InstanceProperties.jgroupstcpport = jgroupstcpport;
    }


}
