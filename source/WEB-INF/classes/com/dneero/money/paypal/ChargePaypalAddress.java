package com.dneero.money.paypal;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Sep 25, 2006
 * Time: 10:04:44 AM
 */
public class ChargePaypalAddress {

    public static void charge(String paypalAddress, double amt) throws Exception {

        Logger logger = Logger.getLogger(ChargePaypalAddress.class);
        logger.error("PayPal can't be used to charge");


    }

//        DoDirectPaymentRequestType request = new DoDirectPaymentRequestType();
//        DoDirectPaymentRequestDetailsType details = new DoDirectPaymentRequestDetailsType();
//
//        CreditCardDetailsType creditCard = new CreditCardDetailsType();
//        creditCard.setCreditCardNumber("4721930402892796");
//        creditCard.setCreditCardType(CreditCardTypeType.Visa);
//        creditCard.setCVV2("000");
//        creditCard.setExpMonth(11);
//        creditCard.setExpYear(2007);
//
//        PayerInfoType cardOwner = new PayerInfoType();
//        cardOwner.setPayerCountry(CountryCodeType.US);
//
//
//        AddressType address = new AddressType();
//        address.setPostalCode("95101");
//        address.setStateOrProvince("CA");
//        address.setStreet1("123 Main St");
//        address.setCountryName("US");
//        address.setCountry(CountryCodeType.US);
//        address.setCityName("San Jose");
//        cardOwner.setAddress(address);
//
//        PersonNameType payerName = new PersonNameType();
//        payerName.setFirstName("SDK");
//        payerName.setLastName("Buyer");
//        cardOwner.setPayerName(payerName);
//
//        creditCard.setCardOwner(cardOwner);
//        details.setCreditCard(creditCard);
//
//        details.setIPAddress("12.36.5.78");
//        details.setMerchantSessionId("456977");
//        details.setPaymentAction(PaymentActionCodeType.Sale);
//
//        PaymentDetailsType payment = new PaymentDetailsType();
//
//        BasicAmountType orderTotal = new BasicAmountType();
//        orderTotal.setCurrencyID(CurrencyCodeType.USD);
//        orderTotal.set_value(Str.formatForMoney(amt));
//        payment.setOrderTotal(orderTotal);
//
//        details.setPaymentDetails(payment);
//        request.setDoDirectPaymentRequestDetails(details);

}
