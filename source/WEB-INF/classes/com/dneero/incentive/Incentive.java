package com.dneero.incentive;

import com.dneero.dao.Response;
import com.dneero.dao.Surveyincentive;

/**
 * User: Joe Reger Jr
 * Date: Jul 29, 2008
 * Time: 1:30:24 PM
 */
public interface Incentive {

    public int getID();
    public String  getSystemName();
    public double getResearcherCostPerResponse();
    public double getBloggerEarningsPerResponse();
    public double getEstimatedMaxValueOfEarnings();
    public void doImmediatelyAfterResponse(Response response);
    public String getInstructionsAfterResponse(Response response);
    public void doAwardIncentive(Response response);
    public void doRemoveIncentive(Response response);
    public String getShortSummary();
    public String getFullSummary();
    public String getFullSummaryHtml();
    public String getInstructionsAfterAward(Response response);
    public Surveyincentive getSurveyincentive();

}
