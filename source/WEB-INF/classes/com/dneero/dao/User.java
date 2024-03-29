package com.dneero.dao;

import com.dneero.dao.hibernate.BasePersistentClass;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.session.AuthControlled;
import org.apache.log4j.Logger;

import java.util.Set;
import java.util.HashSet;
import java.util.Date;
// Generated Apr 17, 2006 3:45:25 PM by Hibernate Tools 3.1.0.beta4



/**
 * User generated by hbm2java
 */

public class User extends BasePersistentClass implements java.io.Serializable, AuthControlled {


     private int userid;
     private boolean isenabled;
     private int bloggerid;
     private int researcherid;
     private int plid;
     private int referredbyuserid;
     private boolean isqualifiedforrevshare;
     private String email;
     private String password;
     private String name;
     private boolean isactivatedbyemail;
     private String emailactivationkey;
     private Date emailactivationlastsent;
     private Date createdate;
     private int paymethod;
     private int paymethodcreditcardid;
     private String paymethodpaypaladdress;
     private int chargemethod;
     private int chargemethodcreditcardid;
     private int notifyofnewsurveysbyemaileveryexdays;
     private Date notifyofnewsurveyslastsent;
     private boolean allownoncriticalemails;
     private boolean instantnotifybyemailison;
     private boolean instantnotifyxmppison;
     private String instantnotifyxmppusername;
     private double charityamtdonated;
     private int facebookuserid;
     private boolean isfacebookappremoved;
     private Date facebookappremoveddate;
     private String resellercode;
     private double resellerpercent;
     private double currentbalance;
     private double currentbalanceblogger;
     private double currentbalanceresearcher;
     private Date lastlogindate;
     private String nickname;
     private double sirpoints;
     private int sirrank;
     private Date sirdate;
     private String sirdebug;
     private int siralgorithm;
     private boolean isanonymous;

     //Association
    private Set<Userrole> userroles = new HashSet<Userrole>();
    private Set<Usereula> usereulas = new HashSet<Usereula>();
    private Set<Responsepending> responsependings = new HashSet<Responsepending>();
    //@todo performance check: should surveydiscusses be included in all user loads?
    private Set<Surveydiscuss> surveydiscusses = new HashSet<Surveydiscuss>();



    public static User get(int id) {
        Logger logger = Logger.getLogger("com.dneero.dao.User");
        try {
            logger.debug("User.get(" + id + ") called.");
            User obj = (User) HibernateUtil.getSession().get(User.class, id);
            if (obj == null) {
                logger.debug("User.get(" + id + ") returning new instance because hibernate returned null.");
                return new User();
            }
            return obj;
        } catch (Exception ex) {
            logger.error("com.dneero.dao.User", ex);
            return new User();
        }
    }

    // Constructors

    /** default constructor */
    public User() {
    }

    public boolean canRead(User user){
        if (user.getUserid()==userid){
            return true;
        }
        return false;
    }

    public boolean canEdit(User user){
        return canRead(user);
    }


    

   
    // Property accessors

    public int getUserid() {
        return this.userid;
    }
    
    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getPlid() {
        return plid;
    }

    public void setPlid(int plid) {
        this.plid=plid;
    }

    public boolean getIsenabled() {
        return isenabled;
    }

    public void setIsenabled(boolean isenabled) {
        this.isenabled = isenabled;
    }

    public int getReferredbyuserid() {
        return referredbyuserid;
    }

    public void setReferredbyuserid(int referredbyuserid) {
        this.referredbyuserid = referredbyuserid;
    }

    public boolean getIsqualifiedforrevshare() {
        return isqualifiedforrevshare;
    }

    public void setIsqualifiedforrevshare(boolean isqualifiedforrevshare) {
        this.isqualifiedforrevshare = isqualifiedforrevshare;
    }

    public String getEmail() {
        return this.email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsactivatedbyemail() {
        return isactivatedbyemail;
    }

    public void setIsactivatedbyemail(boolean isactivatedbyemail) {
        this.isactivatedbyemail = isactivatedbyemail;
    }

    public String getEmailactivationkey() {
        return emailactivationkey;
    }

    public void setEmailactivationkey(String emailactivationkey) {
        this.emailactivationkey = emailactivationkey;
    }

    public Date getEmailactivationlastsent() {
        return emailactivationlastsent;
    }

    public void setEmailactivationlastsent(Date emailactivationlastsent) {
        this.emailactivationlastsent = emailactivationlastsent;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
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


    public int getBloggerid() {
        return bloggerid;
    }

    public void setBloggerid(int bloggerid) {
        this.bloggerid = bloggerid;
    }

    public int getResearcherid() {
        return researcherid;
    }

    public void setResearcherid(int researcherid) {
        this.researcherid = researcherid;
    }

    public int getChargemethod() {
        return chargemethod;
    }

    public void setChargemethod(int chargemethod) {
        this.chargemethod = chargemethod;
    }


    public int getPaymethodcreditcardid() {
        return paymethodcreditcardid;
    }

    public void setPaymethodcreditcardid(int paymethodcreditcardid) {
        this.paymethodcreditcardid = paymethodcreditcardid;
    }

    public int getChargemethodcreditcardid() {
        return chargemethodcreditcardid;
    }

    public void setChargemethodcreditcardid(int chargemethodcreditcardid) {
        this.chargemethodcreditcardid = chargemethodcreditcardid;
    }

    public int getNotifyofnewsurveysbyemaileveryexdays() {
        return notifyofnewsurveysbyemaileveryexdays;
    }

    public void setNotifyofnewsurveysbyemaileveryexdays(int notifyofnewsurveysbyemaileveryexdays) {
        this.notifyofnewsurveysbyemaileveryexdays = notifyofnewsurveysbyemaileveryexdays;
    }


    public Date getNotifyofnewsurveyslastsent() {
        return notifyofnewsurveyslastsent;
    }

    public void setNotifyofnewsurveyslastsent(Date notifyofnewsurveyslastsent) {
        this.notifyofnewsurveyslastsent = notifyofnewsurveyslastsent;
    }


    public boolean getAllownoncriticalemails() {
        return allownoncriticalemails;
    }

    public void setAllownoncriticalemails(boolean allownoncriticalemails) {
        this.allownoncriticalemails = allownoncriticalemails;
    }


    public boolean getInstantnotifybyemailison() {
        return instantnotifybyemailison;
    }

    public void setInstantnotifybyemailison(boolean instantnotifybyemailison) {
        this.instantnotifybyemailison = instantnotifybyemailison;
    }

    public boolean getInstantnotifyxmppison() {
        return instantnotifyxmppison;
    }

    public void setInstantnotifyxmppison(boolean instantnotifyxmppison) {
        this.instantnotifyxmppison = instantnotifyxmppison;
    }

    public String getInstantnotifyxmppusername() {
        return instantnotifyxmppusername;
    }

    public void setInstantnotifyxmppusername(String instantnotifyxmppusername) {
        this.instantnotifyxmppusername = instantnotifyxmppusername;
    }

    public Set<Userrole> getUserroles() {
        return userroles;
    }

    public void setUserroles(Set<Userrole> userroles) {
        this.userroles = userroles;
    }

    public Set<Usereula> getUsereulas() {
        return usereulas;
    }

    public void setUsereulas(Set<Usereula> usereulas) {
        this.usereulas = usereulas;
    }

    public Set<Responsepending> getResponsependings() {
        return responsependings;
    }

    public void setResponsependings(Set<Responsepending> responsependings) {
        this.responsependings = responsependings;
    }

    public Set<Surveydiscuss> getSurveydiscusses() {
        return surveydiscusses;
    }

    public void setSurveydiscusses(Set<Surveydiscuss> surveydiscusses) {
        this.surveydiscusses = surveydiscusses;
    }

    public double getCharityamtdonated() {
        return charityamtdonated;
    }

    public void setCharityamtdonated(double charityamtdonated) {
        this.charityamtdonated = charityamtdonated;
    }

    public int getFacebookuserid() {
        return facebookuserid;
    }

    public void setFacebookuserid(int facebookuserid) {
        this.facebookuserid = facebookuserid;
    }

    public boolean getIsfacebookappremoved() {
        return isfacebookappremoved;
    }

    public void setIsfacebookappremoved(boolean isfacebookappremoved) {
        this.isfacebookappremoved = isfacebookappremoved;
    }

    public Date getFacebookappremoveddate() {
        return facebookappremoveddate;
    }

    public void setFacebookappremoveddate(Date facebookappremoveddate) {
        this.facebookappremoveddate = facebookappremoveddate;
    }

    public String getResellercode() {
        return resellercode;
    }

    public void setResellercode(String resellercode) {
        this.resellercode = resellercode;
    }

    public double getResellerpercent() {
        return resellerpercent;
    }

    public void setResellerpercent(double resellerpercent) {
        this.resellerpercent = resellerpercent;
    }

    public double getCurrentbalance() {
        return currentbalance;
    }

    public void setCurrentbalance(double currentbalance) {
        this.currentbalance=currentbalance;
    }

    public double getCurrentbalanceblogger() {
        return currentbalanceblogger;
    }

    public void setCurrentbalanceblogger(double currentbalanceblogger) {
        this.currentbalanceblogger=currentbalanceblogger;
    }

    public double getCurrentbalanceresearcher() {
        return currentbalanceresearcher;
    }

    public void setCurrentbalanceresearcher(double currentbalanceresearcher) {
        this.currentbalanceresearcher=currentbalanceresearcher;
    }

    public Date getLastlogindate() {
        return lastlogindate;
    }

    public void setLastlogindate(Date lastlogindate) {
        this.lastlogindate=lastlogindate;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname=nickname;
    }



    public int getSirrank() {
        return sirrank;
    }

    public void setSirrank(int sirrank) {
        this.sirrank=sirrank;
    }

    public Date getSirdate() {
        return sirdate;
    }

    public void setSirdate(Date sirdate) {
        this.sirdate=sirdate;
    }

    public String getSirdebug() {
        return sirdebug;
    }

    public void setSirdebug(String sirdebug) {
        this.sirdebug=sirdebug;
    }

    public int getSiralgorithm() {
        return siralgorithm;
    }

    public void setSiralgorithm(int siralgorithm) {
        this.siralgorithm=siralgorithm;
    }

    public double getSirpoints() {
        return sirpoints;
    }

    public void setSirpoints(double sirpoints) {
        this.sirpoints=sirpoints;
    }

    public boolean getIsanonymous() {
        return isanonymous;
    }

    public void setIsanonymous(boolean isanonymous) {
        this.isanonymous = isanonymous;
    }
}
