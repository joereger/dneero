package com.dneero.dao.hibernate;

import org.hibernate.classic.Lifecycle;
import org.hibernate.classic.Validatable;
import org.hibernate.classic.ValidationFailure;
import org.hibernate.CallbackException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.LockMode;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.SQLException;

import com.dneero.util.GeneralException;

public class BasePersistentClassImpressions implements Lifecycle, Validatable, Serializable {

   //Just adding this to mark this as the record of the class as it's worked for well over a year now.

   public void save() throws GeneralException {
       Logger logger = Logger.getLogger(BasePersistentClassImpressions.class);
       logger.debug("save() called on "+this.getClass().getName());
       Session hsession = HibernateUtilImpressions.getSession();
       try{
            hsession.beginTransaction();
            hsession.saveOrUpdate(this);
            hsession.getTransaction().commit();
            //hsession.refresh(this);
        } catch (HibernateException hex){
            logger.debug("HibernateException found in save()");
            logger.error("HibernateException", hex);
            hsession.getTransaction().rollback();
            HibernateUtilImpressions.closeSession();
            GeneralException vex = new GeneralException();
            vex.addValidationError("Hibernate error saving "+this.getClass().getName());
            throw vex;
        } catch (Exception ex){
            try{
                hsession.getTransaction().rollback();
            } catch (Exception ex2){
                logger.debug("Exception found in save()'s exception block");
                logger.error("Error rolling back exception", ex2);
            }
            //hsession.evict(this);
            HibernateUtilImpressions.closeSession();
            logger.debug("Exception found in save()");
            logger.error("Error in BasePersistentClass", ex);
            GeneralException vex = new GeneralException();
            vex.addValidationError("General exception error saving "+this.getClass().getName());
            throw vex;
        }
   }
   public void delete() throws HibernateException {
        Logger logger = Logger.getLogger(BasePersistentClassImpressions.class);
        logger.debug("delete() called on "+this.getClass().getName());
        Session hsession = HibernateUtilImpressions.getSession();
        hsession.beginTransaction();
        hsession.delete(this);
        hsession.getTransaction().commit();
   }
   public void refresh() throws HibernateException {
        Logger logger = Logger.getLogger(BasePersistentClassImpressions.class);
        logger.debug("Refresh called on "+this.getClass().getName());
        if (HibernateUtilImpressions.getSession().contains(this)){
            logger.debug("    Refreshing from hibernate");
            HibernateUtilImpressions.getSession().refresh(this);
        } else {
            //logger.debug("    Loading from hibernate");
            //HibernateUtilImpressions.getSession().load(this, _id);
        }
   }
   public void lock() throws HibernateException, SQLException {
      HibernateUtilImpressions.getSession().lock(this, LockMode.UPGRADE);
   }

   public boolean onSave(Session s) throws CallbackException {
      return NO_VETO;
   }
   public boolean onDelete(Session s) throws CallbackException {
      return NO_VETO;
   }
   public boolean onUpdate(Session s) throws CallbackException {
      return NO_VETO;
   }
   public void onLoad(Session s, Serializable id) {
      Logger logger = Logger.getLogger(BasePersistentClassImpressions.class);
      logger.debug("onLoad() called _id="+id);
   }

   public void validate() {
   }
}