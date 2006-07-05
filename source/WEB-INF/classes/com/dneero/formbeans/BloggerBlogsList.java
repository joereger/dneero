package com.dneero.formbeans;

import com.dneero.util.SortableList;
import com.dneero.util.Jsf;
import com.dneero.session.UserSession;
import com.dneero.finders.FindSurveysForBlogger;
import com.dneero.dao.Survey;
import com.dneero.dao.hibernate.HibernateUtil;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Comparator;
import java.util.Collections;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class BloggerBlogsList extends SortableList {

    private Logger logger = Logger.getLogger(BloggerBlogsList.class);
    private List blogs;

    public BloggerBlogsList() {
        super("title");
        logger.debug("instanciating BloggerBlogsList");
        load();

    }

    private void load(){
        UserSession userSession = Jsf.getUserSession();
        if (userSession==null){
            logger.debug("userSession is null");
        }
        if (userSession!=null && userSession.getUser()==null){
            logger.debug("userSession.getUser() is null");
        }
        if (userSession!=null && userSession.getUser()!=null && userSession.getUser().getBlogger()==null){
            logger.debug("userSession.getUser().getBlogger() is null");    
        }
        if (userSession!=null && userSession.getUser()!=null && userSession.getUser().getBlogger()!=null){
            logger.debug("userSession, user and blogger not null");
            logger.debug("into loop for userSession.getUser().getBlogger().getBloggerid()="+userSession.getUser().getBlogger().getBloggerid());
            blogs = HibernateUtil.getSession().createQuery("from Blog where bloggerid="+userSession.getUser().getBlogger().getBloggerid()).list();
        }
    }

    public List getBlogs() {
        //logger.debug("getBlogs");
        if (blogs==null){
            load();
        }
        sort(getSort(), isAscending());
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
                Survey survey1 = (Survey)o1;
                Survey survey2 = (Survey)o2;
                if (column == null) {
                    return 0;
                }
                if (column.equals("title")) {
                    return ascending ? survey1.getTitle().compareTo(survey2.getTitle()) : survey2.getTitle().compareTo(survey1.getTitle());
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




}
