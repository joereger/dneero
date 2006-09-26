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
public class PercentCompleteBarTaghandler extends UIComponentTag {

    // Declare a bean property for the hellomsg attribute.
    public String currentvalue = null;
    public String maximumvalue = null;
    public String mintitle = null;
    public String maxtitle = null;
    public String widthinpixels = null;


    // Associate the renderer and component type.
    public String getComponentType() { return "com.dneero.ui.PercentCompleteBar"; }
    public String getRendererType() { return null; }


    protected void setProperties(UIComponent component){
        super.setProperties(component);

        if (currentvalue != null){
            if (isValueReference(currentvalue)) {
                FacesContext context = FacesContext.getCurrentInstance();
                Application app = context.getApplication();
                ValueBinding vb = app.createValueBinding(currentvalue);
                component.setValueBinding("currentvalue", vb);
            } else {
                component.getAttributes().put("currentvalue", currentvalue);
            }
        }

        if (maximumvalue != null){
            if (isValueReference(maximumvalue)) {
                FacesContext context = FacesContext.getCurrentInstance();
                Application app = context.getApplication();
                ValueBinding vb = app.createValueBinding(maximumvalue);
                component.setValueBinding("maximumvalue", vb);
            } else {
                component.getAttributes().put("maximumvalue", maximumvalue);
            }
        }

        if (mintitle != null){
            if (isValueReference(mintitle)) {
                FacesContext context = FacesContext.getCurrentInstance();
                Application app = context.getApplication();
                ValueBinding vb = app.createValueBinding(mintitle);
                component.setValueBinding("mintitle", vb);
            } else {
                component.getAttributes().put("mintitle", mintitle);
            }
        }

        if (maxtitle != null){
            if (isValueReference(maxtitle)) {
                FacesContext context = FacesContext.getCurrentInstance();
                Application app = context.getApplication();
                ValueBinding vb = app.createValueBinding(maxtitle);
                component.setValueBinding("maxtitle", vb);
            } else {
                component.getAttributes().put("maxtitle", maxtitle);
            }
        }

        if (widthinpixels != null){
            if (isValueReference(widthinpixels)) {
                FacesContext context = FacesContext.getCurrentInstance();
                Application app = context.getApplication();
                ValueBinding vb = app.createValueBinding(widthinpixels);
                component.setValueBinding("widthinpixels", vb);
            } else {
                component.getAttributes().put("widthinpixels", widthinpixels);
            }
        }


    }

    public void release(){
        super.release();
        currentvalue = null;
        maximumvalue = null;
        mintitle = null;
        maxtitle = null;
        widthinpixels = null;
    }





}
