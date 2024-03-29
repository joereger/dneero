package com.dneero.dao;

import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.session.AuthControlled;
import org.apache.log4j.Logger;
// Generated Apr 17, 2006 3:45:24 PM by Hibernate Tools 3.1.0.beta4



/**
 * Userrole generated by hbm2java
 */

public class Userrole extends BasePersistentClass implements java.io.Serializable, AuthControlled {

    public static final int ANONYMOUS = 0;
    public static final int BLOGGER = 1;
    public static final int RESEARCHER = 2;
    public static final int SYSTEMADMIN = 3;
    public static final int CUSTOMERCARE = 4;
    public static final int CREATESURVEYS = 5;
    public static final int CREATETWITASKS = 6;
    public static final int EDITLAUNCHEDSURVEYS = 7;
    // Fields    

     private int userroleid;
     private int userid;
     private int roleid;




    public static Userrole get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.Userrole");
        try {
            logger.debug("Userrole.get(" + id + ") called.");
            Userrole obj = (Userrole) HibernateUtil.getSession().get(Userrole.class, id);
            if (obj == null) {
                logger.debug("Userrole.get(" + id + ") returning new instance because hibernate returned null.");
                return new Userrole();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.Userrole", ex);
            return new Userrole();
        }
    }

    // Constructors

    /** default constructor */
    public Userrole() {
    }

    public boolean canRead(User user){
        User thisUser = User.get(userid);
        return thisUser.canRead(user);
    }

    public boolean canEdit(User user){
        return canRead(user);
    }

    
    /** full constructor */
    public Userrole(int userroleid, int userid, int roleid) {
        this.userroleid = userroleid;
        this.userid = userid;
        this.roleid = roleid;
    }
    

   
    // Property accessors

    public int getUserroleid() {
        return this.userroleid;
    }
    
    public void setUserroleid(int userroleid) {
        this.userroleid = userroleid;
    }

    public int getUserid() {
        return this.userid;
    }
    
    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getRoleid() {
        return this.roleid;
    }
    
    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }
   








}
