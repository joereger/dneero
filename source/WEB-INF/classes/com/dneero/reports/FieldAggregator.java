package com.dneero.reports;

import com.dneero.dao.Blogger;
import com.dneero.dao.User;
import com.dneero.util.Time;
import com.dneero.util.DateDiff;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Calendar;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jan 28, 2008
 * Time: 2:10:47 PM
 */
public class FieldAggregator implements Serializable {

    private ArrayList<Blogger> bloggers;

    private TreeMap<String, Integer> age = new TreeMap<String, Integer>();
    private TreeMap<String, Integer> gender = new TreeMap<String, Integer>();
    private TreeMap<String, Integer> ethnicity = new TreeMap<String, Integer>();
    private TreeMap<String, Integer> maritalstatus = new TreeMap<String, Integer>();
    private TreeMap<String, Integer> income = new TreeMap<String, Integer>();
    private TreeMap<String, Integer> educationlevel = new TreeMap<String, Integer>();
    private TreeMap<String, Integer> state = new TreeMap<String, Integer>();
    private TreeMap<String, Integer> city = new TreeMap<String, Integer>();
    private TreeMap<String, Integer> profession = new TreeMap<String, Integer>();
    private TreeMap<String, Integer> blogfocus = new TreeMap<String, Integer>();
    private TreeMap<String, Integer> politics = new TreeMap<String, Integer>();
    private TreeMap<String, Integer> dneerousagemethods = new TreeMap<String, Integer>();


    public FieldAggregator(ArrayList<Blogger> bloggers){
        this.bloggers = bloggers;
        process();
    }

    private void process(){
        for (Iterator<Blogger> iterator = bloggers.iterator(); iterator.hasNext();) {
            Blogger blogger = iterator.next();
            User user = User.get(blogger.getUserid());

            addData(gender, blogger.getGender());
            addData(ethnicity, blogger.getEthnicity());
            addData(maritalstatus, blogger.getMaritalstatus());
            addData(income, blogger.getIncomerange());
            addData(educationlevel, blogger.getEducationlevel());
            addData(state, blogger.getState());
            addData(city, blogger.getCity());
            addData(profession, blogger.getProfession());
            addData(blogfocus, blogger.getBlogfocus());
            addData(politics, blogger.getPolitics());
            //dneerousagemethod
            String dneerousagemethod = "dNeero.com";
            if (user.getFacebookuserid()>0){
                dneerousagemethod = "Facebook App";
            }
            addData(dneerousagemethods, dneerousagemethod);
            //age
            int ageinyears = DateDiff.dateDiff("year", Calendar.getInstance(), Time.getCalFromDate(blogger.getBirthdate()));
            addData(age, String.valueOf(ageinyears));
        }
    }

    private void addData(TreeMap<String, Integer> map, String data){
        if (map==null){
            map = new TreeMap<String, Integer>();
        }
        if (map.containsKey(data)){
            map.put(data, map.get(data)+1);
        } else {
            map.put(data, 1);
        }
    }


    public ArrayList<Blogger> getBloggers() {
        return bloggers;
    }

    public TreeMap<String, Integer> getGender() {
        return gender;
    }

    public TreeMap<String, Integer> getEthnicity() {
        return ethnicity;
    }

    public TreeMap<String, Integer> getMaritalstatus() {
        return maritalstatus;
    }

    public TreeMap<String, Integer> getIncome() {
        return income;
    }

    public TreeMap<String, Integer> getEducationlevel() {
        return educationlevel;
    }

    public TreeMap<String, Integer> getState() {
        return state;
    }

    public TreeMap<String, Integer> getCity() {
        return city;
    }

    public TreeMap<String, Integer> getProfession() {
        return profession;
    }

    public TreeMap<String, Integer> getBlogfocus() {
        return blogfocus;
    }

    public TreeMap<String, Integer> getPolitics() {
        return politics;
    }

    public TreeMap<String, Integer> getDneerousagemethods() {
        return dneerousagemethods;
    }

    public TreeMap<String, Integer> getAge() {
        return age;
    }
}
