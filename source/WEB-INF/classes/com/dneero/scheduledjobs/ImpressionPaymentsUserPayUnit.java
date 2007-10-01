package com.dneero.scheduledjobs;

import java.util.HashMap;

/**
 * User: Joe Reger Jr
 * Date: Oct 1, 2007
 * Time: 12:55:49 PM
 */
public class ImpressionPaymentsUserPayUnit {

    private double amt=0;
    private HashMap charityDonations=new HashMap();


    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt=amt;
    }


    public HashMap getCharityDonations() {
        return charityDonations;
    }

    public void setCharityDonations(HashMap charityDonations) {
        this.charityDonations=charityDonations;
    }

    public void addCharityDonation(String charityName, double amt){
        if (charityDonations==null){
            charityDonations = new HashMap();
        }
        if (charityDonations.containsKey(charityName)){
            double currentCharityAmt = Double.parseDouble(String.valueOf(charityDonations.get(charityName)));
            charityDonations.put(charityName, currentCharityAmt+amt);
        } else {
            charityDonations.put(charityName, amt);
        }
    }


}
