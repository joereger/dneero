package com.dneero.htmluibeans;

import com.dneero.charity.CharityReport;
import com.dneero.reports.FieldAggregator;
import com.dneero.reports.SimpleTableOutput;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.hibernate.NumFromUniqueResult;
import com.dneero.dao.Blogger;
import com.dneero.dao.Eula;
import com.dneero.util.Time;
import com.dneero.eula.EulaHelper;

import java.io.Serializable;
import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Nov 10, 2006
 * Time: 3:02:56 PM
 */
public class SysadminUsageReport implements Serializable {

    private int totalUsers = 0;
    private int totalActiveUsers = 0;
    private int totalDisabledUsers = 0;
    private int usersWhoHaveJoinedAConvo = 0;
    private int usersWhoHavePosted = 0;
    private int usersWhoHaveBeenPaidInBalance = 0;
    private int usersWhoHaveBeenPaidInRealWorld = 0;
    private int usersWhoHaveNeverJoinedAConvo = 0;
    private TreeMap<Integer, Integer> numberOfConvosVsUsers = new TreeMap<Integer, Integer>();
    private String numberOfConvosVsUsersAsHtml = "";
    private int usersLoggedInLast3Days = 0;
    private int usersLoggedInLast7Days = 0;
    private int usersLoggedInLast14Days = 0;
    private int usersLoggedInLast30Days = 0;
    private int usersLoggedInLast60Days = 0;
    private int usersLoggedInLast90Days = 0;
    private int usersLoggedInLast180Days = 0;
    private int usersLoggedInLast365Days = 0;
    private int usersOnLatestEULA = 0;

    public SysadminUsageReport(){

    }



    public void initBean(){
        generateReport();
    }

    private void generateReport(){
        countConvosByUser();
        totalUsers = NumFromUniqueResult.getInt("SELECT count(*) from User");
        totalActiveUsers = NumFromUniqueResult.getInt("SELECT count(*) from User where isenabled=true");
        totalDisabledUsers = totalUsers - totalActiveUsers;
        usersWhoHaveJoinedAConvo = NumFromUniqueResult.getInt("SELECT count(distinct bloggerid) from Response");
        usersWhoHaveNeverJoinedAConvo = totalUsers - usersWhoHaveJoinedAConvo;
        usersWhoHavePosted = NumFromUniqueResult.getInt("SELECT count(distinct bloggerid) from Response where impressionstotal>0");
        usersWhoHaveBeenPaidInBalance = NumFromUniqueResult.getInt("SELECT count(distinct userid) from Balance where amt>0");
        usersWhoHaveBeenPaidInRealWorld = NumFromUniqueResult.getInt("SELECT count(distinct userid) from Balancetransaction where issuccessful=true and amt>0");
        Eula mostRecentEula = EulaHelper.getMostRecentEula();
        usersOnLatestEULA = NumFromUniqueResult.getInt("SELECT count(distinct userid) from Usereula where eulaid='"+mostRecentEula.getEulaid()+"'");
        usersLoggedInLast3Days = usersLoggedInInLastXDays(3);
        usersLoggedInLast7Days = usersLoggedInInLastXDays(7);
        usersLoggedInLast14Days = usersLoggedInInLastXDays(14);
        usersLoggedInLast30Days = usersLoggedInInLastXDays(30);
        usersLoggedInLast60Days = usersLoggedInInLastXDays(60);
        usersLoggedInLast90Days = usersLoggedInInLastXDays(90);
        usersLoggedInLast180Days = usersLoggedInInLastXDays(180);
        usersLoggedInLast365Days = usersLoggedInInLastXDays(365);
    }

    public static int usersLoggedInInLastXDays(int xDays){
        Calendar startDate = Time.xDaysAgoStart(Calendar.getInstance(), xDays);
        int totalUsers = NumFromUniqueResult.getInt("SELECT count(*) from User where lastlogindate>'"+Time.dateformatfordb(startDate)+"'");
        return totalUsers;
    }

    private void countConvosByUser(){
        numberOfConvosVsUsers = new TreeMap<Integer, Integer>();
        String emptyString = "";
        int maxSurveysTaken = 0;
        List objectlist = HibernateUtil.getSession().createQuery("select bloggerid, sum(1) from Response group by bloggerid"+emptyString).list();
        for (Iterator iterator = objectlist.iterator(); iterator.hasNext();) {
            Object[] row = (Object[]) iterator.next();
            //Blogger blogger = Blogger.get((Integer)row[0]);
            long surveystaken = (Long)row[1];
            Integer surveystakenInt = (new Long(surveystaken)).intValue();
            if (surveystakenInt>maxSurveysTaken){
                maxSurveysTaken = surveystakenInt;
            }
            if (numberOfConvosVsUsers.containsKey(surveystakenInt)){
                int currValue = numberOfConvosVsUsers.get(surveystakenInt);
                int newValue = currValue + 1;
                numberOfConvosVsUsers.put(surveystakenInt, newValue);
            } else {
                numberOfConvosVsUsers.put(surveystakenInt, 1);
            }
        }
        //Now format to html
        StringBuffer out = new StringBuffer();
        out.append("<table cellpadding='0' cellspacing='0' border='0'>");
        for(int i=1; i<=maxSurveysTaken; i++){
            out.append("<tr>");
            out.append("<td>");
            out.append("<font class=\"tinyfont\"><b>"+i+" convos</b></font>");
            out.append("</td>");
            out.append("<td>");
            out.append("<font class=\"tinyfont\"> --> </font>");
            out.append("</td>");
            int numUsers = 0;
            if (numberOfConvosVsUsers.containsKey(i)){
                numUsers = numberOfConvosVsUsers.get(i);
            }
            out.append("<td>");
            out.append("<font class=\"tinyfont\"><img src='/images/clear.gif' width='5' height='1'>"+numUsers+" users</font>");
            out.append("</td>");
            out.append("</tr>");
        }
        out.append("</table>");
        numberOfConvosVsUsersAsHtml = out.toString();
    }

    public int getUsersWhoHavePosted() {
        return usersWhoHavePosted;
    }

    public void setUsersWhoHavePosted(int usersWhoHavePosted) {
        this.usersWhoHavePosted=usersWhoHavePosted;
    }

    public int getUsersWhoHaveBeenPaidInBalance() {
        return usersWhoHaveBeenPaidInBalance;
    }

    public void setUsersWhoHaveBeenPaidInBalance(int usersWhoHaveBeenPaidInBalance) {
        this.usersWhoHaveBeenPaidInBalance=usersWhoHaveBeenPaidInBalance;
    }

    public int getUsersWhoHaveBeenPaidInRealWorld() {
        return usersWhoHaveBeenPaidInRealWorld;
    }

    public void setUsersWhoHaveBeenPaidInRealWorld(int usersWhoHaveBeenPaidInRealWorld) {
        this.usersWhoHaveBeenPaidInRealWorld=usersWhoHaveBeenPaidInRealWorld;
    }

    public int getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(int totalUsers) {
        this.totalUsers=totalUsers;
    }

    public int getTotalActiveUsers() {
        return totalActiveUsers;
    }

    public void setTotalActiveUsers(int totalActiveUsers) {
        this.totalActiveUsers=totalActiveUsers;
    }

    public int getTotalDisabledUsers() {
        return totalDisabledUsers;
    }

    public void setTotalDisabledUsers(int totalDisabledUsers) {
        this.totalDisabledUsers=totalDisabledUsers;
    }

    public int getUsersWhoHaveJoinedAConvo() {
        return usersWhoHaveJoinedAConvo;
    }

    public void setUsersWhoHaveJoinedAConvo(int usersWhoHaveJoinedAConvo) {
        this.usersWhoHaveJoinedAConvo=usersWhoHaveJoinedAConvo;
    }

    public int getUsersWhoHaveNeverJoinedAConvo() {
        return usersWhoHaveNeverJoinedAConvo;
    }

    public void setUsersWhoHaveNeverJoinedAConvo(int usersWhoHaveNeverJoinedAConvo) {
        this.usersWhoHaveNeverJoinedAConvo=usersWhoHaveNeverJoinedAConvo;
    }

    public TreeMap<Integer, Integer> getNumberOfConvosVsUsers() {
        return numberOfConvosVsUsers;
    }

    public void setNumberOfConvosVsUsers(TreeMap<Integer, Integer> numberOfConvosVsUsers) {
        this.numberOfConvosVsUsers=numberOfConvosVsUsers;
    }

    public String getNumberOfConvosVsUsersAsHtml() {
        return numberOfConvosVsUsersAsHtml;
    }

    public void setNumberOfConvosVsUsersAsHtml(String numberOfConvosVsUsersAsHtml) {
        this.numberOfConvosVsUsersAsHtml=numberOfConvosVsUsersAsHtml;
    }

    public int getUsersLoggedInLast7Days() {
        return usersLoggedInLast7Days;
    }

    public void setUsersLoggedInLast7Days(int usersLoggedInLast7Days) {
        this.usersLoggedInLast7Days=usersLoggedInLast7Days;
    }

    public int getUsersLoggedInLast14Days() {
        return usersLoggedInLast14Days;
    }

    public void setUsersLoggedInLast14Days(int usersLoggedInLast14Days) {
        this.usersLoggedInLast14Days=usersLoggedInLast14Days;
    }

    public int getUsersLoggedInLast30Days() {
        return usersLoggedInLast30Days;
    }

    public void setUsersLoggedInLast30Days(int usersLoggedInLast30Days) {
        this.usersLoggedInLast30Days=usersLoggedInLast30Days;
    }

    public int getUsersLoggedInLast60Days() {
        return usersLoggedInLast60Days;
    }

    public void setUsersLoggedInLast60Days(int usersLoggedInLast60Days) {
        this.usersLoggedInLast60Days=usersLoggedInLast60Days;
    }

    public int getUsersLoggedInLast90Days() {
        return usersLoggedInLast90Days;
    }

    public void setUsersLoggedInLast90Days(int usersLoggedInLast90Days) {
        this.usersLoggedInLast90Days=usersLoggedInLast90Days;
    }

    public int getUsersLoggedInLast180Days() {
        return usersLoggedInLast180Days;
    }

    public void setUsersLoggedInLast180Days(int usersLoggedInLast180Days) {
        this.usersLoggedInLast180Days=usersLoggedInLast180Days;
    }

    public int getUsersLoggedInLast365Days() {
        return usersLoggedInLast365Days;
    }

    public void setUsersLoggedInLast365Days(int usersLoggedInLast365Days) {
        this.usersLoggedInLast365Days=usersLoggedInLast365Days;
    }

    public int getUsersLoggedInLast3Days() {
        return usersLoggedInLast3Days;
    }

    public void setUsersLoggedInLast3Days(int usersLoggedInLast3Days) {
        this.usersLoggedInLast3Days=usersLoggedInLast3Days;
    }

    public int getUsersOnLatestEULA() {
        return usersOnLatestEULA;
    }

    public void setUsersOnLatestEULA(int usersOnLatestEULA) {
        this.usersOnLatestEULA=usersOnLatestEULA;
    }
}