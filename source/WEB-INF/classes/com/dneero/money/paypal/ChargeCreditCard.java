package com.dneero.money.paypal;

import com.paypal.soap.api.*;
import com.paypal.sdk.exceptions.PayPalException;
import com.dneero.dao.Creditcard;
import com.dneero.util.Str;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Oct 16, 2006
 * Time: 11:54:49 AM
 */
public class ChargeCreditCard {

    public void charge(double amt, Creditcard cc){
        Logger logger = Logger.getLogger(ChargeCreditCard.class);
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

            //@todo record paypal transaction details after transaction
            DoDirectPaymentResponseType response = (DoDirectPaymentResponseType) cf.getCaller().call("DoDirectPayment", request);

            System.out.println("Operation Ack: " + response.getAck());
               System.out.println("---------- Results ----------");
               System.out.println("\nTransaction ID: " + response.getTransactionID());
               System.out.println("CVV2: " + response.getCVV2Code());
               System.out.println("AVS: " + response.getAVSCode());
               System.out.println("Gross Amount: " + response.getAmount().getCurrencyID()
                + " " + response.getAmount().get_value());
        } catch (PayPalException ppex){
            logger.error(ppex);
        }

    }


}
