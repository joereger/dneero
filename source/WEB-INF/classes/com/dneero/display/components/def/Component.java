package com.dneero.display.components.def;

import com.dneero.dao.Response;
import com.dneero.display.SurveyResponseParser;

import javax.servlet.http.HttpServletRequest;

/**
 * User: Joe Reger Jr
 * Date: Jul 6, 2006
 * Time: 10:17:58 AM
 */
public interface Component {


    public String getName();
    public int getID();
    public String getHtmlForInput();
    public String getHtmlForDisplay(Response response);
    public String getHtmlForResult();
    public String getHtmlForResultDetail();
    public void validateAnswer(SurveyResponseParser srp) throws ComponentException;
    public void processAnswer(SurveyResponseParser srp, Response response) throws ComponentException;
    public int columnsInCsvOutput();
    public String[] getCsvForResult();

}
