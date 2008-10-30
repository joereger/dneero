package com.dneero.startup;

import java.util.Properties;

import javax.transaction.TransactionManager;
import javax.transaction.Transaction;

import org.hibernate.HibernateException;
import org.jboss.cache.transaction.TransactionManagerLookup;


/**
 * User: joereger
 * Date: Oct 30, 2008
 * Time: 1:33:37 PM
 */
public class AtomikosTransactionManagerLookup implements TransactionManagerLookup {


//    public TransactionManager getTransactionManager(Properties props) throws HibernateException {
//		try {
//			Class clazz = Class.forName("com.atomikos.icatch.jta.UserTransactionManager");
//			return (TransactionManager) clazz.newInstance();
//		} catch (Exception e) {
//			throw new HibernateException("Could not obtain Atomikos transaction manager instance", e);
//		}
//    }



    public String getUserTransactionName() {
        return "java:comp/UserTransaction";
    }


    public TransactionManager getTransactionManager() throws Exception {
        try {
			Class clazz = Class.forName("com.atomikos.icatch.jta.UserTransactionManager");
			return (TransactionManager) clazz.newInstance();
		} catch (Exception e) {
			throw new HibernateException("Could not obtain Atomikos transaction manager instance", e);
		}
    }
}

