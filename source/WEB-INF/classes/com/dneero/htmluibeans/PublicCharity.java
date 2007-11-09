package com.dneero.htmluibeans;

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
    private ArrayList<PublicCharityListItemTopDonators> topdonatingUsers;

    public PublicCharity(){

    }



    public void initBean(){
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

        topdonatingUsers = new ArrayList<PublicCharityListItemTopDonators>();
        List<User> users = HibernateUtil.getSession().createQuery("from User where charityamtdonated>'0' order by charityamtdonated desc").setMaxResults(20).setCacheable(true).list();
        for (Iterator<User> iterator = users.iterator(); iterator.hasNext();) {
            User user = iterator.next();
            PublicCharityListItemTopDonators pclitd = new PublicCharityListItemTopDonators();
            pclitd.setUser(user);
            pclitd.setAmtforscreen("$"+Str.formatForMoney(user.getCharityamtdonated()));
            topdonatingUsers.add(pclitd);
        }

    }


    public ArrayList<PublicCharityListItem> getPublicCharityListItemsMostRecent() {
        return publicCharityListItemsMostRecent;
    }

    public void setPublicCharityListItemsMostRecent(ArrayList<PublicCharityListItem> publicCharityListItemsMostRecent) {
        this.publicCharityListItemsMostRecent = publicCharityListItemsMostRecent;
    }


    public ArrayList<PublicCharityListItemTopDonators> getTopdonatingUsers() {
        return topdonatingUsers;
    }

    public void setTopdonatingUsers(ArrayList<PublicCharityListItemTopDonators> topdonatingUsers) {
        this.topdonatingUsers = topdonatingUsers;
    }
}
