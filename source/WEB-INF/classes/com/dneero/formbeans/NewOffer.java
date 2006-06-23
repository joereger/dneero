package com.dneero.formbeans;

import com.dneero.dao.*;
import com.dneero.util.GeneralException;
import com.dneero.util.Jsf;
import org.apache.log4j.Logger;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class NewOffer {

//    private Offer offer;
    private String title;
    private String description;
    private double willingtopayperrespondent = 1.00;
    private int numberofrespondentsrequested = 10000;
    private Date startdate;
    private Date enddate;
    private int agemin = 13;
    private int agemax = 100;
    private String[] gender;
    private String[] ethnicity;
    private String[] maritalstatus;
    private String[] income;
    private String[] educationlevel;
    private String offerbody;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public NewOffer(){
        logger.debug("Instanciating a NewOffer object.");
        //offer = new Offer();
    }

    public String createNewOffer(){
        logger.debug("createNewOffer() called.");

        Offer offer = new Offer();
        offer.setTitle(title);
        offer.setDescription(description);
        offer.setWillingtopayperrespondent(willingtopayperrespondent);
        offer.setNumberofrespondentsrequested(numberofrespondentsrequested);
        offer.setStartdate(startdate);
        offer.setEnddate(enddate);
        offer.setOfferbody(offerbody);

        try{
            offer.save();
        } catch (GeneralException gex){
            logger.debug("createNewOffer failed: " + gex.getErrorsAsSingleString());
            String message = "New Offer creation failed: " + gex.getErrorsAsSingleString();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO, message, message));
            return null;
        }

        Offercriteria offercriteria = new Offercriteria();
        offercriteria.setOfferid(offer.getOfferid());
        offercriteria.setAgemin(agemin);
        offercriteria.setAgemax(agemax);


        try{
            offercriteria.save();
        } catch (GeneralException gex){
            logger.debug("createNewOffer failed: " + gex.getErrorsAsSingleString());
            String message = "New Offer creation failed: " + gex.getErrorsAsSingleString();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO, message, message));
            return null;
        }

        TreeMap genders = (TreeMap)Jsf.getManagedBean("genders");
        TreeMap ethnicities = (TreeMap)Jsf.getManagedBean("ethnicities");
        TreeMap maritalstatuses = (TreeMap)Jsf.getManagedBean("maritalstatuses");
        TreeMap incomes = (TreeMap)Jsf.getManagedBean("incomes");
        TreeMap educationlevels = (TreeMap)Jsf.getManagedBean("educationlevels");

        //Gender
        for (int i = 0; i < gender.length; i++) {
            if (com.dneero.util.Num.isinteger(gender[i])){
                Offercriteriagender criteria = new Offercriteriagender();
                criteria.setOffercriteriaid(offercriteria.getOffercriteriaid());
                criteria.setGender(Integer.parseInt(gender[i]));
                logger.debug("added gender: "+gender[i]);
                try{
                    criteria.save();
                } catch (GeneralException gex){
                    logger.debug("saving a criteria failed: " + gex.getErrorsAsSingleString());
                    String message = "New Offer creation failed: " + gex.getErrorsAsSingleString();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO, message, message));
                    return null;
                }
            }
        }

        //Ethnicity
        for (int i = 0; i < ethnicity.length; i++) {
            if (com.dneero.util.Num.isinteger(ethnicity[i])){
                Offercriteriaethnicity criteria = new Offercriteriaethnicity();
                criteria.setOffercriteriaid(offercriteria.getOffercriteriaid());
                criteria.setEthnicity(Integer.parseInt(ethnicity[i]));
                logger.debug("added ethnicity: "+ethnicity[i]);
                try{
                    criteria.save();
                } catch (GeneralException gex){
                    logger.debug("saving a criteria failed: " + gex.getErrorsAsSingleString());
                    String message = "New Offer creation failed: " + gex.getErrorsAsSingleString();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO, message, message));
                    return null;
                }
            }
        }

        //Marital Status
        for (int i = 0; i < maritalstatus.length; i++) {
            if (com.dneero.util.Num.isinteger(maritalstatus[i])){
                Offercriteriamaritalstatus criteria = new Offercriteriamaritalstatus();
                criteria.setOffercriteriaid(offercriteria.getOffercriteriaid());
                criteria.setMaritalstatus(Integer.parseInt(maritalstatus[i]));
                logger.debug("added maritalstatus: "+maritalstatus[i]);
                try{
                    criteria.save();
                } catch (GeneralException gex){
                    logger.debug("saving a criteria failed: " + gex.getErrorsAsSingleString());
                    String message = "New Offer creation failed: " + gex.getErrorsAsSingleString();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO, message, message));
                    return null;
                }
            }
        }

        //Income
        for (int i = 0; i < income.length; i++) {
            if (com.dneero.util.Num.isinteger(income[i])){
                Offercriteriaincomerange criteria = new Offercriteriaincomerange();
                criteria.setOffercriteriaid(offercriteria.getOffercriteriaid());
                criteria.setIncomerange(Integer.parseInt(income[i]));
                logger.debug("added income: "+income[i]);
                try{
                    criteria.save();
                } catch (GeneralException gex){
                    logger.debug("saving a criteria failed: " + gex.getErrorsAsSingleString());
                    String message = "New Offer creation failed: " + gex.getErrorsAsSingleString();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO, message, message));
                    return null;
                }
            }
        }

        //Education
        for (int i = 0; i < educationlevel.length; i++) {
            if (com.dneero.util.Num.isinteger(educationlevel[i])){
                Offercriteriaeducationlevel criteria = new Offercriteriaeducationlevel();
                criteria.setOffercriteriaid(offercriteria.getOffercriteriaid());
                criteria.setEducationlevel(Integer.parseInt(educationlevel[i]));
                logger.debug("added educationlevel: "+educationlevel[i]);
                try{
                    criteria.save();
                } catch (GeneralException gex){
                    logger.debug("saving a criteria failed: " + gex.getErrorsAsSingleString());
                    String message = "New Offer creation failed: " + gex.getErrorsAsSingleString();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO, message, message));
                    return null;
                }
            }
        }


        //Refresh
        offer.refresh();


        return "success";
    }

//    public Offer getOffer() {
//        return offer;
//    }
//
//    public void setOffer(Offer offer) {
//        this.offer = offer;
//    }

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

    public String getOfferbody() {
        return offerbody;
    }

    public void setOfferbody(String offerbody) {
        this.offerbody = offerbody;
    }
}
