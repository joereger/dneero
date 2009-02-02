package com.dneero.htmluibeans;

import com.dneero.dao.Response;
import com.dneero.dao.Twitask;
import com.dneero.dao.Twitanswer;

import java.util.Date;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jul 27, 2006
 * Time: 8:00:34 AM
 */
public class BloggerCompletedTwitasksListitem implements Serializable {

    private Twitask twitask;
    private Twitanswer twitanswer;


    public BloggerCompletedTwitasksListitem(){}

    public Twitask getTwitask() {
        return twitask;
    }

    public void setTwitask(Twitask twitask) {
        this.twitask=twitask;
    }

    public Twitanswer getTwitanswer() {
        return twitanswer;
    }

    public void setTwitanswer(Twitanswer twitanswer) {
        this.twitanswer=twitanswer;
    }
}