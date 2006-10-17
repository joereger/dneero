package com.dneero.money;

import com.dneero.dao.User;
import com.dneero.money.paypal.CallerFactory;
import com.dneero.util.Str;
import com.dneero.util.Time;
import com.paypal.sdk.services.CallerServices;
import com.paypal.sdk.exceptions.PayPalException;
import com.paypal.soap.api.*;
import org.apache.log4j.Logger;

import java.util.Calendar;

/**
 * User: Joe Reger Jr
 * Date: Oct 11, 2006
 * Time: 1:12:04 PM
 */
public class PaymentMethodPayPal extends PaymentMethodBase implements PaymentMethod  {
   Logger logger = Logger.getLogger(this.getClass().getName());

    public void pay(User user, double amt) {
        try{
            CallerFactory callerFactory = new CallerFactory();
            CallerServices caller = callerFactory.getCaller();

            MassPayRequestType request = new MassPayRequestType();
            MassPayRequestItemType mprit = new MassPayRequestItemType();

            mprit.setReceiverEmail(user.getPaymethodpaypaladdress());

            BasicAmountType orderTotal = new BasicAmountType();
            orderTotal.setCurrencyID(CurrencyCodeType.USD);
            orderTotal.set_value(Str.formatForMoney(amt));
            mprit.setAmount(orderTotal);

            MassPayRequestItemType[] mpArray = new MassPayRequestItemType[1];
            mpArray[0] = mprit;
            request.setMassPayItem(mpArray);

            try{
                MassPayResponseType response = (MassPayResponseType) caller.call("MassPay", request);
                logger.debug("Operation Ack: " + response.getAck());
                logger.debug("---------- Results ----------");
                logger.debug("getCorrelationID(): " + response.getCorrelationID());
                correlationid = response.getCorrelationID();
                transactionid = "";
                ErrorType[] errors = response.getErrors();
                if (errors!=null){
                    issuccessful = true;
                    for (int i = 0; i < errors.length; i++) {
                        ErrorType error = errors[i];
                        logger.debug("Error "+i+": "+error.getLongMessage());
                        if (error.getSeverityCode()==SeverityCodeType.Error){
                            logger.error("PayPal Error: userid="+user.getUserid()+" :"+error.getLongMessage());
                            notes =  notes + "PayPal Error: "+error.getLongMessage()+" ";
                            issuccessful = false;
                        } else if (error.getSeverityCode()==SeverityCodeType.Warning){
                            logger.error("PayPal Warning: userid="+user.getUserid()+" :"+error.getLongMessage());
                            notes =  notes + "PayPal Warning: "+error.getLongMessage()+" ";
                        }  else if (error.getSeverityCode()==SeverityCodeType.CustomCode){
                            logger.error("PayPal Custom Code Error: userid="+user.getUserid()+" :"+error.getLongMessage());
                            notes =  notes + "PayPal Custom Code Error: "+error.getLongMessage()+" ";
                        }
                    }
                } else {
                    issuccessful = true;
                }
            } catch (PayPalException ppex){
                logger.error(ppex);
                notes = "An internal server error occurred at "+ Time.dateformatcompactwithtime(Calendar.getInstance())+".  No money was exchanged.";
                issuccessful = false;
            } catch (Exception ex){
                logger.error(ex);
                notes = "An internal server error occurred at "+ Time.dateformatcompactwithtime(Calendar.getInstance())+".  No money was exchanged.";
                issuccessful = false;
            }
        } catch (Exception ex){
            logger.error(ex);
            notes = "An internal server error occurred at "+ Time.dateformatcompactwithtime(Calendar.getInstance())+".  No money was exchanged.";
            issuccessful = false;
        }
    }

    public void charge(User user, double amt) {
        logger.error("Can't charge via PayPal... yet. userid="+user.getUserid()+" amt="+amt);
        notes = "Can't charge money to a PayPal account.";
        issuccessful = false;
    }


}
