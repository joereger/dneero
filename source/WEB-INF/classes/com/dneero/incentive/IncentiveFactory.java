package com.dneero.incentive;

import com.dneero.dao.Surveyincentive;

/**
 * User: Joe Reger Jr
 * Date: Jul 29, 2008
 * Time: 3:28:58 PM
 */
public class IncentiveFactory {

    public static Incentive getById(int id, Surveyincentive surveyincentive){
        if (id==IncentiveCash.ID){
            return new IncentiveCash(surveyincentive);
        }
        return null;
    }

    public static Incentive getDefaultIncentive(){
        return new IncentiveCash(null);    
    }

}
