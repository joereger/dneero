package com.dneero.finders;

import com.dneero.dao.Blogger;
import com.dneero.dao.Offercriteria;
import com.dneero.dao.Offer;
import com.dneero.dao.hibernate.HibernateUtil;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jun 27, 2006
 * Time: 7:50:51 AM
 */
public class FindSurveysForBlogger {

    Logger logger = Logger.getLogger(this.getClass().getName());

    private List offers;
    private Blogger blogger;

    public FindSurveysForBlogger(Blogger blogger){
        logger.debug("start building criteria to FindSurveysForBlogger");

        this.offers = new ArrayList();
        this.blogger = blogger;

        //Create the criteria
        Criteria crit = HibernateUtil.getSession().createCriteria(Offercriteria.class);

        //Birthdate
        crit.add(Restrictions.between("agemin", 0, 100));
        crit.add(Restrictions.between("agemax", 0, 100));
        //Gender
        crit.createCriteria("offercriteriagender")
            .add(Restrictions.eq("gender", blogger.getGender()));
        //Ethnicity
        crit.createCriteria("offercriteriaethnicity")
            .add(Restrictions.eq("ethnicity", blogger.getEthnicity()));
        //Marital Status
        crit.createCriteria("offercriteriamaritalstatus")
            .add(Restrictions.eq("maritalstatus", blogger.getMaritalstatus()));
        //Income Range
        crit.createCriteria("offercriteriaincomerange")
            .add(Restrictions.eq("incomerange", blogger.getIncomerange()));
        //Education Level
        crit.createCriteria("offercriteriaeducationlevel")
            .add(Restrictions.eq("educationlevel", blogger.getEducationlevel()));

        //Run the query
        List<Offercriteria> offercriteria = crit.list();
        logger.debug("offercriteria.size()=" + offercriteria.size());
        //Iterate results
        for (Iterator it = offercriteria.iterator(); it.hasNext(); ) {
            Offercriteria oc = (Offercriteria)it.next();
            offers.add(Offer.get(oc.getOfferid()));
        }

    }

    public List getOffers() {
        return offers;
    }

    public void setOffers(List offers) {
        this.offers = offers;
    }

    public Blogger getBlogger() {
        return blogger;
    }

    public void setBlogger(Blogger blogger) {
        this.blogger = blogger;
    }

}
