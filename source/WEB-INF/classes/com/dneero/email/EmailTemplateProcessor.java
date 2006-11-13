package com.dneero.email;

import com.dneero.dao.User;
import com.dneero.util.*;
import com.dneero.systemprops.SystemProperty;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

/**
 * User: Joe Reger Jr
 * Date: Sep 10, 2006
 * Time: 11:31:16 AM
 */
public class EmailTemplateProcessor {

    public static void sendMail(String subject, String emailTemplateFilenameWithoutExtension, User userTo){
        sendMail(subject, emailTemplateFilenameWithoutExtension, userTo, null, null, null);
    }

    public static void sendMail(String subject, String emailTemplateFilenameWithoutExtension, User userTo, String[] args){
        sendMail(subject, emailTemplateFilenameWithoutExtension, userTo, args, null, null);
    }

    public static void sendMail(String subject, String emailTemplateFilenameWithoutExtension, User userTo, String[] args, String toaddress, String fromaddress){
        Logger logger = Logger.getLogger(EmailTemplateProcessor.class);
        String htmlEmailHeader = Io.textFileRead(WebAppRootDir.getWebAppRootPath() + "emailtemplates" + java.io.File.separator + "emailheader.html").toString();
        String htmlEmailFooter = Io.textFileRead(WebAppRootDir.getWebAppRootPath() + "emailtemplates" + java.io.File.separator + "emailfooter.html").toString();
        String htmlTemplate = Io.textFileRead(WebAppRootDir.getWebAppRootPath() + "emailtemplates" + java.io.File.separator + emailTemplateFilenameWithoutExtension + ".html").toString();
        String txtTemplate = Io.textFileRead(WebAppRootDir.getWebAppRootPath() + "emailtemplates" + java.io.File.separator + emailTemplateFilenameWithoutExtension + ".txt").toString();
        String htmlMessage = processTemplate(htmlTemplate, userTo, args);
        String txtMessage = processTemplate(txtTemplate, userTo, args);
        htmlMessage = translateImageLinks(htmlMessage);
        txtMessage = translateImageLinks(txtMessage);

        try{
            HtmlEmail email = new HtmlEmail();
            if (toaddress!=null && !toaddress.equals("")){
                email.addTo(toaddress);
            } else {
                email.addTo(userTo.getEmail());
            }
            if (fromaddress!=null && !fromaddress.equals("")){
                email.setFrom(fromaddress);
            } else {
                email.setFrom(EmailSendThread.DEFAULTFROM);
            }
            email.setSubject(subject);
            email.setHtmlMsg(htmlEmailHeader + htmlMessage + htmlEmailFooter);
            email.setTextMsg(txtMessage);
            EmailSend.sendMail(email);
        } catch (Exception e){
            logger.error(e);
        }
    }

    private static String processTemplate(String template, User user, String[] args){
        Logger logger = Logger.getLogger(EmailTemplateProcessor.class);
        StringBuffer out = new StringBuffer();
        Pattern p = Pattern.compile("\\<\\$(.|\\n)*?\\$\\>");
        Matcher m = p.matcher(template);
        while(m.find()) {
            String tag = m.group();
            m.appendReplacement(out, Str.cleanForAppendreplacement(findWhatToAppend(tag, user, args)));
        }
        try{ m.appendTail(out); } catch (Exception e){}
        return out.toString();
    }

    private static String findWhatToAppend(String tag, User user, String[] args){
        Logger logger = Logger.getLogger(EmailTemplateProcessor.class);
        String out = "";

        if (tag.equals("<$user.email$>")){
            return user.getEmail();
        } else if (tag.equals("<$user.firstname$>")){
            return user.getFirstname();
        } else if (tag.equals("<$user.lastname$>")){
            return user.getLastname();
        } else if (tag.equals("<$user.emailactivationkey$>")){
            return user.getEmailactivationkey();
        } else if (tag.equals("<$user.password$>")){
            return user.getPassword();
        } else if (tag.equals("<$user.userid$>")){
            return String.valueOf(user.getUserid());
        } else if (tag.equals("<$systemProps.baseurl$>")){
            return SystemProperty.getProp(SystemProperty.PROP_BASEURL);
        }


        //<$args.1$> <$args.2$> <$args.3$>
        if (tag.indexOf("args")>-1){
            String tagStripped = tag.substring(2, tag.length()-2);
            String[] tagSplit = tagStripped.split(".");
            if (tagSplit.length>1){
                if (tagSplit[1]!=null && Num.isinteger(tagSplit[1])){
                    int index = Integer.parseInt(tagSplit[1]);
                    if (args!=null && args[index]!=null){
                        return args[index];
                    }
                }
            }
        }
        return out;
    }

    private static String translateImageLinks(String template){
        Logger logger = Logger.getLogger(EmailTemplateProcessor.class);
        try{
            StringBuffer out = new StringBuffer();
            Pattern p = Pattern.compile("img src=(['\"])?images", Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(template);
            while(m.find()) {
                String tag = m.group();
                logger.debug("found tag="+tag);
                String openquote = "";
                if (m.group(1)!=null){
                    openquote = m.group(1);
                }
                String replacement = "img src="+openquote+ServerRootUrl.getServerRootUrl()+"emailtemplates/images";
                logger.debug("replacement ="+replacement);
                m.appendReplacement(out, Str.cleanForAppendreplacement(replacement));
            }
            try{ m.appendTail(out); } catch (Exception e){}
            return out.toString();
        } catch (Exception ex){
            logger.error(ex);
            return template;
        }



//        template = template.replaceAll("img src='images", "img src='"+ServerRootUrl.getServerRootUrl()+"emailtemplates/images");
//        template = template.replaceAll("IMG SRC='images", "img src='"+ServerRootUrl.getServerRootUrl()+"emailtemplates/images");
//        template = template.replaceAll("img src=\"images", "img src='"+ServerRootUrl.getServerRootUrl()+"emailtemplates/images");
//        template = template.replaceAll("IMG SRC=\"images", "img src='"+ServerRootUrl.getServerRootUrl()+"emailtemplates/images");
    }




}
