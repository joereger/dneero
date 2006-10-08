package com.dneero.paypal;

import com.dneero.dao.User;
import com.dneero.util.Str;
import com.paypal.sdk.services.CallerServices;
import com.paypal.soap.api.*;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Sep 25, 2006
 * Time: 9:52:40 AM
 */
public class Pay {

    public static void pay(String paypalAddress, double amt){
        Logger logger = Logger.getLogger(Charge.class);
        CallerFactory callerFactory = new CallerFactory();
        CallerServices caller = callerFactory.getCaller();

        MassPayRequestType request = new MassPayRequestType();
        MassPayRequestItemType mprit = new MassPayRequestItemType();

        mprit.setReceiverEmail(paypalAddress);

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
            ErrorType[] errors = response.getErrors();
            if (errors!=null){
                for (int i = 0; i < errors.length; i++) {
                    ErrorType error = errors[i];
                    logger.debug("Error "+i);
                    logger.debug(error.getLongMessage());
                }
            }
            logger.debug("---------- Results ----------");
            logger.debug("getCorrelationID(): " + response.getCorrelationID());
            //logger.debug("Gross Amount: " + response.getDoExpressCheckoutPaymentResponseDetails().getPaymentInfo().getGrossAmount().getCurrencyID()+ " " + response.getDoExpressCheckoutPaymentResponseDetails().getPaymentInfo().getGrossAmount().get_value());
        } catch (Exception ex){
            logger.error(ex);
        }
    }

}
