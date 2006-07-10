package com.dneero.display.components;

import javax.servlet.http.HttpServletRequest;

/**
 * User: Joe Reger Jr
 * Date: Jul 6, 2006
 * Time: 10:17:58 AM
 */
public interface Component {


    public String getName();
    public int getID();
    public String getHtmlForInput();
    public String getHtmlForDisplay();
    public void validateAnswer(HttpServletRequest request) throws ComponentException;
    public void processAnswer(HttpServletRequest request) throws ComponentException;
    
}
