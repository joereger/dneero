package com.dneero.htmluibeans;

import com.dneero.dao.*;
import com.dneero.ui.SurveyEnhancer;
import com.dneero.money.SurveyMoneyStatus;

import com.dneero.survey.servlet.SurveyJavascriptServlet;
import com.dneero.display.SurveyTakerDisplay;
import com.dneero.finders.SurveyCriteriaXML;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;

import java.io.Serializable;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class SysadminMassemailSend implements Serializable {

    private Massemail massemail;
    private String password;




    public void initBean(){
        if (com.dneero.util.Num.isinteger(Pagez.getRequest().getParameter("massemailid"))){
            massemail = Massemail.get(Integer.parseInt(Pagez.getRequest().getParameter("massemailid")));
        }

    }

    public String send() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (password.equals("pupper") && massemail.getStatus()==Massemail.STATUS_NEW){
            massemail.setDate(new Date());
            massemail.setStatus(Massemail.STATUS_PROCESSING);
            try{massemail.save();}catch(Exception ex){logger.error("",ex);}
            Pagez.getUserSession().setMessage("Mass email scheduled for send! Rock on!");
            Pagez.sendRedirect("/sysadmin/massemaillist.jsp");
            return "";
        } else {
            throw new ValidationException("Password fails!");
        }
    }


    public Massemail getMassemail() {
        return massemail;
    }

    public void setMassemail(Massemail massemail) {
        this.massemail = massemail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
