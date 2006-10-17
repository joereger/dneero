package com.dneero.formbeans;

import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;

import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;
import com.dneero.util.Num;
import com.dneero.dao.Blogger;
import com.dneero.dao.Userrole;
import com.dneero.dao.User;
import com.dneero.dao.Creditcard;
import com.dneero.session.UserSession;
import com.dneero.session.Roles;
import com.dneero.money.PaymentMethod;


/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class BloggerDetails {

    //Form props
    private Date birthdate;
    private int notifyofnewsurveysbyemail=1;
    private String gender;
    private String ethnicity;
    private String income;
    private String maritalstatus;
    private String educationlevel;
    private String state;
    private String city;
    private String profession;
    private String blogfocus;
    private String politics;
    private int paymethod;
    private String paymethodpaypaladdress;
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



    //Other props
    private int userid;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public BloggerDetails(){
        UserSession userSession = Jsf.getUserSession();
        if (userSession.getUser()!=null && userSession.getUser().getBlogger()!=null){
            Blogger blogger = userSession.getUser().getBlogger();
            birthdate = blogger.getBirthdate();
            if (blogger.getNotifyofnewsurveysbyemail()){
                notifyofnewsurveysbyemail = 1;
            } else {
                notifyofnewsurveysbyemail = 0;
            }
            gender = String.valueOf(blogger.getGender());
            ethnicity = String.valueOf(blogger.getEthnicity());
            income = String.valueOf(blogger.getIncomerange());
            maritalstatus = String.valueOf(blogger.getMaritalstatus());
            educationlevel = String.valueOf(blogger.getEducationlevel());
            state = String.valueOf(blogger.getState());
            city = String.valueOf(blogger.getCity());
            profession = String.valueOf(blogger.getProfession());
            politics = String.valueOf(blogger.getPolitics());
            paymethod = userSession.getUser().getPaymethod();
            paymethodpaypaladdress = userSession.getUser().getPaymethodpaypaladdress();

            if(userSession.getUser().getPaymethodcreditcardid()>0){
                Creditcard cc = Creditcard.get(userSession.getUser().getPaymethodcreditcardid());
                ccnum = cc.getCcnum();
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
            }
        }
    }

    public LinkedHashMap getPaymethods(){
        LinkedHashMap out = new LinkedHashMap();
        out.put("PayPal", PaymentMethod.PAYMENTMETHODPAYPAL);
        out.put("Credit Card", PaymentMethod.PAYMENTMETHODCREDITCARD);
        return out;
    }

    public LinkedHashMap getCreditcardtypes(){
        LinkedHashMap out = new LinkedHashMap();
        out.put("Visa", Creditcard.CREDITCARDTYPE_VISA);
        out.put("Master Card", Creditcard.CREDITCARDTYPE_MASTERCARD);
        out.put("American Express", Creditcard.CREDITCARDTYPE_AMEX);
        out.put("Discover", Creditcard.CREDITCARDTYPE_DISCOVER);
        return out;
    }

    public void setPaymethods(){
        
    }

    public String saveAction(){

        UserSession userSession = Jsf.getUserSession();


        //Start validation
        if (paymethod==PaymentMethod.PAYMENTMETHODCREDITCARD){
            //@todo better validation
            if (ccnum.equals("")){
                Jsf.setFacesMessage("bloggerdetails:ccnum", "You've chosen to be paid via credit card so you must provide a credit card number.");
                return "";
            }
        }
        //End validation

        Blogger blogger;
        boolean isnewblogger = false;
        if (userSession.getUser()!=null && userSession.getUser().getBlogger()!=null){
            blogger =  userSession.getUser().getBlogger();
        } else {
            blogger = new Blogger();
            blogger.setQuality(0);
            blogger.setQuality90days(0);
            blogger.setLastnewsurveynotificationsenton(new Date());
            isnewblogger = true;
        }

        if (userSession.getUser()!=null){

            blogger.setUserid(userSession.getUser().getUserid());
            blogger.setBirthdate(birthdate);
            blogger.setEducationlevel(educationlevel);
            blogger.setEthnicity(ethnicity);
            blogger.setGender(gender);
            blogger.setIncomerange(income);
            blogger.setMaritalstatus(maritalstatus);
            blogger.setState(state);
            blogger.setCity(city);
            blogger.setProfession(profession);
            blogger.setPolitics(politics);
            if (notifyofnewsurveysbyemail==0){
                blogger.setNotifyofnewsurveysbyemail(false);
            } else {
                blogger.setNotifyofnewsurveysbyemail(true);
            }

            if (userSession.getUser().getBlogger()==null){
                userSession.getUser().setBlogger(blogger);
            }

            try{
                blogger.save();
            } catch (GeneralException gex){
                Jsf.setFacesMessage("Error saving record: "+gex.getErrorsAsSingleString());
                logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                return null;
            }

           



            boolean hasroleassigned = false;
            if (userSession.getUser()!=null && userSession.getUser().getUserroles()!=null){
                for (Iterator iterator = userSession.getUser().getUserroles().iterator(); iterator.hasNext();) {
                    Userrole role =  (Userrole)iterator.next();
                    if (role.getRoleid()== Roles.BLOGGER){
                        hasroleassigned = true;
                    }
                }
            }
            if (!hasroleassigned && userSession.getUser()!=null){
                Userrole role = new Userrole();
                role.setUserid(userSession.getUser().getUserid());
                role.setRoleid(Roles.BLOGGER);
                userSession.getUser().getUserroles().add(role);
                try{
                    role.save();
                } catch (GeneralException gex){
                    Jsf.setFacesMessage("Error saving role record: "+gex.getErrorsAsSingleString());
                    logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                    return null;
                }
            }


            Creditcard cc = new Creditcard();
            if(paymethod==PaymentMethod.PAYMENTMETHODCREDITCARD){
                if(userSession.getUser().getPaymethodcreditcardid()>0){
                    cc = Creditcard.get(userSession.getUser().getPaymethodcreditcardid());
                }
                cc.setCcexpmo(ccexpmo);
                cc.setCcexpyear(ccexpyear);
                cc.setCcnum(ccnum);
                cc.setCctype(cctype);
                cc.setCity(cccity);
                cc.setCvv2(cvv2);
                cc.setFirstname(firstname);
                //@todo set IP Address for paypal
                cc.setIpaddress("192.168.1.1");
                cc.setLastname(lastname);
                //@todo set merchant sessionid for paypal
                cc.setMerchantsessionid("12345");
                cc.setPostalcode(postalcode);
                cc.setState(ccstate);
                cc.setStreet(street);
                cc.setUserid(userSession.getUser().getUserid());
                try{
                    cc.save();
                } catch (GeneralException gex){
                    Jsf.setFacesMessage("Error saving record: "+gex.getErrorsAsSingleString());
                    logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                    return null;
                }
            }



            userSession.getUser().setPaymethod(paymethod);
            if(paymethod==PaymentMethod.PAYMENTMETHODCREDITCARD){
                userSession.getUser().setPaymethodcreditcardid(cc.getCreditcardid());
            }
            userSession.getUser().setPaymethodpaypaladdress(paymethodpaypaladdress);


            try{
                userSession.getUser().save();
            } catch (GeneralException gex){
                Jsf.setFacesMessage("Error saving record: "+gex.getErrorsAsSingleString());
                logger.debug("saveAction failed: " + gex.getErrorsAsSingleString());
                return null;
            }





            userSession.getUser().refresh();

            if (isnewblogger){
                return "success_newblogger";
            } else {
                return "accountmain";
            }
        } else {
            Jsf.setFacesMessage("UserSession.getUser() is null.  Please log in.");
            return null;
        }
    }






    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public String getMaritalstatus() {
        return maritalstatus;
    }

    public void setMaritalstatus(String maritalstatus) {
        this.maritalstatus = maritalstatus;
    }

    public String getEducationlevel() {
        return educationlevel;
    }

    public void setEducationlevel(String educationlevel) {
        this.educationlevel = educationlevel;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getBlogfocus() {
        return blogfocus;
    }

    public void setBlogfocus(String blogfocus) {
        this.blogfocus = blogfocus;
    }

    public String getPolitics() {
        return politics;
    }

    public void setPolitics(String politics) {
        this.politics = politics;
    }



    public int getNotifyofnewsurveysbyemail() {
        return notifyofnewsurveysbyemail;
    }

    public void setNotifyofnewsurveysbyemail(int notifyofnewsurveysbyemail) {
        this.notifyofnewsurveysbyemail = notifyofnewsurveysbyemail;
    }


    public int getPaymethod() {
        return paymethod;
    }

    public void setPaymethod(int paymethod) {
        this.paymethod = paymethod;
    }

    public String getPaymethodpaypaladdress() {
        return paymethodpaypaladdress;
    }

    public void setPaymethodpaypaladdress(String paymethodpaypaladdress) {
        this.paymethodpaypaladdress = paymethodpaypaladdress;
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
}
