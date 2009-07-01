package com.dneero.display.components.def;

import com.dneero.dao.Questionresponse;
import com.dneero.dao.Rank;
import com.dneero.dao.Response;
import com.dneero.dao.Question;
import com.dneero.display.SurveyResponseParser;
import com.dneero.rank.RankUnit;

import java.util.List;
import java.util.ArrayList;

import org.jdom.Element;

/**
 * User: Joe Reger Jr
 * Date: Jul 6, 2006
 * Time: 10:17:58 AM
 */
public interface Component {


    public String getName();
    public Question getQuestion();
    public int getID();
    public String getHtmlForInput(Response response);
    public String getHtmlForDisplay(Response response);
    public String getHtmlForResult(List<Questionresponse> questionresponses);
    public String getHtmlForResultDetail(List<Questionresponse> questionresponses);
    public void validateAnswer(SurveyResponseParser srp) throws ComponentException;
    public void processAnswer(SurveyResponseParser srp, Response response) throws ComponentException;
    public int columnsInCsvOutput();
    public String[] getCsvForResult();
    public boolean supportsRank();
    public ArrayList<RankUnit> calculateRankPoints(Rank rank, Response response);
    public Element getXmlForDisplay(Response response);


}
