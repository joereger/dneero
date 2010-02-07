package com.dneero.htmluibeans;

import com.dneero.htmlui.ValidationException;
import com.dneero.htmlui.Pagez;
import com.dneero.money.paypal.CallerFactory;
import com.dneero.systemprops.BaseUrl;
import com.dneero.util.Time;
import com.dneero.email.EmailTemplateProcessor;
import com.paypal.sdk.services.CallerServices;
import com.paypal.sdk.exceptions.PayPalException;
import com.paypal.soap.api.*;

import java.io.Serializable;
import java.util.Calendar;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Sep 21, 2006
 * Time: 8:47:39 AM
 */
public class PublicTshirtConfirm implements Serializable {

    private String token = "";
    private String payerID = "";
    private boolean ispaysuccess = false;

    public PublicTshirtConfirm(){
    }



    public void initBean(){


    }

    public void buy() throws ValidationException {

        Logger logger = Logger.getLogger(this.getClass().getName());
        if (Pagez.getRequest().getParameter("token")!=null && !Pagez.getRequest().getParameter("token").equals("")){
            token = Pagez.getRequest().getParameter("token");
        }
        if (Pagez.getRequest().getParameter("PayerID")!=null && !Pagez.getRequest().getParameter("PayerID").equals("")){
            payerID = Pagez.getRequest().getParameter("PayerID");
        }

        try {
            String notes = "";
            StringBuffer debug = new StringBuffer();
            String correlationid = "";
            String transactionid = "";
            boolean issuccessful = false;


            CallerFactory callerFactory = new CallerFactory();
            CallerServices caller = callerFactory.getCaller();

            GetExpressCheckoutDetailsRequestType xcorequest = new GetExpressCheckoutDetailsRequestType();

            xcorequest.setToken(token);

            try {
                GetExpressCheckoutDetailsResponseType xcresponse = (GetExpressCheckoutDetailsResponseType) caller.call("GetExpressCheckoutDetails", xcorequest);
                logger.debug("Operation Ack: " + xcresponse.getAck());
                logger.debug("getCorrelationID(): " + xcresponse.getCorrelationID());


                GetExpressCheckoutDetailsResponseDetailsType details = xcresponse.getGetExpressCheckoutDetailsResponseDetails();
                AddressType address = details.getBillingAddress();

                logger.debug(address.toString());
                StringBuffer body = new StringBuffer();
                body.append(address.getName());
                body.append("-");
                body.append(address.getPhone());
                body.append("-");
                body.append(address.toString());
                EmailTemplateProcessor.sendGenericEmail("regerj@gmail.com", "SEND dNEERO SHIRT TO", body.toString());

                correlationid = xcresponse.getCorrelationID();
                transactionid = "";
                ErrorType[] errors = xcresponse.getErrors();
                if (errors != null) {
                    issuccessful = true;
                    for (int i = 0; i < errors.length; i++) {
                        ErrorType error = errors[i];
                        debug.append("error[" + i + "]=" + error.getLongMessage() + "<br/>\n");
                        debug.append("error.getSeverityCode()=" + error.getSeverityCode() + "<br/>\n");
                        if (error.getSeverityCode() == SeverityCodeType.Error) {
                            logger.warn("PayPal Error::" + error.getLongMessage());
                            notes = notes + "PayPal Error: " + error.getLongMessage() + " ";
                            issuccessful = false;
                        } else if (error.getSeverityCode() == SeverityCodeType.Warning) {
                            logger.warn("PayPal Warning:" + error.getLongMessage());
                            notes = notes + "PayPal Warning: " + error.getLongMessage() + " ";
                        } else if (error.getSeverityCode() == SeverityCodeType.CustomCode) {
                            logger.warn("PayPal Custom Code Error:" + error.getLongMessage());
                            notes = notes + "PayPal Custom Code Error: " + error.getLongMessage() + " ";
                        }
                    }
                } else {
                    issuccessful = true;
                    debug.append("xcresponse.getErrors()==null" + "<br/>\n");
                }
            } catch (PayPalException ppex) {
                logger.error(ppex);
                notes = "An internal server error occurred at " + Time.dateformatcompactwithtime(Calendar.getInstance()) + ".  No money was exchanged.";
                issuccessful = false;
                debug.append("ppex.getMessage()=" + ppex.getMessage() + "<br/>\n");
            } catch (Exception ex) {
                logger.error("", ex);
                notes = "An internal server error occurred at " + Time.dateformatcompactwithtime(Calendar.getInstance()) + ".  No money was exchanged.";
                issuccessful = false;
                debug.append("ex.getMessage()=" + ex.getMessage() + "<br/>\n");
            }
        } catch (Throwable e) {
            logger.error("", e);
        }

    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPayerID() {
        return payerID;
    }

    public void setPayerID(String payerID) {
        this.payerID = payerID;
    }

    public boolean getIspaysuccess() {
        return ispaysuccess;
    }

    public void setIspaysuccess(boolean ispaysuccess) {
        this.ispaysuccess = ispaysuccess;
    }
}
