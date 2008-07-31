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
    public String  getName();
    public double getResearcherCostPerResponse();
    public double getBloggerEarningsPerResponse();
    public void awardIncentive(Response response);
    public void undoIncentiveForResponse(Response response);
    public String getShortSummary();
    public String getFullSummary();
    public String getInstructions();
    public Surveyincentive getSurveyincentive();

}
