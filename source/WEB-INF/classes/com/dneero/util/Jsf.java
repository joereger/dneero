package com.dneero.util;

import com.dneero.session.UserSession;

import javax.faces.context.FacesContext;
import javax.faces.application.FacesMessage;
import javax.faces.el.ValueBinding;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * User: Joe Reger Jr
 * Date: Jun 21, 2006
 * Time: 2:24:00 PM
 */
public class Jsf {

    public static Object getManagedBean(String beanName){
        if (FacesContext.getCurrentInstance()!=null){
            Object out = FacesContext.getCurrentInstance().getApplication().getVariableResolver().resolveVariable(FacesContext.getCurrentInstance(), beanName);
            return out;
        }
        return null;
    }

    public static void bindObjectToExpressionLanguage(String elNameBoundTo, Object obj){
        FacesContext ctx = FacesContext.getCurrentInstance();
        ValueBinding binding = ctx.getApplication().createValueBinding(elNameBoundTo);
        binding.setValue(ctx, obj);
    }

    public static void setFacesMessage(String message){
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage( FacesMessage.SEVERITY_INFO, message, message));
    }

    public static void setFacesMessage(String clientId, String message){
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage( FacesMessage.SEVERITY_INFO, message, message));
    }

    public static UserSession getUserSession(){
        return (UserSession)getManagedBean("userSession");
    }

    public static HttpServletRequest getHttpServletRequest(){
        return (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    public static void redirectResponse(String url) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(url);
    }

    public static String getRequestParam(String paramName){
        return (String)FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(paramName);
    }

}
