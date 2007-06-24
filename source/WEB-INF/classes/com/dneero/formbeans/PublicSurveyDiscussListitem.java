package com.dneero.formbeans;

import com.dneero.dao.Surveydiscuss;
import com.dneero.dao.Blogger;
import com.dneero.dao.User;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jun 13, 2007
 * Time: 9:58:09 AM
 */
public class PublicSurveyDiscussListitem implements Serializable {

    private Surveydiscuss surveydiscuss;
    private User user;


    public Surveydiscuss getSurveydiscuss() {
        return surveydiscuss;
    }

    public void setSurveydiscuss(Surveydiscuss surveydiscuss) {
        this.surveydiscuss = surveydiscuss;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
