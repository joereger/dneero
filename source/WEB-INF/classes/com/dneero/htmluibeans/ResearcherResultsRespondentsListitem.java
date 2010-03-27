package com.dneero.htmluibeans;

import com.dneero.dao.User;

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
    private String nickname;
    private String name;
    private User user;

    public ResearcherResultsRespondentsListitem(){}




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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user=user;
    }
}
