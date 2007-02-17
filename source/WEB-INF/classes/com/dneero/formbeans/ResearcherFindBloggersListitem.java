package com.dneero.formbeans;

import com.dneero.dao.Panelmembership;
import com.dneero.dao.Blogger;
import com.dneero.dao.User;

/**
 * User: Joe Reger Jr
 * Date: Feb 9, 2007
 * Time: 10:42:52 AM
 */
public class ResearcherFindBloggersListitem {

    private Blogger blogger;
    private User user;


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
