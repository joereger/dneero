package com.dneero.xmpp;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import com.dneero.threadpool.ThreadPool;
import com.dneero.systemprops.SystemProperty;

/**
 * User: Joe Reger Jr
 * Date: Oct 28, 2006
 * Time: 7:22:38 AM
 */
public class SendXMPPMessage implements Runnable {

    private String recipientaddress = "joereger@jabber.org";
    private String jabberserver = "jabber.org";
    private String senderusername = "dneeroserver";
    private String senderpassword = "dneerorules";
    private String message = "";
    private int grouptosendto = 0;

    private static ThreadPool tp;

    //Add and remove people from these notification groups.
    //They will need to have an account on jabber.org
    public static int GROUP_SYSADMINS = 1;
    private String[] groupSYSADMINS = {"joereger@jabber.org", "frogfire@jabber.com"};

    public static int GROUP_SALES = 2;
    private String[] groupSALES = {"joereger@jabber.org", "frogfire@jabber.com"};

    public static int GROUP_CUSTOMERSUPPORT = 3;
    private String[] groupCUSTOMERSUPPORT = {"joereger@jabber.org", "frogfire@jabber.com"};

    public static int GROUP_DEBUG = 4;
    private String[] groupDEBUG = {"joereger@jabber.org", "frogfire@jabber.com"};

    public SendXMPPMessage(int grouptosendto, String message){
        this.grouptosendto = grouptosendto;
        this.message = message;
    }

    public SendXMPPMessage(String message){
        this.message = message;
    }

    public SendXMPPMessage(String recipientaddress, String message){
        this.recipientaddress = recipientaddress;
        this.message = message;
    }

    public SendXMPPMessage(String recipientaddress, String message, String jabberserver, String senderusername, String senderpassword){
        this.recipientaddress = recipientaddress;
        this.message = message;
        this.jabberserver = jabberserver;
        this.senderusername = senderusername;
        this.senderpassword = senderpassword;
    }

    public void run(){
        String[] recipients = {recipientaddress};
        if (grouptosendto>0){
            if (grouptosendto==GROUP_SYSADMINS){
                recipients = groupSYSADMINS;
            } else if (grouptosendto==GROUP_SALES){
                recipients = groupSALES;
            } else if (grouptosendto==GROUP_CUSTOMERSUPPORT){
                recipients = groupCUSTOMERSUPPORT;
            }  else if (grouptosendto==GROUP_DEBUG){
                recipients = groupDEBUG;
            }
        }
        for (int i = 0; i < recipients.length; i++) {
            String recipient = recipients[i];
            if (!recipient.equals("")){
                if (SystemProperty.getProp(SystemProperty.PROP_SENDXMPP).equals("1")){
                    try{
                        XMPPConnection con = new XMPPConnection(jabberserver);
                        con.login(senderusername, senderpassword);
                        con.createChat(recipient).sendMessage(message);
                    } catch (XMPPException xmppex){
                        System.out.println("Couldn't send XMPP to "+recipient+": "+xmppex.getMessage());
                    }
                }
            }
        }
    }


    public void send(){
        if (tp==null){
            tp = new ThreadPool(15);
        }
        tp.assign(this);
    }


}
