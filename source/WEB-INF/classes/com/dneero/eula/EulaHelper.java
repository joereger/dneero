package com.dneero.eula;

import com.dneero.dao.Eula;
import com.dneero.dao.Pl;
import com.dneero.dao.User;
import com.dneero.dao.Usereula;
import com.dneero.dao.hibernate.HibernateUtil;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 * User: Joe Reger Jr
 * Date: Nov 10, 2006
 * Time: 2:03:56 PM
 */
public class EulaHelper {

    public static TreeMap<Integer, Eula> eulas;

    public static Eula getMostRecentEula(Pl pl){
        if (eulas==null){ eulas = new TreeMap<Integer, Eula>(); }
        if (eulas.get(pl.getPlid())==null){
            refreshMostRecentEula(pl);
        }
        Eula eula = eulas.get(pl.getPlid());
        if (eula==null){ eula = getEmptyEula(pl); }
        return eula;
    }

    private static Eula getEmptyEula(Pl pl){
        Eula eula = new Eula();
        eula.setDate(new Date());
        eula.setEula("End User License Agreement");
        eula.setPlid(pl.getPlid());
        return eula;
    }

    public static void refreshMostRecentEula(Pl pl){
        synchronized(eulas){
            List eulasThisPl = HibernateUtil.getSession().createQuery("from Eula where plid='"+pl.getPlid()+"' order by eulaid desc").list();
            if (eulasThisPl!=null && eulasThisPl.size()>0){
                Eula eula = (Eula)eulasThisPl.get(0);
                eulas.put(pl.getPlid(), eula);
                return;
            }
            //But since none was found in DB, create a blank empty one
            eulas.put(pl.getPlid(), getEmptyEula(pl));
        }
    }

    public static boolean isUserUsingMostRecentEula(User user){
        Logger logger = Logger.getLogger(EulaHelper.class);
        Pl pl = Pl.get(user.getPlid());
        int highestEulaidForUser = 0;
        List results = HibernateUtil.getSession().createQuery("from Usereula where userid='"+user.getUserid()+"'").list();
        for (Iterator<Usereula> iterator = results.iterator(); iterator.hasNext();) {
            Usereula usereula = iterator.next();
            logger.debug("usereulaid="+usereula.getUsereulaid()+" eulaid="+usereula.getEulaid());
            if (usereula.getEulaid()>highestEulaidForUser){
                highestEulaidForUser = usereula.getEulaid();
                logger.debug("setting highestEulaidForUser="+highestEulaidForUser);
            }
        }
        logger.debug("highestEulaidForUser="+highestEulaidForUser);
        logger.debug("getMostRecentEula().getEulaid()="+getMostRecentEula(pl).getEulaid());
        if (highestEulaidForUser>=getMostRecentEula(pl).getEulaid()){
            logger.debug("returning true because highesteulaidforuser>=getMostRecentEula().getEulaid()");
            return true;
        }
        logger.debug("returning false");
        return false;
    }


}
