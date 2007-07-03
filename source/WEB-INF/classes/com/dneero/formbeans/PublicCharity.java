package com.dneero.formbeans;

import com.dneero.dao.Charitydonation;
import com.dneero.dao.User;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.Str;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.io.Serializable;


/**
 * User: Joe Reger Jr
 * Date: Jul 2, 2007
 * Time: 9:57:05 PM
 */
public class PublicCharity implements Serializable {

    private ArrayList<PublicCharityListItem> publicCharityListItemsMostRecent;

    public PublicCharity(){
        load();
    }

    public String beginView(){
        return "publiccharity";
    }

    private void load(){
        publicCharityListItemsMostRecent =  new ArrayList<PublicCharityListItem>();
        List<Charitydonation> charitydonations = HibernateUtil.getSession().createQuery("from Charitydonation where balanceid>0 order by charitydonationid desc").setMaxResults(50).setCacheable(true).list();
        for (Iterator<Charitydonation> iterator = charitydonations.iterator(); iterator.hasNext();) {
            Charitydonation charitydonation = iterator.next();
            PublicCharityListItem pcli = new PublicCharityListItem();
            pcli.setCharitydonation(charitydonation);
            pcli.setUser(User.get(charitydonation.getUserid()));
            pcli.setAmtForScreen("$"+Str.formatForMoney(charitydonation.getAmt()));
            publicCharityListItemsMostRecent.add(pcli);
        }
    }


    public ArrayList<PublicCharityListItem> getPublicCharityListItemsMostRecent() {
        return publicCharityListItemsMostRecent;
    }

    public void setPublicCharityListItemsMostRecent(ArrayList<PublicCharityListItem> publicCharityListItemsMostRecent) {
        this.publicCharityListItemsMostRecent = publicCharityListItemsMostRecent;
    }
}
