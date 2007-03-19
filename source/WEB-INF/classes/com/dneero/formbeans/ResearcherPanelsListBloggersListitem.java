package com.dneero.formbeans;

import com.dneero.dao.Blogger;
import com.dneero.dao.User;
import com.dneero.dao.Panelmembership;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Feb 9, 2007
 * Time: 10:42:52 AM
 */
public class ResearcherPanelsListBloggersListitem implements Serializable {

    private Panelmembership panelmembership;
    private Blogger blogger;
    private User user;


    public Panelmembership getPanelmembership() {
        return panelmembership;
    }

    public void setPanelmembership(Panelmembership panelmembership) {
        this.panelmembership = panelmembership;
    }

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
}
