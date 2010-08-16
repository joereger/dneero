package com.dneero.htmluibeans;

import au.com.bytecode.opencsv.CSVWriter;
import com.dneero.dao.*;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentTypes;
import com.dneero.htmlui.Pagez;
import com.dneero.util.Time;
import com.dneero.util.Util;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Nov 18, 2006
 * Time: 9:06:22 AM
 */
public class ResearcherResultsAnswersCsv extends HttpServlet {

    private Survey survey;
    private String results;

    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        survey = Survey.get(Pagez.getUserSession().getCurrentSurveyid());
        if (com.dneero.util.Num.isinteger(Pagez.getRequest().getParameter("surveyid"))){
            Pagez.getUserSession().setCurrentSurveyid(Integer.parseInt(Pagez.getRequest().getParameter("surveyid")));
            survey = Survey.get((Integer.parseInt(Pagez.getRequest().getParameter("surveyid"))));
        }
        if (survey!=null){
            if (Pagez.getUserSession().getUser()!=null && survey.canEdit(Pagez.getUserSession().getUser())){
                int numberOfNonQuestionColumns = 5;
                results = "";
                String[][] array = new String[survey.getResponses().size() + 1][0];
                int currentRow = 0;
                //Column Headers
                String[] headerRow = new String[survey.getQuestions().size() + numberOfNonQuestionColumns];
                headerRow[0]= "USERID";
                headerRow[1]= "NAME";
                headerRow[2]= "NICKNAME";
                headerRow[3]= "RESPONSE DATE";
                for (Iterator<Question> iterator1 = survey.getQuestions().iterator(); iterator1.hasNext();) {
                    Question question = iterator1.next();
                    String colHeader = question.getQuestion().toUpperCase();
                    if (question.getIsuserquestion()){
                        colHeader = "USERQUESTION " + colHeader;
                    }
                    String[] tmp = new String[1];
                    tmp[0]= colHeader;
                    headerRow = Util.appendToEndOfStringArray(headerRow, tmp);
                }
                array[currentRow] = headerRow;
                currentRow++;
                //Iterate responses
                for (Iterator<Response> iterator = survey.getResponses().iterator(); iterator.hasNext();) {
                    Response resp = iterator.next();
                    //Choose how many cols this will have... later on this will get more complex and i'll have to call each component to get the sizing
                    String[] row = new String[survey.getQuestions().size() + numberOfNonQuestionColumns];
                    //Start out with the basic info for each resp
                    row[0]= String.valueOf(User.get(resp.getBloggerid()).getUserid());
                    row[1]= User.get(Blogger.get(resp.getBloggerid()).getUserid()).getName();
                    row[2]= User.get(Blogger.get(resp.getBloggerid()).getUserid()).getNickname();
                    row[3]= Time.dateformatcompactwithtime(Time.getCalFromDate(resp.getResponsedate()));
                    for (Iterator<Question> iterator1 = survey.getQuestions().iterator(); iterator1.hasNext();) {
                        Question question = iterator1.next();
                        Component component = ComponentTypes.getComponentByType(question.getComponenttype(), question, Blogger.get(resp.getBloggerid()));
                        //Append each question to the end of the row
                        row = Util.appendToEndOfStringArray(row, component.getCsvForResult());
                    }
                    array[currentRow] = row;
                    currentRow++;
                }
                //Convert a String[][] to a string representing a csv file
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
            }
        }
        //Make sure something gets output
        if (results==null || results.equals("")){
            results = "No data";
        }
        //Write out
        writeResponseOut(response);
    }

    public void writeResponseOut(HttpServletResponse response){
        Logger logger = Logger.getLogger(this.getClass().getName());




        //Now we create some variables we will use for writting the file to the response
        int read = 0;
        byte[] bytes = new byte[1024];


        //Now set the content type for our response, be sure to use the best suitable content type depending on your file
        //the content type presented here is ok for, lets say, text files and others (like  CSVs, PDFs)
        response.setContentType("application/csv");

        //This is another important attribute for the header of the response
        //Here fileName, is a String with the name that you will suggest as a name to save as
        //I use the same name as it is stored in the file system of the server.
        response.setHeader("Content-Disposition", "attachment;filename=\"" + "dNeeroResultAsCsv.txt" + "\"");

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
