package com.dneero.util;

import javax.faces.context.FacesContext;

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

}
