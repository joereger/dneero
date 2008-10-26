package com.dneero.cache.html;

/**
 * User: Joe Reger Jr
 * Date: Oct 26, 2008
 * Time: 12:00:03 PM
 */
public class CacheTTL12hr implements CacheTTL {

    private static int hrs = 12;
    private static int min = 0;


    public int getMinutesTL(){
        return (hrs*60)+(min);
    }

}