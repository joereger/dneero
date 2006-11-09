package com.dneero.formbeans;

import org.apache.log4j.Logger;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import java.util.Calendar;
import java.util.LinkedHashMap;

import com.dneero.dao.*;
import com.dneero.util.*;
import com.dneero.session.UserSession;
import com.dneero.finders.FindBloggersForSurvey;
import com.dneero.money.PaymentMethod;
import com.dneero.money.MoveMoneyInRealWorld;
import com.dneero.scheduledjobs.CollectIncrementalSurveyFees;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherSurveyDetail06 {

    private int status;
    private int numberofbloggersqualifiedforthissurvey = 0;
    private int daysinsurveyperiod = 0;
    private String startdate;
    private String enddate;
    private String maxrespondentpayments = "";
    private String maximpressionpayments = "";
    private String maxincentive = "0";
    private String dneerofee = "0";
    private String maxpossiblespend = "0";
    private int numberofquestions = 0;
    private String averagebloggerwillbepaid = "0";
    private boolean warningtimeperiodtooshort = false;
    private boolean warningnumberofbloggerslessthanrequested = false;
    private boolean warningnumberrequestedratiotoobig = false;
    private boolean warningtoomanyquestions = false;
    private boolean warningnoquestions = false;
    private boolean warningdonthaveccinfo = false;


    private double maxresppay = 0;
    private double maximppay = 0;
    private double maxspend = 0;
    private double dfee = 0;
    private double maxpossiblespnd = 0;


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




    Logger logger = Logger.getLogger(this.getClass().getName());

    public ResearcherSurveyDetail06(){
        logger.debug("Instanciating object.");
        loadSurvey(Jsf.getUserSession().getCurrentSurveyid());
    }



    public void loadSurvey(int surveyid){
        logger.debug("loadSurvey called");
        Survey survey = Survey.get(surveyid);
        if (survey!=null){
            logger.debug("Found survey in db: survey.getSurveyid()="+survey.getSurveyid()+" survey.getTitle()="+survey.getTitle());
            if (Jsf.getUserSession().getUser()!=null && survey.canEdit(Jsf.getUserSession().getUser())){
                status = survey.getStatus();
                FindBloggersForSurvey fb = new FindBloggersForSurvey(survey);
                numberofbloggersqualifiedforthissurvey = fb.getBloggers().size();

                daysinsurveyperiod = DateDiff.dateDiff("day", Time.getCalFromDate(survey.getStartdate()), Time.getCalFromDate(survey.getEnddate()));

                startdate = Time.dateformatcompactwithtime(Time.getCalFromDate(survey.getStartdate()));
                enddate = Time.dateformatcompactwithtime(Time.getCalFromDate(survey.getEnddate()));

                maxresppay = (survey.getWillingtopayperrespondent() * survey.getNumberofrespondentsrequested());
                maxrespondentpayments = "$"+Str.formatForMoney(maxresppay);

                maximppay = ((survey.getWillingtopaypercpm()*survey.getMaxdisplaystotal())/1000);
                maximpressionpayments = "$"+Str.formatForMoney(maximppay);


                maxspend =maxresppay + maximppay;
                maxincentive = "$"+Str.formatForMoney(maxspend);

                dfee = maxspend * .25;
                dneerofee = "$"+Str.formatForMoney(dfee);

                maxpossiblespnd = maxspend + dfee;
                maxpossiblespend = "$"+Str.formatForMoney(maxpossiblespnd);

                numberofquestions = survey.getQuestions().size();

                int avgbloggerimpressions = 250;
                if (avgbloggerimpressions>survey.getMaxdisplaysperblog()){
                    avgbloggerimpressions = survey.getMaxdisplaysperblog();
                }
                double avgbloggerpaid =
                (survey.getWillingtopayperrespondent())
                + ((survey.getWillingtopaypercpm()*avgbloggerimpressions)/1000);
                averagebloggerwillbepaid = "$"+Str.formatForMoney(avgbloggerpaid);

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

                //Warning: Enough billing info?
                if (Jsf.getUserSession().getUser().getChargemethod()== PaymentMethod.PAYMENTMETHODCREDITCARD){
                    if (Jsf.getUserSession().getUser().getChargemethodcreditcardid()>0){
                        warningdonthaveccinfo = false;
                        Creditcard cc = Creditcard.get(Jsf.getUserSession().getUser().getChargemethodcreditcardid());
                        ccnum = cc.getCcnum();
                        String ccnumStr = String.valueOf(cc.getCcnum());
                        String lastFour = ccnumStr.substring(ccnumStr.length()-4, ccnumStr.length());
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
                        warningdonthaveccinfo = true;
                    }
                } else if (Jsf.getUserSession().getUser().getChargemethod()== PaymentMethod.PAYMENTMETHODMANUAL){
                    warningdonthaveccinfo = false;
                } else if (Jsf.getUserSession().getUser().getChargemethod()== PaymentMethod.PAYMENTMETHODPAYPAL){
                    logger.error("Paypal chosen as chargemethod (which is not allowed) for userid="+Jsf.getUserSession().getUser().getUserid());
                    warningdonthaveccinfo = true;
                } else {
                    logger.error("No valid chargemethod chosen for userid="+Jsf.getUserSession().getUser().getUserid());
                    warningdonthaveccinfo = true;
                }


            }
        }

    }

    public String saveSurvey(){
        logger.debug("saveSurvey() called.");
        if (status<=Survey.STATUS_WAITINGFORSTARTDATE){
            UserSession userSession = Jsf.getUserSession();

            Survey survey = new Survey();
            if (userSession.getCurrentSurveyid()>0){
                logger.debug("saveSurvey() called: going to get Survey.get(surveyid)="+userSession.getCurrentSurveyid());
                survey = Survey.get(userSession.getCurrentSurveyid());
            }

            if (Jsf.getUserSession().getUser()!=null && survey.canEdit(Jsf.getUserSession().getUser())){
                Calendar startdate = Time.getCalFromDate(survey.getStartdate());
                Calendar now = Calendar.getInstance();
                logger.debug("now="+Time.dateformatfordb(now));
                logger.debug("startdate="+Time.dateformatfordb(startdate));
                logger.debug("startdate.before(now)="+startdate.before(now));
                logger.debug("startdate.after(now)="+startdate.after(now));

                //@todo Save credit card info and set creditcardid for user charge method

                //Charge the card the initial 20% or whatever
                double amttocharge =  maxpossiblespnd  * (CollectIncrementalSurveyFees.INCREMENTALPERCENTTOCHARGE/100);
                MoveMoneyInRealWorld mmirw = new MoveMoneyInRealWorld(Jsf.getUserSession().getUser(), (-1)*amttocharge);
                mmirw.move();

                if (startdate.before(now)){
                    survey.setStatus(Survey.STATUS_OPEN);
                } else {
                    survey.setStatus(Survey.STATUS_WAITINGFORSTARTDATE);
                }

                try{
                    logger.debug("saveSurvey() about to save survey.getSurveyid()=" + survey.getSurveyid());
                    survey.save();
                    logger.debug("saveSurvey() done saving survey.getSurveyid()=" + survey.getSurveyid());
                } catch (GeneralException gex){
                    logger.debug("saveSurvey() failed: " + gex.getErrorsAsSingleString());
                    String message = "saveSurvey() save failed: " + gex.getErrorsAsSingleString();
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO, message, message));
                    return null;
                }


                //Refresh
                survey.refresh();


            }
        }

        return "success";
    }

    public String saveSurveyAsDraft(){
        logger.debug("saveSurvey() called.");
        UserSession userSession = Jsf.getUserSession();

        Survey survey = new Survey();
        if (userSession.getCurrentSurveyid()>0){
            logger.debug("saveSurvey() called: going to get Survey.get(surveyid)="+userSession.getCurrentSurveyid());
            survey = Survey.get(userSession.getCurrentSurveyid());
        }

        if (Jsf.getUserSession().getUser()!=null && survey.canEdit(Jsf.getUserSession().getUser())){
            survey.setStatus(Survey.STATUS_DRAFT);
            try{survey.save();} catch (GeneralException gex){
                logger.debug("saveSurvey() failed: " + gex.getErrorsAsSingleString());
                String message = "saveSurvey() save failed: " + gex.getErrorsAsSingleString();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO, message, message));
                return null;
            }


            //Refresh
            survey.refresh();
        }
        return "success";
    }


    public LinkedHashMap getCreditcardtypes(){
        LinkedHashMap out = new LinkedHashMap();
        out.put("Visa", Creditcard.CREDITCARDTYPE_VISA);
        out.put("Master Card", Creditcard.CREDITCARDTYPE_MASTERCARD);
        out.put("American Express", Creditcard.CREDITCARDTYPE_AMEX);
        out.put("Discover", Creditcard.CREDITCARDTYPE_DISCOVER);
        return out;
    }

    public LinkedHashMap getMonthsForCreditcard(){
        LinkedHashMap out = new LinkedHashMap();
        out.put("Jan(01)", 1);
        out.put("Feb(02)", 2);
        out.put("Mar(03)", 3);
        out.put("Apr(04)", 4);
        out.put("May(05)", 5);
        out.put("Jun(06)", 6);
        out.put("Jul(07)", 7);
        out.put("Aug(08)", 8);
        out.put("Sep(09)", 9);
        out.put("Oct(10)", 10);
        out.put("Nov(11)", 11);
        out.put("Dec(12)", 12);
        return out;
    }

    public LinkedHashMap getYearsForCreditcard(){
        LinkedHashMap out = new LinkedHashMap();
        out.put("2006", 2006);
        out.put("2007", 2007);
        out.put("2008", 2008);
        out.put("2009", 2009);
        out.put("2010", 2010);
        out.put("2011", 2011);
        out.put("2012", 2012);
        out.put("2013", 2013);
        out.put("2014", 2014);
        out.put("2015", 2015);
        out.put("2016", 2016);
        out.put("2017", 2017);
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

    public String getAveragebloggerwillbepaid() {
        return averagebloggerwillbepaid;
    }

    public void setAveragebloggerwillbepaid(String averagebloggerwillbepaid) {
        this.averagebloggerwillbepaid = averagebloggerwillbepaid;
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




    public double getMaxresppay() {
        return maxresppay;
    }

    public void setMaxresppay(double maxresppay) {
        this.maxresppay = maxresppay;
    }

    public double getMaximppay() {
        return maximppay;
    }

    public void setMaximppay(double maximppay) {
        this.maximppay = maximppay;
    }

    public double getMaxspend() {
        return maxspend;
    }

    public void setMaxspend(double maxspend) {
        this.maxspend = maxspend;
    }

    public double getDfee() {
        return dfee;
    }

    public void setDfee(double dfee) {
        this.dfee = dfee;
    }

    public double getMaxpossiblespnd() {
        return maxpossiblespnd;
    }

    public void setMaxpossiblespnd(double maxpossiblespnd) {
        this.maxpossiblespnd = maxpossiblespnd;
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
}
