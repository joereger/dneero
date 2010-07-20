package com.dneero.privatelabel;

import com.dneero.dao.Pl;
import com.dneero.systemprops.WebAppRootDir;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: joereger
 * Date: Jul 20, 2010
 * Time: 7:55:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class JspOverrideFramework {


    public static boolean overrideExists(String jspPageName, Pl pl){
        Logger logger = Logger.getLogger(PlTemplate.class);
        //If Pl has a templatedirectory defined
        if (pl!=null && pl.getTemplatedirectory()!=null && pl.getTemplatedirectory().length()>0){
            try{
                //Test to make sure it exists
                File f = new File(WebAppRootDir.getWebAppRootPath()+"template/"+pl.getTemplatedirectory()+"/jspoverride"+jspPageName);
                if (f.exists()){
                    return true;
                }
            } catch (Exception ex){
                logger.error("", ex);
            }
        }
        return false;
    }

    public static String getOverrideFilePath(String jspPageName, Pl pl){
        Logger logger = Logger.getLogger(PlTemplate.class);
        //If Pl has a templatedirectory defined
        if (pl!=null && pl.getTemplatedirectory()!=null && pl.getTemplatedirectory().length()>0){
            return "/template/"+pl.getTemplatedirectory()+"/jspoverride"+jspPageName;
        }
        return jspPageName;
    }

//    public static String getOverrideFilePathOriginal(String jspPageName, Pl pl){
//        Logger logger = Logger.getLogger(PlTemplate.class);
//        //If Pl has a templatedirectory defined
//        if (pl!=null && pl.getTemplatedirectory()!=null && pl.getTemplatedirectory().length()>0){
//            try{
//                //Test to make sure it exists
//                File f = new File(WebAppRootDir.getWebAppRootPath()+"template/"+pl.getTemplatedirectory()+"/"+jspPageName);
//                if (f.exists()){
//                    return "/template/"+pl.getTemplatedirectory()+"/"+jspPageName;
//                } else {
//                    return "/template/default/"+jspPageName;
//                }
//            } catch (Exception ex){
//                logger.error("", ex);
//            }
//        }
//        return "/template/default/"+jspPageName;
//    }


}
