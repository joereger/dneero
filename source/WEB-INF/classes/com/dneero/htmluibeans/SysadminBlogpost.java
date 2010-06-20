package com.dneero.htmluibeans;

import com.dneero.cachedstuff.BlogPosts;
import com.dneero.cachedstuff.BlogPostsFull;
import com.dneero.cachedstuff.GetCachedStuff;
import com.dneero.dao.Betainvite;
import com.dneero.dao.Blogpost;
import com.dneero.dao.Pl;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.systemprops.BaseUrl;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class SysadminBlogpost implements Serializable {

    private List<Blogpost> blogposts;
    private int blogpostid=0;
    private Date date=new Date();
    private String author="";
    private String title="";
    private String body="";
    private String categories="";
    private int plid = 1;


    public SysadminBlogpost() {

    }



    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("load()");
        blogposts = HibernateUtil.getSession().createQuery("from Blogpost order by date DESC").list();

        date = new Date();                   
        blogpostid=0;
        author= Pagez.getUserSession().getUser().getNickname();
        title="";
        body="";
        categories="";
        plid = 1;

        String tmpBlogpostid = Pagez.getRequest().getParameter("blogpostid");
        if (com.dneero.util.Num.isinteger(tmpBlogpostid) && Integer.parseInt(tmpBlogpostid)>0){
            Blogpost blogpost = Blogpost.get(Integer.parseInt(tmpBlogpostid));
            blogpostid = blogpost.getBlogpostid();
            date = blogpost.getDate();
            author = blogpost.getAuthor();    
            title = blogpost.getTitle();
            body = blogpost.getBody();
            categories = blogpost.getCategories();
            plid = blogpost.getPlid();
        }
    }

    public void save() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        Blogpost blogpost = new Blogpost();
        boolean doPingomatic = true;
        if (blogpostid>0){
            blogpost = Blogpost.get(blogpostid);
            doPingomatic = false;
        }
        blogpost.setDate(date);
        blogpost.setAuthor(author);
        blogpost.setTitle(title);
        blogpost.setBody(body);
        blogpost.setCategories(categories);
        blogpost.setPlid(plid);
        try{blogpost.save();}catch(Exception ex){logger.error("",ex);}
        initBean();
        //Refresh the blog posts on the homepage
        GetCachedStuff.flush(new BlogPosts(), Pl.get(plid));
        GetCachedStuff.flush(new BlogPostsFull(), Pl.get(plid));
        try{
            if (doPingomatic && BaseUrl.get(false).indexOf("localhost")<=-1){
                //Pingomatic.ping("dNeero Blog", BaseUrl.get(false)+"blog.jsp", BaseUrl.get(false)+"rss.xml");
            }
        } catch (Exception ex){
            logger.error("",ex);
        }
    }

    public void delete() throws ValidationException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("blogpostid="+blogpostid);
        if (blogpostid>0){
            Blogpost blogpost = Blogpost.get(blogpostid);
            try{blogpost.delete();}catch(Exception ex){logger.error("",ex);}
        }
        initBean();
    }



    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }

    protected void sort(final String column, final boolean ascending) {
        //logger.debug("sort called");
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                Betainvite user1 = (Betainvite)o1;
                Betainvite user2 = (Betainvite)o2;

                if (column == null) {
                    return 0;
                }
                if (column.equals("betainviteid")){
                    return ascending ? user2.getBetainviteid()-user1.getBetainviteid() : user1.getBetainviteid()-user2.getBetainviteid() ;
                } else {
                    return 0;
                }
            }
        };

        //sort and also set our model with the new sort, since using DataTable with
        //ListDataModel on front end
        if (blogposts != null && !blogposts.isEmpty()) {
            //logger.debug("sorting betainvites and initializing ListDataModel");
            Collections.sort(blogposts, comparator);
        }
    }


    public int getBlogpostid() {
        return blogpostid;
    }

    public void setBlogpostid(int blogpostid) {
        this.blogpostid = blogpostid;
    }

    public List<Blogpost> getBlogposts() {
        return blogposts;
    }

    public void setBlogposts(List<Blogpost> blogposts) {
        this.blogposts = blogposts;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public int getPlid() {
        return plid;
    }

    public void setPlid(int plid) {
        this.plid = plid;
    }
}
