package com.dneero.money;

import com.dneero.dao.User;
import com.dneero.money.paypal.CallerFactory;
import com.dneero.util.Str;
import com.dneero.util.Time;
import com.dneero.util.Num;
import com.dneero.email.EmailTemplateProcessor;
import com.dneero.systemprops.SystemProperty;
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


    public PaymentMethodPayPal(User user, double amt){
        super(user, amt);
    }


    public void giveUserThisAmt() {

        if (SystemProperty.getProp(SystemProperty.PROP_PAYPALENABLED)==null || !SystemProperty.getProp(SystemProperty.PROP_PAYPALENABLED).equals("1")){
            notes =  notes + "PayPal Temporarily Down: "+user.getUserid()+" : amt="+amt;
            issuccessful = false;
            logger.debug("PayPal Temporarily Disabled: would have paid userid="+user.getUserid()+" amt="+amt);
            EmailTemplateProcessor.sendGenericEmail("joe@joereger.com", "dNeero PayPal Test Transaction", "PayPal Temporarily Disabled: would have paid userid="+user.getUserid()+"\n"+"email="+user.getEmail()+"\n"+"name="+user.getFirstname()+" "+user.getLastname()+"\n"+" amt="+amt);
            return;
        }

        if (amt<=0){
            logger.error("PayPal can not charge people money (negative amt sent to giveUserThisAmt()) userid="+user.getUserid()+" amt="+amt);
            return;
        }
        logger.debug("---------- PayPal Call Start ----------");
        StringBuffer debug = new StringBuffer();
        debug.append("PaymentMethodPayPal run at: "+Time.dateformatcompactwithtime(Time.nowInUserTimezone("EST"))+"\n\n");
        try{
            CallerFactory callerFactory = new CallerFactory();
            CallerServices caller = callerFactory.getCaller();

            MassPayRequestType request = new MassPayRequestType();
            MassPayRequestItemType mprit = new MassPayRequestItemType();

            mprit.setReceiverEmail(user.getPaymethodpaypaladdress());
            debug.append("user.getPaymethodpaypaladdress()="+user.getPaymethodpaypaladdress()+"\n");

            BasicAmountType orderTotal = new BasicAmountType();
            orderTotal.setCurrencyID(CurrencyCodeType.USD);
            String amtAsStr = Str.formatForFinancialTransactionsNoCommas(amt);
            if (amtAsStr.equals("")){
                logger.error("amtAsStr is blank... setting to 0: amt="+amt+" : Str.formatForFinancialTransactionsNoCommas(amt)="+Str.formatForFinancialTransactionsNoCommas(amt));
                amtAsStr = "0";
            }
            orderTotal.set_value(amtAsStr);
            mprit.setAmount(orderTotal);
            debug.append("amtAsStr="+amtAsStr+"\n");

            MassPayRequestItemType[] mpArray = new MassPayRequestItemType[1];
            mpArray[0] = mprit;
            request.setMassPayItem(mpArray);

            try{
                MassPayResponseType response = (MassPayResponseType) caller.call("MassPay", request);
                logger.debug("Operation Ack: " + response.getAck());
                debug.append("response.getAck()="+response.getAck()+"\n");
                logger.debug("getCorrelationID(): " + response.getCorrelationID());
                debug.append("response.getCorrelationID()="+response.getCorrelationID()+"\n");
                correlationid = response.getCorrelationID();
                transactionid = "";
                ErrorType[] errors = response.getErrors();
                if (errors!=null){
                    issuccessful = true;
                    for (int i = 0; i < errors.length; i++) {
                        ErrorType error = errors[i];
                        debug.append("error["+i+"]="+error.getLongMessage()+"\n");
                        debug.append("error.getSeverityCode()="+error.getSeverityCode()+"\n");
                        logger.debug("Error "+i+": userid="+user.getUserid()+" : amtAsStr="+amtAsStr+" : "+error.getLongMessage());
                        if (error.getSeverityCode()==SeverityCodeType.Error){
                            logger.error("PayPal Error: userid="+user.getUserid()+" : amtAsStr="+amtAsStr+" :"+error.getLongMessage());
                            notes =  notes + "PayPal Error: "+user.getUserid()+" : amtAsStr="+amtAsStr+" : "+error.getLongMessage()+" ";
                            issuccessful = false;
                        } else if (error.getSeverityCode()==SeverityCodeType.Warning){
                            logger.error("PayPal Warning: userid="+user.getUserid()+" : amt="+amt+" :"+error.getLongMessage());
                            notes =  notes + "PayPal Warning: userid="+user.getUserid()+" : amt="+amt+" : "+error.getLongMessage()+" ";
                        }  else if (error.getSeverityCode()==SeverityCodeType.CustomCode){
                            logger.error("PayPal Custom Code Error: userid="+user.getUserid()+" : amt="+amt+" :"+error.getLongMessage());
                            notes =  notes + "PayPal Custom Code Error: userid="+user.getUserid()+" : amt="+amt+" : "+error.getLongMessage()+" ";
                        }
                    }
                } else {
                    issuccessful = true;
                    debug.append("response.getErrors()==null"+"\n");
                }
            } catch (PayPalException ppex){
                logger.error(ppex);
                notes = "An internal server error occurred at "+ Time.dateformatcompactwithtime(Calendar.getInstance())+".  No money was exchanged.";
                issuccessful = false;
                debug.append("ppex.getMessage()="+ppex.getMessage()+"\n");
            } catch (Exception ex){
                logger.error("",ex);
                notes = "An internal server error occurred at "+ Time.dateformatcompactwithtime(Calendar.getInstance())+".  No money was exchanged.";
                issuccessful = false;
                debug.append("ex.getMessage()="+ex.getMessage()+"\n");
            }
        } catch (Exception ex){
            logger.error("",ex);
            notes = "An internal server error occurred at "+ Time.dateformatcompactwithtime(Calendar.getInstance())+".  No money was exchanged.";
            issuccessful = false;
            debug.append("ex.getMessage()="+ex.getMessage()+"\n");
        }
        debug.append("End PayPal run."+"\n");
        EmailTemplateProcessor.sendGenericEmail("joe@joereger.com", "dNeero PayPal Transaction", debug.toString());
        logger.debug("---------- PayPal Call End ----------");
    }




}
