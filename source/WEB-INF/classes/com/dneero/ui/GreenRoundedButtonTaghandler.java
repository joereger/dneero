package com.dneero.ui;

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
public class GreenRoundedButtonTaghandler extends UIComponentTag {

    // Declare a bean property for the hellomsg attribute.
    public String pathtoapproot = null;


    // Associate the renderer and component type.
    public String getComponentType() { return "com.dneero.ui.GreenRoundedButton"; }
    public String getRendererType() { return null; }


    protected void setProperties(UIComponent component){
        super.setProperties(component);

        if (pathtoapproot != null){
            if (isValueReference(pathtoapproot)) {
                FacesContext context = FacesContext.getCurrentInstance();
                Application app = context.getApplication();
                ValueBinding vb = app.createValueBinding(pathtoapproot);
                component.setValueBinding("pathtoapproot", vb);
            } else {
                component.getAttributes().put("pathtoapproot", pathtoapproot);
            }
        }



    }

    public void release(){
        super.release();
        pathtoapproot = null;
    }





}
