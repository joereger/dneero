package com.dneero.log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ErrorCode;

import java.util.Calendar;

import com.dneero.db.Db;
import com.dneero.util.Str;
import com.dneero.util.Time;
import com.dneero.startup.ApplicationStartup;
import com.dneero.xmpp.SendXMPPMessage;
import com.dneero.email.EmailTemplateProcessor;

/**
 * User: Joe Reger Jr
 * Date: Oct 17, 2006
 * Time: 11:34:38 AM
 */
public class Log4jCustomAppender extends AppenderSkeleton {

    public boolean requiresLayout(){
        return true;
    }

    public synchronized void append( LoggingEvent event ){
        StringBuffer errorMessage = new StringBuffer();
        StringBuffer errorMessageAsHtml = new StringBuffer();
        if( this.layout == null ){
            errorHandler.error("No layout for appender " + name , null, ErrorCode.MISSING_LAYOUT );
            return;
        }
        //Get main message
        errorMessage.append(this.layout.format(event));
        CustomHtmlLayout htmlLayout = new CustomHtmlLayout();
        errorMessageAsHtml.append(htmlLayout.format(event));
        //If layout doesn't handle throwables
        if( layout.ignoresThrowable() ){
            String[] messages = event.getThrowableStrRep();
            if( messages != null ){
                for( int j = 0; j < messages.length; ++j ){
                    errorMessage.append(messages[j]);
                    errorMessage.append( '\n' );
                }
            }
        }

        if (shouldRecordThis(errorMessageAsHtml.toString())){
            //Write to database and send via email
            if (ApplicationStartup.getIsappstarted()){
                if (event.getLevel()==Level.ERROR || event.getLevel()==Level.FATAL){
                    try{
                        //-----------------------------------
                        //-----------------------------------
                        int identity = Db.RunSQLInsert("INSERT INTO error(error, level, status, date) VALUES('"+Str.cleanForSQL(errorMessageAsHtml.toString())+"', '"+event.getLevel().toInt()+"', '"+com.dneero.dao.Error.STATUS_NEW+"', '"+ Time.dateformatfordb(Calendar.getInstance())+"')", false);
                        //-----------------------------------
                        //-----------------------------------


                    } catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
            //XMPP (Instant Messages)
            if (event.getLevel()==Level.ERROR || event.getLevel()==Level.FATAL){
                SendXMPPMessage xmpp = new SendXMPPMessage(SendXMPPMessage.GROUP_SYSADMINS, Str.truncateString(errorMessage.toString(), 300));
                xmpp.send();
            }
            //Send via email
            if (event.getLevel()==Level.ERROR || event.getLevel()==Level.FATAL){
                //EmailTemplateProcessor.sendGenericEmail("joe@joereger.com", "dNeero Error: "+event.getMessage().toString(), errorMessageAsHtml.toString());
            }
        }
    }
    
    public synchronized void close(){

    }

    //Allows me to filter out annoying framework-based errors that can't be fixed otherwise
    public boolean shouldRecordThis(String err){
        return true;
    }



}
