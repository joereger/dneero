package com.dneero.dbgrid;

import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.Date;

/**
 * User: Joe Reger Jr
 * Date: Nov 7, 2007
 * Time: 12:15:02 PM
 */
public class GridColRendererFactory {



    public static GridColRenderer getByObjectType(Object colObj){
        Logger logger = Logger.getLogger(GridColRendererFactory.class);
        if (colObj instanceof String){
            return new GridColRendererString();
        } else if (colObj instanceof Calendar){
            return new GridColRendererDatetimecompact();
        } else if (colObj instanceof Date){
            return new GridColRendererDatetimecompact();
        } else if (colObj instanceof Integer){
            return new GridColRendererString();
        } else if (colObj instanceof Boolean){
            return new GridColRendererString();
        }
        if (colObj!=null && colObj.getClass()!=null){
            logger.debug("GridColRenderer not found for colObj.getClass().getName()="+colObj.getClass().getName());
        }
        return new GridColRendererString();
    }

     public static GridColRenderer getByID(int id){
        Logger logger = Logger.getLogger(GridColRendererFactory.class);
        if (id==Grid.GRIDCOLRENDERER_STRING){
            return new GridColRendererString();
        } else if (id==Grid.GRIDCOLRENDERER_DATETIMECOMPACT){
            return new GridColRendererDatetimecompact();
        } else if (id==Grid.GRIDCOLRENDERER_DOUBLEASMONEY){
            return new GridColRendererDoubleAsMoney();
        }
        logger.debug("GridColRenderer not found for id="+id);
        return new GridColRendererString();
    }
}
