package com.dneero.helpers;

import com.dneero.dao.*;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.display.SurveyResponseParser;
import com.dneero.display.components.def.ComponentException;
import com.dneero.display.components.def.Component;
import com.dneero.display.components.def.ComponentTypes;
import com.dneero.display.components.Dropdown;
import com.dneero.display.components.Textbox;
import com.dneero.display.components.Essay;
import com.dneero.finders.FindSurveysForBlogger;
import com.dneero.rank.RankForResponseThread;
import com.dneero.scheduledjobs.UpdateResponsePoststatus;
import com.dneero.facebook.FacebookApiWrapper;
import com.dneero.htmlui.Pagez;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.session.SurveysTakenToday;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Iterator;
import java.util.Date;

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
        //Make sure blogger hasn't taken already
        List<Response> responses = HibernateUtil.getSession().createCriteria(Response.class)
                                           .add(Restrictions.eq("bloggerid", blogger.getBloggerid()))
                                           .setCacheable(false)
                                           .list();
        for (Iterator<Response> iterator = responses.iterator(); iterator.hasNext();) {
            Response response = iterator.next();
            if (response.getSurveyid()==survey.getSurveyid()){
                allCex.addValidationError("You have already joined this conversation.");
            }
        }
        //Make sure blogger is qualified to take
        if (!FindSurveysForBlogger.isBloggerQualifiedToTakeSurvey(blogger, survey)){
            allCex.addValidationError("Sorry, you're not qualified to join this conversation.  Your qualification is determined by your Profile.  Conversation igniters determine their intended audience when they create a conversation.");
        }
        //Userquestion validation
        if (srp.getNameValuePairs().get(SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"userquestion-question")!=null){
            String[] uqArr =(String[]) srp.getNameValuePairs().get(SurveyResponseParser.DNEERO_REQUEST_PARAM_IDENTIFIER+"userquestion-question");
            if (uqArr[0].equals("")){
                allCex.addValidationError("You must add your own question to the conversation.");
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
                allCex.addValidationError("You must tell us how people can answer the question you added to the conversation.");
            }
        } else {
            allCex.addValidationError("You must add your own question to the conversation.");
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
                Response response = new Response();
                response.setBloggerid(blogger.getBloggerid());
                response.setResponsedate(new Date());
                response.setSurveyid(survey.getSurveyid());
                response.setReferredbyuserid(referredbyuserid);
                response.setIsforcharity(isforcharity);
                response.setCharityname(charityname);
                response.setPoststatus(Response.POSTATUS_NOTPOSTED);
                response.setIspaid(false);
                response.setResponsestatushtml("");
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
                //Process each question
                if (allCex.getErrors().length<=0){
                    for (Iterator<Question> iterator = survey.getQuestions().iterator(); iterator.hasNext();) {
                        Question question = iterator.next();
                        logger.debug("start processing questionid="+question.getQuestionid()+" "+question.getQuestion());
                        Component component = ComponentTypes.getComponentByID(question.getComponenttype(), question, blogger);
                        try{component.processAnswer(srp, response);} catch (ComponentException cex){allCex.addErrorsFromAnotherGeneralException(cex);}
                        logger.debug("end processing questionid="+question.getQuestionid()+" "+question.getQuestion());
                    }
                }
                logger.debug("done processing each question");
                logger.debug("saving blogger");
                //Refresh blogger
                try{blogger.save();} catch (Exception ex){logger.error("",ex);}
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

                        Question question = new Question();
                        question.setSurveyid(survey.getSurveyid());
                        question.setQuestion(uqArr[0]);
                        question.setIsrequired(false);
                        question.setComponenttype(componenttype);
                        question.setIsuserquestion(true);
                        question.setUserid(blogger.getUserid());

                        survey.getQuestions().add(question);
                        try{survey.save();} catch (Exception ex){logger.error("", ex);}

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
                                Questionconfig qc1 = new Questionconfig();
                                qc1.setQuestionid(question.getQuestionid());
                                qc1.setName("options");
                                qc1.setValue(options.toString());
                                question.getQuestionconfigs().add(qc1);
                                try{survey.save();} catch (Exception ex){logger.error("", ex);}
                            }
                        }
                    }
                }

                //Handle rankings in a thread
                try{
                    RankForResponseThread qThread = new RankForResponseThread(response.getResponseid());
                    qThread.startThread();
                } catch (Exception ex){logger.error("",ex);};

                //Process the statusHtml for the response
                try{
                    UpdateResponsePoststatus.processSingleResponse(response);} catch (Exception ex){logger.error("",ex);};

                //Update Facebook
                try{
                    FacebookApiWrapper facebookApiWrapper = new FacebookApiWrapper(Pagez.getUserSession());
                    facebookApiWrapper.postToFeed(survey, response);
                    facebookApiWrapper.updateProfile(user);
                } catch (Exception ex){
                    logger.error(ex);
                }
            }
        } catch (Exception ex){
            logger.error("",ex);
            allCex.addValidationError(ex.getMessage());
        }
        //Notify
        if (allCex.getErrors().length<=0){
            //Notify debug group
            SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_CUSTOMERSUPPORT, "dNeero Conversation Joined: "+ survey.getTitle()+" (surveyid="+survey.getSurveyid()+") by "+Pagez.getUserSession().getUser().getFirstname()+" "+Pagez.getUserSession().getUser().getLastname()+" ("+Pagez.getUserSession().getUser().getEmail()+")");
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


    }



}
