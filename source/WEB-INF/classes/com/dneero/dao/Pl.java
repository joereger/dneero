package com.dneero.dao;

import com.dneero.cache.providers.CacheFactory;
import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.privatelabel.PlFinder;
import com.dneero.session.AuthControlled;
import com.dneero.util.GeneralException;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;



public class Pl extends BasePersistentClass implements java.io.Serializable, AuthControlled {


    // Fields
    private int plid;
    private String name;
    private String nameforui;
    private String subdomain;
    private String customdomain1;
    private String customdomain2;
    private String customdomain3;
    private String webhtmlheader;
    private String webhtmlfooter;
    private String emailhtmlheader;
    private String emailhtmlfooter;
    private boolean ishttpson;



    public static Pl get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Pl");
        try {
            logger.debug("Pl.get(" + id + ") called.");
            Pl obj = (Pl) HibernateUtil.getSession().get(Pl.class, id);
            if (obj == null) {
                logger.debug("Pl.get(" + id + ") returning new instance because hibernate returned null.");
                return new Pl();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Pl", ex);
            return new Pl();
        }
    }

    public void save() throws GeneralException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        //Do the main save
        super.save();
        //Must clear cache
        try{
            CacheFactory.getCacheProvider().flush(PlFinder.CACHEGROUP);
        }catch(Exception ex){logger.error("",ex);}
    }

    public void delete() throws HibernateException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        //Do the main delete
        super.delete();
        //Must clear cache
        try{
            CacheFactory.getCacheProvider().flush(PlFinder.CACHEGROUP);
        }catch(Exception ex){logger.error("",ex);}
    }



    // Constructors

    /** default constructor */
    public Pl() {
    }

    public boolean canRead(User user){
        return true;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }




    // Property accessors

    public int getPlid() {
        return plid;
    }

    public void setPlid(int plid) {
        this.plid=plid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(String subdomain) {
        this.subdomain=subdomain;
    }

    public String getCustomdomain1() {
        return customdomain1;
    }

    public void setCustomdomain1(String customdomain1) {
        this.customdomain1=customdomain1;
    }

    public String getCustomdomain2() {
        return customdomain2;
    }

    public void setCustomdomain2(String customdomain2) {
        this.customdomain2=customdomain2;
    }

    public String getCustomdomain3() {
        return customdomain3;
    }

    public void setCustomdomain3(String customdomain3) {
        this.customdomain3=customdomain3;
    }

    public String getWebhtmlheader() {
        return webhtmlheader;
    }

    public void setWebhtmlheader(String webhtmlheader) {
        this.webhtmlheader=webhtmlheader;
    }

    public String getWebhtmlfooter() {
        return webhtmlfooter;
    }

    public void setWebhtmlfooter(String webhtmlfooter) {
        this.webhtmlfooter=webhtmlfooter;
    }

    public String getEmailhtmlheader() {
        return emailhtmlheader;
    }

    public void setEmailhtmlheader(String emailhtmlheader) {
        this.emailhtmlheader=emailhtmlheader;
    }

    public String getEmailhtmlfooter() {
        return emailhtmlfooter;
    }

    public void setEmailhtmlfooter(String emailhtmlfooter) {
        this.emailhtmlfooter=emailhtmlfooter;
    }

    public String getNameforui() {
        return nameforui;
    }

    public void setNameforui(String nameforui) {
        this.nameforui=nameforui;
    }

    public boolean getIshttpson() {
        return ishttpson;
    }

    public void setIshttpson(boolean ishttpson) {
        this.ishttpson=ishttpson;
    }
}