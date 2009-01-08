package com.dneero.htmluibeans;

import com.dneero.dao.User;
import com.dneero.dao.Blogger;

/**
 * User: Joe Reger Jr
 * Date: Jan 8, 2009
 * Time: 2:56:39 PM
 */
public class CustomercareUserListItem {

    private User user;
    private String country="";
    private String state="";
    private String city="";

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user=user;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country=country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state=state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city=city;
    }
}
