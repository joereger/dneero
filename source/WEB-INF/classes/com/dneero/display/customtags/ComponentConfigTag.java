package com.dneero.display.customtags;

import org.apache.log4j.Logger;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import java.util.HashMap;

/**
 * User: Joe Reger Jr
 * Date: Jul 7, 2006
 * Time: 10:04:09 AM
 */
public class ComponentConfigTag extends UIComponentTag {

    private String value;
    Logger logger = Logger.getLogger(this.getClass().getName());

    public String getComponentType() {
        return "com.dneero.display.components.ComponentConfig";
    }

    public String getRendererType() {
        return null;
    }

    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        logger.debug("made it to setProperties()");
        FacesContext context = FacesContext.getCurrentInstance();
        if (null != value) {
            logger.debug("value is not null");
            if (isValueReference(value)) {
                logger.debug("isValueReference(value)=true");
                ValueBinding vb = context.getApplication().createValueBinding(value);
                component.setValueBinding("value", vb);
            } else {
                logger.debug("isValueReference(value)=false");
                ((UIInput)component).setValue(value);
            }
        } else {
            logger.debug("value is null");
        }
    }


    public String getValue() {
        logger.debug("getValue() called");
        return value;
    }

    public void setValue(String value) {
        logger.debug("setValue() called");
        this.value = value;
    }

}
