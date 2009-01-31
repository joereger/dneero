package com.dneero.htmluibeans;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;


import java.util.*;
import java.io.Serializable;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.util.*;
import com.dneero.htmlui.UserSession;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.money.*;
import com.dneero.scheduledjobs.ResearcherRemainingBalanceOperations;
import com.dneero.instantnotify.InstantNotifyOfNewSurvey;
import com.dneero.survey.servlet.EmbedCacheFlusher;


/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class ResearcherTwitaskDetail06 implements Serializable {


    private String title;
    private Twitask twitask;

    private TwitaskMoneyStatus tms;

    private int status;
    private int numberofbloggersqualifiedforthissurvey = 0;
    private String startdate;
    private String maxrespondentpayments = "";
    private String maxincentive = "0";
    private String dneerofee = "0";
    private String maxpossiblespend = "0";
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

    private boolean warningdonthaveccinfo = false;

    public ResearcherTwitaskDetail06(){

    }





    public void initBean(){
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("loadSurvey called");
        if (Num.isinteger(Pagez.getRequest().getParameter("twitaskid"))){
            twitask = Twitask.get((Integer.parseInt(Pagez.getRequest().getParameter("twitaskid"))));
        }
        if (twitask!=null){
            logger.debug("Found survey in db: twitask.getTwitaskid()="+twitask.getTwitaskid()+" twitask.getQuestion()="+twitask.getQuestion());
            title = twitask.getQuestion();
            if (Pagez.getUserSession().getUser()!=null && twitask.canEdit(Pagez.getUserSession().getUser())){
                //Basic survey props
                status = twitask.getStatus();
                startdate = Time.dateformatcompactwithtime(Time.getCalFromDate(twitask.getStartdate()));
                resellercode = twitask.getResellercode();

                //Find bloggers qualified for this survey
                //FindBloggersForSurvey fb = new FindBloggersForSurvey(survey);
                //numberofbloggersqualifiedforthissurvey = fb.getBloggers().size();

                //Calculate the financials of the survey
                tms= new TwitaskMoneyStatus(twitask);
                maxrespondentpayments = "$"+Str.formatForMoney(tms.getMaxPossiblePayoutForResponses());
                maxincentive = "$"+Str.formatForMoney(tms.getMaxPossiblePayoutForResponses());
                dneerofee = "$"+Str.formatForMoney(tms.getMaxPossibledNeeroFee());
                maxpossiblespend = "$"+Str.formatForMoney(tms.getMaxPossibleSpend());

                //The user's current account balance
                CurrentBalanceCalculator cbc = new CurrentBalanceCalculator(Pagez.getUserSession().getUser());
                double currentbalance = cbc.getCurrentbalance();




                //Only worry about the credit carc stuff if there's not enough in the account currently
                logger.debug("currentbalance: "+currentbalance);
                logger.debug("tms.getMaxPossibleSpend()*(ResearcherRemainingBalanceOperations.INCREMENTALPERCENTTOCHARGE/100): "+(tms.getMaxPossibleSpend()*(ResearcherRemainingBalanceOperations.INCREMENTALPERCENTTOCHARGE/100)));
                if(currentbalance<(tms.getMaxPossibleSpend()*(ResearcherRemainingBalanceOperations.INCREMENTALPERCENTTOCHARGE/100))){
                    logger.debug("currentbalance is < tms.get.....");
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



    public void save() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("save() called.");
        if (status<=twitask.STATUS_DRAFT){
            UserSession userSession = Pagez.getUserSession();

            if (Pagez.getUserSession().getUser()!=null && twitask.canEdit(Pagez.getUserSession().getUser())){
                Calendar startdate = Time.getCalFromDate(twitask.getStartdate());
                Calendar now = Calendar.getInstance();
                TwitaskMoneyStatus sms = new TwitaskMoneyStatus(twitask);
                logger.debug("now="+Time.dateformatfordb(now));
                logger.debug("startdate="+Time.dateformatfordb(startdate));
                logger.debug("startdate.before(now)="+startdate.before(now));
                logger.debug("startdate.after(now)="+startdate.after(now));

                CurrentBalanceCalculator cbc = new CurrentBalanceCalculator(Pagez.getUserSession().getUser());
                double currentbalance = cbc.getCurrentbalance();

                //See if there's enough in the account
                boolean enoughinaccountnow = true;
                if (currentbalance<(sms.getMaxPossibleSpend()*(ResearcherRemainingBalanceOperations.INCREMENTALPERCENTTOCHARGE/100))){
                    enoughinaccountnow = false;
                }
                //Only worry about the credit card stuff if there's not enough in the account currently
                boolean ccinfolooksok = true;
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

                //Manage the status
                if (enoughinaccountnow){
                    if (startdate.before(now)){
                        twitask.setStatus(twitask.STATUS_OPEN);
                        twitask.setStartdate(new Date());
                    } else {
                        twitask.setStatus(twitask.STATUS_WAITINGFORSTARTDATE);
                    }
                } else {
                    twitask.setStatus(twitask.STATUS_WAITINGFORFUNDS);
                }

                //Set the reseller code
                twitask.setResellercode(resellercode);

                //Save the survey
                try{
                    logger.debug("save() about to save twitask.getTwitaskid()=" + twitask.getTwitaskid());
                    twitask.save();
                    EmbedCacheFlusher.flushCache(twitask.getTwitaskid());
                    logger.debug("save() done saving twitask.getTwitaskid()=" + twitask.getTwitaskid());
                } catch (GeneralException gex){
                    logger.debug("save() failed: " + gex.getErrorsAsSingleString());
                    String message = "save() save failed: " + gex.getErrorsAsSingleString();
                    vex.addValidationError(message);
                    throw vex;
                }

                //Charge the per-survey creation fee
                MoveMoneyInAccountBalance.charge(userSession.getUser(), TwitaskMoneyStatus.PERTWITASKCREATIONFEE, "TwitAsk creation fee for '"+twitask.getQuestion()+"'", true, false, false, false);


                //Make sure user has enough in their account by running the remaining balance algorithm for just this researcher
                if (Pagez.getUserSession().getUser()!=null){
                    //Run in its own thread so that the user's screen progresses
                    //ResearcherSurveyDetail06BalancecheckThread thr = new ResearcherSurveyDetail06BalancecheckThread(Researcher.get(Pagez.getUserSession().getUser().getResearcherid()));
                    //thr.startThread();
                    ResearcherRemainingBalanceOperations.processResearcher(Researcher.get(Pagez.getUserSession().getUser().getResearcherid()));
                }

                //Refresh
                twitask.refresh();

                //InstantNotify
                InstantNotifyOfNewSurvey inons = new InstantNotifyOfNewSurvey(twitask.getTwitaskid());
                inons.sendNotifications();
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
                    resellername = userReseller.getFirstname()+" "+userReseller.getLastname();
                    twitask.setResellercode(resellercode);
                    try{
                        twitask.save();
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


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    public Twitask getTwitask() {
        return twitask;
    }

    public void setTwitask(Twitask twitask) {
        this.twitask=twitask;
    }

    public TwitaskMoneyStatus getTms() {
        return tms;
    }

    public void setTms(TwitaskMoneyStatus tms) {
        this.tms=tms;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status=status;
    }

    public int getNumberofbloggersqualifiedforthissurvey() {
        return numberofbloggersqualifiedforthissurvey;
    }

    public void setNumberofbloggersqualifiedforthissurvey(int numberofbloggersqualifiedforthissurvey) {
        this.numberofbloggersqualifiedforthissurvey=numberofbloggersqualifiedforthissurvey;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate=startdate;
    }

    public String getMaxrespondentpayments() {
        return maxrespondentpayments;
    }

    public void setMaxrespondentpayments(String maxrespondentpayments) {
        this.maxrespondentpayments=maxrespondentpayments;
    }

    public String getMaxincentive() {
        return maxincentive;
    }

    public void setMaxincentive(String maxincentive) {
        this.maxincentive=maxincentive;
    }

    public String getDneerofee() {
        return dneerofee;
    }

    public void setDneerofee(String dneerofee) {
        this.dneerofee=dneerofee;
    }

    public String getMaxpossiblespend() {
        return maxpossiblespend;
    }

    public void setMaxpossiblespend(String maxpossiblespend) {
        this.maxpossiblespend=maxpossiblespend;
    }

    public String getResellercode() {
        return resellercode;
    }

    public void setResellercode(String resellercode) {
        this.resellercode=resellercode;
    }

    public String getCcnumfordisplayonscreen() {
        return ccnumfordisplayonscreen;
    }

    public void setCcnumfordisplayonscreen(String ccnumfordisplayonscreen) {
        this.ccnumfordisplayonscreen=ccnumfordisplayonscreen;
    }

    public String getCcnum() {
        return ccnum;
    }

    public void setCcnum(String ccnum) {
        this.ccnum=ccnum;
    }

    public int getCctype() {
        return cctype;
    }

    public void setCctype(int cctype) {
        this.cctype=cctype;
    }

    public String getCvv2() {
        return cvv2;
    }

    public void setCvv2(String cvv2) {
        this.cvv2=cvv2;
    }

    public int getCcexpmo() {
        return ccexpmo;
    }

    public void setCcexpmo(int ccexpmo) {
        this.ccexpmo=ccexpmo;
    }

    public int getCcexpyear() {
        return ccexpyear;
    }

    public void setCcexpyear(int ccexpyear) {
        this.ccexpyear=ccexpyear;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode=postalcode;
    }

    public String getCcstate() {
        return ccstate;
    }

    public void setCcstate(String ccstate) {
        this.ccstate=ccstate;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street=street;
    }

    public String getCccity() {
        return cccity;
    }

    public void setCccity(String cccity) {
        this.cccity=cccity;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname=firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname=lastname;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
        this.ipaddress=ipaddress;
    }

    public String getMerchantsessionid() {
        return merchantsessionid;
    }

    public void setMerchantsessionid(String merchantsessionid) {
        this.merchantsessionid=merchantsessionid;
    }

    public boolean getWarningdonthaveccinfo() {
        return warningdonthaveccinfo;
    }

    public void setWarningdonthaveccinfo(boolean warningdonthaveccinfo) {
        this.warningdonthaveccinfo=warningdonthaveccinfo;
    }
}