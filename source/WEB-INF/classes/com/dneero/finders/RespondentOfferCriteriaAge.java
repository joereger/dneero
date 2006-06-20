package com.dneero.finders;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import com.dneero.dao.Offer;
import com.dneero.dao.Blogger;

import java.util.Calendar;

/**
 * User: Joe Reger Jr
 * Date: Jun 19, 2006
 * Time: 12:28:33 PM
 */
public class RespondentOfferCriteriaAge implements RespondentOfferCriteria {

    public Criteria findBloggers(Offer offer, Criteria criteria){
        Calendar start = com.dneero.util.Time.xDaysAgoStart(Calendar.getInstance(), offer.getOffercriteria().getAgemax()*365);
        Calendar end = com.dneero.util.Time.xDaysAgoStart(Calendar.getInstance(), offer.getOffercriteria().getAgemin()*365);

        criteria.add( Restrictions.between("birthdate", start.getTime(), end.getTime()) );

        return criteria;
    }
    public Criteria findOffers(Blogger blogger, Criteria criteria){
        return null;
    }

}
