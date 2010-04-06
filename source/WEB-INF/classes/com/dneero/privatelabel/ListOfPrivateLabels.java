package com.dneero.privatelabel;

import com.dneero.dao.Pl;
import com.dneero.dao.hibernate.HibernateUtil;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by IntelliJ IDEA.
 * User: joereger
 * Date: Apr 5, 2010
 * Time: 6:55:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class ListOfPrivateLabels {

    public static TreeMap<String, String> getList(){
        Logger logger = Logger.getLogger(ListOfPrivateLabels.class);
        TreeMap<String, String> out = new TreeMap<String, String>();
        List<Pl> pls = HibernateUtil.getSession().createCriteria(Pl.class)
                       .setCacheable(true)
                       .list();
        for (Iterator<Pl> plIterator=pls.iterator(); plIterator.hasNext();) {
            Pl pl=plIterator.next();
            out.put(String.valueOf(pl.getPlid()), pl.getName());
        }
        return out;
    }


}
