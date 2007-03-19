package com.dneero.formbeans;

import com.dneero.dao.hibernate.HibernateCacheStats;
import com.dneero.util.Jsf;

import javax.faces.context.FacesContext;
import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Nov 28, 2006
 * Time: 12:30:25 PM
 */
public class SysadminHibernateCache implements Serializable {

    private String cacheashtml;

    public SysadminHibernateCache(){
        cacheashtml = HibernateCacheStats.getCacheDump();


 
    }


    public String getCacheashtml() {
        return cacheashtml;
    }

    public void setCacheashtml(String cacheashtml) {
        this.cacheashtml = cacheashtml;
    }
}
