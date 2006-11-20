package com.dneero.util;

import javax.faces.convert.Converter;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;

public class DisplayAsMoneyConverter implements Converter {

    public String getAsString(FacesContext context,
							  UIComponent component, Object value) {

        String str = String.valueOf(value);
        if (Num.isdouble(str)){
            return Str.formatForMoney(Double.parseDouble(str));
        }
        return "";
    }

    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (Num.isdouble(value)){
            return Str.formatForMoney(Double.parseDouble(value));
        }
        return "";
    }

}
