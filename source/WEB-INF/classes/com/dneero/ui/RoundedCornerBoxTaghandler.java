package com.dneero.ui;

import javax.faces.webapp.UIComponentTag;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.application.Application;

/**
 * User: Joe Reger Jr
 * Date: Jun 22, 2006
 * Time: 5:22:12 PM
 */
public class RoundedCornerBoxTaghandler extends UIComponentTag {

    // Declare a bean property for the hellomsg attribute.
    public String uniqueboxname = null;
    public String widthinpercent = null;
    public String titlecolor = null;
    public String subtitlecolor = null;
    public String bodycolor = null;
    public String bordercolor = null;
    public String title = null;
    public String subtitle = null;


    // Associate the renderer and component type.
    public String getComponentType() { return "com.dneero.ui.RoundedCornerBox"; }
    public String getRendererType() { return null; }


    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);

        if (uniqueboxname != null){
            if (isValueReference(uniqueboxname)) {
                FacesContext context = FacesContext.getCurrentInstance();
                Application app = context.getApplication();
                ValueBinding vb = app.createValueBinding(uniqueboxname);
                component.setValueBinding("uniqueboxname", vb);
            } else {
                component.getAttributes().put("uniqueboxname", uniqueboxname);
            }
        }

        if (widthinpercent != null){
            if (isValueReference(widthinpercent)) {
                FacesContext context = FacesContext.getCurrentInstance();
                Application app = context.getApplication();
                ValueBinding vb = app.createValueBinding(widthinpercent);
                component.setValueBinding("widthinpercent", vb);
            } else {
                component.getAttributes().put("widthinpercent", widthinpercent);
            }
        }

        if (titlecolor != null){
            if (isValueReference(titlecolor)) {
                FacesContext context = FacesContext.getCurrentInstance();
                Application app = context.getApplication();
                ValueBinding vb = app.createValueBinding(titlecolor);
                component.setValueBinding("titlecolor", vb);
            } else {
                component.getAttributes().put("titlecolor", titlecolor);
            }
        }

        if (subtitlecolor != null){
            if (isValueReference(subtitlecolor)) {
                FacesContext context = FacesContext.getCurrentInstance();
                Application app = context.getApplication();
                ValueBinding vb = app.createValueBinding(subtitlecolor);
                component.setValueBinding("subtitlecolor", vb);
            } else {
                component.getAttributes().put("subtitlecolor", subtitlecolor);
            }
        }

        if (bodycolor != null){
            if (isValueReference(bodycolor)) {
                FacesContext context = FacesContext.getCurrentInstance();
                Application app = context.getApplication();
                ValueBinding vb = app.createValueBinding(bodycolor);
                component.setValueBinding("bodycolor", vb);
            } else {
                component.getAttributes().put("bodycolor", bodycolor);
            }
        }

        if (bordercolor != null){
            if (isValueReference(bordercolor)) {
                FacesContext context = FacesContext.getCurrentInstance();
                Application app = context.getApplication();
                ValueBinding vb = app.createValueBinding(bordercolor);
                component.setValueBinding("bordercolor", vb);
            } else {
                component.getAttributes().put("bordercolor", bordercolor);
            }
        }

        if (title != null){
            if (isValueReference(title)) {
                FacesContext context = FacesContext.getCurrentInstance();
                Application app = context.getApplication();
                ValueBinding vb = app.createValueBinding(title);
                component.setValueBinding("title", vb);
            } else {
                component.getAttributes().put("title", title);
            }
        }

        if (subtitle != null){
            if (isValueReference(subtitle)) {
                FacesContext context = FacesContext.getCurrentInstance();
                Application app = context.getApplication();
                ValueBinding vb = app.createValueBinding(subtitle);
                component.setValueBinding("subtitle", vb);
            } else {
                component.getAttributes().put("subtitle", subtitle);
            }
        }
    }

    public void release(){
        super.release();
        uniqueboxname = null;
        widthinpercent = null;
        titlecolor = null;
        subtitlecolor = null;
        bodycolor = null;
        bordercolor = null;
        title = null;
        subtitle = null;
    }





}
