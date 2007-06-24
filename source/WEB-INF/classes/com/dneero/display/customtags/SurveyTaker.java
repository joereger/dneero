package com.dneero.display.customtags;

import org.apache.log4j.Logger;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.io.IOException;

import com.dneero.util.Jsf;
import com.dneero.dao.*;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentTypes;
import com.dneero.display.components.def.ComponentException;
import com.dneero.display.SurveyTakerDisplay;
import com.dneero.display.SurveyResponseParser;
import com.dneero.finders.FindSurveysForBlogger;
import com.dneero.formbeans.PublicSurveyTake;
import com.dneero.formbeans.BloggerIndex;

/**
 * User: Joe Reger Jr
 * Date: Jul 7, 2006
 * Time: 9:51:56 AM
 */
public class SurveyTaker extends UIInput {




    //THIS TAG IS NO LONGER IN USE... IT IS JUST A GOOD EXAMPLE OF A CUSTOM COMPONENT
    //ACTUALLY, IT'S LIKELY A POOR EXAMPLE

















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

        //Make sure blogger hasn't taken already
        Blogger blogger = Blogger.get(Jsf.getUserSession().getUser().getBloggerid());
        for (Iterator<Response> iterator = blogger.getResponses().iterator(); iterator.hasNext();) {
            Response response = iterator.next();
            if (response.getSurveyid()==survey.getSurveyid()){
                allCex.addValidationError("You have already taken this survey before.  Each survey can only be answered once.");
            }
        }

        //Make sure blogger is qualified to take
        if (!FindSurveysForBlogger.isBloggerQualifiedToTakeSurvey(blogger, survey)){
            allCex.addValidationError("Sorry, you're not qualified to take this survey.");
        }

        //Make sure each component is validated
        for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
            Question question = iterator.next();
            logger.debug("found question.getQuestionid()="+question.getQuestionid());
            Component component = ComponentTypes.getComponentByID(question.getComponenttype(), question, Blogger.get(Jsf.getUserSession().getUser().getBloggerid()));
            logger.debug("found component.getName()="+component.getName());
            try{
                SurveyResponseParser srp = new SurveyResponseParser((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest());
                component.validateAnswer(srp);
            } catch (ComponentException cex){
                logger.debug(cex);
                allCex.addErrorsFromAnotherGeneralException(cex);
            }
        }
        if (allCex.getErrors().length>0){
            Jsf.setFacesMessage(allCex.getErrorsAsSingleString());
        } else {
            SurveyResponseParser srp = new SurveyResponseParser((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest());
            if (!Jsf.getUserSession().getIsloggedin()){
                //Not logged-in... store this response in memory for now
                Jsf.getUserSession().setPendingSurveyResponseSurveyid(survey.getSurveyid());
                Jsf.getUserSession().setPendingSurveyResponseAsString(srp.getAsString());
                logger.debug("Storing survey response in memory: surveyid="+survey.getSurveyid()+" : srp.getAsString()="+srp.getAsString());
            } else {
                //Create Response
                try{BloggerIndex.createResponse(survey, srp, Blogger.get(Jsf.getUserSession().getUser().getBloggerid()), 0);} catch (ComponentException cex){logger.debug(cex);allCex.addErrorsFromAnotherGeneralException(cex);}
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
            out.append(SurveyTakerDisplay.getHtmlForSurveyTaking(Survey.get(Jsf.getUserSession().getCurrentSurveyid()), Blogger.get(Jsf.getUserSession().getUser().getBloggerid()), true));
        }

        ResponseWriter writer = context.getResponseWriter();
        writer.write(out.toString());

    }


}
