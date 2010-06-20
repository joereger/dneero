package com.dneero.helpers;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.display.SurveyResponseParser;
import com.dneero.display.components.Dropdown;
import com.dneero.display.components.Essay;
import com.dneero.display.components.Textbox;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentException;
import com.dneero.display.components.def.ComponentTypes;
import com.dneero.facebook.FacebookApiWrapper;
import com.dneero.finders.FindSurveysForBlogger;
import com.dneero.htmlui.Pagez;
import com.dneero.rank.RankForResponseThread;
import com.dneero.scheduledjobs.UpdateResponsePoststatus;
import com.dneero.session.SurveysTakenToday;
import com.dneero.survey.servlet.EmbedCacheFlusher;
import com.dneero.survey.servlet.ImpressionsByDayUtil;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.iptrack.RecordIptrackUtil;
import com.dneero.iptrack.Activitytype;
import com.dneero.util.Str;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * User: Joe Reger Jr
 * Date: Apr 28, 2008
 * Time: 9:55:29 AM
 */
public class StoreResponse {


    public static void storeResponseInDb(Survey survey, SurveyResponseParser srp, Blogger blogger, int referredbyuserid)  throws ComponentException {
        Logger logger = Logger.getLogger(StoreResponse.class);
        ComponentException allCex = new ComponentException();
        User user = User.get(blogger.getUserid());
        Response response = new Response();
        //If user has taken already, get that response
        List<Response> responses = HibernateUtil.getSession().createCriteria(Response.class)
                                           .add(Restrictions.eq("bloggerid", blogger.getBloggerid()))
                                           .setCacheable(false)
                                           .list();
        for (Iterator<Response> iterator = responses.iterator(); iterator.hasNext();) {
            Response rsp = iterator.next();
            if (rsp.getSurveyid()==survey.getSurveyid()){
                //allCex.addValidationError("You have already joined.");
                //Person's already joined the convo... so use that response
                response = rsp;
            }
        }
        //Make sure blogger is qualified to take
        if (!FindSurveysForBlogger.isBloggerQualifiedToTakeSurvey(blogger, survey)){
            allCex.addValidationError("Sorry, you're not qualified to join.  Your qualification is determined by your Profile.");
        }
        //Userquestion validation
        //@todo One problem with user question validation is that there really isn't any requirement to answer... test it... ignore some user questions... it'll let you... this code only kicks in on questions that are answered but fail somehow
        if (srp.getNameValuePairs().get(SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"userquestion-question")!=null){
            String[] uqArr =(String[]) srp.getNameValuePairs().get(SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"userquestion-question");
            if (uqArr[0].equals("")){
                allCex.addValidationError("You must add your own question.");
            }
            if (srp.getNameValuePairs().get(SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"userquestion-componenttype")!=null){
                String[] uqctArr =(String[]) srp.getNameValuePairs().get(SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"userquestion-componenttype");
                if (uqctArr[0].equals("MultipleChoice")){
                    //Need to val options
                    if (srp.getNameValuePairs().get(SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"userquestion-predefinedanswer")!=null){
                        String[] uqmcArr =(String[]) srp.getNameValuePairs().get(SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"userquestion-predefinedanswer");
                        int numberofanswers = 0;
                        for (int i=0; i<uqmcArr.length; i++) {
                            String s=uqmcArr[i];
                            if (s!=null && !s.trim().equals("")){
                                numberofanswers++;
                            }
                        }
                        if (numberofanswers<=1){
                            allCex.addValidationError("You need to create at least two possible answers to your question.");
                        }
                    } else {
                        allCex.addValidationError("You need to create possible answers to your question.");
                    }
                } else if (uqctArr[0].equals("ShortText")){
                    //No more val needed
                } else if (uqctArr[0].equals("LongText")){
                    //No more val needed
                }
            } else {
                allCex.addValidationError("You must tell us how people can answer the question you added.");
            }
        } else {
            allCex.addValidationError("You must add your own question.");
        }

        //Create Response
        int responseid = 0;
        boolean isforcharity = false;
        String charityname = "";
        try{
            //Charity
            if (srp.getNameValuePairs().get(SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"charity-isforcharity")!=null){
                logger.debug("srp.getNameValuePairs().get(SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+\"charity-isforcharity\")!=null");
                logger.debug("srp.getNameValuePairs().get(SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+\"charity-isforcharity\")="+srp.getNameValuePairs().get(SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"charity-isforcharity"));
                String[] isforcharityArr =(String[]) srp.getNameValuePairs().get(SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"charity-isforcharity");
                if (isforcharityArr[0].equals("1")){
                    isforcharity = true;
                    if (srp.getNameValuePairs().get(SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"charity-charityname")!=null){
                        String[] charitynameArr = (String[])srp.getNameValuePairs().get(SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"charity-charityname");
                        charityname = charitynameArr[0];
                    } else {
                        charityname = "Default Charity";
                    }
                }
            } else {
                logger.debug("srp.getNameValuePairs().get(SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+\"charity-isforcharity\")==null");
            }
            //This is just a backup that forces a charity give in case somebody hacks the form and disables the hidden field isforcharity
            if (survey.getIscharityonly()){
                isforcharity = true;
                if (charityname.equals("")){
                    charityname = "Default Charity";
                }
            }
            logger.debug("isforcharity = "+isforcharity);
            logger.debug("charityname = "+charityname);

            //Create the response
            if (allCex.getErrors().length<=0){
                logger.debug("start saving the response");
                if (response.getResponseid()<=0){
                    logger.debug("it's a new response because responseid<=0");
                    //This is a new response so set some first-time stuff
                    response.setResponsedate(new Date());
                    response.setReferredbyuserid(referredbyuserid);
                    response.setIspaid(false);
                    response.setResponsestatushtml("");
                    response.setPoststatus(Response.POSTATUS_NOTPOSTED);
                    response.setImpressionstotal(0);
                    response.setImpressionstobepaid(0);
                    response.setImpressionspaid(0);
                    response.setScorebyresearcher(0);
                    response.setScorebysysadmin(0);
                    response.setImpressionsbyday(new ImpressionsByDayUtil("").getAsString());
                    //Can't change charity info when editing answers
                    response.setIsforcharity(isforcharity);
                    response.setCharityname(charityname);
                    //Set the surveyincentiveid... note that this is the most current one associated with the survey but if this is an edit then the old one will be used.
                    response.setSurveyincentiveid(survey.getIncentive().getSurveyincentive().getSurveyincentiveid());
                    response.setSurveyincentive(survey.getIncentive().getSurveyincentive());
                    logger.debug("response.getSurveyincentiveid()="+response.getSurveyincentiveid());
                    logger.debug("response.getSurveyincentive().getSurveyincentiveid()="+response.getSurveyincentive().getSurveyincentiveid());
                }
                response.setBloggerid(blogger.getBloggerid());
                response.setSurveyid(survey.getSurveyid());
                //Reset all content flags so that it gets checked by researcher again
                response.setIsresearcherreviewed(false);
                response.setIssysadminreviewed(false);
                response.setIsresearcherrejected(false);
                response.setIssysadminrejected(false);

                //survey.getResponses().add(response);
                try{
                    response.save();
                    responseid = response.getResponseid();
                    //survey.refresh();
                } catch (Exception ex){
                    logger.error("",ex);
                    allCex.addValidationError(ex.getMessage());
                }
                logger.debug("end saving the response");
                logger.debug("start processing each question");
                //Delete any existing question responses
                HibernateUtil.getSession().createQuery("delete Questionresponse q where q.bloggerid='"+blogger.getBloggerid()+"' and q.responseid='"+response.getResponseid()+"'").executeUpdate();
                //Process each question
                if (allCex.getErrors().length<=0){
                    for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
                        Question question = iterator.next();
                        logger.debug("start processing questionid="+question.getQuestionid()+" "+question.getQuestion());
                        Component component = ComponentTypes.getComponentByType(question.getComponenttype(), question, blogger);
                        try{component.processAnswer(srp, response);} catch (ComponentException cex){allCex.addErrorsFromAnotherGeneralException(cex);}
                        logger.debug("end processing questionid="+question.getQuestionid()+" "+question.getQuestion());
                    }
                }
                logger.debug("done processing each question");
                logger.debug("saving blogger");
                //Refresh blogger
                try{blogger.save(); blogger.refresh();} catch (Exception ex){logger.error("",ex);}
                //logger.debug("refreshing survey");
                //Refresh survey
                //try{survey.refresh();} catch (Exception ex){logger.error("",ex);}


                //Userquestion processing
                if (srp.getNameValuePairs().get(SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"userquestion-question")!=null){
                    String[] uqArr =(String[]) srp.getNameValuePairs().get(SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"userquestion-question");
                    if (srp.getNameValuePairs().get(SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"userquestion-componenttype")!=null){
                        String[] uqctArr =(String[]) srp.getNameValuePairs().get(SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"userquestion-componenttype");
                        int componenttype = 0;
                        if (uqctArr[0].equals("MultipleChoice")){
                            componenttype = Dropdown.ID;
                        } else if (uqctArr[0].equals("ShortText")){
                            componenttype = Textbox.ID;
                        } else if (uqctArr[0].equals("LongText")){
                            componenttype = Essay.ID;
                        }
                        //See if user already has a question out there
                        Question question = new Question();
                        boolean userhadquestionalready = false;
                        boolean userchangedquestion = false;
                        List<Question> uqs = HibernateUtil.getSession().createCriteria(Question.class)
                                           .add(Restrictions.eq("surveyid", survey.getSurveyid()))
                                           .add(Restrictions.eq("userid", blogger.getUserid()))
                                           .add(Restrictions.eq("isuserquestion", true))
                                           .setCacheable(true)
                                           .list();
                        for (Iterator<Question> iterator = uqs.iterator(); iterator.hasNext();) {
                            Question q = iterator.next();
                            userhadquestionalready = true;
                            //Determine whether or not question was edited
                            if (!q.getQuestion().equals(String.valueOf(uqArr[0]))){
                                userchangedquestion = true;
                            }
                            if (q.getComponenttype()!=componenttype){
                                userchangedquestion = true;
                            }
                            //Use this question
                            question = q;
                        }
                        //Delete existing answers if user changed
                        if (userchangedquestion){
                            survey.getQuestions().remove(question);
                            try{question.delete();}catch(Exception ex){logger.error("", ex);}
                            question = new Question();
                        }
                        question.setSurveyid(survey.getSurveyid());
                        question.setQuestion(uqArr[0]);
                        question.setIsrequired(false);
                        question.setComponenttype(componenttype);
                        question.setIsuserquestion(true);
                        question.setUserid(blogger.getUserid());
                        //Reset all review flags if it's been edited or if it's new
                        if (userchangedquestion || question.getQuestionid()<=0){
                            question.setIsresearcherreviewed(false);
                            question.setIssysadminreviewed(false);
                            question.setIsresearcherrejected(false);
                            question.setIssysadminrejected(false);
                            question.setScorebyresearcher(0);
                            question.setScorebysysadmin(0);
                        }
                        //Set order if it's new
                        question.setQuestionorder(QuestionOrder.calculateNewQuestionOrder(question));
                        //if (!userhadquestionalready){
                            survey.getQuestions().add(question);
                        //}
                        try{survey.save();EmbedCacheFlusher.flushCache(survey.getSurveyid(), blogger.getUserid());} catch (Exception ex){logger.error("", ex);}

                        if (componenttype==Dropdown.ID){
                            if (srp.getNameValuePairs().get(SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"userquestion-predefinedanswer")!=null){
                                StringBuffer options = new StringBuffer();
                                String[] uqmcArr =(String[]) srp.getNameValuePairs().get(SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"userquestion-predefinedanswer");
                                for (int i=0; i<uqmcArr.length; i++) {
                                    String s=uqmcArr[i];
                                    if (s!=null && !s.trim().equals("")){
                                        options.append(s.trim());
                                        options.append("\n");
                                    }
                                }
                                //See if user already has a questionconfig out there
                                Questionconfig qc1 = new Questionconfig();
                                boolean userhadquestionconfigalready = false;
                                List<Questionconfig> uqc = HibernateUtil.getSession().createCriteria(Questionconfig.class)
                                                   .add(Restrictions.eq("questionid", question.getQuestionid()))
                                                   .add(Restrictions.eq("name", "options"))
                                                   .setCacheable(true)
                                                   .list();
                                for (Iterator<Questionconfig> iterator = uqc.iterator(); iterator.hasNext();) {
                                    Questionconfig qc = iterator.next();
                                    qc1 = qc;
                                    userhadquestionconfigalready = true;
                                }
                                qc1.setQuestionid(question.getQuestionid());
                                qc1.setName("options");
                                qc1.setValue(options.toString());
                                if (!userhadquestionconfigalready){
                                    question.getQuestionconfigs().add(qc1);
                                }
                                try{survey.save();EmbedCacheFlusher.flushCache(survey.getSurveyid(), blogger.getUserid());} catch (Exception ex){logger.error("", ex);}
                            }
                        }

                    }
                }

                //Handle rankings in a thread
                try{
                    RankForResponseThread qThread = new RankForResponseThread(response.getResponseid());
                    qThread.startThread();
                } catch (Exception ex){logger.error("",ex);}

                //Process the statusHtml for the response
                try{UpdateResponsePoststatus.processSingleResponse(response);} catch (Exception ex){logger.error("",ex);};

                //Update Facebook
                try{
                    FacebookApiWrapper facebookApiWrapper = new FacebookApiWrapper(Pagez.getUserSession());
                    facebookApiWrapper.postToFeed(survey, response);
                    facebookApiWrapper.updateProfile(user);
                } catch (Exception ex){
                    logger.error(ex);
                }

                //Do the Incentive hook call
                try{
                    response.getIncentive().doImmediatelyAfterResponse(response);
                } catch (Exception ex){logger.error("",ex);};

                //Record Iptrack Activity
                RecordIptrackUtil.record(Pagez.getRequest(), user.getUserid(), Activitytype.CONVOJOIN, Str.truncateString(survey.getTitle(), 100));
            }
        } catch (Exception ex){
            logger.error("",ex);
            allCex.addValidationError(ex.getMessage());
        }
        //Notify
        if (allCex.getErrors().length<=0){
            //Notify debug group
            SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_CUSTOMERSUPPORT, "Join: "+ survey.getTitle()+" (surveyid="+survey.getSurveyid()+") by "+Pagez.getUserSession().getUser().getNickname()+" ("+Pagez.getUserSession().getUser().getEmail()+")");
            xmpp.send();
        } else {
            throw allCex;
        }
        //Update the session data on number of surveys taken today
        try{
            Pagez.getUserSession().setSurveystakentoday(SurveysTakenToday.getNumberOfSurveysTakenToday(Pagez.getUserSession().getUser()));
        } catch (Exception ex){
            logger.error("",ex);
        }

        //Flush the embed cache
        try{
            if (survey!=null && blogger!=null){
                EmbedCacheFlusher.flushCache(survey.getSurveyid(), blogger.getUserid());
            }
        } catch (Exception ex){
            logger.error("", ex);
        }

    }



}
