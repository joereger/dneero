package com.dneero.formbeans;

import com.dneero.dao.User;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jul 3, 2007
 * Time: 6:03:21 PM
 */
public class PublicCharityListItemTopDonators implements Serializable {

    private User user;
    private String amtforscreen;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAmtforscreen() {
        return amtforscreen;
    }

    public void setAmtforscreen(String amtforscreen) {
        this.amtforscreen = amtforscreen;
    }
}
