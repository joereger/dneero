package com.dneero.formbeans;

import com.dneero.dao.Blogpost;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Jsf;
import com.dneero.ui.SocialBookmarkLinks;
import com.dneero.finders.SurveyCriteriaXML;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Sep 21, 2006
 * Time: 8:47:39 AM
 */
public class PublicBlogPost implements Serializable {

    Blogpost blogpost;

    public PublicBlogPost(){
        load();
    }

    public String beginView(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        //load();
        return "publicblogpost";
    }

    private void load(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("beginView called");
        String tmpBlogpostid = Jsf.getRequestParam("blogpostid");
        if (com.dneero.util.Num.isinteger(tmpBlogpostid)){
            logger.debug("beginView called: found blogpostid in param="+tmpBlogpostid);
            blogpost = Blogpost.get(Integer.parseInt(tmpBlogpostid));
            String bodyTmp = blogpost.getBody().replaceAll( PublicBlog.CARRIAGERETURN + PublicBlog.LINEBREAK, "<br>");
            blogpost.setBody(bodyTmp);
        } else {
            logger.debug("beginView called: NOT found blogpostid in param="+tmpBlogpostid);
        }  
    }


    public Blogpost getBlogpost() {
        return blogpost;
    }

    public void setBlogpost(Blogpost blogpost) {
        this.blogpost = blogpost;
    }
}
