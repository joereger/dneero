package com.dneero.privatelabel;

import com.dneero.dao.Pl;
import com.dneero.dao.hibernate.HibernateUtil;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Jun 21, 2008
 * Time: 8:56:12 AM
 */
public class PlVerification {

    public static boolean isValid(Pl pl){
        if (!isSubdomainUnique(pl)){
            return false;
        }
        if (!isCustomdomain1Unique(pl)){
            return false;
        }
        if (pl.getName()==null || pl.getName().equals("")){
            return false;   
        }
        return true;
    }

    private static boolean isSubdomainUnique(Pl pl){
        List<Pl> pls = HibernateUtil.getSession().createCriteria(Pl.class)
                                           .add(Restrictions.ne("plid", pl.getPlid()))
                                           .add(Restrictions.eq("subdomain", pl.getSubdomain().toLowerCase()))
                                           .setCacheable(true)
                                           .list();
        if (pls!=null && pls.size()>0){
            return false;
        }
        return true;
    }

    private static boolean isCustomdomain1Unique(Pl pl){
        if (!isSpecificCustomdomainUnique(pl, pl.getCustomdomain1())){
            return false;
        }
        if (!isSpecificCustomdomainUnique(pl, pl.getCustomdomain2())){
            return false;
        }
        if (!isSpecificCustomdomainUnique(pl, pl.getCustomdomain3())){
            return false;
        }
        return true;
    }

    private static boolean isSpecificCustomdomainUnique(Pl pl, String customdomain){
        if (customdomain==null || customdomain.length()==0){
            return true;
        }
        if (1==1){
            List<Pl> pls = HibernateUtil.getSession().createCriteria(Pl.class)
                                               .add(Restrictions.ne("plid", pl.getPlid()))
                                               .add(Restrictions.eq("customdomain1", customdomain.toLowerCase()))
                                               .setCacheable(true)
                                               .list();
            if (pls!=null && pls.size()>0){
                return false;
            }
        }
        if (1==1){
            List<Pl> pls = HibernateUtil.getSession().createCriteria(Pl.class)
                                               .add(Restrictions.ne("plid", pl.getPlid()))
                                               .add(Restrictions.eq("customdomain2", customdomain.toLowerCase()))
                                               .setCacheable(true)
                                               .list();
            if (pls!=null && pls.size()>0){
                return false;
            }
        }
        if (1==1){
            List<Pl> pls = HibernateUtil.getSession().createCriteria(Pl.class)
                                               .add(Restrictions.ne("plid", pl.getPlid()))
                                               .add(Restrictions.eq("customdomain3", customdomain.toLowerCase()))
                                               .setCacheable(true)
                                               .list();
            if (pls!=null && pls.size()>0){
                return false;
            }
        }
        return true;
    }


}
