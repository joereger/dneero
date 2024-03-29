package com.dneero.htmluibeans;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.ValidationException;
import com.dneero.instantnotify.InstantNotifyOfNewSurvey;
import com.dneero.money.CurrentBalanceCalculator;
import com.dneero.money.MoveMoneyInAccountBalance;
import com.dneero.money.PaymentMethod;
import com.dneero.money.SurveyMoneyStatus;
import com.dneero.scheduledjobs.ResearcherRemainingBalanceOperations;
import com.dneero.survey.servlet.EmbedCacheFlusher;
import com.dneero.util.*;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.io.Serializable;
import java.util.*;


/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherSurveyDetail06 implements Serializable {


    private String title;
    private Survey survey;

    private SurveyMoneyStatus sms;

    private int status;
    private int numberofbloggersqualifiedforthissurvey = 0;
    private int daysinsurveyperiod = 0;
    private String startdate;
    private String enddate;
    private String maxrespondentpayments = "";
    private String maximpressionpayments = "";
    private String maxincentive = "0";
    private String dneerofee = "0";
    private String hideresultsfee = "0";
    private String maxpossiblespend = "0";
    private int numberofquestions = 0;
    private boolean warningtimeperiodtooshort = false;
    private boolean warningnumberofbloggerslessthanrequested = false;
    private boolean warningnumberrequestedratiotoobig = false;
    private boolean warningtoomanyquestions = false;
    private boolean warningnoquestions = false;
    private boolean warningdonthaveccinfo = false;
    private String coupondiscountamt = "0";
    private String resellercode = "";



    private String ccnumfordisplayonscreen;
    private String ccnum;
    private int cctype;
    private String cvv2;
    private int ccexpmo;
    private int ccexpyear;
    private String postalcode;
    private String ccstate;
    private String street;
    private String cccity;
    private String firstname;
    private String lastname;
    private String ipaddress;
    private String merchantsessionid;

    private ArrayList<Coupon> coupons = new ArrayList<Coupon>();
    private String couponcode="";


    public ResearcherSurveyDetail06(){

    }





    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("loadSurvey called");
        if (Num.isinteger(Pagez.getRequest().getParameter("surveyid"))){
            Pagez.getUserSession().setCurrentSurveyid(Integer.parseInt(Pagez.getRequest().getParameter("surveyid")));
            survey = Survey.get((Integer.parseInt(Pagez.getRequest().getParameter("surveyid"))));
        }
        if (survey!=null){
            logger.debug("Found survey in db: survey.getSurveyid()="+survey.getSurveyid()+" survey.getTitle()="+survey.getTitle());
            title = survey.getTitle();
            if (Pagez.getUserSession().getUser()!=null && survey.canEdit(Pagez.getUserSession().getUser())){
                //Basic survey props
                status = survey.getStatus();
                daysinsurveyperiod = DateDiff.dateDiff("day", Time.getCalFromDate(survey.getStartdate()), Time.getCalFromDate(survey.getEnddate()));
                startdate = Time.dateformatcompactwithtime(Time.getCalFromDate(survey.getStartdate()));
                enddate = Time.dateformatcompactwithtime(Time.getCalFromDate(survey.getEnddate()));
                numberofquestions = survey.getQuestions().size();
                resellercode = survey.getResellercode();

                //Find bloggers qualified for this survey
                //FindBloggersForSurvey fb = new FindBloggersForSurvey(survey);
                //numberofbloggersqualifiedforthissurvey = fb.getBloggers().size();

                //Calculate the financials of the survey
                sms = new SurveyMoneyStatus(survey);
                maxrespondentpayments = "$"+Str.formatForMoney(sms.getMaxPossiblePayoutForResponses());
                maximpressionpayments = "$"+Str.formatForMoney(sms.getMaxPossiblePayoutForImpressions());
                maxincentive = "$"+Str.formatForMoney(sms.getMaxPossiblePayoutForResponses() + sms.getMaxPossiblePayoutForImpressions());
                dneerofee = "$"+Str.formatForMoney(sms.getMaxPossibledNeeroFee());
                maxpossiblespend = "$"+Str.formatForMoney(sms.getMaxPossibleSpend());
                hideresultsfee = "$"+Str.formatForMoney(sms.getHidesurveyfee());
                coupondiscountamt = "$"+Str.formatForMoney(sms.getCouponDiscountAmt());

                //The user's current account balance
                CurrentBalanceCalculator cbc = new CurrentBalanceCalculator(Pagez.getUserSession().getUser());
                double currentbalance = cbc.getCurrentbalance();

                //Warning: time period too short
                if (DateDiff.dateDiff("day", Time.getCalFromDate(survey.getEnddate()), Time.getCalFromDate(survey.getStartdate()))<7){
                    warningtimeperiodtooshort = true;
                }

                //Warning: number of bloggers less than requested
                if (numberofbloggersqualifiedforthissurvey < survey.getNumberofrespondentsrequested()){
                    warningnumberofbloggerslessthanrequested = true;
                }

                //Warning: number of bloggers requested is over 50% of those qualified
                if (numberofbloggersqualifiedforthissurvey>0){
                    double percent = survey.getNumberofrespondentsrequested() / numberofbloggersqualifiedforthissurvey;
                    if (percent > .5){
                        warningnumberrequestedratiotoobig = true;
                    }
                } else {
                    warningnumberrequestedratiotoobig = true;
                }

                //Warning: too many questions
                if (numberofquestions>20){
                    warningtoomanyquestions = true;
                }

                //Warning: no questions
                if (numberofquestions<=0){
                    warningnoquestions = true;   
                }

                //Load coupons
                loadCoupons(survey);

                //Only worry about the credit carc stuff if there's not enough in the account currently
                logger.debug("currentbalance: "+currentbalance);
                logger.debug("sms.getMaxPossibleSpend()*(ResearcherRemainingBalanceOperations.INCREMENTALPERCENTTOCHARGE/100): "+(sms.getMaxPossibleSpend()*(ResearcherRemainingBalanceOperations.INCREMENTALPERCENTTOCHARGE/100)));
                if(currentbalance<(sms.getMaxPossibleSpend()*(ResearcherRemainingBalanceOperations.INCREMENTALPERCENTTOCHARGE/100))){
                    logger.debug("currentbalance is < sms.get.....");
                    if (Pagez.getUserSession().getUser().getChargemethod()==PaymentMethod.PAYMENTMETHODCREDITCARD){
                        logger.debug("Pagez.getUserSession().getUser().getChargemethod()== PaymentMethod.PAYMENTMETHODCREDITCARD");
                        if (Pagez.getUserSession().getUser().getChargemethodcreditcardid()>0){
                            logger.debug("Pagez.getUserSession().getUser().getChargemethodcreditcardid()>0");
                            warningdonthaveccinfo = false;
                            Creditcard cc = Creditcard.get(Pagez.getUserSession().getUser().getChargemethodcreditcardid());
                            ccnum = cc.getCcnum();
                            String ccnumStr = String.valueOf(cc.getCcnum());
                            String lastFour = "";
                            if (ccnumStr.length()>=4){
                                lastFour = ccnumStr.substring(ccnumStr.length()-4, ccnumStr.length());
                            }
                            ccnumfordisplayonscreen = "************" + lastFour;
                            cctype = cc.getCctype();
                            cvv2 = cc.getCvv2();
                            ccexpmo = cc.getCcexpmo();
                            ccexpyear = cc.getCcexpyear();
                            postalcode = cc.getPostalcode();
                            ccstate = cc.getState();
                            street = cc.getStreet();
                            cccity = cc.getCity();
                            firstname = cc.getFirstname();
                            lastname = cc.getLastname();
                            ipaddress = cc.getIpaddress();
                            merchantsessionid = cc.getMerchantsessionid();
                        } else {
                            logger.debug("warningdonthaveccinfo = true because Pagez.getUserSession().getUser().getChargemethodcreditcardid()<=0");
                            warningdonthaveccinfo = true;
                        }
                    } else if (Pagez.getUserSession().getUser().getChargemethod()== PaymentMethod.PAYMENTMETHODMANUAL){
                        warningdonthaveccinfo = false;
                    } else if (Pagez.getUserSession().getUser().getChargemethod()== PaymentMethod.PAYMENTMETHODPAYPAL){
                        logger.error("Paypal chosen as chargemethod (which is not allowed) for userid="+Pagez.getUserSession().getUser().getUserid());
                        warningdonthaveccinfo = true;
                    } else {
                        logger.error("No valid chargemethod chosen for userid="+Pagez.getUserSession().getUser().getUserid());
                        warningdonthaveccinfo = true;
                    }
                }

            }
        }

    }



    public void saveSurvey() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("saveSurvey() called.");
        if (status<=Survey.STATUS_DRAFT){
            UserSession userSession = Pagez.getUserSession();

            if (Pagez.getUserSession().getUser()!=null && survey.canEdit(Pagez.getUserSession().getUser())){
                Calendar startdate = Time.getCalFromDate(survey.getStartdate());
                Calendar now = Calendar.getInstance();
                SurveyMoneyStatus sms = new SurveyMoneyStatus(survey);
                logger.debug("now="+Time.dateformatfordb(now));
                logger.debug("startdate="+Time.dateformatfordb(startdate));
                logger.debug("startdate.before(now)="+startdate.before(now));
                logger.debug("startdate.after(now)="+startdate.after(now));

                CurrentBalanceCalculator cbc = new CurrentBalanceCalculator(Pagez.getUserSession().getUser());
                double currentbalance = cbc.getCurrentbalance();

                //See if there's enough in the account
                boolean enoughinaccountnow = true;
                if (!survey.getIsfree()){
                    if (currentbalance<(sms.getMaxPossibleSpend()*(ResearcherRemainingBalanceOperations.INCREMENTALPERCENTTOCHARGE/100))){
                        enoughinaccountnow = false;
                    }
                }
                //Only worry about the credit card stuff if there's not enough in the account currently
                boolean ccinfolooksok = true;
                if (!survey.getIsfree()){
                    if(!enoughinaccountnow){
                        //Validate
                        if (firstname==null || firstname.equals("")){
                            ccinfolooksok = false;
                        }
                        if (ccnum==null || ccnum.equals("")){
                            ccinfolooksok = false;
                        }
                        if (cccity==null || cccity.equals("")){
                            ccinfolooksok = false;
                        }
                        if (cvv2==null || cvv2.equals("")){
                            ccinfolooksok = false;
                        }
                        if (lastname==null || lastname.equals("")){
                            ccinfolooksok = false;
                        }
                        if (postalcode==null || postalcode.equals("")){
                            ccinfolooksok = false;
                        }
                        if (ccstate==null || ccstate.equals("")){
                            ccinfolooksok = false;
                        }
                        if (street==null || street.equals("")){
                            ccinfolooksok = false;
                        }
                        if (!ccinfolooksok){
                            throw new ValidationException("Credit Card information is not complete.");
                        }
                        //If the cc info looks ok on initial inspection
                        if (!ccinfolooksok){
                            //Save credit card info and set creditcardid for user charge method
                            Creditcard cc = new Creditcard();
                            if(userSession.getUser().getChargemethodcreditcardid()>0){
                                cc = Creditcard.get(userSession.getUser().getChargemethodcreditcardid());
                            }
                            cc.setCcexpmo(ccexpmo);
                            cc.setCcexpyear(ccexpyear);
                            cc.setCcnum(ccnum);
                            cc.setCctype(cctype);
                            cc.setCity(cccity);
                            cc.setCvv2(cvv2);
                            cc.setFirstname(firstname);
                            cc.setIpaddress(Pagez.getRequest().getRemoteAddr());
                            cc.setLastname(lastname);
                            cc.setMerchantsessionid(Pagez.getRequest().getSession().getId());
                            cc.setPostalcode(postalcode);
                            cc.setState(ccstate);
                            cc.setStreet(street);
                            cc.setUserid(userSession.getUser().getUserid());
                            try{
                                cc.save();
                            } catch (GeneralException gex){
                                Pagez.getUserSession().setMessage("Error saving record: "+gex.getErrorsAsSingleString());
                                logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                                return;
                            }

                            User user = User.get(userSession.getUser().getUserid());
                            user.setChargemethod(PaymentMethod.PAYMENTMETHODCREDITCARD);
                            user.setChargemethodcreditcardid(cc.getCreditcardid());

                            try{
                                user.save();
                            } catch (GeneralException gex){
                                vex.addValidationError("Error saving record: "+gex.getErrorsAsSingleString());
                                logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                                throw vex;
                            }
                            userSession.setUser(user);
                        }
                    }
                }
                //Manage the status
                logger.info("enoughinaccountnow="+enoughinaccountnow);
                if (enoughinaccountnow){
                    if (startdate.before(now)){
                        logger.info("startdate.before(now)=true so setting Survey.STATUS_OPEN");
                        survey.setStatus(Survey.STATUS_OPEN);
                        survey.setStartdate(new Date());
                    } else {
                        logger.info("startdate.before(now)=false so setting Survey.STATUS_WAITINGFORSTARTDATE");
                        survey.setStatus(Survey.STATUS_WAITINGFORSTARTDATE);
                    }
                } else {
                    logger.info("setting Survey.STATUS_WAITINGFORFUNDS");
                    survey.setStatus(Survey.STATUS_WAITINGFORFUNDS);
                }

                //Set the reseller code
                survey.setResellercode(resellercode);

                //Save the survey
                try{
                    logger.debug("saveSurvey() about to save survey.getSurveyid()=" + survey.getSurveyid());
                    survey.save();
                    EmbedCacheFlusher.flushCache(survey.getSurveyid());
                    logger.debug("saveSurvey() done saving survey.getSurveyid()=" + survey.getSurveyid());
                } catch (GeneralException gex){
                    logger.debug("saveSurvey() failed: " + gex.getErrorsAsSingleString());
                    String message = "saveSurvey() save failed: " + gex.getErrorsAsSingleString();
                    vex.addValidationError(message);
                    throw vex;
                }

                //Charge the per-survey creation fee
                //MoveMoneyInAccountBalance.charge(userSession.getUser(), SurveyMoneyStatus.PERSURVEYCREATIONFEE, "Survey creation fee for '"+survey.getTitle()+"'", true, false, false, false);

                //Charge the hide results fee, if applicable
                if (!survey.getIsfree() && survey.getIsresultshidden() && sms.getHidesurveyfee()>0){
                    MoveMoneyInAccountBalance.charge(userSession.getUser(), sms.getHidesurveyfee(), "Hide overall aggregate results fee for '"+survey.getTitle()+"'", true, false, false, false);
                }

                //Make sure user has enough in their account by running the remaining balance algorithm for just this researcher
                if (Pagez.getUserSession().getUser()!=null){
                    //Run in its own thread so that the user's screen progresses
                    //ResearcherSurveyDetail06BalancecheckThread thr = new ResearcherSurveyDetail06BalancecheckThread(Researcher.get(Pagez.getUserSession().getUser().getResearcherid()));
                    //thr.startThread();
                    ResearcherRemainingBalanceOperations.processResearcher(Researcher.get(Pagez.getUserSession().getUser().getResearcherid()));
                }

                //Refresh
                survey.refresh();

                //InstantNotify
                InstantNotifyOfNewSurvey inons = new InstantNotifyOfNewSurvey(survey.getSurveyid());
                inons.sendNotifications();
            }
        }
    }

    private void loadCoupons(Survey survey){
        coupons = new ArrayList<Coupon>();
        List<Couponredemption> couponredemptions = HibernateUtil.getSession().createCriteria(Couponredemption.class)
                                           .add(Restrictions.eq("surveyid", survey.getSurveyid()))
                                           .setCacheable(true)
                                           .list();
        if (couponredemptions!=null && couponredemptions.size()>0){
            for (Iterator<Couponredemption> iterator = couponredemptions.iterator(); iterator.hasNext();) {
                Couponredemption couponredemption = iterator.next();
                Coupon coupon = Coupon.get(couponredemption.getCouponid());
                coupons.add(coupon);
            }
        }
    }

    public void applyResellerCode() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        boolean resellerfound = false;
        String resellername = "";
        if (resellercode!=null && !resellercode.equals("")){
            //Force upper case
            resellercode = resellercode.toUpperCase();
            //Find the user with this resellercode
            List<User> userResellers = HibernateUtil.getSession().createCriteria(User.class)
                                               .add(Restrictions.eq("resellercode", resellercode))
                                               .setCacheable(true)
                                               .setMaxResults(1)
                                               .list();
            if (userResellers!=null && userResellers.size()>0){
                User userReseller = userResellers.get(0);
                if (userReseller!=null){
                    //Can't apply your own code
                    if (userReseller.getUserid()==Pagez.getUserSession().getUser().getUserid()){
                        vex.addValidationError("Sorry, you can't use your own Reseller Code.");
                        throw vex;
                    }
                    //We have a reseller, save
                    resellerfound = true;
                    resellername = userReseller.getNickname();
                    survey.setResellercode(resellercode);
                    try{
                        survey.save();
                    } catch (GeneralException gex){
                        logger.debug("saveSurvey() failed: " + gex.getErrorsAsSingleString());
                        resellerfound = false;
                        throw vex;
                    }
                }
            }
        }
        if (resellerfound){
            //initBean();
            Pagez.getUserSession().setMessage("Your reseller code has been applied and '"+resellername+"' will now earn a commission.  Thanks!");
        } else {
            Pagez.getUserSession().setMessage("Sorry, no reseller was found for that code.");
        }
    }

    public void applyCoupon() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        boolean couponfound = false;
        List<Coupon> coupons = HibernateUtil.getSession().createCriteria(Coupon.class)
                                           .add(Restrictions.eq("couponcode", couponcode.toUpperCase()))
                                           .setCacheable(true)
                                           .list();
        for (Iterator<Coupon> iterator = coupons.iterator(); iterator.hasNext();) {
            Coupon coupon =  iterator.next();
            couponfound = true;
            //Make sure it's not already redeemed
            boolean hasbeenappliedalready=false;
            List<Couponredemption> redemptions = HibernateUtil.getSession().createCriteria(Couponredemption.class)
                                               .add(Restrictions.eq("surveyid", survey.getSurveyid()))
                                               .add(Restrictions.eq("couponid", coupon.getCouponid()))
                                               .setCacheable(true)
                                               .list();
            if (redemptions!=null && redemptions.size()>0){
                hasbeenappliedalready = true;
            }
            //If it hasn't been redeemed
            if (!hasbeenappliedalready){
                //Create and save the coupon redemption
                Couponredemption couponredemption = new Couponredemption();
                couponredemption.setCouponid(coupon.getCouponid());
                couponredemption.setDate(new Date());
                couponredemption.setSurveyid(survey.getSurveyid());
                couponredemption.setUserid(Pagez.getUserSession().getUser().getUserid());
                try{
                    couponredemption.save();
                } catch (Exception ex){
                    logger.error(ex);
                }
            }
        }
        if (couponfound){
            initBean();
            Pagez.getUserSession().setMessage("Your coupon has been applied.");
        } else {
            Pagez.getUserSession().setMessage("Sorry, no coupon was found for that code.");
        }

    }


    public TreeMap<String, String> getCreditcardtypes(){
        TreeMap<String, String> out = new TreeMap<String, String>();
        out.put(String.valueOf(Creditcard.CREDITCARDTYPE_VISA), "Visa");
        out.put(String.valueOf(Creditcard.CREDITCARDTYPE_MASTERCARD), "Master Card");
        out.put(String.valueOf(Creditcard.CREDITCARDTYPE_AMEX), "American Express");
        out.put(String.valueOf(Creditcard.CREDITCARDTYPE_DISCOVER), "Discover");
        return out;
    }

    public TreeMap<String, String> getMonthsForCreditcard(){
        TreeMap<String, String> out = new TreeMap<String, String>();
        out.put(String.valueOf(1), "Jan(01)");
        out.put(String.valueOf(2), "Feb(02)");
        out.put(String.valueOf(3), "Mar(03)");
        out.put(String.valueOf(4), "Apr(04)");
        out.put(String.valueOf(5), "May(05)");
        out.put(String.valueOf(6), "Jun(06)");
        out.put(String.valueOf(7), "Jul(07)");
        out.put(String.valueOf(8), "Aug(08)");
        out.put(String.valueOf(9), "Sep(09)");
        out.put(String.valueOf(10), "Oct(10)");
        out.put(String.valueOf(11), "Nov(11)");
        out.put(String.valueOf(12), "Dec(12)");
        return out;
    }

    public TreeMap<String, String> getYearsForCreditcard(){
        TreeMap<String, String> out = new TreeMap<String, String>();
        out.put("2008", "2008");
        out.put("2009", "2009");
        out.put("2010", "2010");
        out.put("2011", "2011");
        out.put("2012", "2012");
        out.put("2013", "2013");
        out.put("2014", "2014");
        out.put("2015", "2015");
        out.put("2016", "2016");
        out.put("2017", "2017");
        return out;
    }




    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNumberofbloggersqualifiedforthissurvey() {
        return numberofbloggersqualifiedforthissurvey;
    }

    public void setNumberofbloggersqualifiedforthissurvey(int numberofbloggersqualifiedforthissurvey) {
        this.numberofbloggersqualifiedforthissurvey = numberofbloggersqualifiedforthissurvey;
    }

    public int getDaysinsurveyperiod() {
        return daysinsurveyperiod;
    }

    public void setDaysinsurveyperiod(int daysinsurveyperiod) {
        this.daysinsurveyperiod = daysinsurveyperiod;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getMaxpossiblespend() {
        return maxpossiblespend;
    }

    public void setMaxpossiblespend(String maxpossiblespend) {
        this.maxpossiblespend = maxpossiblespend;
    }

    public int getNumberofquestions() {
        return numberofquestions;
    }

    public void setNumberofquestions(int numberofquestions) {
        this.numberofquestions = numberofquestions;
    }

    public boolean getWarningtimeperiodtooshort() {
        return warningtimeperiodtooshort;
    }

    public void setWarningtimeperiodtooshort(boolean warningtimeperiodtooshort) {
        this.warningtimeperiodtooshort = warningtimeperiodtooshort;
    }

    public boolean getWarningnumberofbloggerslessthanrequested() {
        return warningnumberofbloggerslessthanrequested;
    }

    public void setWarningnumberofbloggerslessthanrequested(boolean warningnumberofbloggerslessthanrequested) {
        this.warningnumberofbloggerslessthanrequested = warningnumberofbloggerslessthanrequested;
    }

    public boolean getWarningnumberrequestedratiotoobig() {
        return warningnumberrequestedratiotoobig;
    }

    public void setWarningnumberrequestedratiotoobig(boolean warningnumberrequestedratiotoobig) {
        this.warningnumberrequestedratiotoobig = warningnumberrequestedratiotoobig;
    }

    public boolean getWarningtoomanyquestions() {
        return warningtoomanyquestions;
    }

    public void setWarningtoomanyquestions(boolean warningtoomanyquestions) {
        this.warningtoomanyquestions = warningtoomanyquestions;
    }

    public String getDneerofee() {
        return dneerofee;
    }

    public void setDneerofee(String dneerofee) {
        this.dneerofee = dneerofee;
    }

    public String getMaxincentive() {
        return maxincentive;
    }

    public void setMaxincentive(String maxincentive) {
        this.maxincentive = maxincentive;
    }

    public String getMaxrespondentpayments() {
        return maxrespondentpayments;
    }

    public void setMaxrespondentpayments(String maxrespondentpayments) {
        this.maxrespondentpayments = maxrespondentpayments;
    }

    public String getMaximpressionpayments() {
        return maximpressionpayments;
    }

    public void setMaximpressionpayments(String maximpressionpayments) {
        this.maximpressionpayments = maximpressionpayments;
    }

    public boolean getWarningnoquestions() {
        return warningnoquestions;
    }

    public void setWarningnoquestions(boolean warningnoquestions) {
        this.warningnoquestions = warningnoquestions;
    }


    public boolean getWarningdonthaveccinfo() {
        return warningdonthaveccinfo;
    }

    public void setWarningdonthaveccinfo(boolean warningdonthaveccinfo) {
        this.warningdonthaveccinfo = warningdonthaveccinfo;
    }






    public String getCcnum() {
        return ccnum;
    }

    public void setCcnum(String ccnum) {
        this.ccnum = ccnum;
    }

    public int getCctype() {
        return cctype;
    }

    public void setCctype(int cctype) {
        this.cctype = cctype;
    }

    public String getCvv2() {
        return cvv2;
    }

    public void setCvv2(String cvv2) {
        this.cvv2 = cvv2;
    }

    public int getCcexpmo() {
        return ccexpmo;
    }

    public void setCcexpmo(int ccexpmo) {
        this.ccexpmo = ccexpmo;
    }

    public int getCcexpyear() {
        return ccexpyear;
    }

    public void setCcexpyear(int ccexpyear) {
        this.ccexpyear = ccexpyear;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getCcstate() {
        return ccstate;
    }

    public void setCcstate(String ccstate) {
        this.ccstate = ccstate;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCccity() {
        return cccity;
    }

    public void setCccity(String cccity) {
        this.cccity = cccity;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress = ipaddress;
    }

    public String getMerchantsessionid() {
        return merchantsessionid;
    }

    public void setMerchantsessionid(String merchantsessionid) {
        this.merchantsessionid = merchantsessionid;
    }


    public String getCcnumfordisplayonscreen() {
        return ccnumfordisplayonscreen;
    }

    public void setCcnumfordisplayonscreen(String ccnumfordisplayonscreen) {
        this.ccnumfordisplayonscreen = ccnumfordisplayonscreen;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getHideresultsfee() {
        return hideresultsfee;
    }

    public void setHideresultsfee(String hideresultsfee) {
        this.hideresultsfee = hideresultsfee;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey=survey;
    }

    public ArrayList<Coupon> getCoupons() {
        return coupons;
    }

    public void setCoupons(ArrayList<Coupon> coupons) {
        this.coupons = coupons;
    }

    public String getCouponcode() {
        return couponcode;
    }

    public void setCouponcode(String couponcode) {
        this.couponcode = couponcode;
    }

    public String getCoupondiscountamt() {
        return coupondiscountamt;
    }

    public void setCoupondiscountamt(String coupondiscountamt) {
        this.coupondiscountamt = coupondiscountamt;
    }

    public String getResellercode() {
        return resellercode;
    }

    public void setResellercode(String resellercode) {
        this.resellercode = resellercode;
    }
}
