package com.dneero.dao;

import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.RegerEntity;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.session.AuthControlled;
import com.dneero.util.GeneralException;

import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;

/**
 * Marketer generated by hbm2java
 */

public class Creditcard extends BasePersistentClass implements java.io.Serializable, RegerEntity, AuthControlled {

    public static int CREDITCARDTYPE_MASTERCARD=1;
    public static int CREDITCARDTYPE_VISA=2;
    public static int CREDITCARDTYPE_AMEX=3;
    public static int CREDITCARDTYPE_DISCOVER=4;

    // Fields

     private int creditcardid;
     private int userid;
     private String ccnum;
     private int cctype;
     private String cvv2;
     private int ccexpmo;
     private int ccexpyear;
     private String postalcode;
     private String state;
     private String street;
     private String city;
     private String firstname;
     private String lastname;
     private String ipaddress;
     private String merchantsessionid;


    


    //Validator
    public void validateRegerEntity() throws GeneralException {

    }

    //Loader
    public void load(){

    }

    public static Creditcard get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Creditcard");
        try {
            logger.debug("Creditcard.get(" + id + ") called.");
            Creditcard obj = (Creditcard) HibernateUtil.getSession().get(Creditcard.class, id);
            if (obj == null) {
                logger.debug("Creditcard.get(" + id + ") returning new instance because hibernate returned null.");
                return new Creditcard();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Creditcard", ex);
            return new Creditcard();
        }
    }

    // Constructors

    /** default constructor */
    public Creditcard() {
    }


    public boolean canRead(User user){
        if (user.getUserid()==userid){
            return true;
        }
        return false;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }



    /** full constructor */
    public Creditcard(int creditcardid) {
        this.creditcardid = creditcardid;
    }



    // Property accessors


    public int getCreditcardid() {
        return creditcardid;
    }

    public void setCreditcardid(int creditcardid) {
        this.creditcardid = creditcardid;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getCcnum() {
        return ccnum;
    }

    public void setCcnum(String ccnum) {
        this.ccnum = ccnum;
    }

    public int getCctype() {
        return cctype;
    }

    public void setCctype(int cctype) {
        this.cctype = cctype;
    }

    public String getCvv2() {
        return cvv2;
    }

    public void setCvv2(String cvv2) {
        this.cvv2 = cvv2;
    }

    public int getCcexpmo() {
        return ccexpmo;
    }

    public void setCcexpmo(int ccexpmo) {
        this.ccexpmo = ccexpmo;
    }

    public int getCcexpyear() {
        return ccexpyear;
    }

    public void setCcexpyear(int ccexpyear) {
        this.ccexpyear = ccexpyear;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getMerchantsessionid() {
        return merchantsessionid;
    }

    public void setMerchantsessionid(String merchantsessionid) {
        this.merchantsessionid = merchantsessionid;
    }
}