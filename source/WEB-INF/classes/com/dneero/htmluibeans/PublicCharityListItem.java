package com.dneero.htmluibeans;

import com.dneero.dao.Charitydonation;
import com.dneero.dao.User;
import com.dneero.dao.Blogger;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jul 3, 2007
 * Time: 12:02:24 PM
 */
public class PublicCharityListItem implements Serializable {

    private Charitydonation charitydonation;
    private User user;
    private String amtForScreen;



    public Charitydonation getCharitydonation() {
        return charitydonation;
    }

    public void setCharitydonation(Charitydonation charitydonation) {
        this.charitydonation = charitydonation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String getAmtForScreen() {
        return amtForScreen;
    }

    public void setAmtForScreen(String amtForScreen) {
        this.amtForScreen = amtForScreen;
    }
}
