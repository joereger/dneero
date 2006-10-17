package com.dneero.log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ErrorCode;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import java.util.Date;
import java.util.Calendar;

import com.dneero.db.Db;
import com.dneero.db.DbConfig;
import com.dneero.util.Util;
import com.dneero.util.Str;
import com.dneero.util.Time;
import com.dneero.dao.hibernate.HibernateUtil;
import com.dneero.startup.ApplicationStartup;

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
        org.apache.log4j.HTMLLayout htmlLayout = new org.apache.log4j.HTMLLayout();
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
        //Write error message to the DB
//        System.out.println("CUSTOMAPPENDER: "+errorMessage.toString());
        if (ApplicationStartup.getIsappstarted()){
            com.dneero.dao.Error error = new com.dneero.dao.Error();
            error.setDate(new Date());
            error.setLevel(event.getLevel().toInt());
            error.setStatus(com.dneero.dao.Error.STATUS_NEW);
            error.setError("<table>"+errorMessageAsHtml.toString()+"</table>");
            try{error.save();}catch(Exception ex){ex.printStackTrace();}
        }

//        try{
//            //-----------------------------------
//            //-----------------------------------
//            int identity = Db.RunSQLInsert("INSERT INTO error(error, level, status, date) VALUES('"+Str.cleanForSQL(errorMessage.toString())+"', '"+event.getLevel().toInt()+"', '"+com.dneero.dao.Error.STATUS_NEW+"', '"+ Time.dateformatfordb(Calendar.getInstance())+"')");
//            //-----------------------------------
//            //-----------------------------------
//        } catch (Exception ex){
//            ex.printStackTrace();
//        }



        //XMPP (Instant Messages)
        //@todo spin this XMPP send off in a thread... abstract all XMPP stuff to thread class
        if (event.getLevel()==Level.ERROR || event.getLevel()==Level.FATAL){
            try{
                XMPPConnection con = new XMPPConnection("jabber.org");
                con.login("dneeroserver", "dneerorules");
                con.createChat("joereger@jabber.org").sendMessage(errorMessage.toString());
            } catch (XMPPException xmppex){
                System.out.println("Couldn't send XMPP. "+xmppex.getMessage());
            }
        }

    }
    
    public synchronized void close(){

    }



}
