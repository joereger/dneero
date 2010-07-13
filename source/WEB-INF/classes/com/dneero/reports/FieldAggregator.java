package com.dneero.reports;

import com.dneero.dao.Blogger;
import com.dneero.dao.Demographic;
import com.dneero.dao.Pl;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.finders.DemographicsXML;
import com.dneero.util.DateDiff;
import com.dneero.util.Time;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Jan 28, 2008
 * Time: 2:10:47 PM
 */
public class FieldAggregator implements Serializable {

    private ArrayList<Blogger> bloggers;
    private Pl pl;
    private TreeMap<String, Integer> age = new TreeMap<String, Integer>();
    private TreeMap<String, Integer> dneerousagemethods = new TreeMap<String, Integer>();
    private TreeMap<Integer, TreeMap<String, Integer>> demographicDataCounts = new TreeMap<Integer, TreeMap<String, Integer>>();

    public FieldAggregator(ArrayList<Blogger> bloggers, Pl pl){
        this.bloggers = bloggers;
        this.pl = pl;
        process();
    }

    private void process(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        for (Iterator<Blogger> iterator = bloggers.iterator(); iterator.hasNext();) {
            Blogger blogger = iterator.next();
            User user = User.get(blogger.getUserid());
            //Only include for the current pl
            if (user.getPlid()==pl.getPlid() || pl==null){
                DemographicsXML demographicsXML = new DemographicsXML(blogger.getDemographicsxml(), Pl.get(user.getPlid()), false);
                try{
                    List<Demographic> demographics = HibernateUtil.getSession().createCriteria(Demographic.class)
                                                   .add(Restrictions.eq("plid", user.getPlid()))
                                                   .setCacheable(true)
                                                   .list();
                    for (Iterator<Demographic> demographicIterator = demographics.iterator(); demographicIterator.hasNext();) {
                        Demographic demographic = demographicIterator.next();
                        String value = demographicsXML.getValue(demographic.getDemographicid());
                        addDemographicData(demographic.getDemographicid(), value);
                    }
                } catch (Exception ex){
                    logger.error("", ex);
                }
                //dneerousagemethod
                String dneerousagemethod = "Web";
                if (user.getFacebookuserid()>0){
                    dneerousagemethod = "Facebook App";
                }
                addData(dneerousagemethods, dneerousagemethod);
                //age
                int ageinyears = DateDiff.dateDiff("year", Calendar.getInstance(), Time.getCalFromDate(blogger.getBirthdate()));
                addData(age, String.valueOf(ageinyears));
            }
        }
    }

    private void addDemographicData(int demographicid, String value){
        //Temp map
        TreeMap<String, Integer> map = new TreeMap<String, Integer>();
        //If there's already a map keyed to demographicid, use it
        if (demographicDataCounts.containsKey(demographicid)){
            map = demographicDataCounts.get(demographicid);
        }
        //If in the map there's already a count for value, increment
        if (map.containsKey(value)){
            map.put(value, map.get(value)+1);
        } else {
            //Otherwise start the count for this value at 1
            map.put(value, 1);
        }
        //Put the map back into the data treemap
        demographicDataCounts.put(demographicid, map);
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

    public TreeMap<String, Integer> getDemographicResults(int demographicid){
        if (demographicDataCounts.containsKey(demographicid)){
            return demographicDataCounts.get(demographicid);
        }
        return new TreeMap<String, Integer>();
    }

    public TreeMap<String, Integer> getDneerousagemethods() {
        return dneerousagemethods;
    }

    public TreeMap<String, Integer> getAge() {
        return age;
    }



}
