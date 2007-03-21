package com.dneero.formbeans;

import com.dneero.dao.Survey;
import com.dneero.dao.Blog;
import com.dneero.dao.Blogger;
import com.dneero.dao.Userrole;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.session.UserSession;
import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;
import org.apache.log4j.Logger;
import org.apache.commons.validator.UrlValidator;

import javax.faces.context.FacesContext;
import java.util.Iterator;
import java.util.List;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class BloggerBlogDetail implements Serializable {

    private int blogid;
    private String url;
    private String title;
    private String blogfocus;
    private boolean isonetimeconfig = false;



    public BloggerBlogDetail(){
       
    }


    public String beginView(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("Start of beginView: blogid="+blogid);
        String tmpBlogid = Jsf.getRequestParam("blogid");
        if (com.dneero.util.Num.isinteger(tmpBlogid) && Integer.parseInt(tmpBlogid)>0){
            logger.debug("beginView called: found blogid in param="+tmpBlogid);
            try{
                Blog blog = Blog.get(Integer.parseInt(tmpBlogid));
                if (Jsf.getUserSession().getUser()!=null && blog.canEdit(Jsf.getUserSession().getUser())){
                    blogid = blog.getBlogid();
                    url = blog.getUrl();
                    title = blog.getTitle();
                    blogfocus = blog.getBlogfocus();
                }
            } catch (Exception ex){
                logger.debug("Error in beginView()");
                logger.error(ex);
            }
        } else {
            logger.debug("beginView called: NOT found blogid in param="+tmpBlogid);
            //Determine whether or not this is a one-time config
            List blogs = HibernateUtil.getSession().createQuery("from Blog where bloggerid='"+Jsf.getUserSession().getUser().getBloggerid()+"'").list();
            logger.debug("blogs.size()="+blogs.size()+" for Jsf.getUserSession().getUser().getBloggerid()="+Jsf.getUserSession().getUser().getBloggerid());
            if (blogs.size()==0){
                isonetimeconfig = true;
            }
        }
        logger.debug("End of beginView: blogid="+blogid);
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




            //URL validation/correction
            if (url!=null && !url.equals("")){
                //Append http
                if (!url.substring(0,7).equals("http://") && !url.substring(0,8).equals("https://")){
                    url = "http://" + url;
                }
            } else {
                Jsf.setFacesMessage("Your URL can't be blank.");
                return null;
            }



//            String[] schemes = {"http","https"};
//            UrlValidator urlValidator = new UrlValidator();
//            if (!urlValidator.isValid("http://www.ghost.com/")){
//                Jsf.setFacesMessage("Url '"+url+"' is not valid.  Make sure you have http:// or https://.");
//                return null;
//            }

            //url must be unique
            List blogs = HibernateUtil.getSession().createQuery("from Blog where url='"+url+"' and blogid<>'"+blogid+"'").list();
            if (blogs.size()>0){
                //@todo way for user to report that somebody else is using their blog url
                Jsf.setFacesMessage("Url "+url+" is already claimed by somebody else.");
                return null;
            }

            if (title.equals("")){
                title = url;
            }

            blog.setUrl(url);
            blog.setTitle(title);
            blog.setBlogfocus(blogfocus);
            blog.setBloggerid(userSession.getUser().getBloggerid());

            try{
                blog.save();
                blogid = blog.getBlogid();
            } catch (GeneralException gex){
                Jsf.setFacesMessage("Error saving record: "+gex.getErrorsAsSingleString());
                Logger logger = Logger.getLogger(this.getClass().getName());
                logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                return null;
            }

            Blogger.get(userSession.getUser().getBloggerid()).refresh();
        }

        List userblogs = HibernateUtil.getSession().createQuery("from Blog where bloggerid='"+Jsf.getUserSession().getUser().getBloggerid()+"'").list();
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


    public boolean getIsonetimeconfig() {
        return isonetimeconfig;
    }

    public void setIsonetimeconfig(boolean isonetimeconfig) {
        this.isonetimeconfig = isonetimeconfig;
    }
}
