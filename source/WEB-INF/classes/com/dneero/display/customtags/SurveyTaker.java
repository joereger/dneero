package com.dneero.display.customtags;

import org.apache.log4j.Logger;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.io.IOException;

import com.dneero.util.Jsf;
import com.dneero.util.GeneralException;
import com.dneero.dao.Question;
import com.dneero.dao.Survey;
import com.dneero.dao.Response;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentTypes;
import com.dneero.display.components.def.ComponentException;
import com.dneero.display.SurveyTakerDisplay;

/**
 * User: Joe Reger Jr
 * Date: Jul 7, 2006
 * Time: 9:51:56 AM
 */
public class SurveyTaker extends UIInput {

    Logger logger = Logger.getLogger(this.getClass().getName());

    private HashMap value = new HashMap();

    public SurveyTaker() {
        super();
        setRendererType( null );
    }

    public String getFamily() {
        return "SURVEYTAKER_FAMILY";
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
            String val = (String)mapentry.getValue();
            logger.debug("key=" + key + " val=" + val);
            value.put(key, val);
        }
        setValue(value);
        //setSubmittedValue(value);


        //Validation
        Survey survey = Survey.get(Jsf.getUserSession().getCurrentSurveyid());
        ComponentException allCex = new ComponentException();
        for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
            Question question = iterator.next();
            logger.debug("found question.getQuestionid()="+question.getQuestionid());
            Component component = ComponentTypes.getComponentByID(question.getComponenttype(), question, Jsf.getUserSession().getUser().getBlogger());
            logger.debug("found component.getName()="+component.getName());
            try{
                component.validateAnswer((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest());
            } catch (ComponentException cex){
                logger.debug(cex);
                allCex.addErrorsFromAnotherGeneralException(cex);
            }
        }
        if (allCex.getErrors().length>0){
            Jsf.setFacesMessage(allCex.getErrorsAsSingleString());
        } else {

            //Create the response
            Response response = new Response();
            response.setBloggerid(Jsf.getUserSession().getUser().getBlogger().getBloggerid());
            response.setResponsedate(new Date());
            response.setSurveyid(survey.getSurveyid());
            survey.getResponses().add(response);
            try{survey.save();} catch (GeneralException gex){
                logger.debug("processAnswer() failed: " + gex.getErrorsAsSingleString());
            }

            //Processing each question
            for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
                Question question = iterator.next();
                Component component = ComponentTypes.getComponentByID(question.getComponenttype(), question, Jsf.getUserSession().getUser().getBlogger());
                try{
                    component.processAnswer((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest(), response);
                } catch (ComponentException cex){
                    allCex.addErrorsFromAnotherGeneralException(cex);
                }
            }

        }
    }

    public void encodeEnd(FacesContext context) throws IOException {
        logger.debug("encodeEnd called getClientId(context)=" + getClientId(context));

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

        StringBuffer out = new StringBuffer();
        if (Jsf.getUserSession().getUser()!=null){
            out.append(SurveyTakerDisplay.getHtmlForSurveyTaking(Survey.get(Jsf.getUserSession().getCurrentSurveyid()), Jsf.getUserSession().getUser().getBlogger()));
        }
//   Survey survey = Survey.get(Jsf.getUserSession().getCurrentSurveyid());
//        for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
//            Question question = iterator.next();
//            logger.debug("found question.getQuestionid()="+question.getQuestionid());
//            Component component = ComponentTypes.getComponentByID(question.getComponenttype(), question, Jsf.getUserSession().getUser().getBlogger());
//            logger.debug("found component.getName()="+component.getName());
//            out.append(component.getHtmlForInput());
//            if (iterator.hasNext()){
//                out.append("<br/>");
//            }
//        }

        ResponseWriter writer = context.getResponseWriter();
        writer.write(out.toString());

    }


}
