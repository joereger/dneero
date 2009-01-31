package com.dneero.incentivetwit;

import com.dneero.dao.Response;
import com.dneero.dao.Surveyincentive;
import com.dneero.dao.Twitanswer;
import com.dneero.dao.Twitaskincentive;

/**
 * User: Joe Reger Jr
 * Date: Jul 29, 2008
 * Time: 1:30:24 PM
 */
public interface Incentivetwit {

    public int getID();
    public String  getSystemName();
    public double getResearcherCostPerResponse();
    public double getBloggerEarningsPerResponse();
    public double getEstimatedMaxValueOfEarnings();
    public void doImmediatelyAfterResponse(Twitanswer twitanswer);
    public String getInstructionsAfterResponse(Twitanswer twitanswer);
    public void doAwardIncentive(Twitanswer twitanswer);
    public void doRemoveIncentive(Twitanswer twitanswer);
    public String getShortSummary();
    public String getFullSummary();
    public String getFullSummaryHtml();
    public String getInstructionsAfterAward(Twitanswer twitanswer);
    public Twitaskincentive getTwitaskincentive();

}