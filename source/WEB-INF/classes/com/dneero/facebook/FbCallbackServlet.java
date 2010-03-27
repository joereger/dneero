package com.dneero.facebook;

import com.dneero.dao.Survey;
import com.dneero.dao.User;
import com.dneero.htmlui.Pagez;
import com.dneero.survey.servlet.RecordImpression;
import com.dneero.systemprops.SystemProperty;
import com.dneero.systemprops.WebAppRootDir;
import com.dneero.util.Num;
import com.dneero.xmpp.SendXMPPMessage;
import org.apache.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * User: Joe Reger Jr
 * Date: Jun 19, 2006
 * Time: 10:31:40 AM
 */
public class FbCallbackServlet extends HttpServlet {

    public void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    public void doPost (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.debug("FbCallbackServlet called to service.");

        //Make sure the app responds with the facebook ui
        //==================================================================================
        Pagez.getUserSession().setIsfacebookui(true);

        //Need to record impressions
        //==================================================================================
        //Note: Facebook only allows me to append a single var to the end of my url so I have to do some splitting crap to make things work.
        //The basic format I'm using is action-var1-var2-var3 where the vars are specific to each action.  It's crap but it works.
        if (Pagez.getRequest().getParameter("action")!=null && Pagez.getRequest().getParameter("action").indexOf("showsurvey")>-1){
            RecordImpression.record(Pagez.getRequest());
        }

        //Need to set referred by userid in the usersession
        //==================================================================================
        //Note: Facebook only allows me to append a single var to the end of my url so I have to do some splitting crap to make things work.
        //The basic format I'm using is action-var1-var2-var3 where the vars are specific to each action.  It's crap but it works.
        try{
            if (Pagez.getRequest().getParameter("action")!=null && Pagez.getRequest().getParameter("action").indexOf("showsurvey")>-1){
                String[] split = Pagez.getRequest().getParameter("action").split("-");
                if (split.length>=3){
                    //Set the referredbyuserid value in the session
                    if (split[2]!=null && Num.isinteger(split[2])){
                        Pagez.getUserSession().setReferredbyOnlyUsedForSignup(Integer.parseInt(split[2]));
                    }
                }
            }
        } catch (Exception ex) {logger.error("",ex);}

        //Save referral state to the database if we have a facebookuid
        //==================================================================================
        try{
            if (Pagez.getUserSession().getReferredbyOnlyUsedForSignup()>0){
                if (Pagez.getUserSession().getFacebookUser()!=null && Num.isinteger(Pagez.getUserSession().getFacebookUser().getUid())){
                    FacebookPendingReferrals.addReferredBy(Pagez.getUserSession().getReferredbyOnlyUsedForSignup(), Integer.parseInt(Pagez.getUserSession().getFacebookUser().getUid()));
                }
            }
        } catch (Exception ex){logger.error("",ex);}

        //If we should display a specific survey, do so
        //==================================================================================
        if (Pagez.getRequest().getParameter("action")!=null && Pagez.getRequest().getParameter("action").indexOf("showsurvey")>-1){
            String[] split = Pagez.getRequest().getParameter("action").split("-");
            if (split.length>=3){
                //Set the referredbyuserid value in the session
                if (split[2]!=null && Num.isinteger(split[2])){
                    Pagez.getUserSession().setReferredbyOnlyUsedForSignup(Integer.parseInt(split[2]));
                    String referredbyname = "";
                    if (split[2]!=null && Num.isinteger(split[2])){
                        User userReferer = User.get(Integer.parseInt(split[2]));
                        referredbyname = userReferer.getNickname();
                    }
                    String surveytitle = "";
                    if (split[1]!=null && Num.isinteger(split[1])){
                        Survey surveyTmp = Survey.get(Integer.parseInt(split[1]));
                        surveytitle = surveyTmp.getTitle();
                    }
                    //Notify admins
                    if (Pagez.getUserSession().getFacebookUser()!=null){
                        //Notify via XMPP
                        SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "Facebook user "+ Pagez.getUserSession().getFacebookUser().getFirst_name() + " " + Pagez.getUserSession().getFacebookUser().getLast_name() + " referred by userid="+split[2]+" ("+referredbyname+") to surveyid="+split[1]+" ("+surveytitle+")");
                        xmpp.send();
                    } else {
                        //Notify via XMPP
                        SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "Facebook user Unknown:"+Pagez.getUserSession().getFacebookSessionKey()+" referred by userid="+split[2]+" ("+referredbyname+") to surveyid="+split[1]+" ("+surveytitle+")");
                        xmpp.send();
                    }
                }
                //If the user has the app added, redirect to the survey
                String urltoredirectto = "";
                if (Pagez.getUserSession().getIsfacebookui() &&  Pagez.getUserSession().getFacebookUser()!=null && Pagez.getUserSession().getFacebookUser().getHas_added_app()){
                    request.getRequestDispatcher("/survey.jsp?s="+split[1]+"&u="+split[2]+"&p=0").forward(request, response);
                    return;
                } else {
                    //If we see this code we may be displaying the app add page which means we'll need a link
                    try{
                        String next = URLEncoder.encode("&action="+request.getParameter("action"), "UTF-8");
                        String addurl = "http://www.facebook.com/add.php?api_key="+ SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_KEY) + "&next="+next;
                        Pagez.sendRedirect(addurl, false);
                        //request.getRequestDispatcher("/facebookappadd.jsp").forward(request, response);
                        //return;
                    } catch(Exception ex){logger.error("",ex);}
                }
            }
        }

        //Display publicsurveylist.jsp
        //==================================================================================
        if (Pagez.getRequest().getParameter("addedapp")!=null && Pagez.getRequest().getParameter("addedapp").equals("1")){
            //Notify admins
            if (Pagez.getUserSession().getFacebookUser()!=null){
                //Notify via XMPP
                SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "Facebook user "+ Pagez.getUserSession().getFacebookUser().getFirst_name() + " " + Pagez.getUserSession().getFacebookUser().getLast_name() + " just added app!");
                xmpp.send();
            } else {
                //Notify via XMPP
                SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "Facebook user Unknown just added app!");
                xmpp.send();
            }
            request.getRequestDispatcher("/publicsurveylist.jsp?addedapp=1").forward(request, response);
            return;
        }

        //If we have a dpage value, forward the request
        //==================================================================================
        if (Pagez.getRequest().getParameter("dpage")!=null && !Pagez.getRequest().getParameter("dpage").equals("")){
            String dpage = Pagez.getRequest().getParameter("dpage");
            String dpagedecoded = URLDecoder.decode(dpage, "UTF-8");
            logger.debug("dpagedecoded="+dpagedecoded);
            String[] split = dpagedecoded.split("\\?");
            String splitZero = split[0];
            logger.debug("splitZero="+splitZero);
            //Test to see if file exists
            boolean fileexists = false;
            if (splitZero.length()>0 && splitZero.substring(0,1).equals("/")){
                //Treat search as relative
                File testFile = new File(WebAppRootDir.getWebAppRootPath()+splitZero);
                logger.debug("looking for "+testFile.getAbsolutePath());
                if (testFile.exists()){
                    fileexists = true;
                }
            } else {
                //Treat search as absolute
                File testFile = new File(splitZero);
                logger.debug("looking for "+testFile.getAbsolutePath());
                if (testFile.exists()){
                    fileexists = true;
                }
            }
            logger.debug("fileexists="+fileexists);
            //If the file exists
            if (fileexists){
                RequestDispatcher srd = request.getRequestDispatcher(splitZero);
                if (srd!=null){
                    srd.forward(request, response);
                    return;
                } else {
                    logger.error("Have dpage="+dpage+" but RequestDispatcher is null.");
                }
            } else {
                logger.error("Have dpage="+dpage+" but file can't be found.");
            }
        }

        //Redirect to publicsurveylist.jsp if the user has added the app
        //==================================================================================
        if (Pagez.getUserSession().getIsfacebookui() && Pagez.getUserSession().getFacebookUser()!=null && Pagez.getUserSession().getFacebookUser().getHas_added_app()){
            request.getRequestDispatcher("/publicsurveylist.jsp").forward(request, response);
            return;
        }

        //Display the app add page
        //==================================================================================
        try{
            String referredbyuserid = "";
            String referredtosurveyid = "";
            if (Pagez.getRequest().getParameter("action")!=null && Pagez.getRequest().getParameter("action").indexOf("showsurvey")>-1){
                String[] split = Pagez.getRequest().getParameter("action").split("-");
                if (split.length>=3){
                    referredbyuserid = split[2];
                    referredtosurveyid = split[1];
                }
            }
            //Notify admins
            logger.debug("Facebook app add page shown to user");
            logger.debug("Pagez.getUserSession().getIsfacebookui():"+Pagez.getUserSession().getIsfacebookui());
            if (Pagez.getUserSession().getFacebookUser()!=null){
                //Notify via XMPP
                SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "Facebook app add page shown to "+ Pagez.getUserSession().getFacebookUser().getFirst_name() + " " + Pagez.getUserSession().getFacebookUser().getLast_name() + " referred by userid="+referredbyuserid+" to surveyid="+referredtosurveyid);
                xmpp.send();
            } else {
                //Notify via XMPP
                SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_DEBUG, "Facebook app add page shown to Unknown:"+Pagez.getUserSession().getFacebookSessionKey()+" referred by userid="+referredbyuserid+" to surveyid="+referredtosurveyid);
                xmpp.send();
            }

            String next = URLEncoder.encode("&action="+request.getParameter("action"), "UTF-8");
            String addurl = "http://www.facebook.com/add.php?api_key="+ SystemProperty.getProp(SystemProperty.PROP_FACEBOOK_API_KEY) + "&next="+next;
            Pagez.sendRedirect(addurl, false);
            //request.getRequestDispatcher("/facebookappadd.jsp").forward(request, response);
            //return;
        }catch(Exception ex){logger.error("",ex);}

    }





}
