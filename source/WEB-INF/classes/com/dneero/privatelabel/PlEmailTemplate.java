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
public class PlEmailTemplate {

    private static String getFileOrPlProp(String filename, String templatedirectory){
        Logger logger = Logger.getLogger(PlEmailTemplate.class);
        String out = "";
        String tdir = "default";
        if (templatedirectory!=null && templatedirectory.length()>0){
            tdir = templatedirectory;
        }
        //Return the value from /template/something/filename
        out = Io.textFileRead(WebAppRootDir.getWebAppRootPath()+"template/"+tdir+"/email/"+filename).toString();
        //If the directory wasn't readable, etc, use default
        if (out==null || out.length()==0){
            out = Io.textFileRead(WebAppRootDir.getWebAppRootPath()+"template/default/email/"+filename).toString();
        }
        return out;
    }

    public static String getHtml(Pl pl, String filename){
        if (pl!=null){
            return getFileOrPlProp(filename, pl.getTemplatedirectory());
        } else {
            return getFileOrPlProp(filename, "");
        }
    }








}