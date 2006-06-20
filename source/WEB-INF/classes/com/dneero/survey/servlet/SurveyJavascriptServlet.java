package com.dneero.survey.servlet;

import com.dneero.dao.Offer;
import com.dneero.dao.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;
import java.io.IOException;

/**
 * User: Joe Reger Jr
 * Date: Jun 19, 2006
 * Time: 10:31:40 AM
 */
public class SurveyJavascriptServlet extends HttpServlet {

    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Offer offer = null;
        if (request.getParameter("offerid")!=null && com.dneero.util.Num.isinteger(request.getParameter("offerid"))){
            offer = Offer.get(Integer.parseInt(request.getParameter("offerid")));
        }

        User user = null;
        if (request.getParameter("userid")!=null && com.dneero.util.Num.isinteger(request.getParameter("userid"))){
            user = User.get(Integer.parseInt(request.getParameter("userid")));
        }

        if (offer!=null && user!=null){
            


        }



    }


}
