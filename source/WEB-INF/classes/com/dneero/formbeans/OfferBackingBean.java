package com.dneero.formbeans;

import com.dneero.dao.*;
import com.dneero.util.GeneralException;
import com.dneero.util.Jsf;
import com.dneero.util.Str;
import com.dneero.session.UserSession;
import org.apache.log4j.Logger;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class OfferBackingBean {

    private int offerid=0;
    private String title;
    private String description;
    private double willingtopayperrespondent;
    private int numberofrespondentsrequested;
    private Date startdate;
    private Date enddate;
    private String offerbody;
    private int agemin = 13;
    private int agemax = 100;
    private String[] gender;
    private String[] ethnicity;
    private String[] maritalstatus;
    private String[] income;
    private String[] educationlevel;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public OfferBackingBean(){
        logger.debug("Instanciating object... tmpofferid="+offerid);

    }

    public String beginView(){
        //logger.debug("beginView called:");
        String tmpOfferid = (String)FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("offerid");
        if (com.dneero.util.Num.isinteger(tmpOfferid)){
            logger.debug("beginView called: found offerid in param="+tmpOfferid);
            loadOffer(Integer.parseInt(tmpOfferid));
        }
        return "researcherofferdetail";
    }

    public void loadOffer(int offerid){

        Offer offer = Offer.get(offerid);
        if (offer.getOffercriteria()!=null){
            logger.debug("Found offer in db: offer.getOfferid()="+offer.getOfferid()+" offer.getTitle()="+offer.getTitle());

            this.offerid = offer.getOfferid();
            title = offer.getTitle();
            description = offer.getDescription();
            willingtopayperrespondent = offer.getWillingtopayperrespondent();
            numberofrespondentsrequested = offer.getNumberofrespondentsrequested();
            startdate = offer.getStartdate();
            enddate = offer.getEnddate();
            offerbody = offer.getOfferbody();

            agemin = offer.getOffercriteria().getAgemin();
            agemax = offer.getOffercriteria().getAgemax();
            if (offer.getOffercriteria().getOffercriteriagender()!=null){
                for (Iterator iterator = offer.getOffercriteria().getOffercriteriagender().iterator(); iterator.hasNext();) {
                    Offercriteriagender criteria =  (Offercriteriagender)iterator.next();
                    gender = Str.addToStringArray(gender, String.valueOf(criteria.getGender()));
                }
            }
            if (offer.getOffercriteria().getOffercriteriaethnicity()!=null){
                for (Iterator iterator = offer.getOffercriteria().getOffercriteriaethnicity().iterator(); iterator.hasNext();) {
                    Offercriteriaethnicity criteria =  (Offercriteriaethnicity)iterator.next();
                    ethnicity = Str.addToStringArray(ethnicity, String.valueOf(criteria.getEthnicity()));
                }
            }
            if (offer.getOffercriteria().getOffercriteriamaritalstatus()!=null){
                for (Iterator iterator = offer.getOffercriteria().getOffercriteriamaritalstatus().iterator(); iterator.hasNext();) {
                    Offercriteriamaritalstatus criteria =  (Offercriteriamaritalstatus)iterator.next();
                    maritalstatus = Str.addToStringArray(maritalstatus, String.valueOf(criteria.getMaritalstatus()));
                }
            }
            if (offer.getOffercriteria().getOffercriteriaincomerange()!=null){
                for (Iterator iterator = offer.getOffercriteria().getOffercriteriaincomerange().iterator(); iterator.hasNext();) {
                    Offercriteriaincomerange criteria =  (Offercriteriaincomerange)iterator.next();
                    income = Str.addToStringArray(income, String.valueOf(criteria.getIncomerange()));
                }
            }
            if (offer.getOffercriteria().getOffercriteriaeducationlevel()!=null){
                for (Iterator iterator = offer.getOffercriteria().getOffercriteriaeducationlevel().iterator(); iterator.hasNext();) {
                    Offercriteriaeducationlevel criteria =  (Offercriteriaeducationlevel)iterator.next();
                    educationlevel = Str.addToStringArray(educationlevel, String.valueOf(criteria.getEducationlevel()));
                }
            }
        }

    }

    public String saveOffer(){
        logger.debug("saveOffer() called.");

        UserSession userSession = (UserSession)Jsf.getManagedBean("userSession");

        Offer offer = new Offer();
        if (offerid>0){
            logger.debug("saveOffer() called: going to get Offer.get(offerid)="+offerid);
            offer = Offer.get(offerid);
        }

        offer.setResearcherid(userSession.getUser().getResearcher().getResearcherid());
        offer.setTitle(title);
        offer.setDescription(description);
        offer.setWillingtopayperrespondent(willingtopayperrespondent);
        offer.setNumberofrespondentsrequested(numberofrespondentsrequested);
        offer.setStartdate(startdate);
        offer.setEnddate(enddate);
        offer.setOfferbody(offerbody);

        try{
            logger.debug("saveOffer() about to save offer.getOfferid()=" + offer.getOfferid());
            offer.save();
            logger.debug("saveOffer() done saving offer.getOfferid()=" + offer.getOfferid());
        } catch (GeneralException gex){
            logger.debug("saveOffer() failed: " + gex.getErrorsAsSingleString());
            String message = "saveOffer() save failed: " + gex.getErrorsAsSingleString();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO, message, message));
            return null;
        }

        Offercriteria offercriteria = new Offercriteria();
        if (offer.getOffercriteria()!=null){
            logger.debug("saveOffer() offer.getOffercriteria().getOffercriteriaid()=" + offer.getOffercriteria().getOffercriteriaid());
            offercriteria = offer.getOffercriteria();
        }
        offercriteria.setOfferid(offer.getOfferid());
        offercriteria.setAgemin(agemin);
        offercriteria.setAgemax(agemax);
        offer.setOffercriteria(offercriteria);

        try{
            offer.save();
        } catch (GeneralException gex){
            logger.debug("createNewOffer failed: " + gex.getErrorsAsSingleString());
            String message = "New Offer creation failed: " + gex.getErrorsAsSingleString();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO, message, message));
            return null;
        }

//        TreeMap genders = (TreeMap)Jsf.getManagedBean("genders");
//        TreeMap ethnicities = (TreeMap)Jsf.getManagedBean("ethnicities");
//        TreeMap maritalstatuses = (TreeMap)Jsf.getManagedBean("maritalstatuses");
//        TreeMap incomes = (TreeMap)Jsf.getManagedBean("incomes");
//        TreeMap educationlevels = (TreeMap)Jsf.getManagedBean("educationlevels");

        //Gender
        if (offer.getOffercriteria().getOffercriteriagender()!=null){
            for (Iterator iterator = offer.getOffercriteria().getOffercriteriagender().iterator(); iterator.hasNext();) {
                Offercriteriagender criteria =  (Offercriteriagender)iterator.next();
                logger.debug("removing criteria.getOffercriteriagenderid(): "+criteria.getOffercriteriagenderid()+" from offer object");
                iterator.remove();
            }
        }
        for (int i = 0; i < gender.length; i++) {
            if (com.dneero.util.Num.isinteger(gender[i])){
                Offercriteriagender criteria = new Offercriteriagender();
                criteria.setOffercriteriaid(offer.getOffercriteria().getOffercriteriaid());
                criteria.setGender(Integer.parseInt(gender[i]));
                offer.getOffercriteria().getOffercriteriagender().add(criteria);
                logger.debug("added gender: "+gender[i]);
            }
        }

        //Ethnicity
        if (offer.getOffercriteria().getOffercriteriaethnicity()!=null){
            for (Iterator iterator = offer.getOffercriteria().getOffercriteriaethnicity().iterator(); iterator.hasNext();) {
                Offercriteriaethnicity criteria =  (Offercriteriaethnicity)iterator.next();
                logger.debug("removing criteria.getOffercriteriaethnicityid(): "+criteria.getOffercriteriaethnicityid()+" from offer object");
                iterator.remove();
            }
        }
        for (int i = 0; i < ethnicity.length; i++) {
            if (com.dneero.util.Num.isinteger(ethnicity[i])){
                Offercriteriaethnicity criteria = new Offercriteriaethnicity();
                criteria.setOffercriteriaid(offer.getOffercriteria().getOffercriteriaid());
                criteria.setEthnicity(Integer.parseInt(ethnicity[i]));
                offer.getOffercriteria().getOffercriteriaethnicity().add(criteria);
                logger.debug("added ethnicity: "+ethnicity[i]);
            }
        }



        //Marital Status
        if (offer.getOffercriteria().getOffercriteriamaritalstatus()!=null){
            for (Iterator iterator = offer.getOffercriteria().getOffercriteriamaritalstatus().iterator(); iterator.hasNext();) {
                Offercriteriamaritalstatus criteria =  (Offercriteriamaritalstatus)iterator.next();
                logger.debug("removing criteria.getOffercriteriamaritalstatusid(): "+criteria.getOffercriteriamaritalstatusid()+" from offer object");
                iterator.remove();
            }
        }
        for (int i = 0; i < maritalstatus.length; i++) {
            if (com.dneero.util.Num.isinteger(maritalstatus[i])){
                Offercriteriamaritalstatus criteria = new Offercriteriamaritalstatus();
                criteria.setOffercriteriaid(offer.getOffercriteria().getOffercriteriaid());
                criteria.setMaritalstatus(Integer.parseInt(maritalstatus[i]));
                offer.getOffercriteria().getOffercriteriamaritalstatus().add(criteria);
                logger.debug("added maritalstatus: "+maritalstatus[i]);
            }
        }


        //Income
        if (offer.getOffercriteria().getOffercriteriaincomerange()!=null){
            for (Iterator iterator = offer.getOffercriteria().getOffercriteriaincomerange().iterator(); iterator.hasNext();) {
                Offercriteriaincomerange criteria =  (Offercriteriaincomerange)iterator.next();
                logger.debug("removing criteria.getOffercriteriaincomerangeid(): "+criteria.getOffercriteriaincomerangeid()+" from offer object");
                iterator.remove();
            }
        }
        for (int i = 0; i < income.length; i++) {
            if (com.dneero.util.Num.isinteger(income[i])){
                Offercriteriaincomerange criteria = new Offercriteriaincomerange();
                criteria.setOffercriteriaid(offer.getOffercriteria().getOffercriteriaid());
                criteria.setIncomerange(Integer.parseInt(income[i]));
                offer.getOffercriteria().getOffercriteriaincomerange().add(criteria);
                logger.debug("added income: "+income[i]);
            }
        }


        //Education
        if (offer.getOffercriteria().getOffercriteriaeducationlevel()!=null){
            for (Iterator iterator = offer.getOffercriteria().getOffercriteriaeducationlevel().iterator(); iterator.hasNext();) {
                Offercriteriaeducationlevel criteria =  (Offercriteriaeducationlevel)iterator.next();
                logger.debug("removing criteria.getOffercriteriaeducationlevelid(): "+criteria.getOffercriteriaeducationlevelid()+" from offer object");
                iterator.remove();
            }
        }
        for (int i = 0; i < educationlevel.length; i++) {
            if (com.dneero.util.Num.isinteger(educationlevel[i])){
                Offercriteriaeducationlevel criteria = new Offercriteriaeducationlevel();
                criteria.setOffercriteriaid(offer.getOffercriteria().getOffercriteriaid());
                criteria.setEducationlevel(Integer.parseInt(educationlevel[i]));
                offer.getOffercriteria().getOffercriteriaeducationlevel().add(criteria);
                logger.debug("added educationlevel: "+educationlevel[i]);
            }
        }


        //Final save
        try{
            logger.debug("saveOffer() about to save (for 2nd time) offer.getOfferid()=" + offer.getOfferid());
            offer.save();
            logger.debug("saveOffer() done saving (for 2nd time) offer.getOfferid()=" + offer.getOfferid());
        } catch (GeneralException gex){
            logger.debug("saveOffer() failed: " + gex.getErrorsAsSingleString());
            String message = "saveOffer() save failed: " + gex.getErrorsAsSingleString();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO, message, message));
            return null;
        }

        //Refresh
        offer.refresh();


        return "success";
    }

    public int getOfferid() {
        return offerid;
    }

    public void setOfferid(int offerid) {
        this.offerid = offerid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getWillingtopayperrespondent() {
        return willingtopayperrespondent;
    }

    public void setWillingtopayperrespondent(double willingtopayperrespondent) {
        this.willingtopayperrespondent = willingtopayperrespondent;
    }

    public int getNumberofrespondentsrequested() {
        return numberofrespondentsrequested;
    }

    public void setNumberofrespondentsrequested(int numberofrespondentsrequested) {
        this.numberofrespondentsrequested = numberofrespondentsrequested;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public String getOfferbody() {
        return offerbody;
    }

    public void setOfferbody(String offerbody) {
        this.offerbody = offerbody;
    }

    public int getAgemin() {
        return agemin;
    }

    public void setAgemin(int agemin) {
        this.agemin = agemin;
    }

    public int getAgemax() {
        return agemax;
    }

    public void setAgemax(int agemax) {
        this.agemax = agemax;
    }

    public String[] getGender() {
        return gender;
    }

    public void setGender(String[] gender) {
        this.gender = gender;
    }

    public String[] getEducationlevel() {
        return educationlevel;
    }

    public void setEducationlevel(String[] educationlevel) {
        this.educationlevel = educationlevel;
    }

    public String[] getIncome() {
        return income;
    }

    public void setIncome(String[] income) {
        this.income = income;
    }

    public String[] getMaritalstatus() {
        return maritalstatus;
    }

    public void setMaritalstatus(String[] maritalstatus) {
        this.maritalstatus = maritalstatus;
    }

    public String[] getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String[] ethnicity) {
        this.ethnicity = ethnicity;
    }


}
