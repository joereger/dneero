package com.dneero.iptrack;

import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

import com.dneero.dao.Iptrack;
import com.dneero.dao.User;
import com.dneero.htmlui.Pagez;

import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Dec 3, 2008
 * Time: 8:20:21 PM
 */
public class RecordIptrackUtil {



    public static void record(HttpServletRequest request, int userid, int activitytypeid){
        record(request, userid, activitytypeid, "");
    }

    public static void record(HttpServletRequest request, int userid, int activitytypeid, String desc){
        Logger logger = Logger.getLogger(RecordIptrackUtil.class);
        try{
            //Don't log facebookui stuff
            if (Pagez.getUserSession().getIsfacebookui()){
                return;
            }
            //Don't work with empty requests
            if (request!=null){
                IpAddressParser ipAddressParser = new IpAddressParser(request);
                logger.debug("ip="+ipAddressParser.getIpaddress()+" octet1="+ipAddressParser.getOctet1()+" octet2="+ipAddressParser.getOctet2()+" octet3="+ipAddressParser.getOctet3()+" octet4="+ipAddressParser.getOctet4());
                Iptrack iptrack = new Iptrack();
                iptrack.setDatetime(new Date());
                iptrack.setUserid(userid);
                iptrack.setActivitytypeid(activitytypeid);
                iptrack.setDescription(Activitytype.getDescription(activitytypeid)+" "+desc);
                iptrack.setIp(ipAddressParser.getIpaddress());
                iptrack.setOctet1(ipAddressParser.getOctet1());
                iptrack.setOctet2(ipAddressParser.getOctet2());
                iptrack.setOctet3(ipAddressParser.getOctet3());
                iptrack.setOctet4(ipAddressParser.getOctet4());
                iptrack.save();
            }
        } catch (Exception ex){
            logger.error("", ex);
        }
    }


}
