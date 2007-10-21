package com.dneero.formbeans;

import org.apache.log4j.Logger;
import com.dneero.dao.*;
import com.dneero.util.Jsf;
import com.dneero.util.Time;
import com.dneero.util.Util;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentTypes;

import java.util.Iterator;
import java.io.*;

import au.com.bytecode.opencsv.CSVWriter;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

/**
 * User: Joe Reger Jr
 * Date: Nov 18, 2006
 * Time: 9:06:22 AM
 */
public class ResearcherResultsAnswersCsv implements Serializable {

    private Survey survey;
    private String results;

    public ResearcherResultsAnswersCsv(){

    }

    public String beginView(){
        load();
        return "researcherresultsanswerscsv";
    }

    private void load(){
        loadSurvey(Jsf.getUserSession().getCurrentSurveyid());
    }

    public void loadSurvey(int surveyid){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("loadSurvey called");
        survey = Survey.get(surveyid);
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
                    row[0]= User.get(Blogger.get(response.getBloggerid()).getUserid()).getFirstname() + " " + User.get(Blogger.get(response.getBloggerid()).getUserid()).getLastname();
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
                    logger.error("",ex);
                }
                //Make sure something gets output
                if (results==null || results.equals("")){
                    results = "No data";
                }
            }
        }
    }

    public void getCsv(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        FacesContext context = FacesContext.getCurrentInstance();

        //Then we have to get the Response to write our file to
        HttpServletResponse response = (HttpServletResponse) context.getExternalContext().getResponse();

        //Now we create some variables we will use for writting the file to the response
        int read = 0;
        byte[] bytes = new byte[1024];


        //Now set the content type for our response, be sure to use the best suitable content type depending on your file
        //the content type presented here is ok for, lets say, text files and others (like  CSVs, PDFs)
        response.setContentType("application/csv");

        //This is another important attribute for the header of the response
        //Here fileName, is a String with the name that you will suggest as a name to save as
        //I use the same name as it is stored in the file system of the server.
        response.setHeader("Content-Disposition", "attachment;filename=\"" + "dNeeroSurveyResultAsCsv.txt" + "\"");

        try{
            //First we load the file in our InputStream
            ByteArrayInputStream fis = new ByteArrayInputStream(results.getBytes());
            OutputStream os = response.getOutputStream();

            //While there are still bytes in the file, read them and write them to our OutputStream
            while((read = fis.read(bytes)) != -1){
                os.write(bytes,0,read);
            }

            //Clean resources
            os.flush();
            os.close();
        } catch (Exception ex){
            logger.error("",ex);
        }

        //This option isn't quite necessary, It worked for me with or without it
        FacesContext.getCurrentInstance().responseComplete();
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
