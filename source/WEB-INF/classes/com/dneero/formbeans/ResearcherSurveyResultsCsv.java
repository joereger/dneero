package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Jsf;
import com.dneero.util.Time;
import com.dneero.util.Util;
import com.dneero.session.UserSession;
import com.dneero.display.SurveyResultsDisplay;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentTypes;

import javax.faces.context.FacesContext;
import java.util.Iterator;
import java.util.List;
import java.io.FileWriter;
import java.io.StringWriter;

import au.com.bytecode.opencsv.CSVWriter;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherSurveyResultsCsv {

    Logger logger = Logger.getLogger(this.getClass().getName());

    private Survey survey;
    private String results;

    public ResearcherSurveyResultsCsv(){
        logger.debug("Instanciating object.");
        loadSurvey(Jsf.getUserSession().getCurrentSurveyid());
    }


    public void loadSurvey(int surveyid){
        logger.debug("loadSurvey called");
        Survey survey = Survey.get(surveyid);
        if (survey!=null){
            if (Jsf.getUserSession().getUser()!=null && survey.canEdit(Jsf.getUserSession().getUser())){
                results = "";
                String[][] array = new String[survey.getResponses().size()][0];
                int arrayindex = 0;
                for (Iterator<Response> iterator = survey.getResponses().iterator(); iterator.hasNext();) {
                    Response response = iterator.next();
                    //Choose how many cols this will have... later on this will get more complex and i'll have to call each component to get the sizing
                    String[] row = new String[survey.getQuestions().size() + 2];
                    //Start out with the basic info for each response
                    row[0]=User.get(Blogger.get(response.getBloggerid()).getUserid()).getFirstname() + " " + User.get(Blogger.get(response.getBloggerid()).getUserid()).getFirstname();
                    row[1]= Time.dateformatcompactwithtime(Time.getCalFromDate(response.getResponsedate()));
                    for (Iterator<Question> iterator1 = survey.getQuestions().iterator(); iterator1.hasNext();) {
                        Question question = iterator1.next();
                        Component component = ComponentTypes.getComponentByID(question.getComponenttype(), question, Blogger.get(response.getBloggerid()));
                        //Append each question to the end of the row
                        row = Util.appendToEndOfStringArray(row, component.getCsvForResult());
                    }
                    array[arrayindex] = row;
                    arrayindex = arrayindex + 1;
                }
                //Now I need to convert a String[][] to a string representing a csv file
                try{
                    StringWriter sw = new StringWriter();
                    CSVWriter writer = new CSVWriter(sw, '\t');
                    for (int i = 0; i < array.length; i++) {
                        String[] strings = array[i];
                        writer.writeNext(strings);
                    }
                    writer.close();
                    results = sw.toString();
                } catch (Exception ex){
                    logger.error(ex);
                }
            }
        }
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }
}
