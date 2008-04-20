package com.dneero.rank;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentTypes;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Apr 18, 2008
 * Time: 10:40:58 AM
 */
public class RankForQuestion {

    public static void processAndSave(Question question, Response response){
        Blogger blogger = Blogger.get(response.getBloggerid());
        Component qComp = ComponentTypes.getComponentByID(question.getComponenttype(), question, blogger);
        if (qComp.supportsRank()){
            //Find rankings for this question
            ArrayList<Integer> rankids = new ArrayList<Integer>();
            List<Rankquestion> rankquestions = HibernateUtil.getSession().createCriteria(Rankquestion.class)
                                   .add(Restrictions.eq("questionid", question.getQuestionid()))
                                   .setCacheable(true)
                                   .list();
            for (Iterator<Rankquestion> rankquestionIterator = rankquestions.iterator(); rankquestionIterator.hasNext();){
                Rankquestion rankquestion = rankquestionIterator.next();
                if (!rankids.contains(rankquestion.getRankid())){
                    rankids.add(rankquestion.getRankid());
                }
            }
            //Iterate rankids that need to be processed
            for (Iterator it = rankids.iterator(); it.hasNext(); ) {
                Integer rankid = (Integer)it.next();
                Rank rank = Rank.get(rankid);
                //Save/update the ranking
                ArrayList<RankUnit> rankUnits = qComp.calculateRankPoints(rank, response);
                for (Iterator<RankUnit> rankUnitIterator = rankUnits.iterator(); rankUnitIterator.hasNext();) {
                    RankUnit rankUnit = rankUnitIterator.next();
                    //Handle Storage
                    RankUnitStorage.store(rank, response, rankUnit);
                }
            }
        }
    }

    public static void processAndSave(Question question){
        Logger logger = Logger.getLogger(RankForQuestion.class);
        //Find unique responses
        ArrayList<Integer> responseids = new ArrayList<Integer>();
        List<Questionresponse> questionresponses = HibernateUtil.getSession().createCriteria(Questionresponse.class)
                                           .add(Restrictions.eq("questionid", question.getQuestionid()))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Questionresponse> questionresponseIterator = questionresponses.iterator(); questionresponseIterator.hasNext();){
            Questionresponse questionresponse = questionresponseIterator.next();
            if (!responseids.contains(questionresponse.getResponseid())){
                responseids.add(questionresponse.getResponseid());
            }
        }
        //Iterate all unique responses
        for (Iterator it = responseids.iterator(); it.hasNext(); ) {
            Integer responseid = (Integer)it.next();
            Response response = Response.get(responseid);
            logger.debug("iterating responses... found responseid="+response.getResponseid());
            processAndSave(question, response);
        }
    }

    public static ArrayList<RankUnit> calculatePointsForSpecificRank(Rank rank, Question question, Response response){
        Blogger blogger = Blogger.get(response.getBloggerid());
        Component qComp = ComponentTypes.getComponentByID(question.getComponenttype(), question, blogger);
        ArrayList<RankUnit> rankUnits = qComp.calculateRankPoints(rank, response);
        return rankUnits;
    }






}
