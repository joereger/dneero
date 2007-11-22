package com.dneero.htmluibeans;

import org.apache.log4j.Logger;
import com.dneero.systemprops.SystemProperty;
import com.dneero.systemprops.BaseUrl;
import com.dneero.systemprops.InstanceProperties;

import com.dneero.util.GeneralException;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Oct 6, 2006
 * Time: 3:35:02 AM
 */
public class SysadminInstanceProps implements Serializable {

    private String dbConnectionUrl;
    private String dbUsername;
    private String dbPassword;
    private String dbMaxActive;
    private String dbMaxIdle;
    private String dbMinIdle;
    private String dbMaxWait;
    private String dbDriverName;
    private String runScheduledTasksOnThisInstance;
    private String instancename;

    public SysadminInstanceProps(){

    }



    public void initBean(){
        dbConnectionUrl = InstanceProperties.getDbConnectionUrl();
        dbUsername = InstanceProperties.getDbUsername();
        dbPassword = InstanceProperties.getDbPassword();
        dbMaxActive = String.valueOf(InstanceProperties.getDbMaxActive());
        dbMaxIdle = String.valueOf(InstanceProperties.getDbMaxIdle());
        dbMinIdle = String.valueOf(InstanceProperties.getDbMinIdle());
        dbMaxWait = String.valueOf(InstanceProperties.getDbMaxWait());
        dbDriverName = InstanceProperties.getDbDriverName();
        if (InstanceProperties.getRunScheduledTasksOnThisInstance()){
            runScheduledTasksOnThisInstance = "1";
        } else {
            runScheduledTasksOnThisInstance = "0";
        }
        instancename = InstanceProperties.getInstancename();
    }

    public String saveProps() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        try{
            //This assumes page-level validation
            InstanceProperties.setDbConnectionUrl(dbConnectionUrl);
            InstanceProperties.setDbUsername(dbUsername);
            InstanceProperties.setDbPassword(dbPassword);
            InstanceProperties.setDbMaxActive(String.valueOf(dbMaxActive));
            InstanceProperties.setDbMaxActive(String.valueOf(dbMaxIdle));
            InstanceProperties.setDbMinIdle(String.valueOf(dbMinIdle));
            InstanceProperties.setDbMaxWait(String.valueOf(dbMaxWait));
            InstanceProperties.setDbDriverName(dbDriverName);
            if (runScheduledTasksOnThisInstance.equals("1")){
                InstanceProperties.setRunScheduledTasksOnThisInstance(true);
            } else {
                InstanceProperties.setRunScheduledTasksOnThisInstance(false);
            }
            InstanceProperties.setInstancename(instancename);
            try{
                InstanceProperties.save();
                if (InstanceProperties.haveValidConfig()){
                    Pagez.getUserSession().setMessage("Save complete.");
                } else {
                    Pagez.getUserSession().setMessage("Save failed! Values reset.");
                }
            } catch (GeneralException gex){
                Pagez.getUserSession().setMessage("Save failed: " + gex.getErrorsAsSingleString());
            }

        } catch (Exception ex){
            logger.error("",ex);
            Pagez.getUserSession().setMessage("Save failed: " + ex.getMessage());
        }

        return "sysadmininstanceprops";
    }


    public String getRunScheduledTasksOnThisInstance() {
        return runScheduledTasksOnThisInstance;
    }

    public void setRunScheduledTasksOnThisInstance(String runScheduledTasksOnThisInstance) {
        this.runScheduledTasksOnThisInstance = runScheduledTasksOnThisInstance;
    }

    public String getDbConnectionUrl() {
        return dbConnectionUrl;
    }

    public void setDbConnectionUrl(String dbConnectionUrl) {
        this.dbConnectionUrl = dbConnectionUrl;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDbMaxActive() {
        return dbMaxActive;
    }

    public void setDbMaxActive(String dbMaxActive) {
        this.dbMaxActive = dbMaxActive;
    }

    public String getDbMaxIdle() {
        return dbMaxIdle;
    }

    public void setDbMaxIdle(String dbMaxIdle) {
        this.dbMaxIdle = dbMaxIdle;
    }

    public String getDbMinIdle() {
        return dbMinIdle;
    }

    public void setDbMinIdle(String dbMinIdle) {
        this.dbMinIdle = dbMinIdle;
    }

    public String getDbMaxWait() {
        return dbMaxWait;
    }

    public void setDbMaxWait(String dbMaxWait) {
        this.dbMaxWait = dbMaxWait;
    }

    public String getDbDriverName() {
        return dbDriverName;
    }

    public void setDbDriverName(String dbDriverName) {
        this.dbDriverName = dbDriverName;
    }

    public String getInstancename() {
        return instancename;
    }

    public void setInstancename(String instancename) {
        this.instancename = instancename;
    }
}
