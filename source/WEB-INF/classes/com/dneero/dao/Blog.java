package com.dneero.dao;

import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.RegerEntity;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;

import java.util.Date;

import org.apache.log4j.Logger;

/**
 * Blogger generated by hbm2java
 */

public class Blog extends BasePersistentClass implements java.io.Serializable, RegerEntity {


    // Fields
     private int blogid;
     private int bloggerid;
     private String url;
     private String title;
     private int blogfocus;

    //Validator
    public void validateRegerEntity() throws GeneralException {
        
    }

    //Loader
    public void load(){

    }

    public static Blog get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Blog");
        try {
            logger.debug("Blog.get(" + id + ") called.");
            Blog obj = (Blog) HibernateUtil.getSession().get(Blog.class, id);
            if (obj == null) {
                logger.debug("Blog.get(" + id + ") returning new instance because hibernate returned null.");
                return new Blog();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Blog", ex);
            return new Blog();
        }
    }

    // Constructors

    /** default constructor */
    public Blog() {
    }






    // Property accessors

    public int getBlogid() {
        return blogid;
    }

    public void setBlogid(int blogid) {
        this.blogid = blogid;
    }

    public int getBloggerid() {
        return bloggerid;
    }

    public void setBloggerid(int bloggerid) {
        this.bloggerid = bloggerid;
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

    public int getBlogfocus() {
        return blogfocus;
    }

    public void setBlogfocus(int blogfocus) {
        this.blogfocus = blogfocus;
    }


}