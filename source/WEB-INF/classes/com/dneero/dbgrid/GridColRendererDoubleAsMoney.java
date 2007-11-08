package com.dneero.dbgrid;

import com.dneero.util.Time;
import com.dneero.util.Str;

import java.util.Calendar;
import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Nov 7, 2007
 * Time: 12:16:27 PM
 */
public class GridColRendererDoubleAsMoney implements GridColRenderer {

    public String render(Object in) {
        if (in instanceof Double){
            return "$"+Str.formatForMoney((Double)in);
        }
        return "";
    }

}
