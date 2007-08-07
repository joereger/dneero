package com.dneero.formbeans;

import com.dneero.dao.Blogpost;
import com.dneero.dao.Blogpostcomment;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Jsf;
import com.dneero.util.jcaptcha.CaptchaServiceSingleton;
import com.dneero.ui.SocialBookmarkLinks;
import com.dneero.finders.SurveyCriteriaXML;
import com.dneero.xmpp.SendXMPPMessage;
import com.octo.captcha.service.CaptchaServiceException;

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

    private Blogpost blogpost;
    private String name;
    private String url;
    private String comment;
    private String j_captcha_response;

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
        boolean isCaptchaCorrect = false;
        try {
            isCaptchaCorrect = CaptchaServiceSingleton.getInstance().validateResponseForID(Jsf.getHttpServletRequest().getSession().getId(), j_captcha_response);
        } catch (CaptchaServiceException e) {
            //should not happen, may be thrown if the id is not valid
        }
        if (!isCaptchaCorrect){
            Jsf.setFacesMessage("blogpost:j_captcha_response", "You failed to correctly type the letters into the box.");
            return null;
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
        //Notify via XMPP
        SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_CUSTOMERSUPPORT, "dNeero Blog Comment: "+ name + ": " + comment + " (http://dneero.com/blogpost.jsf?blogpostid="+blogpost.getBlogpostid()+")");
        xmpp.send();
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

    public String getJ_captcha_response() {
        return j_captcha_response;
    }

    public void setJ_captcha_response(String j_captcha_response) {
        this.j_captcha_response = j_captcha_response;
    }
}
