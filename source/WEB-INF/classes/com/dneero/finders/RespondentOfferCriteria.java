package com.dneero.finders;

import org.hibernate.Query;
import org.hibernate.Criteria;
import com.dneero.dao.Offer;
import com.dneero.dao.Blogger;

/**
 * User: Joe Reger Jr
 * Date: Jun 19, 2006
 * Time: 12:26:19 PM
 */
public interface RespondentOfferCriteria {

    public Criteria findBloggers(Offer offer, Criteria criteria);
    public Criteria findOffers(Blogger blogger, Criteria criteria);

}
