package com.dneero.htmluibeans;

import com.dneero.dao.Blogpost;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.ValidationException;
import com.dneero.htmlui.Pagez;
import com.dneero.util.Time;
import com.dneero.money.paypal.CallerFactory;
import com.dneero.systemprops.BaseUrl;
import com.paypal.soap.api.*;
import com.paypal.sdk.exceptions.PayPalException;
import com.paypal.sdk.services.CallerServices;

import java.io.Serializable;
import java.util.List;
import java.util.Calendar;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Sep 21, 2006
 * Time: 8:47:39 AM
 */
public class PublicTshirt implements Serializable {

    private String token = "";

    public PublicTshirt(){
    }



    public void initBean(){
         
    }

    public void buy() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());

        try {
            String notes = "";
            StringBuffer debug = new StringBuffer();
            String correlationid = "";
            String transactionid = "";
            boolean issuccessful = false;


            CallerFactory callerFactory = new CallerFactory();
            CallerServices caller = callerFactory.getCaller();

            SetExpressCheckoutRequestType xcorequest = new SetExpressCheckoutRequestType();
            SetExpressCheckoutRequestDetailsType details = new SetExpressCheckoutRequestDetailsType();

            AddressType address = new AddressType();
            address.setName("dNeero User");
            details.setAddress(address);

            details.setCancelURL(BaseUrl.get(false)+"tshirt.jsp");

            BasicAmountType amount = new BasicAmountType();
            amount.set_value("350.00");
            amount.setCurrencyID(CurrencyCodeType.USD);
            details.setOrderTotal(amount);

            details.setReturnURL(BaseUrl.get(false)+"tshirt-confirm.jsp?");

            xcorequest.setSetExpressCheckoutRequestDetails(details);

            try {
                SetExpressCheckoutResponseType xcresponse = (SetExpressCheckoutResponseType) caller.call("SetExpressCheckout", xcorequest);
                logger.debug("Operation Ack: " + xcresponse.getAck());
                logger.debug("getCorrelationID(): " + xcresponse.getCorrelationID());
                token = xcresponse.getToken();
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
}
