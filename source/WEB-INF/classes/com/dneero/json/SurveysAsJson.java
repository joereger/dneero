package com.dneero.json;

import com.dneero.cache.html.DbcacheexpirableCache;
import com.dneero.dao.Survey;
import com.dneero.util.Num;
import org.apache.log4j.Logger;
import org.json.simple.JSONValue;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: joereger
 * Date: Sep 5, 2010
 * Time: 7:42:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class SurveysAsJson extends HttpServlet {



    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("SurveysAsJson doPost()");
        String jsonString = "";


        //Put incoming surveyids being requested into a list
        ArrayList<Integer> surveyids = new ArrayList<Integer>();
        if (request.getParameterValues("surveyid")!=null && request.getParameterValues("surveyid").length>0){
            for (int i = 0; i < request.getParameterValues("surveyid").length; i++) {
                String s = request.getParameterValues("surveyid")[i];
                if (Num.isinteger(s)){
                    if (!surveyids.contains(Integer.parseInt(s))){
                        surveyids.add(Integer.parseInt(s));
                    }
                }
            }
        }

        //If we, you know, have surveyids
        if (surveyids.size()>0){

            //Build key for cache
            String surveyidsStringKey = "";
            for (Iterator it = surveyids.iterator(); it.hasNext(); ) {
                int i = (Integer)it.next();
                surveyidsStringKey = surveyidsStringKey + "-" + i;
            }

            //Cache
            String cacheKey = "surveyidsStringKey"+surveyidsStringKey;
            String cacheGroup = "SurveysAsJson";
            Object fromCache = DbcacheexpirableCache.get(cacheKey, cacheGroup);
            if (fromCache!=null){
                try{jsonString = (String)fromCache;}catch(Exception ex){logger.error("", ex);}
            } else {
                List l = new LinkedList();
                for (Iterator it = surveyids.iterator(); it.hasNext(); ) {
                    int i = (Integer)it.next();
                    Survey survey = Survey.get(i);
                    Map m = SurveyResultsAsJson.getSurveyJson(survey);
                    l.add(m);
                }
                //Now convert json to text
                jsonString = JSONValue.toJSONString(l);
                DbcacheexpirableCache.put(cacheKey, cacheGroup, jsonString, DbcacheexpirableCache.expireIn3Hrs());
            }


        }

        //Handle jsonp callback thing
        String jsonCallbackParam = request.getParameter("jsoncallback");
        String out = "";
        if (jsonCallbackParam!=null) {
            out = jsonCallbackParam + "(" + jsonString + ");";
        } else {
            out = jsonString;
        }

        //Send it on along now y'hear
        ServletOutputStream outStream = response.getOutputStream();
        response.setContentType("application/json");
        outStream.write(out.getBytes());
        outStream.close();

    }



    





}
