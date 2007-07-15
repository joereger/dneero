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
public class PageloadTaghandler extends UIComponentTag {

    // Declare a bean property for the hellomsg attribute.



    // Associate the renderer and component type.
    public String getComponentType() { return "com.dneero.session.Pageload"; }
    public String getRendererType() { return null; }


    protected void setProperties(UIComponent component)
    {
        super.setProperties(component);
        
    }

    public void release(){
        super.release();
    }



}
