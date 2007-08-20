package com.dneero.formbeans;

import com.dneero.facebook.FacebookUser;
import com.dneero.dao.User;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Aug 20, 2007
 * Time: 9:06:49 AM
 */
public class PublicSurveyFacebookFriendListitem implements Serializable {

    private FacebookUser facebookUser;
    private int userid = 0;
    private int responseid = 0;

    public PublicSurveyFacebookFriendListitem(){

    }


    public FacebookUser getFacebookUser() {
        return facebookUser;
    }

    public void setFacebookUser(FacebookUser facebookUser) {
        this.facebookUser = facebookUser;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getResponseid() {
        return responseid;
    }

    public void setResponseid(int responseid) {
        this.responseid = responseid;
    }
}
