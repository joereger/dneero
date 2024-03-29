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

   private Session getSession(){
        return HibernateUtil.getSession();
   }

   private void closeSession(){
       HibernateUtil.closeSession();
   }

   //Below this line should be identical in all BasePersistentClasses

   public void save() throws GeneralException {
       Logger logger = Logger.getLogger(BasePersistentClass.class);
       logger.debug("save() called on "+this.getClass().getName());
       Session hsession = getSession();
       try{
            hsession.getTransaction().setTimeout(120);
            hsession.beginTransaction();
            hsession.getTransaction().setTimeout(120);
            hsession.saveOrUpdate(this);
            if (!hsession.getTransaction().wasRolledBack()){
                hsession.getTransaction().commit();
            }
        } catch (Exception ex){
            logger.error("", ex);
            try{
                if (hsession.getTransaction().isActive()){
                    hsession.getTransaction().rollback();
                }
            } catch (Exception ex2){
                logger.error("", ex2);
            }
            closeSession();
        }
   }
   public void delete() throws HibernateException {
       Logger logger = Logger.getLogger(BasePersistentClass.class);
       Session hsession = getSession();
       try{
            hsession.getTransaction().setTimeout(120);
            hsession.beginTransaction();
            hsession.getTransaction().setTimeout(120);
            hsession.delete(this);
            hsession.getTransaction().commit();
       } catch (Exception ex){
            logger.error("", ex);
            try{
                if (hsession.getTransaction().isActive()){
                    hsession.getTransaction().rollback();
                }
            } catch (Exception ex2){
                logger.error("", ex2);
            }
            closeSession();
       }
   }

   public void refresh() throws HibernateException {
        Logger logger = Logger.getLogger(BasePersistentClass.class);
        logger.debug("Refresh called on "+this.getClass().getName());
        if (getSession().contains(this)){
            logger.debug("    Refreshing from hibernate");
            getSession().refresh(this);
        } else {
            //logger.debug("    Loading from hibernate");
            //getSession().load(this, _id);
        }
   }

   public void lock() throws HibernateException, SQLException {
      getSession().lock(this, LockMode.UPGRADE);
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
      Logger logger = Logger.getLogger(BasePersistentClass.class);
      logger.debug("onLoad() called _id="+id);
   }

   public void validate() {
   }
}
