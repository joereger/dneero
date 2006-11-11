package com.dneero.formbeans;

import com.dneero.dao.Survey;
import com.dneero.dao.Blog;
import com.dneero.dao.Blogger;
import com.dneero.dao.Userrole;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.session.UserSession;
import com.dneero.session.Roles;
import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;
import org.apache.log4j.Logger;
import org.apache.commons.validator.UrlValidator;

import javax.faces.context.FacesContext;
import java.util.Iterator;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class BloggerBlogDetail {

    private int blogid;
    private String url;
    private String title;
    private String blogfocus;


    Logger logger = Logger.getLogger(this.getClass().getName());

    public BloggerBlogDetail(){
        logger.debug("BloggerBlogDetail instanciated.");

    }


    public String beginView(){
        logger.debug("beginView called: blogid="+blogid);
        String tmpBlogid = Jsf.getRequestParam("blogid");
        if (com.dneero.util.Num.isinteger(tmpBlogid)){
            logger.debug("beginView called: found blogid in param="+tmpBlogid);
            Blog blog = Blog.get(Integer.parseInt(tmpBlogid));
            if (Jsf.getUserSession().getUser()!=null && blog.canEdit(Jsf.getUserSession().getUser())){
                blogid = blog.getBlogid();
                url = blog.getUrl();
                title = blog.getTitle();
                blogfocus = blog.getBlogfocus();
            }

        } else {
            logger.debug("beginView called: NOT found blogid in param="+tmpBlogid);
        }
        return "bloggerblogdetail";
    }

    public String saveAction(){

        UserSession userSession = Jsf.getUserSession();

        Blog blog = new Blog();
        blog.setQuality(0);
        blog.setQuality90days(0);
        if(blogid>0){
            blog = Blog.get(blogid);
        }

        if (blogid==0 || blog.canEdit(Jsf.getUserSession().getUser())){

            //@todo url validation
    //        UrlValidator urlValidator = new UrlValidator(new String[]{"http", "https"});
    //        if (!urlValidator.isValid(url)){
    //            Jsf.setFacesMessage("Url "+url+" is not valid.  Example: http://www.yourblog.com");
    //            return null;
    //        }

            //url must be unique
            List blogs = HibernateUtil.getSession().createQuery("from Blog where url='"+url+"' and blogid<>'"+blogid+"'").list();
            if (blogs.size()>0){
                //@todo way for user to report that somebody else is using their blog url
                Jsf.setFacesMessage("Url "+url+" is already claimed by somebody else.");
                return null;
            }

            blog.setUrl(url);
            blog.setTitle(title);
            blog.setBlogfocus(blogfocus);
            blog.setBloggerid(userSession.getUser().getBlogger().getBloggerid());

            try{
                blog.save();
                blogid = blog.getBlogid();
            } catch (GeneralException gex){
                Jsf.setFacesMessage("Error saving record: "+gex.getErrorsAsSingleString());
                logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                return null;
            }

            userSession.getUser().refresh();
        }

        //#{userSession.isloggedin and (userSession.user.blogger ne null) and (empty usersession.user.blogger.blogs)}
        List userblogs = HibernateUtil.getSession().createQuery("from Blog where bloggerid='"+Jsf.getUserSession().getUser().getBlogger().getBloggerid()+"'").list();
        if (userblogs.size()==1){
            return "bloggerwelcomenewblogger";
        }

        return "bloggerblogslist";


    }

    public int getBlogid() {
        return blogid;
    }

    public void setBlogid(int blogid) {
        this.blogid = blogid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBlogfocus() {
        return blogfocus;
    }

    public void setBlogfocus(String blogfocus) {
        this.blogfocus = blogfocus;
    }

}
