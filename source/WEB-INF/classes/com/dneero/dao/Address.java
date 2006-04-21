package com.dneero.dao;

import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.RegerEntity;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.GeneralException;
import org.apache.log4j.Logger;
// Generated Apr 17, 2006 3:45:25 PM by Hibernate Tools 3.1.0.beta4



/**
 * Address generated by hbm2java
 */

public class Address extends BasePersistentClass implements java.io.Serializable, RegerEntity {


    // Fields    

     private int addressid;
     private String address1;
     private String address2;
     private String city;
     private String state;
     private String zip;

    //Validator
    public void validateRegerEntity() throws GeneralException {

    }

    //Loader
    public void load(){

    }

    // Constructors

    public static Address get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Address");
        try {
            logger.debug("Address.get(" + id + ") called.");
            Address obj = (Address) HibernateUtil.getSession().get(Address.class, id);
            if (obj == null) {
                logger.debug("Address.get(" + id + ") returning new instance because hibernate returned null.");
                return new Address();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Address", ex);
            return new Address();
        }
    }


    // Constructors

    /** default constructor */
    public Address() {
    }

	/** minimal constructor */
    public Address(int addressid, String address1) {
        this.addressid = addressid;
        this.address1 = address1;
    }
    
    /** full constructor */
    public Address(int addressid, String address1, String address2, String city, String state, String zip) {
        this.addressid = addressid;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }
    

   
    // Property accessors

    public int getAddressid() {
        return this.addressid;
    }
    
    public void setAddressid(int addressid) {
        this.addressid = addressid;
    }

    public String getAddress1() {
        return this.address1;
    }
    
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return this.address2;
    }
    
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return this.city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }
    
    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return this.zip;
    }
    
    public void setZip(String zip) {
        this.zip = zip;
    }
   








}
