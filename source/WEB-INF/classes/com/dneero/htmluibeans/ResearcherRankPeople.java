package com.dneero.htmluibeans;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.util.Num;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;
import java.util.Collections;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class ResearcherRankPeople implements Serializable {

    private ArrayList<ResearcherRankPeopleListitem> rrplis = new ArrayList<ResearcherRankPeopleListitem>();
    private Rank rank;


    public ResearcherRankPeople(){

    }



    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (Pagez.getRequest().getParameter("rankid")!=null && Num.isinteger(Pagez.getRequest().getParameter("rankid"))){
            rank = Rank.get(Integer.parseInt(Pagez.getRequest().getParameter("rankid")));
        }
        if (rank!=null){
            rrplis = new ArrayList<ResearcherRankPeopleListitem>();
            ArrayList<Object[]> stats = (ArrayList<Object[]>)HibernateUtil.getSession().createQuery("select rankuser.userid, sum(points) as summ, avg(normalizedpoints), user from Rankuser rankuser where rankid='"+rank.getRankid()+"' group by rankuser.userid").list();
            for (Iterator iterator = stats.iterator(); iterator.hasNext();) {
                Object[] res = (Object[])iterator.next();
                int userid = (Integer)res[0];
                long points = (Long)res[1];
                Double avgnormalizedpoints = (Double)res[2];
                User user = (User)res[3];

                Double avgnormalizedpointsMult = Double.parseDouble(String.valueOf(avgnormalizedpoints*100));

                ResearcherRankPeopleListitem rrpli = new ResearcherRankPeopleListitem();
                rrpli.setAvgnormalizedpoints(avgnormalizedpointsMult);
                rrpli.setAvgnormalizedpointsStr(avgnormalizedpointsMult.intValue()+"/100 Ranking Strength");
                rrpli.setName(user.getFirstname()+" "+user.getLastname());
                rrpli.setPoints(Integer.parseInt(String.valueOf(points)));
                rrpli.setUserid(user.getUserid());
                rrplis.add(rrpli);
            }
            Collections.sort(rrplis, new ResearcherRankPeopleComparatorPoints());
            
        }

    }

    public void saveAction() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());

    }

    public ArrayList<ResearcherRankPeopleListitem> getRrplis() {
        return rrplis;
    }

    public void setRrplis(ArrayList<ResearcherRankPeopleListitem> rrplis) {
        this.rrplis = rrplis;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }
}