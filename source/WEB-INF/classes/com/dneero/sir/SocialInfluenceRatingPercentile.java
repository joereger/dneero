package com.dneero.sir;

import org.apache.log4j.Logger;
import com.dneero.dao.Blogger;
import com.dneero.scheduledjobs.SystemStats;

/**
 *
 */
public class SocialInfluenceRatingPercentile {


    public static int getRankingPercentile(Blogger blogger){
        return getPercentileOfRanking(SystemStats.getTotalbloggers(), blogger.getSocialinfluenceratingranking());
    }
    public static int getRankingPercentile90days(Blogger blogger){
        return getPercentileOfRanking(SystemStats.getTotalbloggers(), blogger.getSocialinfluenceratingranking90days());
    }


    public static int getRankingOfGivenPercentile(int max, int percentile){
        Logger logger = Logger.getLogger(SocialInfluenceRatingPercentile.class);
        Double percentileDbl = ((Integer)percentile).doubleValue();
        if (percentile==0){
            percentileDbl = .000001;    
        }
        int out = 1;
        try{
            double rnk = ((Integer)max).doubleValue() * ( percentileDbl / 100 ) ;
            out = (new Double(rnk)).intValue();
        } catch (Exception ex){
            logger.error("",ex);
        }
        return out;
    }

    public static int getPercentileOfRanking(int max, int ranking){
        Logger logger = Logger.getLogger(SocialInfluenceRatingPercentile.class);
        int out = 1;
        try{
            double rnkPct = (((Integer)ranking).doubleValue() / ((Integer)max).doubleValue()) * 100;
            out = (new Double(rnkPct)).intValue();
        } catch (Exception ex){
            logger.error("",ex);
        }
        return out;
    }
    

    





}
