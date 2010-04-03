package com.dneero.htmluibeans;

import com.dneero.dao.User;
import com.dneero.htmlui.Pagez;
import com.dneero.htmlui.ValidationException;
import com.dneero.util.GeneralException;
import com.dneero.util.Num;
import org.apache.log4j.Logger;

import java.io.Serializable;

/**
 * User: Joe Reger Jr
 * Date: Apr 21, 2006
 * Time: 10:38:03 AM
 */
public class LostPasswordChoose implements Serializable {

    //Form props
    private String password;
    private String passwordverify;
    private String u;
    private String k;

    public LostPasswordChoose(){

    }

    public void initBean(){
        checkKey();
    }

    public void checkKey() {
        Logger logger = Logger.getLogger(this.getClass().getName());

        User user=null;
        if (Pagez.getRequest().getParameter("u")!=null && Num.isinteger(Pagez.getRequest().getParameter("u"))){
            user = User.get(Integer.parseInt(Pagez.getRequest().getParameter("u")));
        }

        String k = "";
        if (Pagez.getRequest().getParameter("k")!=null && !Pagez.getRequest().getParameter("k").equals("")){
            k = Pagez.getRequest().getParameter("k");
        }

        logger.debug("user.getUserid()="+user.getUserid()+" k="+k);

        if (user!=null && user.getEmailactivationkey()!=null && k!=null && user.getEmailactivationkey().trim().equals(k.trim())){
            user.setIsactivatedbyemail(true);
            try{
                user.save();
            } catch (GeneralException gex){
                logger.error("registerAction failed: " + gex.getErrorsAsSingleString());
            }

            //Set the flag in the session that'll allow this user to reset their password
            if (Pagez.getUserSession()!=null){
                Pagez.getUserSession().setAllowedToResetPasswordBecauseHasValidatedByEmail(true);
                Pagez.getUserSession().setUser(user);
                Pagez.getUserSession().setIsloggedin(false);
                return;
            } else {
                //Pagez.sendRedirect("/lostpassword.jsp");
                return;
            }
        } else {
            //Pagez.sendRedirect("/lostpassword.jsp");
            return;
        }
    }

    public void choosePassword() throws ValidationException {
        ValidationException vex = new ValidationException();
        Logger logger = Logger.getLogger(this.getClass().getName());

        boolean haveErrors = false;

        if (password==null || password.equals("") || password.length()<6){
            vex.addValidationError("Password must be at least six characters long.");
            haveErrors = true;
        }

        if (!password.equals(passwordverify)){
            vex.addValidationError("Password and Verify Password must match.");
            haveErrors = true;
        }


        
        if (haveErrors){
            throw vex;
        }

        //Checks the key and userid again
        checkKey();


        if (Pagez.getUserSession().getIsAllowedToResetPasswordBecauseHasValidatedByEmail()){

            User user = Pagez.getUserSession().getUser();
            user.setPassword(password);
            user.setLastlogindate(new java.util.Date());
            try{
                user.save();
            } catch (GeneralException gex){
                logger.error("registerAction failed: " + gex.getErrorsAsSingleString());
            }

            Pagez.getUserSession().setIsloggedin(true);
        } else {
            vex.addValidationError("Sorry, it doesn't appear that you came to this page from a valid email link.");
            throw vex;
        }
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordverify() {
        return passwordverify;
    }

    public void setPasswordverify(String passwordverify) {
        this.passwordverify = passwordverify;
    }




    public String getU() {
        return u;
    }

    public void setU(String u) {
        this.u=u;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k=k;
    }


}
