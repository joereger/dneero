package com.dneero.incentivetwit;


import com.dneero.dao.Twitaskincentive;


/**
 * User: Joe Reger Jr
 * Date: Jul 29, 2008
 * Time: 3:28:58 PM
 */
public class IncentivetwitFactory {

    public static Incentivetwit getBySurveyIncentive(Twitaskincentive twitaskincentive){
        return getById(twitaskincentive.getType(), twitaskincentive);
    }

    public static Incentivetwit getById(int id, Twitaskincentive twitaskincentive){
        if (id==IncentivetwitCash.ID){
            return new IncentivetwitCash(twitaskincentive);
        } else if (id==IncentivetwitCoupon.ID){
            return new IncentivetwitCoupon(twitaskincentive);
        } else if (id==IncentivetwitNone.ID){
            return new IncentivetwitNone(twitaskincentive);
        }
        return null;
    }


    public static Incentivetwit getDefaultIncentive(){
        return new IncentivetwitNone(null);
    }

}