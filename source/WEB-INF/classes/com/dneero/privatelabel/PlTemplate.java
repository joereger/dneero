package com.dneero.privatelabel;

import com.dneero.dao.Pl;
import com.dneero.systemprops.WebAppRootDir;
import com.dneero.util.Io;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: joereger
 * Date: Apr 3, 2010
 * Time: 1:57:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlTemplate {

    private static String getFileOrPlProp(String filename, String templatedirectory, String plprop){
        Logger logger = Logger.getLogger(PlTemplate.class);
        String out = "";
        if (plprop!=null && plprop.length()>0){
            //Return value from the private label
            out = plprop;
        } else {
            String tdir = "default";
            if (templatedirectory!=null && templatedirectory.length()>0){
                tdir = templatedirectory;
            }
            //Return the value from /template/something/filename
            out = Io.textFileRead(WebAppRootDir.getWebAppRootPath()+"template/"+tdir+"/"+filename).toString();
            //If the directory wasn't readable, etc, use default
            if (out==null || out.length()==0){
                out = Io.textFileRead(WebAppRootDir.getWebAppRootPath()+"template/default/"+filename).toString();    
            }
        }
        return out;
    }

    public static String getFileFromPlTemplateDir(Pl pl, String filename){
        return getFileOrPlProp(filename, pl.getTemplatedirectory(), "");
    }

    public static String getWebhtmlheader(Pl pl){
        String filename = "webhtmlheader.vm";
        if (pl!=null){
            String plprop = pl.getWebhtmlheader();
            return getFileOrPlProp(filename, pl.getTemplatedirectory(), plprop);
        } else {
            return getFileOrPlProp(filename, "", "");
        }
    }

    public static String getWebhtmlfooter(Pl pl){
        String filename = "webhtmlfooter.vm";
        if (pl!=null){
            String plprop = pl.getWebhtmlfooter();
            return getFileOrPlProp(filename, pl.getTemplatedirectory(), plprop);
        } else {
            return getFileOrPlProp(filename, "", "");
        }
    }

    public static String getHomepagetemplate(Pl pl){
        String filename = "homepagetemplate.vm";
        if (pl!=null){
            String plprop = pl.getHomepagetemplate();
            return getFileOrPlProp(filename, pl.getTemplatedirectory(), plprop);
        } else {
            return getFileOrPlProp(filename, "", "");
        }
    }

    public static String getMaincss(Pl pl){
        String filename = "main.css";
        if (pl!=null){
            String plprop = pl.getMaincss();
            return getFileOrPlProp(filename, pl.getTemplatedirectory(), plprop);
        } else {
            return getFileOrPlProp(filename, "", "");
        }
    }

    public static String getEmailhtmlheader(Pl pl){
        String filename = "emailhtmlheader.html";
        if (pl!=null){
            String plprop = pl.getEmailhtmlheader();
            return getFileOrPlProp(filename, pl.getTemplatedirectory(), plprop);
        } else {
            return getFileOrPlProp(filename, "", "");
        }
    }

    public static String getEmailhtmlfooter(Pl pl){
        String filename = "emailhtmlfooter.html";
        if (pl!=null){
            String plprop = pl.getEmailhtmlfooter();
            return getFileOrPlProp(filename, pl.getTemplatedirectory(), plprop);
        } else {
            return getFileOrPlProp(filename, "", "");
        }
    }




}
