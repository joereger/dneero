package com.dneero.privatelabel;

import com.dneero.dao.Pl;

/**
 * Created by IntelliJ IDEA.
 * User: joereger
 * Date: Jun 19, 2010
 * Time: 3:17:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlUtil {


    public static String surveyCalled(Pl pl, boolean iscaps, boolean isplural){
        String out = "survey";
        //Get our basic values from the pl
        String surveycalled = "survey";
        String surveycalledplural = "surveys";
        if (pl!=null){
            if (pl.getSurveycalled()!=null && !pl.getSurveycalled().equals("")){
                surveycalled = pl.getSurveycalled();
            }
            if (pl.getSurveycalledplural()!=null && !pl.getSurveycalledplural().equals("")){
                surveycalledplural = pl.getSurveycalledplural();
            }
        }
        //If plural use that one
        if (isplural){
            out = surveycalledplural;
        } else {
            out = surveycalled;
        }
        //Caps or not
        if (iscaps){
            String firstLetter = out.substring(0,1);
            String restOfLetters = out.substring(1,out.length());
            out = firstLetter.toUpperCase() + restOfLetters;
        }
        //return
        return out;
    }

}
