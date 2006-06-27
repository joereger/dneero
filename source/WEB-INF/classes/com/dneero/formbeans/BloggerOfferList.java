package com.dneero.formbeans;

import com.dneero.util.SortableList;
import com.dneero.util.Jsf;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.dao.Offer;
import com.dneero.dao.Blogger;
import com.dneero.session.UserSession;
import com.dneero.finders.FindSurveysForBlogger;

import java.util.*;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jun 8, 2006
 * Time: 10:16:03 AM
 */
public class BloggerOfferList extends SortableList {

    private Logger logger = Logger.getLogger(UserList.class);
    private List offers;

    public BloggerOfferList() {
        super("title");
        logger.debug("instanciating BloggerOfferList");
        //Default sort column

        //Go get the offers from the database
        //offers = HibernateUtil.getSession().createQuery("from Offer").list();

        UserSession userSession = (UserSession)Jsf.getManagedBean("userSession");

        if (userSession!=null && userSession.getUser()!=null && userSession.getUser().getBloggers()!=null){
            logger.debug("userSession, user and blogger not null");
            Set bloggers = userSession.getUser().getBloggers();
            logger.debug("bloggers.size()="+bloggers.size());
            for (Iterator iterator = bloggers.iterator(); iterator.hasNext();) {
                Blogger blogger = (Blogger) iterator.next();
                logger.debug("into loop for bloggers.getBloggerid()="+blogger.getBloggerid());
                FindSurveysForBlogger finder = new FindSurveysForBlogger(blogger);
                offers = finder.getOffers();
            }
        }


    }

    public List getOffers() {
        //logger.debug("getOffers");
        sort(getSort(), isAscending());
        return offers;
    }

    public void setOffers(List offers) {
        //logger.debug("setOffers");
        this.offers = offers;
    }

    protected boolean isDefaultAscending(String sortColumn) {
        return true;
    }



    protected void sort(final String column, final boolean ascending) {
        //logger.debug("sort called");
        Comparator comparator = new Comparator() {
            public int compare(Object o1, Object o2) {
                Offer offer1 = (Offer)o1;
                Offer offer2 = (Offer)o2;
                if (column == null) {
                    return 0;
                }
                if (column.equals("title")) {
                    return ascending ? offer1.getTitle().compareTo(offer2.getTitle()) : offer2.getTitle().compareTo(offer1.getTitle());
                } else {
                    return 0;
                }
            }
        };

        //sort and also set our model with the new sort, since using DataTable with
        //ListDataModel on front end
        if (offers != null && !offers.isEmpty()) {
            //logger.debug("sorting offers and initializing ListDataModel");
            Collections.sort(offers, comparator);
        }
    }




}
