package com.dneero.htmluibeans;

import com.dneero.dao.User;
import com.dneero.dao.Response;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Aug 13, 2007
 * Time: 11:18:05 AM
 */
public class PublicSurveyRespondentsListitem implements Serializable {

    private User user;
    private Response response;

    public PublicSurveyRespondentsListitem(){

    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
