package com.dneero.money;

import org.apache.log4j.Logger;
import com.dneero.dao.User;
import com.dneero.dao.Creditcard;
import com.dneero.money.paypal.CallerFactory;
import com.dneero.util.Time;
import com.dneero.util.Str;
import com.paypal.soap.api.*;
import com.paypal.sdk.exceptions.PayPalException;

import java.util.Calendar;

/**
 * User: Joe Reger Jr
 * Date: Oct 11, 2006
 * Time: 1:12:04 PM
 */
public class PaymentMethodCreditCard extends PaymentMethodBase implements PaymentMethod {
   Logger logger = Logger.getLogger(this.getClass().getName());

    public void pay(User user, double amt) {
        charge(user, (-1)*amt);
    }

    public void charge(User user, double amt) {
        try{
            if (user.getPaymethodcreditcardid()>0){
                Creditcard cc = Creditcard.get(user.getPaymethodcreditcardid());

                DoDirectPaymentRequestType request = new DoDirectPaymentRequestType();
                DoDirectPaymentRequestDetailsType details = new DoDirectPaymentRequestDetailsType();

                CreditCardDetailsType creditCard = new CreditCardDetailsType();
                creditCard.setCreditCardNumber(cc.getCcnum());
                if (cc.getCctype()==Creditcard.CREDITCARDTYPE_VISA){
                    creditCard.setCreditCardType(CreditCardTypeType.Visa);
                } else if (cc.getCctype()==Creditcard.CREDITCARDTYPE_MASTERCARD){
                    creditCard.setCreditCardType(CreditCardTypeType.MasterCard);
                } else if (cc.getCctype()==Creditcard.CREDITCARDTYPE_AMEX){
                    creditCard.setCreditCardType(CreditCardTypeType.Amex);
                } else if (cc.getCctype()==Creditcard.CREDITCARDTYPE_DISCOVER){
                    creditCard.setCreditCardType(CreditCardTypeType.Discover);
                }

                creditCard.setCVV2(cc.getCvv2());
                creditCard.setExpMonth(cc.getCcexpmo());
                creditCard.setExpYear(cc.getCcexpyear());

                PayerInfoType cardOwner = new PayerInfoType();
                cardOwner.setPayerCountry(CountryCodeType.US);

                AddressType address = new AddressType();
                address.setPostalCode(cc.getPostalcode());
                address.setStateOrProvince(cc.getState());
                address.setStreet1(cc.getStreet());
                address.setCountryName("US");
                address.setCountry(CountryCodeType.US);
                address.setCityName(cc.getCity());
                cardOwner.setAddress(address);

                PersonNameType payerName = new PersonNameType();
                payerName.setFirstName(cc.getFirstname());
                payerName.setLastName(cc.getLastname());
                cardOwner.setPayerName(payerName);

                creditCard.setCardOwner(cardOwner);
                details.setCreditCard(creditCard);

                details.setIPAddress(cc.getIpaddress());
                details.setMerchantSessionId(cc.getMerchantsessionid());
                details.setPaymentAction(PaymentActionCodeType.Sale);

                PaymentDetailsType payment = new PaymentDetailsType();

                BasicAmountType orderTotal = new BasicAmountType();
                orderTotal.setCurrencyID(CurrencyCodeType.USD);
                orderTotal.set_value(Str.formatForMoney(amt));
                payment.setOrderTotal(orderTotal);

                details.setPaymentDetails(payment);
                request.setDoDirectPaymentRequestDetails(details);

                CallerFactory cf = new CallerFactory();

                try{
                    DoDirectPaymentResponseType response = (DoDirectPaymentResponseType) cf.getCaller().call("DoDirectPayment", request);
                    logger.debug("Operation Ack: " + response.getAck());
                    logger.debug("---------- Results ----------");
                    logger.debug("Transaction ID: " + response.getTransactionID());
                    logger.debug("CVV2: " + response.getCVV2Code());
                    logger.debug("AVS: " + response.getAVSCode());
                    if (response.getAmount()!=null){
                        logger.debug("Gross Amount: " + response.getAmount().getCurrencyID()+ " " + response.getAmount().get_value());
                    }
                    correlationid = response.getCorrelationID();
                    transactionid = response.getTransactionID();
                    ErrorType[] errors = response.getErrors();
                    if (errors!=null){
                        issuccessful = true;
                        for (int i = 0; i < errors.length; i++) {
                            ErrorType error = errors[i];
                            logger.debug("Error "+i+": "+error.getLongMessage());
                            if (error.getSeverityCode()==SeverityCodeType.Error){
                                logger.error("Credit Card Error: userid="+user.getUserid()+" :"+error.getLongMessage());
                                notes =  notes + "Credit Card Error: "+error.getLongMessage()+" ";
                                issuccessful = false;
                            } else if (error.getSeverityCode()==SeverityCodeType.Warning){
                                logger.error("Credit Card Warning: userid="+user.getUserid()+" :"+error.getLongMessage());
                                notes =  notes + "Credit Card Warning: "+error.getLongMessage()+" ";
                            }  else if (error.getSeverityCode()==SeverityCodeType.CustomCode){
                                logger.error("Credit Card Custom Code Error: userid="+user.getUserid()+" :"+error.getLongMessage());
                                notes =  notes + "Credit Card Custom Code Error: "+error.getLongMessage()+" ";
                            }
                        }
                    } else {
                        issuccessful = true;
                    }
                } catch (PayPalException ppex){
                    ppex.printStackTrace();
                    logger.error(ppex);
                    notes = "An internal server error occurred at "+ Time.dateformatcompactwithtime(Calendar.getInstance())+".  No money was exchanged.";
                    issuccessful = false;
                } catch (Exception ex){
                    ex.printStackTrace();
                    logger.error(ex);
                    notes = "An internal server error occurred at "+ Time.dateformatcompactwithtime(Calendar.getInstance())+".  No money was exchanged.";
                    issuccessful = false;
                }
            } else {
                notes = user.getFirstname() + " "+user.getLastname()+" (userid="+user.getUserid()+") can't be paid because no credit card is tied to the account.";
                issuccessful = false;
            }
        } catch (Exception ex){
            ex.printStackTrace();
            logger.error(ex);
            notes = "An internal server error occurred at "+ Time.dateformatcompactwithtime(Calendar.getInstance())+".  No money was exchanged.";
            issuccessful = false;
        }
    }


}
