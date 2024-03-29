package com.dneero.htmluibeans;

import com.dneero.dao.Blogpost;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import org.apache.log4j.Logger;

import java.io.Serializable;

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


    public PublicBlogPost(){

    }



    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("beginView called");
        String tmpBlogpostid = Pagez.getRequest().getParameter("blogpostid");
        if (com.dneero.util.Num.isinteger(tmpBlogpostid)){
            logger.debug("beginView called: found blogpostid in param="+tmpBlogpostid);
            blogpost = Blogpost.get(Integer.parseInt(tmpBlogpostid));
            String bodyTmp = blogpost.getBody().replaceAll( PublicBlog.CARRIAGERETURN + PublicBlog.LINEBREAK, "<br>");
            blogpost.setBody(bodyTmp);
        } else {
            logger.debug("beginView called: NOT found blogpostid in param="+tmpBlogpostid);
        }  
    }

    public String postComment() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        if (name==null || name.equals("")){
            name = "Anonymous";
        }
        if (url==null || url.equals("")){
            url = "#";
        }

        //@todo fix captcha systemwide
        throw new ValidationException("Sorry, comments have been turned off because one person was abusing them.  We'll get them back online shortly.");




//       if (comment!=null && !comment.equals("")){
//            Blogpostcomment blogpostcomment = new Blogpostcomment();
//            blogpostcomment.setBlogpostid(blogpost.getBlogpostid());
//            blogpostcomment.setDate(new Date());
//            blogpostcomment.setName(name);
//            blogpostcomment.setUrl(url);
//            blogpostcomment.setComment(comment);
//            blogpostcomment.setIsapproved(true);
//            try{blogpostcomment.save();}catch(Exception ex){logger.error("",ex);}
//            blogpost.getBlogpostcomments().add(blogpostcomment);
//            try{blogpost.save();}catch(Exception ex){logger.error("",ex);}
//        }
//        //Notify via XMPP
//        SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_CUSTOMERSUPPORT, "Blog Comment: "+ name + ": " + comment + " (http://dneero.com/blogpost.jsp?blogpostid="+blogpost.getBlogpostid()+")");
//        xmpp.send();
//        //load();
//        Pagez.sendRedirect("/blogpost.jsp?blogpostid="+blogpost.getBlogpostid());
//        return null;


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
