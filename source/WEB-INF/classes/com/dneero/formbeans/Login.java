package com.dneero.formbeans;

import org.apache.log4j.Logger;

import java.util.Date;

import com.dneero.dao.Offer;
import com.dneero.util.GeneralException;

/**
 * User: Joe Reger Jr
 * Date: Jun 15, 2006
 * Time: 9:54:08 AM
 */
public class Login {

    private String email;
    private String password;

    Logger logger = Logger.getLogger(this.getClass().getName());

    public Login(){
        logger.debug("Instanciating a NewOffer object.");
        //offer = new Offer();
    }

    public String login(){
        logger.debug("login() called.");




        return "success";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
