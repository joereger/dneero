package com.dneero.formbeans;

import com.dneero.dao.Blogpost;
import com.dneero.dao.Blogpostcomment;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Jsf;
import com.dneero.ui.SocialBookmarkLinks;
import com.dneero.finders.SurveyCriteriaXML;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Sep 21, 2006
 * Time: 8:47:39 AM
 */
public class PublicBlogPost implements Serializable {

    public Blogpost blogpost;
    public String name;
    public String url;
    public String comment;

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

    public String postComment(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (name==null || name.equals("")){
            name = "Anonymous";
        }
        if (url==null || url.equals("")){
            url = "#";
        }
        if (comment!=null && !comment.equals("")){
            Blogpostcomment blogpostcomment = new Blogpostcomment();
            blogpostcomment.setBlogpostid(blogpost.getBlogpostid());
            blogpostcomment.setDate(new Date());
            blogpostcomment.setName(name);
            blogpostcomment.setUrl(url);
            blogpostcomment.setComment(comment);
            blogpostcomment.setIsapproved(true);
            try{blogpostcomment.save();}catch(Exception ex){logger.error(ex);}
            blogpost.getBlogpostcomments().add(blogpostcomment);
            try{blogpost.save();}catch(Exception ex){logger.error(ex);}
        }
        //load();
        PublicBlogPost bean = (PublicBlogPost)Jsf.getManagedBean("publicBlogPost");
        try{Jsf.redirectResponse("/blogpost.jsf?blogpostid="+blogpost.getBlogpostid());}catch(Exception ex){logger.error(ex);}
        return null;


    }


    public Blogpost getBlogpost() {
        return blogpost;
    }

    public void setBlogpost(Blogpost blogpost) {
        this.blogpost = blogpost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
