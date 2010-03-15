package com.dneero.htmluibeans;

import com.dneero.dao.Twitask;
import com.dneero.dao.Twitaskincentive;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.incentivetwit.IncentivetwitCash;
import com.dneero.incentivetwit.IncentivetwitCoupon;
import com.dneero.incentivetwit.IncentivetwitNone;
import com.dneero.incentivetwit.IncentivetwitOptionsUtil;
import com.dneero.util.GeneralException;
import com.dneero.util.Num;
import org.apache.log4j.Logger;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherTwitaskDetail05 implements Serializable {

    private Twitask twitask;
    private String title;

    private double willingtopaypertwit = 0.05;
    private int numberofrespondentsrequested = 1000;
    private int status;
    private boolean ischarityonly = false;
    private String charitycustom = "";
    private String charitycustomurl = "";
    private boolean charityonlyallowcustom=false;
    private int incentivetype = 1;
    private String coupontitle = "";
    private String coupondescription = "";
    private String couponinstructions = "";
    private String couponcodeprefix = "";
    private boolean couponcodeaddrandompostfix = true;
    private double couponestimatedcashvalue = 0.0;
    private boolean isfree = true;

    public ResearcherTwitaskDetail05(){

    }




    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("initBean called");
        if (Num.isinteger(Pagez.getRequest().getParameter("twitaskid"))){
            twitask = Twitask.get((Integer.parseInt(Pagez.getRequest().getParameter("twitaskid"))));
        }
        if (twitask!=null){
            logger.debug("Found twitask in db: twitask.getTwitaskid()="+twitask.getTwitaskid()+" twitask.getQuestion()="+twitask.getQuestion());
            if (Pagez.getUserSession().getUser()!=null && twitask.canEdit(Pagez.getUserSession().getUser())){
                logger.debug("twitask.canEdit(Pagez.getUserSession().getUser())="+twitask.canEdit(Pagez.getUserSession().getUser()));
                title = twitask.getQuestion();
                isfree = twitask.getIsfree();
                if (twitask.getIncentive().getID()==IncentivetwitCash.ID){
                    incentivetype = IncentivetwitCash.ID;
                    if (Num.isdouble(IncentivetwitOptionsUtil.getValue(twitask.getIncentive().getTwitaskincentive(), IncentivetwitCash.WILLINGTOPAYPERTWIT))){
                        logger.debug("It's a double");
                        willingtopaypertwit = Double.parseDouble(IncentivetwitOptionsUtil.getValue(twitask.getIncentive().getTwitaskincentive(), IncentivetwitCash.WILLINGTOPAYPERTWIT));
                        logger.debug("willingtopaypertwit="+willingtopaypertwit);
                    } else {
                        logger.debug("It's not a double... defaulting to .05");
                        willingtopaypertwit = 0.05;
                    }
                    coupontitle = "";
                    couponcodeaddrandompostfix = true;
                    couponcodeprefix = "";
                    coupondescription = "";
                    couponestimatedcashvalue = 0.0;
                    couponinstructions = "";
                } else if (twitask.getIncentive().getID()==IncentivetwitCoupon.ID){
                    incentivetype = IncentivetwitCoupon.ID;
                    willingtopaypertwit = 0.0;
                    coupontitle = IncentivetwitOptionsUtil.getValue(twitask.getIncentive().getTwitaskincentive(), IncentivetwitCoupon.COUPONTITLE);
                    String couponcodeaddrandompostfixStr = IncentivetwitOptionsUtil.getValue(twitask.getIncentive().getTwitaskincentive(), IncentivetwitCoupon.COUPONCODEADDRANDOMPOSTFIX);
                    if (couponcodeaddrandompostfixStr.equals("1")){
                        couponcodeaddrandompostfix = true;
                    } else {
                        couponcodeaddrandompostfix = false;
                    }
                    couponcodeprefix = IncentivetwitOptionsUtil.getValue(twitask.getIncentive().getTwitaskincentive(), IncentivetwitCoupon.COUPONCODEPREFIX);
                    coupondescription = IncentivetwitOptionsUtil.getValue(twitask.getIncentive().getTwitaskincentive(), IncentivetwitCoupon.COUPONDESCRIPTION);
                    if (Num.isdouble(IncentivetwitOptionsUtil.getValue(twitask.getIncentive().getTwitaskincentive(), IncentivetwitCoupon.COUPONESTIMATEDCASHVALUE))){
                        couponestimatedcashvalue = Double.parseDouble(IncentivetwitOptionsUtil.getValue(twitask.getIncentive().getTwitaskincentive(), IncentivetwitCoupon.COUPONESTIMATEDCASHVALUE));
                    } else {
                        couponestimatedcashvalue = 0.0;
                    }
                    couponinstructions = IncentivetwitOptionsUtil.getValue(twitask.getIncentive().getTwitaskincentive(), IncentivetwitCoupon.COUPONINSTRUCTIONS);
                }
                numberofrespondentsrequested = twitask.getNumberofrespondentsrequested();
                status = twitask.getStatus();
                ischarityonly = twitask.getIscharityonly();
                charitycustom = twitask.getCharitycustom();
                charitycustomurl = twitask.getCharitycustomurl();
                charityonlyallowcustom = twitask.getCharityonlyallowcustom();
                if (isfree){
                    willingtopaypertwit = 0.0;
                }
            }

        }

    }



    public void save() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("save() called.");
        if (status<=Twitask.STATUS_DRAFT){
            if (Pagez.getUserSession().getUser()!=null && twitask.canEdit(Pagez.getUserSession().getUser())){

                //Validation
                boolean haveError = false;


                if (numberofrespondentsrequested<1){
                    vex.addValidationError("Number of Respondents Requested must be at least 1.");
                }


                if (charityonlyallowcustom){
                    if (charitycustom==null || charitycustom.equals("") || charitycustomurl==null || charitycustomurl.equals("")){
                        vex.addValidationError("You've specified that respondents can only choose from your custom charity but that custom charity is not properly specified.");
                    }
                }
                if (charitycustom!=null && !charitycustom.equals("")){
                    if (charitycustomurl==null || charitycustomurl.equals("")){
                        vex.addValidationError("You've specified a custom charity but no url where respondents can learn about it.");
                    }
                }
                if (charitycustomurl!=null && !charitycustomurl.equals("")){
                    if (charitycustom==null || charitycustom.equals("")){
                        vex.addValidationError("You've specified a custom charity url but no charity name.");
                    }
                }
                if (incentivetype==IncentivetwitCash.ID){
                    if (willingtopaypertwit<.01){
                        vex.addValidationError("Cash Incentive must be at least $0.01.");
                    }
                } else if (incentivetype==IncentivetwitCoupon.ID){
                    if (coupontitle==null || coupontitle.equals("")){
                        vex.addValidationError("You must provide a coupon title.");
                    }
                    if (coupondescription==null || coupondescription.equals("")){
                        vex.addValidationError("You must provide a coupon description.");
                    }
                    if (couponinstructions==null || couponinstructions.equals("")){
                        vex.addValidationError("You must provide coupon redemption instructions.");
                    }
                    if (couponestimatedcashvalue<0){
                        vex.addValidationError("The coupon estimated cash value must be a positive number.");
                    }
                    if (couponcodeprefix==null || couponcodeprefix.equals("") || couponcodeprefix.length()>10){
                        vex.addValidationError("You must provide coupon code prefix.");
                    }
                }
                //Validation return
                if (vex.getErrors()!=null && vex.getErrors().length>0){
                    throw vex;
                }


                //Save
                //survey.setWillingtopayperrespondent(willingtopayperrespondent);
                twitask.setNumberofrespondentsrequested(numberofrespondentsrequested);
                twitask.setIscharityonly(ischarityonly);
                twitask.setCharitycustom(charitycustom);
                twitask.setCharitycustomurl(charitycustomurl);
                twitask.setCharityonlyallowcustom(charityonlyallowcustom);
                twitask.setIsfree(isfree);
                try{
                    logger.debug("save() about to save twitask.getTwitaskid()=" + twitask.getTwitaskid());
                    twitask.save();
                    logger.debug("save() done saving twitask.getTwitaskid()=" + twitask.getTwitaskid());
                } catch (GeneralException gex){
                    logger.debug("save() failed: " + gex.getErrorsAsSingleString());
                    String message = "save() save failed: " + gex.getErrorsAsSingleString();
                    vex.addValidationError(message);
                    throw vex;
                }

                //Save the incentive
                Twitaskincentive si = twitask.getIncentive().getTwitaskincentive();
                if (si==null){
                    si = new Twitaskincentive();
                    logger.debug("Had to create a new Twitaskincentive()");
                } else {
                    logger.debug("Twitaskincentive() already existed for Twitask");
                }
                if (incentivetype==IncentivetwitCash.ID){
                    logger.debug("incentivetype==IncentivetwitCash.ID");
                    //IncentiveCash
                    si.setType(IncentivetwitCash.ID);
                    si.setTwitaskid(twitask.getTwitaskid());
                    try{si.save();}catch(Exception ex){logger.error("", ex);}
                    IncentivetwitOptionsUtil.saveValue(si, IncentivetwitCash.WILLINGTOPAYPERTWIT, String.valueOf(willingtopaypertwit));
                } else if (incentivetype==IncentivetwitCoupon.ID){
                    logger.debug("incentivetype==IncentivetwitCoupon.ID");
                    //IncentiveCoupon
                    si.setType(IncentivetwitCoupon.ID);
                    si.setTwitaskid(twitask.getTwitaskid());
                    try{si.save();}catch(Exception ex){logger.error("", ex);}
                    IncentivetwitOptionsUtil.saveValue(si, IncentivetwitCoupon.COUPONTITLE, coupontitle);
                    IncentivetwitOptionsUtil.saveValue(si, IncentivetwitCoupon.COUPONDESCRIPTION, coupondescription);
                    IncentivetwitOptionsUtil.saveValue(si, IncentivetwitCoupon.COUPONINSTRUCTIONS, couponinstructions);
                    IncentivetwitOptionsUtil.saveValue(si, IncentivetwitCoupon.COUPONCODEPREFIX, couponcodeprefix);
                    String couponcodeaddrandompostfixStr = "0";
                    if (couponcodeaddrandompostfix){
                        couponcodeaddrandompostfixStr = "1";
                    }
                    IncentivetwitOptionsUtil.saveValue(si, IncentivetwitCoupon.COUPONCODEADDRANDOMPOSTFIX, couponcodeaddrandompostfixStr);
                    IncentivetwitOptionsUtil.saveValue(si, IncentivetwitCoupon.COUPONESTIMATEDCASHVALUE, String.valueOf(couponestimatedcashvalue));
                } else if (incentivetype== IncentivetwitNone.ID){
                    //IncentiveNone
                    si.setType(IncentivetwitNone.ID);
                    si.setTwitaskid(twitask.getTwitaskid());
                    try{si.save();}catch(Exception ex){logger.error("", ex);}
                }

                //Refresh the survey
                twitask.refresh();

            }
        }
    }


    public Twitask getTwitask() {
        return twitask;
    }

    public void setTwitask(Twitask twitask) {
        this.twitask=twitask;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    public double getWillingtopaypertwit() {
        return willingtopaypertwit;
    }

    public void setWillingtopaypertwit(double willingtopaypertwit) {
        this.willingtopaypertwit=willingtopaypertwit;
    }

    public int getNumberofrespondentsrequested() {
        return numberofrespondentsrequested;
    }

    public void setNumberofrespondentsrequested(int numberofrespondentsrequested) {
        this.numberofrespondentsrequested=numberofrespondentsrequested;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status=status;
    }

    public boolean getIscharityonly() {
        return ischarityonly;
    }

    public void setIscharityonly(boolean ischarityonly) {
        this.ischarityonly=ischarityonly;
    }

    public String getCharitycustom() {
        return charitycustom;
    }

    public void setCharitycustom(String charitycustom) {
        this.charitycustom=charitycustom;
    }

    public String getCharitycustomurl() {
        return charitycustomurl;
    }

    public void setCharitycustomurl(String charitycustomurl) {
        this.charitycustomurl=charitycustomurl;
    }

    public boolean getCharityonlyallowcustom() {
        return charityonlyallowcustom;
    }

    public void setCharityonlyallowcustom(boolean charityonlyallowcustom) {
        this.charityonlyallowcustom=charityonlyallowcustom;
    }

    public int getIncentivetype() {
        return incentivetype;
    }

    public void setIncentivetype(int incentivetype) {
        this.incentivetype=incentivetype;
    }

    public String getCoupontitle() {
        return coupontitle;
    }

    public void setCoupontitle(String coupontitle) {
        this.coupontitle=coupontitle;
    }

    public String getCoupondescription() {
        return coupondescription;
    }

    public void setCoupondescription(String coupondescription) {
        this.coupondescription=coupondescription;
    }

    public String getCouponinstructions() {
        return couponinstructions;
    }

    public void setCouponinstructions(String couponinstructions) {
        this.couponinstructions=couponinstructions;
    }

    public String getCouponcodeprefix() {
        return couponcodeprefix;
    }

    public void setCouponcodeprefix(String couponcodeprefix) {
        this.couponcodeprefix=couponcodeprefix;
    }

    public boolean getCouponcodeaddrandompostfix() {
        return couponcodeaddrandompostfix;
    }

    public void setCouponcodeaddrandompostfix(boolean couponcodeaddrandompostfix) {
        this.couponcodeaddrandompostfix=couponcodeaddrandompostfix;
    }

    public double getCouponestimatedcashvalue() {
        return couponestimatedcashvalue;
    }

    public void setCouponestimatedcashvalue(double couponestimatedcashvalue) {
        this.couponestimatedcashvalue=couponestimatedcashvalue;
    }

    public boolean getIsfree() {
        return isfree;
    }

    public void setIsfree(boolean isfree) {
        this.isfree = isfree;
    }
}