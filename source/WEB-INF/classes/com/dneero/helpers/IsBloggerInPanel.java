package com.dneero.helpers;

import com.dneero.dao.Panel;
import com.dneero.dao.Blogger;
import com.dneero.dao.Panelmembership;

import java.util.Iterator;

/**
 * User: Joe Reger Jr
 * Date: Jan 21, 2009
 * Time: 10:54:32 AM
 */
public class IsBloggerInPanel {

    public static boolean isBloggerInPanel(Panel panel, Blogger blogger){
        for (Iterator<Panelmembership> iterator = panel.getPanelmemberships().iterator(); iterator.hasNext();) {
            Panelmembership panelmembership = iterator.next();
            if (panelmembership.getBloggerid()==blogger.getBloggerid()){
                return true;
            }
        }
        return false;
    }

}
