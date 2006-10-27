package com.dneero.test;

import org.junit.Test;
import org.junit.Assert;
import org.apache.log4j.Logger;
import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.formbeans.*;
import com.dneero.util.*;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentTypes;
import com.dneero.survey.servlet.ImpressionActivityObject;
import com.dneero.survey.servlet.ImpressionActivityObjectStorage;
import com.dneero.scheduledjobs.*;

import java.util.*;

/**
 * User: Joe Reger Jr
 * Date: Sep 18, 2006
 * Time: 12:01:54 PM
 */
public class FullCycleSurveyTest {

    Logger logger = Logger.getLogger(this.getClass().getName());

    @Test public void bigTest(){
        try{
            Survey survey = createSurvey();
            takeSurvey(survey);
            doImpressions(survey);
            checkResultsAsResearcher();
            closeSurvey(survey);
            createInvoice(survey);
            payInvoice();
            checkEarningsAsBlogger(survey);
            payBlogger();
            checkMarkedPaid();
        } catch (Exception ex){
            logger.error(ex);
        }
    }


    private Survey createSurvey() throws Exception {

        ResearcherSurveyDetail01 researcherSurveyDetail01 = (ResearcherSurveyDetail01)Jsf.getManagedBean("researcherSurveyDetail01");
        researcherSurveyDetail01.setTitle("Test Survey" + RandomString.randomAlphanumeric(5));
        researcherSurveyDetail01.setDescription("This is a test survey created by the automated testing system.");
        researcherSurveyDetail01.setStartdate(new Date());
        researcherSurveyDetail01.setEnddate(Time.AddOneMonth(Calendar.getInstance()).getTime());
        if (!researcherSurveyDetail01.saveSurvey().equals("success")){
            throw new Exception("Problem in ResearcherSurveyDetail01");
        }

        if (Jsf.getUserSession().getCurrentSurveyid()<=0){
            throw new Exception("Surveyid not set in Jsf.getUserSession().getCurrentSurveyid()");
        }

        ResearcherSurveyDetail02checkboxes researcherSurveyDetail02checkboxes = (ResearcherSurveyDetail02checkboxes)Jsf.getManagedBean("researcherSurveyDetail02checkboxes");
        researcherSurveyDetail02checkboxes.setIsrequired(false);
        researcherSurveyDetail02checkboxes.setQuestion("Test Question " + RandomString.randomAlphanumeric(5));
        researcherSurveyDetail02checkboxes.setOptions("Test Option " + RandomString.randomAlphanumeric(5));
        if (!researcherSurveyDetail02checkboxes.saveQuestion().equals("researchersurveydetail_02")){
            throw new Exception("Problem in ResearcherSurveyDetail02checkboxes");
        }

        ResearcherSurveyDetail02dropdown researcherSurveyDetail02dropdown = (ResearcherSurveyDetail02dropdown)Jsf.getManagedBean("researcherSurveyDetail02dropdown");
        researcherSurveyDetail02dropdown.setIsrequired(false);
        researcherSurveyDetail02dropdown.setQuestion("Test Question " + RandomString.randomAlphanumeric(5));
        researcherSurveyDetail02dropdown.setOptions("Test Option " + RandomString.randomAlphanumeric(5));
        if (!researcherSurveyDetail02dropdown.saveQuestion().equals("researchersurveydetail_02")){
            throw new Exception("Problem in ResearcherSurveyDetail02dropdown");
        }

        ResearcherSurveyDetail02essay researcherSurveyDetail02essay = (ResearcherSurveyDetail02essay)Jsf.getManagedBean("researcherSurveyDetail02essay");
        researcherSurveyDetail02essay.setIsrequired(false);
        researcherSurveyDetail02essay.setQuestion("Test Question " + RandomString.randomAlphanumeric(5));
        if (!researcherSurveyDetail02essay.saveQuestion().equals("researchersurveydetail_02")){
            throw new Exception("Problem in ResearcherSurveyDetail02essay");
        }

        ResearcherSurveyDetail02matrix researcherSurveyDetail02matrix = (ResearcherSurveyDetail02matrix)Jsf.getManagedBean("researcherSurveyDetail02matrix");
        researcherSurveyDetail02matrix.setIsrequired(false);
        researcherSurveyDetail02matrix.setQuestion("Test Question " + RandomString.randomAlphanumeric(5));
        researcherSurveyDetail02matrix.setRows("Test Row " + RandomString.randomAlphanumeric(5));
        researcherSurveyDetail02matrix.setCols("Test Col " + RandomString.randomAlphanumeric(5));
        researcherSurveyDetail02matrix.setRespondentcanselectmany(true);
        if (!researcherSurveyDetail02matrix.saveQuestion().equals("researchersurveydetail_02")){
            throw new Exception("Problem in ResearcherSurveyDetail02matrix");
        }

        ResearcherSurveyDetail02range researcherSurveyDetail02range = (ResearcherSurveyDetail02range)Jsf.getManagedBean("researcherSurveyDetail02range");
        researcherSurveyDetail02range.setIsrequired(false);
        researcherSurveyDetail02range.setQuestion("Test Question " + RandomString.randomAlphanumeric(5));
        researcherSurveyDetail02range.setMax(10);
        researcherSurveyDetail02range.setMaxtitle("Max");
        researcherSurveyDetail02range.setMin(1);
        researcherSurveyDetail02range.setMaxtitle("Min");
        researcherSurveyDetail02range.setStep(1);
        if (!researcherSurveyDetail02range.saveQuestion().equals("researchersurveydetail_02")){
            throw new Exception("Problem in ResearcherSurveyDetail02range");
        }

        ResearcherSurveyDetail02textbox researcherSurveyDetail02textbox = (ResearcherSurveyDetail02textbox)Jsf.getManagedBean("researcherSurveyDetail02textbox");
        researcherSurveyDetail02textbox.setIsrequired(false);
        researcherSurveyDetail02textbox.setQuestion("Test Question " + RandomString.randomAlphanumeric(5));
        if (!researcherSurveyDetail02textbox.saveQuestion().equals("researchersurveydetail_02")){
            throw new Exception("Problem in ResearcherSurveyDetail02textbox");
        }

        ResearcherSurveyDetail02 researcherSurveyDetail02 = (ResearcherSurveyDetail02)Jsf.getManagedBean("researcherSurveyDetail02");
        if (!researcherSurveyDetail02.saveSurvey().equals("success")){
            throw new Exception("Problem in ResearcherSurveyDetail02");
        }

        ResearcherSurveyDetail03 researcherSurveyDetail03 = (ResearcherSurveyDetail03)Jsf.getManagedBean("researcherSurveyDetail03");
        researcherSurveyDetail03.setTemplate("Appended to top of template by automated test system<br><br>");
        if (!researcherSurveyDetail03.saveSurvey().equals("researchersurveydetail_03")){
            throw new Exception("Problem in ResearcherSurveyDetail03");
        }

        ResearcherSurveyDetail04 researcherSurveyDetail04 = (ResearcherSurveyDetail04)Jsf.getManagedBean("researcherSurveyDetail04");
        researcherSurveyDetail04.setAgemax(100);
        researcherSurveyDetail04.setAgemin(13);
        researcherSurveyDetail04.setBlogfocus(new String[]{"Aging","College","Education"});
        researcherSurveyDetail04.setBlogquality(0);
        researcherSurveyDetail04.setBlogquality90days(0);
        researcherSurveyDetail04.setCity(new String[]{"Atlanta"});
        researcherSurveyDetail04.setEducationlevel(new String[]{"High School", "Some College"});
        researcherSurveyDetail04.setEthnicity(new String[]{"Not Specified"});
        researcherSurveyDetail04.setGender(new String[]{"Male"});
        researcherSurveyDetail04.setIncome(new String[]{"80001 - 90000"});
        researcherSurveyDetail04.setMaritalstatus(new String[]{"Single", "Divorced"});
        researcherSurveyDetail04.setPolitics(new String[]{"Apathetic", "Democrat"});
        researcherSurveyDetail04.setProfession(new String[]{"Doctor", "Scientist"});
        researcherSurveyDetail04.setState(new String[]{"Georgia", "Alabama"});
        researcherSurveyDetail04.setCity(new String[]{"Atlanta"});
        if (!researcherSurveyDetail04.saveSurvey().equals("success")){
            throw new Exception("Problem in ResearcherSurveyDetail04");
        }

        ResearcherSurveyDetail05 researcherSurveyDetail05 = (ResearcherSurveyDetail05)Jsf.getManagedBean("researcherSurveyDetail05");
        researcherSurveyDetail05.setMaxdisplaysperblog(300);
        researcherSurveyDetail05.setMaxdisplaystotal(100000);
        researcherSurveyDetail05.setNumberofrespondentsrequested(500);
        researcherSurveyDetail05.setWillingtopaypercpm(.1);
        researcherSurveyDetail05.setWillingtopayperrespondent(3.95);
        if (!researcherSurveyDetail05.saveSurvey().equals("success")){
            throw new Exception("Problem in ResearcherSurveyDetail05");
        }


        ResearcherSurveyDetail06 researcherSurveyDetail06 = (ResearcherSurveyDetail06)Jsf.getManagedBean("researcherSurveyDetail06");
        if (!researcherSurveyDetail06.saveSurvey().equals("success")){
            throw new Exception("Problem in ResearcherSurveyDetail06");
        }

        return Survey.get(Jsf.getUserSession().getCurrentSurveyid());
    }

    private Response takeSurvey(Survey survey) throws Exception {
        Jsf.getUserSession().setCurrentSurveyid(survey.getSurveyid());
        for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
            Question question = iterator.next();
            Component component = ComponentTypes.getComponentByID(question.getComponenttype(), question, Jsf.getUserSession().getUser().getBlogger());
            Questionresponse questionresponse = new Questionresponse();
            questionresponse.setQuestionid(question.getQuestionid());
            questionresponse.setBloggerid(Jsf.getUserSession().getUser().getBlogger().getBloggerid());
            questionresponse.setName("response");
            questionresponse.setValue("Random Value " + RandomString.randomAlphanumeric(5));
            try{question.save();} catch (GeneralException gex){logger.debug(gex.getErrorsAsSingleString());}
        }
        Response response = new Response();
        response.setBloggerid(Jsf.getUserSession().getUser().getBlogger().getBloggerid());
        response.setResponsedate(new Date());
        response.setSurveyid(survey.getSurveyid());
        survey.getResponses().add(response);
        try{survey.save();} catch (GeneralException gex){logger.debug(gex.getErrorsAsSingleString());}
        return response;
    }

    private void doImpressions(Survey survey) throws Exception {
        for(int i=0; i<10; i++){
            ImpressionActivityObject iao = new ImpressionActivityObject();
            iao.setSurveyid(survey.getSurveyid());
            iao.setReferer("http://"+RandomString.randomAlphabetic(5)+".referer.org");
            iao.setIp("192.168.1.1");
            iao.setBlogid(1);
            ImpressionActivityObjectStorage.store(iao);
        }
    }

    private void checkResultsAsResearcher() throws Exception {
        //@todo check results as researcher on test
    }

    private void closeSurvey(Survey survey) throws Exception {
        //Make sure the survey is starting in the open status
        Assert.assertTrue(survey.getStatus()==Survey.STATUS_OPEN);
        //Back-date survey
        Calendar startDate = Time.SubtractOneMonth(Calendar.getInstance());
        Calendar endDate = Time.xWeeksAgoStart(Calendar.getInstance(), 2);
        survey.setStartdate(startDate.getTime());
        survey.setEnddate(endDate.getTime());
        try{survey.save();} catch (GeneralException gex){logger.debug(gex.getErrorsAsSingleString());}
        //Run the scheduler
        CloseSurveysByDate close = new CloseSurveysByDate();
        close.execute(null);
        //Make sure the survey is starting in the closed status
        Assert.assertTrue(survey.getStatus()==Survey.STATUS_CLOSED);
    }

    private void createInvoice(Survey survey) throws Exception {
        //Run scheduler task
        CreateInvoices createInvoices = new CreateInvoices();
        createInvoices.execute(null);
        //See if the invoice is created
        HibernateUtil.getSession().saveOrUpdate(survey);
        for (Iterator<Impression> iterator = survey.getImpressions().iterator(); iterator.hasNext();) {
            Impression impression = iterator.next();
            for (Iterator<Impressiondetail> iterator1 = impression.getImpressiondetails().iterator(); iterator1.hasNext();){
                Impressiondetail impressiondetail = iterator1.next();
                Assert.assertTrue(impressiondetail.getInvoiceid()>0);
            }
        }

    }


    private void payInvoice() throws Exception {

    }

    private void checkEarningsAsBlogger(Survey survey) throws Exception {
        //Should see that the money is coming my way
        boolean haveFoundEarningsForSurveyInTest = false;
        BloggerCompletedsurveys bloggerCompletedsurveys = (BloggerCompletedsurveys)Jsf.getManagedBean("bloggerCompletedsurveys");
        for (Iterator<BloggerEarningsListSurveys> iterator = bloggerCompletedsurveys.getList().iterator(); iterator.hasNext();){
            BloggerEarningsListSurveys bloggerEarningsListSurveys = iterator.next();
            if (bloggerEarningsListSurveys.getSurveyid()==survey.getSurveyid()){
                haveFoundEarningsForSurveyInTest = true;
                //Assert.assertTrue(bloggerEarningsListSurveys.getAmttotal()==4.55);
                //Assert.assertTrue(bloggerEarningsListSurveys.getAmtforresponse()==survey.getWillingtopayperrespondent());
            }
        }
        Assert.assertTrue(haveFoundEarningsForSurveyInTest);
    }

    private void payBlogger() throws Exception {
        //Create the Paybloggers
        CreatePaybloggers cpb = new CreatePaybloggers();
        cpb.execute(null);
        
    }

    private void checkMarkedPaid() throws Exception {
        //Make sure it's noted that the blogger has been paid
        Jsf.getUserSession().getUser().getBlogger().getPaybloggers();
    }










}
