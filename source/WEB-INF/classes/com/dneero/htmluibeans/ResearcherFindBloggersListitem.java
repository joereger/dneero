package com.dneero.htmluibeans;

import com.dneero.dao.Panelmembership;
import com.dneero.dao.Blogger;
import com.dneero.dao.User;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Feb 9, 2007
 * Time: 10:42:52 AM
 */
public class ResearcherFindBloggersListitem implements Serializable {

    private Blogger blogger;
    private User user;
    private int socialinfluenceratingpercentile;

    public ResearcherFindBloggersListitem(){}


    public Blogger getBlogger() {
        return blogger;
    }

    public void setBlogger(Blogger blogger) {
        this.blogger = blogger;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public int getSocialinfluenceratingpercentile() {
        return socialinfluenceratingpercentile;
    }

    public void setSocialinfluenceratingpercentile(int socialinfluenceratingpercentile) {
        this.socialinfluenceratingpercentile = socialinfluenceratingpercentile;
    }
}
