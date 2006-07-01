package com.dneero.converters;

import com.dneero.dao.*;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.convert.ConverterException;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Jun 28, 2006
 * Time: 11:09:14 AM
 */
public class Surveycriteriagender implements javax.faces.convert.Converter {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String string) throws ConverterException {
        Set<com.dneero.dao.Surveycriteriagender> ocg = new HashSet<com.dneero.dao.Surveycriteriagender>();

        logger.debug("getAsObject() called");

        uiComponent.getAttributes();

        Iterator keyValuePairs = uiComponent.getAttributes().entrySet().iterator();
        for (int i = 0; i < uiComponent.getAttributes().size(); i++){
            Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
            String key = (String)mapentry.getKey();
            String value = (String)mapentry.getValue();
            logger.debug("getAsObject() -- key:"+key+" value:"+value);
        }

        return ocg;
    }

    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) throws ConverterException {


        return "";
    }
}
