package com.dneero.util;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;

/**
 * User: Joe Reger Jr
 * Date: Jun 21, 2006
 * Time: 2:24:00 PM
 */
public class Jsf {

    public static Object getManagedBean(String beanName){
        Object out = FacesContext.getCurrentInstance().getApplication().getVariableResolver().resolveVariable(FacesContext.getCurrentInstance(), beanName);
        return out;
    }

    public static void setFacesMessage(String message){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO, message, message));
    }

}
