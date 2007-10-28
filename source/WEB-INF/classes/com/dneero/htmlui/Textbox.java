package com.dneero.htmlui;

import com.dneero.util.Str;


/**
 * User: Joe Reger Jr
 * Date: Oct 28, 2007
 * Time: 2:01:39 AM
 */
public class Textbox {

    private String value = "";

    public Textbox(String name, boolean isrequired, int datatyperequired) throws ValidationException {
        if (Pagez.getRequest().getParameter(name)!=null && !Pagez.getRequest().getParameter(name).equals("")){
            
        } else {
            if (isrequired){
                ValidationException vex = new ValidationException();
                vex.addValidationError(name + "is required.");
            }
        }
    }

    public static String getHtml(String name, String beandotprop, int maxlength, int size, String styleclass, String style){
        StringBuffer out = new StringBuffer();
        String beanvalue = (String)Pagez.getBeanMgr().getProp(beandotprop);
        String requestvalue = Pagez.getRequest().getParameter(name);
        String value = beanvalue;

        if (styleclass!=null && !styleclass.equals("")){
            styleclass = "class=\""+styleclass+"\"";
        }
        if (style!=null && !style.equals("")){
            style = "style=\""+style+"\"";
        }
        out.append("<input type=\"hidden\" name=\""+Str.cleanForHtml(name)+"-typehint\" value=\"Textbox\" />");
        out.append("<input type=\"text\" name=\""+Str.cleanForHtml(name)+"\" value=\""+ Str.cleanForHtml(value)+"\" size=\""+size+"\" maxlength=\""+maxlength+"\" "+styleclass+" "+style+" />");
        return out.toString();
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value=value;
    }


}
