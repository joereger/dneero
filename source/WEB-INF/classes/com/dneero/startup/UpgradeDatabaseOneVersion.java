package com.dneero.startup;

import com.dneero.db.DbConfig;

/**
 * User: Joe Reger Jr
 * Date: Nov 26, 2006
 * Time: 11:38:40 AM
 */
public interface UpgradeDatabaseOneVersion {

    void doPreHibernateUpgrade(DbConfig dbConfig);
    void doPostHibernateUpgrade(DbConfig dbConfig);

}
