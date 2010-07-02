package com.dneero.startup.dbversion;

import com.dneero.dao.Pl;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.db.Db;
import com.dneero.db.DbConfig;
import com.dneero.finders.CreateDefaultDemographics;
import com.dneero.startup.UpgradeDatabaseOneVersion;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Nov 26, 2006
 * Time: 11:57:46 AM
 */
public class Version85 implements UpgradeDatabaseOneVersion {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void doPreHibernateUpgrade(DbConfig dbConfig){
        logger.debug("doPreHibernateUpgrade() start");


        logger.debug("doPreHibernateUpgrade() finish");
    }

    public void doPostHibernateUpgrade(DbConfig dbConfig){
        logger.debug("doPostHibernateUpgrade() start");


        List<Pl> pls = HibernateUtil.getSession().createCriteria(Pl.class)
                                           .setCacheable(true)
                                           .list();
        if (pls!=null){
            for (Iterator<Pl> plIterator = pls.iterator(); plIterator.hasNext();) {
                Pl pl = plIterator.next();
                CreateDefaultDemographics.create(pl);
            }
        }



        //-----------------------------------
        //-----------------------------------
        int count1 = Db.RunSQLUpdate("UPDATE blogger SET demographicsxml=''", dbConfig);
        //-----------------------------------
        //-----------------------------------


        logger.debug("doPostHibernateUpgrade() finish");
    }





}