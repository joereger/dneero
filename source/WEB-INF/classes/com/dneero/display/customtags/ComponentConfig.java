package com.dneero.display.customtags;

import org.apache.log4j.Logger;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.io.IOException;

import com.dneero.util.Jsf;
import com.dneero.formbeans.ResearcherSurveyDetail02;

/**
 * User: Joe Reger Jr
 * Date: Jul 7, 2006
 * Time: 9:51:56 AM
 */
public class ComponentConfig extends UIInput {

    Logger logger = Logger.getLogger(this.getClass().getName());

    private HashMap value = new HashMap();

    public ComponentConfig() {
        super();
        setRendererType( null );
    }

    public String getFamily() {
        return "COMPONENTCONFIG_FAMILY";
    }


    public Object saveState(FacesContext context) {
        logger.debug("saveState() called");
        if (value==null){
            logger.debug("value in saveState()=null");
        } else {
            logger.debug("value in saveState() not null");
            Iterator keyValuePairs = value.entrySet().iterator();
            for (int i = 0; i < value.size(); i++){
                Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
                String key = (String)mapentry.getKey();
                String value = (String)mapentry.getValue();
                logger.debug("value in saveState()-> "+key+"="+value);
            }
        }
        Object values[] = new Object[2];
        values[0] = super.saveState(context);
        values[1] = value;
        return ((Object) (values));
    }

    public void restoreState(FacesContext context, Object state) {
        logger.debug("restoreState() called");

        Object values[] = (Object[])state;
        super.restoreState(context, values[0]);
        value = (HashMap)values[1];

        if (value==null){
            logger.debug("value in restoreState()=null");
        } else {
            logger.debug("value in restoreState() not null");
            Iterator keyValuePairs = value.entrySet().iterator();
            for (int i = 0; i < value.size(); i++){
                Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
                String key = (String)mapentry.getKey();
                String value = (String)mapentry.getValue();
                logger.debug("value in restoreState()-> "+key+"="+value);
            }
        }
    }

    public void decode(FacesContext context) {
        logger.debug("decode called getClientId(context)=" + getClientId(context));
        Map requestMap = context.getExternalContext().getRequestParameterMap();
        value = new HashMap();
        Iterator keyValuePairs = requestMap.entrySet().iterator();
        for (int i = 0; i < requestMap.size(); i++){
            Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
            String key = (String)mapentry.getKey();
            String value = (String)mapentry.getValue();
            logger.debug("key=" + key + " value=" + value);
            if (key.indexOf(getClientId(context))>-1){
                this.value.put(key, value);
                logger.debug("added");
            } else {
                logger.debug("not added");
            }
        }
        setValue(value);
        //setSubmittedValue(value);
    }

    public void encodeEnd(FacesContext context) throws IOException {
        logger.debug("encodeEnd called getClientId(context)=" + getClientId(context));

        ResearcherSurveyDetail02 rsd02 = (ResearcherSurveyDetail02)Jsf.getManagedBean("researcherSurveyDetail02");
        value = rsd02.getQuestionconfig();

        if (value==null){
            logger.debug("value in encodeEnd()=null");
        } else {
            logger.debug("value in encodeEnd() not null");
            Iterator keyValuePairs = value.entrySet().iterator();
            for (int i = 0; i < value.size(); i++){
                Map.Entry mapentry = (Map.Entry) keyValuePairs.next();
                String key = (String)mapentry.getKey();
                String value = (String)mapentry.getValue();
                logger.debug("value-> "+key+"="+value);
            }
        }

        ResponseWriter writer = context.getResponseWriter();
        writer.write("<br>");
        writer.write("First Field: ");
        String firstfield = "";
        if (value !=null && value.get(getClientId(context)+"_firstfield")!=null){
            logger.debug("value!=null (String)value.get(\""+getClientId(context)+"_firstfield\")="+(String)value.get(getClientId(context)+"_firstfield"));
            firstfield = (String)value.get(getClientId(context)+"_firstfield");
        } else {
            logger.debug("value==null or value.get(\""+getClientId(context)+"_firstfield\")==null");
        }
        outputInputBox(getClientId(context)+"_firstfield", firstfield, context);
        writer.write("<br>");
        writer.write("Second Field: ");
        String secondfield = "";
        if (value !=null && value.get(getClientId(context)+"_secondfield")!=null){
            secondfield = (String)value.get(getClientId(context)+"_secondfield");
        }
        outputInputBox(getClientId(context)+"_secondfield", secondfield, context);
    }

    private void outputInputBox(String id, String value, FacesContext context)  throws IOException{
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("input", this);
        writer.writeAttribute("type", "text", "text");
        writer.writeAttribute("id", id, "id");
        writer.writeAttribute("name", id, "id");
        writer.writeAttribute("size", 15, "size");
        if (value==null){
            value="";
        }
        writer.writeAttribute("value", value, "value");
        writer.endElement("input");
    }


//    public HashMap getValue() {
//        logger.debug("getValue() called");
//        return value;
//    }
//
//    public void setValue(HashMap value) {
//        logger.debug("setValue() called");
//        this.value = value;
//    }


}
