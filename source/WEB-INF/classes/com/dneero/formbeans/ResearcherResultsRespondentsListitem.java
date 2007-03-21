package com.dneero.formbeans;

import java.util.Date;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Nov 20, 2006
 * Time: 12:41:30 PM
 */
public class ResearcherResultsRespondentsListitem implements Serializable {

    private int responseid;
    private int bloggerid;
    private Date responsedate;
    private String firstname;
    private String lastname;

    public ResearcherResultsRespondentsListitem(){}


    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getResponseid() {
        return responseid;
    }

    public void setResponseid(int responseid) {
        this.responseid = responseid;
    }

    public int getBloggerid() {
        return bloggerid;
    }

    public void setBloggerid(int bloggerid) {
        this.bloggerid = bloggerid;
    }

    public Date getResponsedate() {
        return responsedate;
    }

    public void setResponsedate(Date responsedate) {
        this.responsedate = responsedate;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
}
