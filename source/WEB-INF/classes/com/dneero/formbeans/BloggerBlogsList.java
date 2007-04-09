package com.dneero.formbeans;

import com.dneero.util.SortableList;
import com.dneero.util.Jsf;
import com.dneero.session.UserSession;
import com.dneero.dao.Blog;
import com.dneero.dao.Impressiondetail;
import com.dneero.dao.hibernate.HibernateUtil;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Comparator;
import java.util.Collections;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class BloggerBlogsList implements Serializable {

    private List blogs;
    private String msg = "";

    public BloggerBlogsList() {

    }

    public String beginView(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("instanciating BloggerBlogsList");
        load();
        return "bloggerblogslist";
    }

    private void load(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("Start load()");

        UserSession userSession = Jsf.getUserSession();
        userSession.getUser().refresh();
        if (userSession==null){
            logger.debug("userSession is null");
        }
        if (userSession!=null && userSession.getUser()==null){
            logger.debug("userSession.getUser() is null");
        }
        if (userSession!=null && userSession.getUser()!=null && userSession.getUser().getBloggerid()==0){
            logger.debug("userSession.getUser().getBloggerid() is 0");
        }
        if (userSession!=null && userSession.getUser()!=null && userSession.getUser().getBloggerid()>0){
            logger.debug("userSession, user and bloggerid not 0");
            logger.debug("into loop for userSession.getUser().getBloggerid()="+userSession.getUser().getBloggerid());
            try{

                List<Blog> blogss = HibernateUtil.getSession().createCriteria(Blog.class)
                                               .add( Restrictions.eq("bloggerid", userSession.getUser().getBloggerid()))
                                               .list();
                blogs = blogss;
                //blogs = HibernateUtil.getSession().createQuery("from Blog where bloggerid="+userSession.getUser().getBloggerid()).list();

            } catch (Exception ex){
                logger.debug("Error in load()");
                logger.error(ex);
            }
        }
        logger.debug("End load()");
    }

    public String delete(){
        //@todo Verify that a user deleting a blog never deletes any impressions... only joinimpressions.
        Logger logger = Logger.getLogger(this.getClass().getName());
        String tmpBlogid = Jsf.getRequestParam("blogid");
        if (com.dneero.util.Num.isinteger(tmpBlogid) && Integer.parseInt(tmpBlogid)>0){
            logger.debug("beginView called: found blogid in param="+tmpBlogid);
            try{
                Blog blog = Blog.get(Integer.parseInt(tmpBlogid));
                if (Jsf.getUserSession().getUser()!=null && blog.canEdit(Jsf.getUserSession().getUser())){
                    blog.delete();
                    msg="Blog deleted successfully.";
                }
            } catch (Exception ex){
                logger.debug("Error in beginView()");
                logger.error(ex);
            }
        }
        load();
        return "bloggerblogslist";
    }

    public List getBlogs() {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("getBlogs()");
        if (blogs==null){
            load();
        }
        sort("title", true);
        if (blogs!=null){
            logger.debug("returning blogs.size()="+blogs.size());
        } else {
            logger.debug("returning blogs=null");
        }
        return blogs;
    }

    public void setBlogs(List blogs) {
        //logger.debug("setBlogs");
        this.blogs = blogs;
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }



    protected void sort(final String column, final boolean ascending) {
        //logger.debug("sort called");
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                Blog blog1 = (Blog)o1;
                Blog blog2 = (Blog)o2;
                if (column == null) {
                    return 0;
                }
                if (column.equals("title")) {
                    return ascending ? blog1.getTitle().compareTo(blog2.getTitle()) : blog2.getTitle().compareTo(blog1.getTitle());
                } else {
                    return 0;
                }
            }
        };

        //sort and also set our model with the new sort, since using DataTable with
        //ListDataModel on front end
        if (blogs!=null && !blogs.isEmpty()) {
            //logger.debug("sorting surveys and initializing ListDataModel");
            Collections.sort(blogs, comparator);
        }
    }


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
