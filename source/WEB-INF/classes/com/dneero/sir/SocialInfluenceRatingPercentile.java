package com.dneero.sir;

import org.apache.log4j.Logger;
import com.dneero.dao.Blogger;
import com.dneero.dao.User;
import com.dneero.scheduledjobs.SystemStats;

/**
 *
 */
public class SocialInfluenceRatingPercentile {


    public static int getRankingPercentile(User user){
        return getPercentileOfRanking(SystemStats.getTotalusers(), user.getSirrank());
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
            double rnkPct = (new Double(ranking) / new Double(max)) * 100;
            out = (new Double(rnkPct)).intValue();
        } catch (Exception ex){
            logger.error("",ex);
        }
        return out;
    }
    

    





}
