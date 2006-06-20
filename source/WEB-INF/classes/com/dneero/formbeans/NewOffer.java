package com.dneero.formbeans;

import com.dneero.dao.Offer;
import com.dneero.dao.Offercriteria;
import com.dneero.util.GeneralException;
import org.apache.log4j.Logger;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class NewOffer {

//    private Offer offer;
    private String title;
    private String description;
    private double willingtopayperrespondent;
    private int numberofrespondentsrequested;
    private Date startdate;
    private Date enddate;
    private int agemin;
    private int agemax;

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

        offer.setOffercriteria(offercriteria);


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

}
