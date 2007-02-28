package com.dneero.formbeans;

/**
 * User: Joe Reger Jr
 * Date: Feb 21, 2007
 * Time: 11:26:03 AM
 */
public class SystemStats {

    //BE SURE TO SYNC CODE HERE WITH MAIN SystemStats in scheduledjobs
    private int totalbloggers=com.dneero.scheduledjobs.SystemStats.getTotalbloggers();
    private int totalblogs=com.dneero.scheduledjobs.SystemStats.getTotalblogs();
    private int totalresearchers=com.dneero.scheduledjobs.SystemStats.getTotalresearchers();
    private int totalimpressions=com.dneero.scheduledjobs.SystemStats.getTotalimpressions();
    private int impressions30days=com.dneero.scheduledjobs.SystemStats.getImpressions30days();
    private double dollarsavailabletobloggers=com.dneero.scheduledjobs.SystemStats.getDollarsavailabletobloggers();

    public int getTotalbloggers() {
        return com.dneero.scheduledjobs.SystemStats.getTotalbloggers();
    }

    public void setTotalbloggers(int totalbloggers) {

    }

    public int getTotalblogs() {
        return com.dneero.scheduledjobs.SystemStats.getTotalblogs();
    }

    public void setTotalblogs(int totalblogs) {

    }

    public int getTotalresearchers() {
        return com.dneero.scheduledjobs.SystemStats.getTotalresearchers();
    }

    public void setTotalresearchers(int totalresearchers) {

    }

    public int getTotalimpressions() {
        return com.dneero.scheduledjobs.SystemStats.getTotalimpressions();
    }

    public void setTotalimpressions(int totalimpressions) {

    }


    public int getImpressions30days() {
        return com.dneero.scheduledjobs.SystemStats.getImpressions30days();
    }

    public void setImpressions30days(int impressions30days) {

    }

    public double getDollarsavailabletobloggers() {
        return com.dneero.scheduledjobs.SystemStats.getDollarsavailabletobloggers();
    }

    public void setDollarsavailabletobloggers(double dollarsavailabletobloggers) {

    }

}
