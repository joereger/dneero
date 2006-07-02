package com.dneero.session;

import javax.faces.webapp.UIComponentTag;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.application.Application;
import javax.faces.el.ValueBinding;

/**
 * User: Joe Reger Jr
 * Date: Jun 22, 2006
 * Time: 5:22:12 PM
 */
public class AuthorizationTaghandler extends UIComponentTag {

    // Declare a bean property for the hellomsg attribute.
    public String acl = null;
    public String redirectonfail = null;


    // Associate the renderer and component type.
    public String getComponentType() { return "com.dneero.session.Authorization"; }
    public String getRendererType() { return null; }


    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);
        

        if (acl != null){
            if (isValueReference(acl)) {
                FacesContext context = FacesContext.getCurrentInstance();
                Application app = context.getApplication();
                ValueBinding vb = app.createValueBinding(acl);
                component.setValueBinding("acl", vb);
            } else {
                component.getAttributes().put("acl", acl);
            }
        }

        if (redirectonfail != null){
            if (isValueReference(redirectonfail)) {
                FacesContext context = FacesContext.getCurrentInstance();
                Application app = context.getApplication();
                ValueBinding vb = app.createValueBinding(redirectonfail);
                component.setValueBinding("redirectonfail", vb);
            } else {
                component.getAttributes().put("redirectonfail", redirectonfail);
            }
        }


    }

    public void release(){
        super.release();
        acl = null;
        redirectonfail = null;

    }





}
