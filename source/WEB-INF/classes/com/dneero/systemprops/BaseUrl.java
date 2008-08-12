package com.dneero.systemprops;

import com.dneero.dao.Pl;
import com.dneero.htmlui.Pagez;

/**
 * User: Joe Reger Jr
 * Date: Sep 10, 2006
 * Time: 12:54:56 PM
 */
public class BaseUrl {

    private static String baseUrl;

    public static void refresh(){
        baseUrl = null;
    }

    private static String getNoHttp() {
        //If there's a valid Pl in the userSession
        if (Pagez.getUserSession()!=null && Pagez.getUserSession().getPl()!=null){
            if (Pagez.getUserSession().getPl().getPlid()>0){
                return getNoHttp(Pagez.getUserSession().getPl());
            }
        }
        return getNoHttp(null);
    }

    private static String getNoHttp(Pl pl) {
        if (baseUrl ==null){
            baseUrl = SystemProperty.getProp(SystemProperty.PROP_BASEURL);
        }
        //See if the Pl has anything to offer
        if (pl!=null && pl.getPlid()>0){
            //Rip through the customdomains
            if (!pl.getCustomdomain1().equals("")){
                return pl.getCustomdomain1();
            }
            if (!pl.getCustomdomain2().equals("")){
                return pl.getCustomdomain2();
            }
            if (!pl.getCustomdomain3().equals("")){
                return pl.getCustomdomain3();
            }
        }
        //Otherwise just return the default/system value
        return baseUrl;
    }

    public static String get(boolean makeHttpsIfSSLIsOn){
        //If there's a valid Pl in the userSession
        if (Pagez.getUserSession()!=null && Pagez.getUserSession().getPl()!=null){
            if (Pagez.getUserSession().getPl().getPlid()>0){
                return get(makeHttpsIfSSLIsOn, Pagez.getUserSession().getPl());
            }
        }
        return get(makeHttpsIfSSLIsOn, null);
    }

    public static String get(boolean makeHttpsIfSSLIsOn, int plid){
        Pl pl = Pl.get(plid);
        return get(makeHttpsIfSSLIsOn, pl);
    }

    public static String get(boolean makeHttpsIfSSLIsOn, Pl pl){
        boolean returnwithhttps = false;
        //If there's a PL in the usersession and it has https turned on
        if (pl!=null && pl.getPlid()>0){
            if (makeHttpsIfSSLIsOn && pl.getIshttpson()){
                returnwithhttps = true;
            }
        //Else look to the system for guidance
        } else {
            if (makeHttpsIfSSLIsOn && SystemProperty.getProp(SystemProperty.PROP_ISSSLON).equals("1")){
                returnwithhttps = true;
            }
        }
        //Return the proper baseurl
        if (returnwithhttps){
            return "https://" + getNoHttp(pl) + "/";
        } else {
            return "http://" + getNoHttp(pl) + "/";
        }
    }

    public String getIncludinghttps(){
        return get(true);
    }

    public void setIncludinghttps(){

    }

    public String getIncludinghttp(){
        return get(false);
    }

    public void setIncludinghttp(){

    }

    public String getBase(){
        return baseUrl;
    }

    public void setBase(){

    }





}
