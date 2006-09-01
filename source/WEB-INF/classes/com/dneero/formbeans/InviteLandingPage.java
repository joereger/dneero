package com.dneero.formbeans;

import com.dneero.util.Jsf;
import com.dneero.util.Util;
import com.dneero.util.Num;
import com.dneero.session.UserSession;

/**
 * User: Joe Reger Jr
 * Date: Sep 1, 2006
 * Time: 12:46:48 PM
 */
public class InviteLandingPage {

    public InviteLandingPage(){
        //Set the UserSession var to hold the referral
        if (Num.isinteger(Jsf.getRequestParam("referredby"))){
            Jsf.getUserSession().setReferredbyOnlyUsedForSignup(Integer.parseInt(Jsf.getRequestParam("referredby")));
        }

    }


}
