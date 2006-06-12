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

public class BasePersistentClass implements Lifecycle, Validatable, Serializable {

    //private Logger logger = Logger.getLogger(this.getClass().getName());
    private static Logger logger = Logger.getLogger(BasePersistentClass.class);

   public void save() throws GeneralException {
       logger.debug("save() called on "+this.getClass().getName());
       Session hsession = HibernateUtil.getSession();
       try{
            logger.debug("Start validation in save()");
            ValidateWorker.validate(this);
            logger.debug("End validation in save()");
            hsession.beginTransaction();
            hsession.saveOrUpdate(this);
            hsession.getTransaction().commit();
            hsession.refresh(this);
        } catch (GeneralException vex){
            hsession.getTransaction().rollback();
            //hsession.evict(this);
            HibernateUtil.closeSession();
            logger.debug("GeneralException found in save()");
            throw vex;
        } catch (HibernateException hex){
            hsession.getTransaction().rollback();
            //hsession.evict(this);
            HibernateUtil.closeSession();
            logger.debug("HibernateException found in save()");
            logger.error("HibernateException", hex);
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
            HibernateUtil.closeSession();
            logger.debug("Exception found in save()");
            logger.error("Error in BasePersistentClass", ex);
            GeneralException vex = new GeneralException();
            vex.addValidationError("General exception error saving "+this.getClass().getName());
            throw vex;
        }
   }
   public void delete() throws HibernateException {
        logger.debug("delete() called on "+this.getClass().getName());
        Session hsession = HibernateUtil.getSession();
        hsession.beginTransaction();
        hsession.delete(this);
        hsession.getTransaction().commit();
   }
   public void refresh() throws HibernateException {
        logger.debug("Refresh called on "+this.getClass().getName());
        if (HibernateUtil.getSession().contains(this)){
            logger.debug("    Refreshing from hibernate");
            HibernateUtil.getSession().refresh(this);
        } else {
            //logger.debug("    Loading from hibernate");
            //HibernateUtil.getSession().load(this, _id);
        }
   }
   public void lock() throws HibernateException, SQLException {
      HibernateUtil.getSession().lock(this, LockMode.UPGRADE);
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
      logger.debug("onLoad() called _id="+id);
   }

   public void validate() throws HibValEx {
   }
}
